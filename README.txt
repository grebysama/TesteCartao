Como executar:

Para a base de dados, instalando MySQL CE 8.4 LTS

para criação automática das tabelas e dos dados eu geralmente uso o DBeaver

pelo DBeaver, após criar a base de dados "desafiotecnico", clicar com o botão direito em "desafiotecnico"->Ferramentas->Restaurar Banco de Dados:
no fileChooser "Entrada", selecione o arquivo dump-desafiotecnico-202412101501.sql, disponibilizado na raiz do projeto, depois clica em Iniciar e o banco deverá ser criado.

no projeto, modificar o arquivo /src/main/resources/application.properties substituindo "SuaSenhaAqui" por sua senha no MySQL

O executor do projeto é a classe Main localizada no pacote br.com

o endpoint para ser testado deverá ser o http://localhost:8080/api/transactions/authorize