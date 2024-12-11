package br.com.service;

import br.com.entity.Account;
import br.com.entity.Category;
import br.com.entity.Transaction;
import br.com.repository.AccountRepository;
import br.com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AuthorizationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Map<String, Category> MCC_TO_CATEGORY = Map.of(
            "5411", Category.FOOD,
            "5412", Category.FOOD,
            "5811", Category.MEAL,
            "5812", Category.MEAL
    );

    private static final List<PatternCategory> MERCHANT_PATTERNS = List.of(
            new PatternCategory(Pattern.compile("UBER.*EATS.*"), Category.FOOD),
            new PatternCategory(Pattern.compile("UBER.*TRIP.*"), Category.CASH)
    );

    @Transactional
    public String authorizeTransaction(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccountId()).orElse(null);

        if (account == null) {
            return "07"; // Problema com a conta
        }

        Category category = resolveCategory(transaction);
        double balance = account.getBalances().getOrDefault(category, 0.0);

        if (balance >= transaction.getTotalAmount()) {
            account.getBalances().put(category, balance - transaction.getTotalAmount());
            accountRepository.save(account);
            transactionRepository.save(transaction);
            return "00"; // Transação aprovada
        }

        // Fallback para CASH
        if (category != Category.CASH) {
            double cashBalance = account.getBalances().getOrDefault(Category.CASH, 0.0);
            if (cashBalance >= transaction.getTotalAmount()) {
                account.getBalances().put(Category.CASH, cashBalance - transaction.getTotalAmount());
                accountRepository.save(account);
                transactionRepository.save(transaction);
                return "00";
            }
        }

        return "51"; // Saldo insuficiente
    }

    private Category resolveCategory(Transaction transaction) {
        // Verifica padrões baseados no nome do comerciante
        for (PatternCategory patternCategory : MERCHANT_PATTERNS) {
            if (patternCategory.getPattern().matcher(transaction.getMerchant().trim()).matches()) {
                return patternCategory.getCategory();
            }
        }

        // Fallback para MCC
        return MCC_TO_CATEGORY.getOrDefault(transaction.getMcc(), Category.CASH);
    }

    // Classe auxiliar para associar padrões Regex a categorias
    private static class PatternCategory {
        private final Pattern pattern;
        private final Category category;

        public PatternCategory(Pattern pattern, Category category) {
            this.pattern = pattern;
            this.category = category;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public Category getCategory() {
            return category;
        }
    }
}