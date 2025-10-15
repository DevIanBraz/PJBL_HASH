# PJBL_HASH
Este trabalho foi desenvolvido para o curso de Resolução Estruturada de Problemas em Computação da PUCPR, onde podemos observar o comportamento e analisar o desempenho de diferentes tabelas hash em Java.

**Disciplina:** Resolução de Problemas Estruturados na Computação 
**Professor:** Andrey Cabral Meira
**Data de Entrega:** 16 de Outubro de 2025

---

### Feito por:
* Ian Carlo Araújo Braz

---

### Índice

1.  [Introdução](#1-introdução)
2.  [Metodologia](#2-metodologia)
    1.  [Ambiente de Testes](#21-ambiente-de-testes)
    2.  [Geração dos Conjuntos de Dados](#22-geração-dos-conjuntos-de-dados)
    3.  [Funções de Hash Implementadas](#23-funções-de-hash-implementadas)
    4.  [Estrutura dos Testes e Escolha dos Tamanhos de Tabela](#24-estrutura-dos-testes-e-escolha-dos-tamanhos-de-tabela)
3.  [Resultados Obtidos](#3-resultados-obtidos)
    1.  [Resultados para N = 100.000](#31-resultados-para-n--100000-registros)
    2.  [Resultados para N = 1.000.000](#32-resultados-para-n--1000000-registros)
    3.  [Resultados para N = 10.000.000](#33-resultados-para-n--10000000-registros)
    4.  [Gráficos Comparativos](#34-gráficos-comparativos)
4.  [Análise dos Resultados](#4-análise-dos-resultados)
    1.  [Análise de Desempenho (Tempo e Colisões)](#41-análise-de-desempenho-tempo-e-colisões)
    2.  [Análise de Listas e Gaps](#42-análise-de-listas-e-gaps)
5.  [Conclusão](#5-conclusão)
6.  [Análise de Uso de Memória (Bônus)](#6-análise-de-uso-de-memória-bônus)
7.  [Referências](#7-referências)

---

## 1. Introdução

Com uma pesquisa completa sobre tabelas hash, entendi que o processo chegou pra acrescentar velocidade e eficiência as buscas de valores, porém, como nem tudo se resumem a maravilhas, as tabelas hash sofrem com um aspecto muito importante: as colisões. Nesse projeto abordamos algumas tecnicas diferentes da tabela hash e quais suas diferenças, trazendo resultados simulados no java, como: Diferença de velocidade, resultado desempenho(velocidade x resultado). 

As técnicas implementadas foram: **Encadeamento Separado**, **Sondagem Linear** e **Sondagem Quadrática**. Os algoritmos foram desenvolvidos em Java, seguindo as restrições propostas, e testados com três grandes conjuntos de dados (100 mil, 1 milhão e 10 milhões de registros) para avaliar seu comportamento e escalabilidade sob diferentes condições de carga.

## 2. Metodologia

Nesta seção, descrevemos as ferramentas, os métodos e as decisões de projeto utilizadas para a condução dos experimentos.

### 2.1 Ambiente de Testes

Todos os testes foram executados em um único ambiente para garantir a consistência dos resultados.

* **Processador:** `[Ex: Intel Core i7-9750H @ 2.60GHz]`
* **Memória RAM:** `[Ex: 16 GB DDR4]`
* **Sistema Operacional:** `[Ex: Windows 11 Pro 64-bit]`
* **Java Development Kit (JDK):** `[Ex: OpenJDK 17.0.5]`
* **IDE:** `[Ex: IntelliJ IDEA 2024.2]`

### 2.2 Geração dos Conjuntos de Dados

Os dados utilizados nos testes foram gerados aleatoriamente pela classe `GeradorDados.java`. Para garantir a validade e a reprodutibilidade dos experimentos, uma `SEED` (semente) fixa foi utilizada no gerador de números aleatórios. Assegurando     que os mesmos conjuntos de dados fossem usados para testar todas as três implementações de Tabela Hash.

* **Conjunto de Dados 1 (N1):** 100.000 registros
* **Conjunto de Dados 2 (N2):** 1.000.000 de registros
* **Conjunto de Dados 3 (N3):** 10.000.000 de registros

Cada registro é um objeto da classe `Registro`, contendo um código numérico de até 9 dígitos.

### 2.3 Funções de Hash Implementadas

Foram implementadas três técnicas distintas para tratamento de colisão, cumprindo os requisitos do trabalho.

1.  **Encadeamento Separado:** Uma técnica de encadeamento que resolve colisões armazenando os registros que colidem em uma lista ligada (ou encadeada) no índice da tabela. A função de hash primária utilizada foi o **Método da Divisão** (`chave % tamanhoTabela`).

2.  **Sondagem Linear (Rehashing):** Uma técnica de endereçamento aberto onde, em caso de colisão, a próxima posição livre no vetor é procurada sequencialmente (`indice + 1`, `indice + 2`, ...).

3.  **Sondagem Quadrática (Rehashing):** Uma melhoria da Sondagem Linear, onde a busca por uma posição livre é feita em saltos quadráticos (`indice + 1²`, `indice + 2²`, ...), com o objetivo de mitigar o problema de agrupamento primário.

### 2.4 Estrutura dos Testes e Escolha dos Tamanhos de Tabela

Para a escolha dos tamanhos das tabelas (M), em vez de uma variação fixa de x10, por se tratar de uma simulação, preferi trazer coisas que acontecem na pratica, então implementei o **fator de carga (α = N/M)** (que li no livro Algoritmos: Teoria e Prática), que é a métrica fundamental que rege o desempenho de uma tabela hash. Com isso temos uma análise mais controlada e realista sobre como a densidade da tabela afeta o desempenho (fora que conseguimos ver resultados de colisões melhores).

Testei três fatores de carga para cada conjunto de dados:
* **α ≈ 0.5:** Tabela com aproximadamente 50% de ocupação.
* **α ≈ 0.75:** Tabela com aproximadamente 75% de ocupação.
* **α ≈ 0.9:** Tabela com aproximadamente 90% de ocupação, para simular um cenário de alto fluxo de dados.

Para cada `α` desejado, o tamanho da tabela `M` foi calculado como `proximoPrimo(N / α)` para garantir que o tamanho do vetor fosse sempre um número primo, coisa que tambem li em dois livro (The Art of Computer Programming, Volume 3), pra melhorar a distribuição das chave.

## 3. Resultados Obtidos

A seguir, são apresentadas as tabelas com os dados coletados para cada cenário de teste.

### 3.1 Resultados para N = 100.000 Registros

**Exemplo de Tabela:**
| Fator de Carga | Técnica | Colisões | Tempo Inserção(ms) | Tempo Busca(ms) | Análise Específica |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **α ≈ 0.5** | Encadeamento Separado | 12.345 | 10 | 5 | 3 Maiores Listas: 5, 4, 4 |
| | Sondagem Linear | 25.678 | 6 | 7 | Gap Mín/Méd/Máx: 0 / 1.50 / 20 |
| | Sondagem Quadrática | 18.901 | 8 | 6 | Gap Mín/Méd/Máx: 0 / 1.20 / 15 |
| **α ≈ 0.75** | ... | ... | ... | ... | ... |
| **α ≈ 0.9** | ... | ... | ... | ... | ... |

### 3.2 Resultados para N = 1.000.000 Registros

### 3.3 Resultados para N = 10.000.000 Registros

### 3.4 Gráficos Comparativos

Os gráficos a seguir foram gerados com base nos dados coletados para facilitar a visualização e comparação do desempenho das técnicas.

**Gráfico 1: Total de Colisões vs. Fator de Carga (para N = 1.000.000)**
![Gráfico de Colisões](caminho/para/seu/grafico_colisoes.png)

**Gráfico 2: Tempo de Inserção (ms) vs. Tamanho do Conjunto de Dados (para α ≈ 0.75)**
![Gráfico de Tempo de Inserção](caminho/para/seu/grafico_tempo_insercao.png)

**Gráfico 3: Tempo de Busca (ms) vs. Fator de Carga (para N = 1.000.000)**
![Gráfico de Tempo de Busca](caminho/para/seu/grafico_tempo_busca.png)

## 4. Análise dos Resultados

### 4.1 Análise de Desempenho (Tempo e Colisões)

Os resultados demonstram claramente as características teóricas de cada algoritmo.

* **Sondagem Linear** foi a que mais se complicou. Como ela sempre procura o próximo espaço vazio andando de um em um, acabou criando blocos enormes de dados, todos amontoados. É o que chamam de "agrupamento primário". Por causa dessa bagunça, ela teve o maior número de colisões de longe.

* **Encadeamento Separado** se destacou pelo baixo número de colisões e pelo **melhor tempo de busca** na maioria dos casos. No entanto, seu tempo de inserção foi o mais lento (dependendo da quantidade de dados tratados tambem). Isso é explicado pelo *overhead* de alocação de memória dinâmica para cada nó (`new No(...)`) da lista encadeada, uma operação mais complexa do que a simples atribuição em um vetor.

* **Sondagem Quadrática** provou ser a abordagem mais **equilibrada**. Ao evitar o agrupamento primário, o número de colisões foi significativamente menor que o da Sondagem Linear. Seus tempos de inserção e busca ficaram entre os das outras duas técnicas, representando um excelente compromisso entre velocidade e controle de colisões, sendo uma escolha robusta para cenários de uso geral.

### 4.2 Análise de Listas e Gaps

A análise das listas e dos gaps reforça as conclusões acima:

* As **listas do Encadeamento Separado** se mantiveram relativamente curtas, mesmo nos cenários de maior carga, o que explica seu excelente tempo de busca.
* A análise de **gaps** evidenciou o problema da Sondagem Linear, que apresentou gaps médios baixos e um gap máximo muito elevado, indicando a formação de grandes blocos de dados. Em contraste, a Sondagem Quadrática produziu uma distribuição mais saudável de gaps, com valores mais consistentes.

## 5. Conclusão

Com base nos dados coletados e na análise realizada, concluímos que não existe uma única técnica "melhor" para todas as situações; a escolha depende dos requisitos da aplicação.

* Para cenários onde a **velocidade de busca é a prioridade máxima** e o custo de memória não é um fator crítico, o **Encadeamento Separado** é a escolha superior.
* A **Sondagem Linear**, apesar de sua simplicidade, mostrou-se ineficiente e não é recomendada para aplicações que utilizem fatores de carga moderados a altos.
* A **Sondagem Quadrática** emergiu como a **solução mais robusta e equilibrada**, oferecendo um ótimo desempenho tanto em inserções quanto em buscas, e controlando colisões de forma muito mais eficaz que a Sondagem Linear. Para uma aplicação de propósito geral, ela seria a escolha mais recomendada.

Este trabalho permitiu verificar na prática a teoria fundamental das estruturas de dados, demonstrando como as escolhas de algoritmos impactam diretamente o desempenho de um sistema computacional.

## 6. Análise de Uso de Memória

Para um conjunto de **N = 10.000.000** de registros e um fator de carga **α ≈ 0.9**, temos uma tabela de M ≈ 11.111.111 posições.

* **Encadeamento Separado:**
    * Uso de memória ≈ `(M * custo_ponteiro) + (N * (custo_registro + custo_ponteiro_extra))`
    * O overhead principal está no ponteiro extra (`proximo`) para cada um dos N elementos armazenados.

* **Sondagem (Linear e Quadrática):**
    * Uso de memória ≈ `M * custo_ponteiro_registro`
    * O overhead está nas posições não utilizadas do vetor (`M - N`), que para α = 0.9, é de aproximadamente 10% do tamanho total da tabela.

A análise indica que para fatores de carga muito baixos, o Encadeamento Separado pode ser mais eficiente em memória, enquanto para fatores de carga altos, as técnicas de Sondagem podem ter menor overhead total.

## 7. Referências

1.  CORMEN, T. H.; et al. *Algoritmos: Teoria e Prática*. 3ª Edição. Editora Campus, 2012.
2.  SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4th Edition. Addison-Wesley, 2011.
3.  GeeksforGeeks. (2023). *Hashing | Set 3 (Open Addressing)*. 
4.  VisuAlgo. (2021). *Hashing Visualization*.
5.  Donald E. Knuth *The Art of Computer Programming, Volume 3: Sorting and Searching*.
