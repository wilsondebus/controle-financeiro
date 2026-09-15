# Documentação Oficial — Sistema de Controle Financeiro

## 1. Identificação

**Nome:** Controle Financeiro  
**Tipo:** Aplicação web de controle financeiro pessoal  
**Tecnologia principal:** Java / Spring Boot  
**Interface:** Thymeleaf, HTML, CSS e JavaScript  
**Persistência atual:** Arquivos JSON locais  

---

## 2. Objetivo

O sistema de Controle Financeiro tem como objetivo auxiliar o usuário no acompanhamento de sua vida financeira pessoal.

A aplicação permite registrar e acompanhar:

- receitas;
- gastos;
- categorias;
- formas de pagamento;
- cartões de crédito;
- compras parceladas;
- parcelas;
- faturas;
- compras futuras;
- saldo mensal;
- saldo acumulado.

O sistema também possui um Dashboard que apresenta uma visão consolidada da situação financeira do usuário.

---

## 3. Escopo atual

Atualmente, o sistema contempla:

- cadastro de gastos;
- cadastro de receitas;
- cadastro de categorias;
- cadastro de formas de pagamento;
- cadastro de cartões de crédito;
- geração automática de parcelas;
- controle de parcelas pagas e abertas;
- cálculo de fechamento e vencimento de cartões;
- acompanhamento de próximas faturas;
- controle de compras futuras;
- cálculo de entradas do mês;
- cálculo de gastos do mês;
- cálculo do saldo mensal;
- cálculo do saldo acumulado;
- gráfico de gastos por categoria;
- visualização dos últimos gastos;
- interface responsiva em tema escuro.

O sistema foi desenvolvido inicialmente para uso pessoal e local.

---

## 4. Arquitetura

O projeto utiliza uma arquitetura em camadas.

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

Essa separação facilita a manutenção do código e permite que futuras alterações sejam realizadas sem precisar reconstruir toda a aplicação.

### 4.1 Model

A camada `model` representa as estruturas e entidades manipuladas pelo sistema.

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

Também são utilizados enums:

```text
TipoPagamento
TipoReceita
```

### 4.2 Repository

A camada `repository` é responsável pela leitura e gravação dos dados.

Atualmente, a persistência é realizada por meio de arquivos JSON.

O componente:

```text
JsonRepositoryUtil
```

centraliza operações como:

- leitura de listas;
- gravação de dados;
- criação de diretórios;
- geração de IDs;
- persistência dos objetos.

Fluxo:

```text
Service
   ↓
Repository
   ↓
JsonRepositoryUtil
   ↓
Arquivo JSON
```

### 4.3 Service

A camada `service` contém as regras de negócio da aplicação.

Exemplos de responsabilidades:

- salvar gastos;
- gerar parcelas;
- calcular vencimentos;
- calcular fechamento de faturas;
- calcular total de receitas;
- calcular gastos mensais;
- calcular saldo do mês;
- calcular saldo acumulado;
- agrupar gastos por categoria;
- calcular próximas faturas;
- controlar compras futuras.

### 4.4 Controller

A camada `controller` recebe as requisições HTTP.

Ela é responsável por:

- receber dados dos formulários;
- chamar os serviços;
- enviar dados para os templates Thymeleaf;
- realizar redirecionamentos;
- organizar as rotas da aplicação.

Exemplo:

```text
Usuário
   ↓
GastoController
   ↓
GastoService
   ↓
GastoRepository
```

---

## 5. Estrutura de diretórios

A estrutura principal do projeto é:

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

## 6. Navegação do sistema

A navegação principal possui as seguintes áreas:

```text
Dashboard
Gastos
Receitas
Pagamentos
Categorias
Compras futuras
```

A antiga opção exclusiva de cartões foi removida da sidebar.

Atualmente, os cartões são gerenciados dentro da aba:

```text
Pagamentos
```

---

## 7. Dashboard

O Dashboard apresenta uma visão geral da situação financeira.

As principais informações exibidas são:

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

### 7.1 Entradas do mês

As entradas do mês correspondem à soma das receitas cadastradas no mês atual.

Exemplo:

```text
Salário: R$ 3.000
Freelance: R$ 500

Entradas do mês: R$ 3.500
```

### 7.2 Gastos do mês

Os gastos mensais são calculados utilizando as parcelas cujo vencimento pertence ao mês atual.

Isso permite que uma compra parcelada seja distribuída corretamente ao longo dos meses.

Exemplo:

```text
Notebook

Valor total:
R$ 3.000

Parcelamento:
10x de R$ 300
```

O impacto mensal considerado será:

```text
R$ 300 por mês
```

### 7.3 Saldo mensal

O saldo do mês é calculado através da fórmula:

```text
Saldo do mês =
Receitas do mês
-
Gastos do mês
```

Exemplo:

```text
Receitas: R$ 3.500
Gastos:   R$ 2.000

Saldo do mês: R$ 1.500
```

### 7.4 Saldo acumulado

O saldo acumulado representa o valor restante ao longo dos meses.

A fórmula utilizada é:

```text
Saldo acumulado =
Total de receitas registradas até hoje
-
Total de parcelas com vencimento até hoje
```

Exemplo:

```text
Agosto

Receitas:
R$ 3.000

Gastos:
R$ 2.500

Saldo:
R$ 500
```

```text
Setembro

Receitas:
R$ 3.000

Gastos:
R$ 2.000

Saldo do mês:
R$ 1.000
```

Resultado:

```text
Saldo acumulado:

R$ 500
+
R$ 1.000
=
R$ 1.500
```

O saldo não é armazenado diretamente.

Ele é calculado com base nas movimentações registradas no sistema.

Isso evita inconsistências caso um gasto ou receita antiga seja alterado.

---

## 8. Gastos

O módulo de gastos é responsável pelo cadastro das despesas.

Cada gasto possui as seguintes informações:

```text
Descrição
Valor
Data
Categoria
Forma de pagamento
Cartão
Parcelamento
Quantidade de parcelas
Juros
Valor total com juros
Observação
```

### 8.1 Cadastro de gasto

O usuário pode registrar uma despesa informando:

- descrição;
- valor;
- data;
- categoria;
- forma de pagamento;
- cartão;
- quantidade de parcelas;
- juros;
- observação.

### 8.2 Forma de pagamento

O gasto pode utilizar:

- PIX;
- dinheiro;
- débito;
- outros;
- cartão de crédito.

Quando um gasto utiliza um cartão:

```text
cartaoId != null
```

o sistema mantém:

```text
formaPagamentoId = null
```

Isso evita que o mesmo gasto tenha simultaneamente uma forma comum e um cartão associados.

### 8.3 Exemplo de gasto comum

```text
Descrição:
Mercado

Valor:
R$ 200

Categoria:
Alimentação

Pagamento:
PIX
```

### 8.4 Exemplo de gasto com cartão

```text
Descrição:
Notebook

Valor:
R$ 3.000

Categoria:
Compras

Pagamento:
Cartão Caixa

Parcelamento:
10x
```

---

## 9. Parcelas

O sistema gera automaticamente as parcelas relacionadas aos gastos.

Uma compra à vista também é representada internamente como uma parcela.

### 9.1 Compra à vista

Exemplo:

```text
Valor:
R$ 200

Parcelado:
Não
```

Resultado:

```text
1 parcela de R$ 200
```

### 9.2 Compra parcelada

Exemplo:

```text
Valor:
R$ 1.200

Parcelas:
6
```

Resultado:

```text
Parcela 1 → R$ 200
Parcela 2 → R$ 200
Parcela 3 → R$ 200
Parcela 4 → R$ 200
Parcela 5 → R$ 200
Parcela 6 → R$ 200
```

### 9.3 Juros

Caso uma compra possua juros, o usuário pode informar o valor total final da operação.

Exemplo:

```text
Valor original:
R$ 1.000

Parcelado:
Sim

Quantidade:
5

Possui juros:
Sim

Valor total com juros:
R$ 1.100
```

O cálculo das parcelas será baseado em:

```text
R$ 1.100
```

### 9.4 Arredondamento

Caso exista diferença causada por arredondamento, o sistema ajusta a última parcela.

Exemplo:

```text
R$ 100 / 3
```

Resultado:

```text
R$ 33,33
R$ 33,33
R$ 33,34
```

### 9.5 Status das parcelas

Cada parcela possui o status:

```text
Aberta
Paga
```

O usuário pode alternar entre esses estados.

---

## 10. Receitas

O módulo de receitas representa entradas financeiras.

Cada receita possui:

```text
Descrição
Valor
Data
Tipo
Observação
```

### 10.1 Tipos de receita

Os tipos disponíveis atualmente são:

```text
Salário
Freelance
Venda
Reembolso
Outro
```

### 10.2 Exemplo de receita

```text
Descrição:
Salário Setembro

Valor:
R$ 3.000

Data:
05/09/2026

Tipo:
Salário
```

### 10.3 Uso das receitas

As receitas são utilizadas para calcular:

```text
Entradas do mês
Saldo do mês
Saldo acumulado
```

---

## 11. Pagamentos

A página:

```text
/pagamentos
```

centraliza o gerenciamento das formas de pagamento e dos cartões.

### 11.1 Formas de pagamento comuns

Exemplos:

```text
PIX
Dinheiro
Débito
Outros
```

O usuário pode cadastrar novas formas de pagamento.

### 11.2 Cartões de crédito

Os cartões são gerenciados dentro da mesma página.

Cada cartão possui:

```text
Nome
Banco
Dia de fechamento
Dia de vencimento
```

Exemplo:

```text
Nome:
Cartão Caixa

Banco:
Caixa

Fechamento:
Dia 25

Vencimento:
Dia 05
```

### 11.3 Organização da página

A página de pagamentos possui duas seções:

```text
Formas de pagamento

Cartões de crédito
```

Isso evita a necessidade de uma aba exclusiva para cartões.

---

## 12. Rota antiga de cartões

A antiga rota:

```text
/cartoes
```

não possui mais uma página exclusiva.

Atualmente, qualquer acesso a:

```text
/cartoes
```

é redirecionado para:

```text
/pagamentos
```

Essa decisão mantém compatibilidade com acessos antigos sem duplicar telas.

---

## 13. Regras de cartão de crédito

Para determinar em qual fatura uma compra será incluída, o sistema considera:

- data da compra;
- dia de fechamento;
- dia de vencimento.

### 13.1 Compra antes do fechamento

Exemplo:

```text
Fechamento:
Dia 25

Vencimento:
Dia 05

Compra:
20/09
```

Como a compra ocorreu antes do fechamento:

```text
Vencimento:
05/10
```

### 13.2 Compra depois do fechamento

Exemplo:

```text
Fechamento:
Dia 25

Vencimento:
Dia 05

Compra:
26/09
```

Como ocorreu depois do fechamento:

```text
Vencimento:
05/11
```

---

## 14. Faturas

O Dashboard exibe as próximas faturas dos cartões.

São consideradas parcelas:

```text
Não pagas
+
Com vencimento futuro
+
Associadas a cartão de crédito
```

Atualmente o sistema considera faturas com vencimento nos próximos:

```text
60 dias
```

### 14.1 Informações da fatura

Para cada fatura são exibidos:

```text
Nome do cartão
Valor da fatura
Data de fechamento
Data de vencimento
Dias até o fechamento
Dias até o vencimento
```

### 14.2 Agrupamento das parcelas

As parcelas são agrupadas por:

```text
Cartão
+
Data de vencimento
```

Isso permite que várias compras pertencentes à mesma fatura sejam somadas.

---

## 15. Categorias

As categorias permitem classificar os gastos.

O sistema cria automaticamente categorias padrão na primeira execução.

Categorias iniciais:

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

O usuário também pode adicionar categorias personalizadas.

---

## 16. Compras futuras

O módulo de compras futuras permite planejar gastos que ainda não foram realizados.

Cada compra possui:

```text
Descrição
Valor estimado
Data prevista
Observação
Status
```

### 16.1 Status

A compra pode estar:

```text
Planejada
Realizada
```

### 16.2 Objetivo

Essa funcionalidade permite visualizar despesas planejadas sem incluí-las imediatamente no cálculo financeiro atual.

---

## 17. Gráfico de categorias

O Dashboard possui um gráfico do tipo:

```text
Doughnut Chart
```

implementado com:

```text
Chart.js
```

O gráfico apresenta a distribuição dos gastos do mês entre as categorias.

Exemplo:

```text
Alimentação    R$ 500
Transporte     R$ 250
Lazer          R$ 300
Assinaturas    R$ 100
```

---

## 18. Últimos gastos

O Dashboard apresenta uma lista com os gastos mais recentes.

Atualmente são exibidos:

```text
5 últimos gastos
```

As informações apresentadas são:

```text
Descrição
Data
Valor
```

---

## 19. Persistência

Atualmente o sistema utiliza arquivos JSON.

Os dados ficam armazenados na pasta:

```text
data
```

Estrutura:

```text
data
├── categorias.json
├── formas-pagamento.json
├── cartoes.json
├── gastos.json
├── parcelas.json
├── compras-futuras.json
└── receitas.json
```

---

## 20. JsonRepositoryUtil

A classe:

```text
JsonRepositoryUtil
```

é responsável por operações genéricas de persistência.

Ela executa tarefas como:

```text
Ler listas
Salvar listas
Criar pastas
Gerar próximo ID
```

### 20.1 Leitura

Os arquivos são convertidos em listas de objetos utilizando:

```text
Jackson 3
```

### 20.2 Escrita

Os objetos são convertidos novamente para JSON e armazenados nos arquivos.

---

## 21. Vantagens da persistência JSON nesta versão

A utilização de JSON permitiu:

- desenvolver o projeto sem banco de dados;
- testar as regras de negócio;
- simplificar o desenvolvimento inicial;
- manter persistência entre execuções;
- estudar arquitetura em camadas antes da integração com banco.

---

## 22. Limitações da persistência JSON

A solução atual possui limitações:

- não é adequada para múltiplos usuários;
- não possui relacionamentos relacionais;
- não possui constraints;
- possui menor segurança de integridade;
- não é ideal para grandes volumes de dados;
- pode apresentar problemas em serviços de hospedagem com disco temporário;
- não possui transações de banco.

---

## 23. Evolução para banco de dados

Uma evolução prevista para o sistema é substituir a persistência JSON por um banco relacional.

Banco recomendado:

```text
PostgreSQL
```

Tecnologias planejadas:

```text
Spring Data JPA
Hibernate
PostgreSQL
```

### 23.1 Arquitetura futura

Atualmente:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
JSON
```

Futuramente:

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

### 23.2 Impacto esperado

A migração para banco deve afetar principalmente:

```text
Model
Repository
pom.xml
application.properties
```

A maior parte dos:

```text
Controllers
Services
HTML
CSS
```

poderá ser mantida.

---

## 24. Fluxo de cadastro de gasto

Fluxo principal:

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

Depois do gasto ser salvo:

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

## 25. Fluxo de cadastro de receita

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

## 26. Fluxo do Dashboard

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

## 27. Fluxo de pagamentos

```text
Usuário
   ↓
pagamentos.html
   ↓
FormaPagamentoController
   ↓
 ┌─────────────────────────┐
 │ FormaPagamentoService   │
 │ CartaoService           │
 └─────────────────────────┘
        ↓
Repositories
        ↓
Arquivos JSON
```

---

## 28. Interface

A interface utiliza tema escuro.

Principais características:

- sidebar;
- menu lateral;
- cards financeiros;
- tabelas;
- formulários;
- ícones SVG;
- gráfico;
- responsividade;
- indicadores visuais;
- alertas de fechamento;
- alertas de vencimento;
- valores positivos em verde;
- valores negativos em vermelho.

---

## 29. Responsividade

A interface possui regras CSS para adaptação em diferentes larguras.

Os principais pontos de quebra utilizados são:

```text
1150px
1100px
1000px
900px
650px
600px
```

Em telas menores:

- grids se tornam colunas;
- formulários ocupam largura total;
- sidebar se adapta;
- cartões ficam em uma única coluna;
- botões ocupam toda a largura quando necessário.

---

## 30. Tecnologias utilizadas

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

## 31. Spring Boot

O Spring Boot é responsável pela estrutura principal da aplicação.

Ele fornece:

- servidor web;
- injeção de dependências;
- configuração automática;
- controllers;
- services;
- integração com Thymeleaf.

---

## 32. Spring MVC

O projeto utiliza o padrão MVC:

```text
Model
View
Controller
```

Onde:

```text
Model
→ representa os dados

View
→ páginas HTML com Thymeleaf

Controller
→ controla as requisições
```

---

## 33. Thymeleaf

O Thymeleaf é utilizado para renderizar informações do backend diretamente no HTML.

Exemplos de recursos utilizados:

```text
th:text
th:if
th:each
th:field
th:action
th:href
```

---

## 34. Chart.js

O Chart.js é utilizado no Dashboard.

Ele gera o gráfico de gastos por categoria.

Tipo utilizado:

```text
doughnut
```

---

## 35. CSS

O projeto utiliza um arquivo principal:

```text
style.css
```

Ele controla:

- tema escuro;
- sidebar;
- dashboard;
- cards;
- tabelas;
- formulários;
- pagamentos;
- cartões;
- faturas;
- compras futuras;
- responsividade.

---

## 36. Como executar o projeto

### Pré-requisitos

É necessário possuir:

```text
Java
Maven
```

Também é recomendado utilizar:

```text
IntelliJ IDEA
```

### 36.1 Executar pelo IntelliJ

Abra o projeto.

Execute a classe principal que contém:

```java
@SpringBootApplication
```

### 36.2 Executar pelo Maven

Abra o terminal na raiz do projeto.

Execute:

```bash
mvn spring-boot:run
```

### 36.3 Acesso

Após iniciar o servidor:

```text
http://localhost:5000
```

A porta pode mudar dependendo da configuração existente em:

```text
application.properties
```

---

## 37. Funcionalidades implementadas

- [x] Cadastro de gastos
- [x] Exclusão de gastos
- [x] Edição de gastos
- [x] Categorias
- [x] Categorias personalizadas
- [x] Formas de pagamento
- [x] Cartões de crédito
- [x] Fechamento de cartão
- [x] Vencimento de cartão
- [x] Compras parceladas
- [x] Geração automática de parcelas
- [x] Juros
- [x] Controle de parcelas
- [x] Marcar parcela como paga
- [x] Próximas faturas
- [x] Receitas
- [x] Tipos de receita
- [x] Saldo mensal
- [x] Saldo acumulado
- [x] Compras futuras
- [x] Dashboard
- [x] Gráfico por categoria
- [x] Últimos gastos
- [x] Pagamentos e cartões na mesma página
- [x] Interface em tema escuro
- [x] Interface responsiva

---

## 38. Funcionalidades futuras

- [ ] PostgreSQL
- [ ] Spring Data JPA
- [ ] Hibernate
- [ ] Login de usuário
- [ ] Spring Security
- [ ] Receitas recorrentes
- [ ] Salário automático mensal
- [ ] Histórico financeiro por mês
- [ ] Histórico financeiro anual
- [ ] Relatórios
- [ ] Filtros por período
- [ ] Exportação de dados
- [ ] Exportação em PDF
- [ ] Exportação em Excel
- [ ] Deploy em ambiente web
- [ ] Múltiplos usuários
- [ ] Controle por conta bancária
- [ ] Metas financeiras
- [ ] Limites de gastos por categoria
- [ ] Gráficos de evolução mensal
- [ ] Comparação entre receitas e despesas

---

## 39. Segurança futura

Em uma versão publicada, será necessário adicionar autenticação.

Tecnologia recomendada:

```text
Spring Security
```

Possíveis recursos:

```text
Login
Logout
Senha criptografada
Sessão de usuário
Separação de dados por usuário
Controle de acesso
```

---

## 40. Receitas recorrentes

Uma evolução planejada é permitir receitas recorrentes.

Exemplo:

```text
Descrição:
Salário

Valor:
R$ 3.500

Dia:
05

Recorrente:
Sim
```

O sistema poderá gerar automaticamente uma nova receita todos os meses.

---

## 41. Relatórios

Futuramente, o sistema poderá possuir relatórios como:

```text
Gastos por mês
Receitas por mês
Saldo por mês
Gastos por categoria
Evolução do saldo acumulado
Faturas por cartão
Compras parceladas futuras
```

---

## 42. Filtros

Também poderão ser adicionados filtros por:

```text
Mês
Ano
Categoria
Forma de pagamento
Cartão
Tipo de receita
Status da parcela
```

---

## 43. Metas financeiras

Uma possível evolução é permitir a criação de metas.

Exemplo:

```text
Meta:
Reserva de emergência

Valor:
R$ 10.000

Saldo atual:
R$ 4.500

Progresso:
45%
```

---

## 44. Limites por categoria

Também poderá ser criado um limite mensal para categorias.

Exemplo:

```text
Categoria:
Lazer

Limite:
R$ 500

Gasto atual:
R$ 420

Disponível:
R$ 80
```

---

## 45. Considerações técnicas

### 45.1 Saldo

O saldo não deve ser persistido diretamente.

Ele é calculado a partir das receitas e gastos.

Essa abordagem evita inconsistências quando uma movimentação é alterada.

### 45.2 Parcelas

As parcelas representam o impacto financeiro mensal das compras.

Por isso, elas são utilizadas nos cálculos mensais do Dashboard.

### 45.3 Cartões

Os cartões possuem entidade própria porque possuem características que uma forma de pagamento comum não possui.

Exemplos:

```text
Banco
Dia de fechamento
Dia de vencimento
```

### 45.4 Faturas

As faturas são calculadas a partir das parcelas associadas aos cartões.

Elas não são armazenadas como um objeto independente.

O Dashboard monta os resumos dinamicamente.

---

## 46. Decisões de projeto

Algumas decisões importantes adotadas:

### Persistência em JSON

Foi escolhida inicialmente para facilitar o desenvolvimento e estudo.

### Arquitetura em camadas

Foi utilizada para separar responsabilidades.

### Cartões separados de formas comuns

Cartões possuem regras específicas de fechamento e vencimento.

### Saldo calculado dinamicamente

Evita inconsistências.

### Parcelas como base dos gastos mensais

Permite distribuir corretamente compras parceladas ao longo do tempo.

---

## 47. Objetivo acadêmico

O projeto também é utilizado como forma de estudo e aplicação prática de conceitos de desenvolvimento de software.

Entre os conteúdos aplicados estão:

```text
Programação orientada a objetos
Java
Spring Boot
MVC
Arquitetura em camadas
Persistência de dados
HTML
CSS
JavaScript
Git
GitHub
Regras de negócio
Banco de dados
```

---

## 48. Versionamento

O projeto pode ser versionado utilizando:

```text
Git
GitHub
```

Arquivos importantes:

```text
README.md
DOCUMENTACAO_SISTEMA.md
pom.xml
src/
```

---

## 49. Arquivos de dados

Os arquivos JSON armazenam dados locais.

Dependendo do objetivo do repositório, é recomendado evitar publicar dados financeiros reais.

Uma opção é manter apenas arquivos vazios:

```json
[]
```

ou utilizar dados fictícios.

---

## 50. Estado atual do projeto

O sistema possui uma versão funcional para uso local.

Atualmente é possível controlar:

```text
Receitas
Gastos
Parcelas
Cartões
Faturas
Formas de pagamento
Categorias
Compras futuras
Saldo mensal
Saldo acumulado
```

O sistema possui uma interface visual, Dashboard e persistência local.

---

## 51. Evolução arquitetural

A arquitetura atual foi construída de forma que uma futura migração para banco de dados possa ser realizada sem reconstruir toda a aplicação.

A maior parte das alterações deverá ocorrer nas camadas:

```text
Model
Repository
Configuração
```

Enquanto:

```text
Controller
Service
HTML
CSS
```

tendem a sofrer menos alterações.

---

## 52. Status

> Projeto em desenvolvimento.

O sistema continua sendo aprimorado com:

- novas funcionalidades;
- melhorias de interface;
- melhorias nas regras financeiras;
- evolução da persistência;
- preparação para publicação web.

---

## 53. Conclusão

O projeto Controle Financeiro foi desenvolvido para fornecer uma solução simples e organizada para acompanhamento de finanças pessoais.

A aplicação já permite controlar:

- entradas;
- despesas;
- parcelamentos;
- cartões;
- faturas;
- categorias;
- formas de pagamento;
- compras futuras;
- saldo mensal;
- saldo acumulado.

A arquitetura em camadas permite que o sistema continue evoluindo gradualmente.

Entre as principais evoluções previstas estão:

- integração com PostgreSQL;
- Spring Data JPA;
- Hibernate;
- autenticação com Spring Security;
- receitas recorrentes;
- relatórios financeiros;
- filtros por período;
- metas financeiras;
- exportação de dados;
- deploy em ambiente web.

O projeto também serve como aplicação prática dos conceitos estudados em desenvolvimento de software, programação orientada a objetos, arquitetura MVC, persistência de dados, desenvolvimento web, versionamento e banco de dados.
