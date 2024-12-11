package br.com.entity;

import jakarta.persistence.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(nullable = false)
    private String id;

    @ElementCollection
    @CollectionTable(name = "account_balances", joinColumns = @JoinColumn(name = "account_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "category")
    @Column(name = "balance")
    private Map<Category, Double> balances = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Category, Double> getBalances() {
        return balances;
    }

    public void setBalances(Map<Category, Double> balances) {
        this.balances = balances;
    }
}
