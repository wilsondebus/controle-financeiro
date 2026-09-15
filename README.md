# Controle Financeiro

Sistema web de controle financeiro pessoal desenvolvido em Java com Spring Boot.

O projeto foi criado com o objetivo de organizar receitas, gastos, cartões de crédito, formas de pagamento, categorias, parcelas, faturas e compras futuras em uma única aplicação.

Atualmente, o sistema utiliza arquivos JSON para persistência local dos dados e possui uma arquitetura em camadas preparada para uma futura migração para banco de dados.

---

## Funcionalidades

- [x] Cadastro de gastos
- [x] Cadastro de receitas
- [x] Cadastro de categorias
- [x] Cadastro de formas de pagamento
- [x] Cadastro de cartões de crédito
- [x] Compras parceladas
- [x] Geração automática de parcelas
- [x] Controle de juros
- [x] Controle de parcelas pagas
- [x] Cálculo de vencimento de parcelas
- [x] Cálculo de fechamento de cartão
- [x] Visualização de próximas faturas
- [x] Compras futuras
- [x] Saldo mensal
- [x] Saldo acumulado
- [x] Gráfico de gastos por categoria
- [x] Últimos gastos no Dashboard
- [x] Interface responsiva
- [x] Tema escuro

---

## Dashboard

O Dashboard apresenta uma visão geral da situação financeira.

As principais informações disponíveis são:

- saldo acumulado;
- entradas do mês;
- gastos do mês;
- saldo do mês;
- quantidade de faturas próximas;
- próximas faturas;
- fechamento das faturas;
- vencimento das faturas;
- gráfico de gastos por categoria;
- últimos gastos cadastrados.

### Saldo mensal

```text
Saldo do mês = Receitas do mês - Gastos do mês
```

### Saldo acumulado

```text
Saldo acumulado =
Total de receitas registradas até hoje
-
Total de parcelas com vencimento até hoje
```

Dessa forma, valores restantes de meses anteriores continuam compondo o saldo acumulado.

---

## Gastos

O sistema permite cadastrar gastos com:

- descrição;
- valor;
- data;
- categoria;
- forma de pagamento;
- cartão de crédito;
- parcelamento;
- quantidade de parcelas;
- juros;
- valor total com juros;
- observação.

Exemplo:

```text
Descrição: Notebook
Valor: R$ 3.000
Categoria: Compras
Pagamento: Cartão
Parcelas: 10x
```

---

## Parcelas

As parcelas são geradas automaticamente a partir dos gastos.

Exemplo:

```text
Compra: R$ 1.200
Quantidade de parcelas: 6

Resultado:

Parcela 1 → R$ 200
Parcela 2 → R$ 200
Parcela 3 → R$ 200
Parcela 4 → R$ 200
Parcela 5 → R$ 200
Parcela 6 → R$ 200
```

Caso exista diferença de arredondamento, o valor é ajustado na última parcela.

As parcelas podem ser marcadas como:

```text
Aberta
Paga
```

---

## Receitas

O sistema permite cadastrar entradas financeiras.

Tipos disponíveis:

```text
Salário
Freelance
Venda
Reembolso
Outro
```

Cada receita possui:

- descrição;
- valor;
- data;
- tipo;
- observação.

As receitas são utilizadas nos cálculos de:

```text
Entradas do mês
Saldo mensal
Saldo acumulado
```

---

## Pagamentos

A página de Pagamentos centraliza as formas de pagamento e os cartões de crédito.

### Formas de pagamento

Exemplos:

```text
PIX
Dinheiro
Débito
Outros
```

### Cartões de crédito

Cada cartão possui:

```text
Nome
Banco
Dia de fechamento
Dia de vencimento
```

A antiga página exclusiva de cartões foi removida da navegação.

A rota:

```text
/cartoes
```

redireciona para:

```text
/pagamentos
```

---

## Faturas

O sistema calcula as próximas faturas com base nas parcelas associadas aos cartões.

São considerados:

- cartão;
- data de vencimento;
- data de fechamento;
- valor da fatura;
- dias até o vencimento;
- dias até o fechamento.

Atualmente, o Dashboard considera faturas com vencimento nos próximos 60 dias.

---

## Categorias

O sistema possui categorias padrão criadas automaticamente.

```text
Alimentação
Lazer
Transporte
Saúde
Educação
Moradia
Assinaturas
Compras
Outros
```

Também é possível criar categorias personalizadas.

---

## Compras futuras

A funcionalidade de compras futuras permite planejar despesas que ainda não foram realizadas.

Cada compra futura possui:

```text
Descrição
Valor estimado
Data prevista
Observação
Status
```

A compra pode ser marcada como:

```text
Planejada
Realizada
```

---

## Tecnologias utilizadas

### Backend

```text
Java
Spring Boot
Spring MVC
Jackson 3
Maven
```

### Frontend

```text
HTML5
CSS3
JavaScript
Thymeleaf
Chart.js
```

---

## Arquitetura

O projeto utiliza uma arquitetura em camadas:

```text
Interface
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Persistência JSON
```

### Model

Representa os dados e entidades do sistema.

Principais classes:

```text
Gasto
Parcela
Categoria
FormaPagamento
CartaoCredito
CompraFutura
Receita
FaturaResumo
```

### Controller

Responsável pelas requisições HTTP e comunicação entre interface e serviços.

### Service

Responsável pelas regras de negócio.

### Repository

Responsável pela leitura e gravação dos dados.

---

## Estrutura do projeto

```text
controle-financeiro
│
├── data
│   ├── categorias.json
│   ├── formas-pagamento.json
│   ├── cartoes.json
│   ├── gastos.json
│   ├── parcelas.json
│   ├── compras-futuras.json
│   └── receitas.json
│
├── src
│   └── main
│       │
│       ├── java
│       │   └── br
│       │       └── com
│       │           └── financeiro
│       │               └── controlefinanceiro
│       │                   │
│       │                   ├── controller
│       │                   ├── model
│       │                   ├── repository
│       │                   └── service
│       │
│       └── resources
│           │
│           ├── static
│           │   └── css
│           │       └── style.css
│           │
│           └── templates
│               │
│               ├── fragments
│               │   └── menu.html
│               │
│               ├── dashboard.html
│               ├── gastos.html
│               ├── novo-gasto.html
│               ├── parcelas.html
│               ├── pagamentos.html
│               ├── categorias.html
│               ├── compras-futuras.html
│               └── receitas.html
│
├── pom.xml
├── README.md
└── DOCUMENTACAO_SISTEMA.md
```

---

## Persistência de dados

Atualmente, o sistema utiliza arquivos JSON.

Os dados ficam na pasta:

```text
data
```

Arquivos utilizados:

```text
categorias.json
formas-pagamento.json
cartoes.json
gastos.json
parcelas.json
compras-futuras.json
receitas.json
```

A classe:

```text
JsonRepositoryUtil
```

centraliza operações genéricas de leitura, gravação e geração de IDs.

---

## Como executar

### Pré-requisitos

Tenha instalado:

```text
Java
Maven
```

Também é recomendado utilizar uma IDE como:

```text
IntelliJ IDEA
```

### Executando pelo Maven

Na raiz do projeto:

```bash
mvn spring-boot:run
```

### Executando pela IDE

Execute a classe principal que contém:

```java
@SpringBootApplication
```

Depois, acesse:

```text
http://localhost:5000
```

A porta pode variar conforme a configuração do projeto.

---

## Fluxo de cadastro de gasto

```text
Usuário
   ↓
novo-gasto.html
   ↓
GastoController
   ↓
GastoService
   ↓
GastoRepository
   ↓
gastos.json
```

Após salvar:

```text
GastoService
   ↓
ParcelaService
   ↓
ParcelaRepository
   ↓
parcelas.json
```

---

## Fluxo de cadastro de receita

```text
Usuário
   ↓
receitas.html
   ↓
ReceitaController
   ↓
ReceitaService
   ↓
ReceitaRepository
   ↓
receitas.json
```

---

## Fluxo do Dashboard

```text
DashboardController
        ↓
DashboardService
        ↓
 ┌─────────────────────┐
 │ ReceitaService      │
 │ ParcelaService      │
 │ GastoService        │
 │ CategoriaService    │
 │ CartaoService       │
 └─────────────────────┘
        ↓
dashboard.html
```

---

## Interface

A aplicação utiliza tema escuro e possui:

- sidebar;
- cards;
- tabelas;
- formulários;
- ícones SVG;
- gráficos;
- indicadores de saldo;
- alertas de faturas;
- responsividade.

O gráfico do Dashboard utiliza:

```text
Chart.js
```

com visualização em formato:

```text
Doughnut Chart
```

---

## Persistência atual e evolução futura

A persistência em JSON foi escolhida inicialmente por ser simples e adequada ao desenvolvimento local e ao estudo das regras de negócio.

Porém, para uma versão publicada ou com múltiplos usuários, a evolução planejada é utilizar:

```text
PostgreSQL
Spring Data JPA
Hibernate
```

A arquitetura deverá evoluir de:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
JSON
```

para:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Spring Data JPA
   ↓
Hibernate
   ↓
PostgreSQL
```

---

## Próximas funcionalidades

- [ ] PostgreSQL
- [ ] Spring Data JPA
- [ ] Hibernate
- [ ] Login
- [ ] Spring Security
- [ ] Separação de dados por usuário
- [ ] Receitas recorrentes
- [ ] Salário automático mensal
- [ ] Histórico financeiro mensal
- [ ] Histórico financeiro anual
- [ ] Filtros por período
- [ ] Relatórios financeiros
- [ ] Exportação em PDF
- [ ] Exportação em Excel
- [ ] Metas financeiras
- [ ] Limites de gastos por categoria
- [ ] Gráficos de evolução mensal
- [ ] Controle por conta bancária
- [ ] Deploy em ambiente web
- [ ] Suporte a múltiplos usuários

---

## Segurança

Em uma futura versão publicada, está prevista a implementação de autenticação utilizando:

```text
Spring Security
```

Possíveis funcionalidades:

```text
Login
Logout
Senha criptografada
Sessão
Controle de acesso
Separação de dados por usuário
```

---

## Objetivo acadêmico

Além de sua utilização como ferramenta de controle financeiro, o projeto também foi desenvolvido para aplicar conceitos estudados na área de Ciência da Computação.

Entre os principais conceitos aplicados estão:

```text
Programação orientada a objetos
Java
Spring Boot
Spring MVC
Arquitetura em camadas
Persistência de dados
Desenvolvimento web
HTML
CSS
JavaScript
Git
GitHub
Regras de negócio
Banco de dados
```

---

## Documentação

Uma documentação mais detalhada do funcionamento e da arquitetura está disponível em:

```text
DOCUMENTACAO_SISTEMA.md
```

---

## Versionamento

O projeto utiliza:

```text
Git
GitHub
```

para controle de versão e armazenamento do código-fonte.

---

## Observação sobre os arquivos JSON

Os arquivos JSON podem conter informações financeiras inseridas durante o uso do sistema.

Caso o repositório seja público, é recomendado não publicar dados financeiros reais.

Os arquivos podem ser mantidos vazios:

```json
[]
```

ou preenchidos somente com dados fictícios para demonstração.

---

## Status do projeto

> Em desenvolvimento.

O sistema já possui uma versão funcional para uso local e continua recebendo melhorias de interface, regras financeiras e novas funcionalidades.

---

## Roadmap

### Versão atual

```text
Controle local
Persistência JSON
Dashboard
Receitas
Gastos
Parcelas
Cartões
Faturas
Pagamentos
Categorias
Compras futuras
Saldo mensal
Saldo acumulado
```

### Próxima etapa

```text
PostgreSQL
Spring Data JPA
Hibernate
```

### Futuramente

```text
Autenticação
Múltiplos usuários
Relatórios
Metas
Exportação
Deploy
```

---

## Autor

Projeto desenvolvido como aplicação prática de estudos em desenvolvimento de software e Ciência da Computação.
