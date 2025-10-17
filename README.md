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
    1.  [Geração dos Conjuntos de Dados](#21-geração-dos-conjuntos-de-dados)
    2.  [Funções de Hash Implementadas](#22-funções-de-hash-implementadas)
    3.  [Estrutura dos Testes e Escolha dos Tamanhos de Tabela](#23-estrutura-dos-testes-e-escolha-dos-tamanhos-de-tabela)
    
3.  [Resultados Obtidos](#3-resultados-obtidos)
    1.  [Resultados para N = 100.000](#31-resultados-para-n--100000-registros)
    2.  [Resultados para N = 1.000.000](#32-resultados-para-n--1000000-registros)
    3.  [Resultados para N = 10.000.000](#33-resultados-para-n--10000000-registros)
    4.  [Gráficos Comparativos](#34-gráficos-comparativos)
4.  [Análise dos Resultados](#4-análise-dos-resultados)
    1.  [Análise de Desempenho (Tempo e Colisões)](#41-análise-de-desempenho-tempo-e-colisões)
    2.  [Análise de Listas e Gaps](#42-análise-de-listas-e-gaps)
5.  [Conclusão](#5-conclusão)
6.  [Referências](#6-referências)

---

## 1. Introdução

Com uma pesquisa completa sobre tabelas hash, entendi que o processo chegou pra acrescentar velocidade e eficiência as buscas de valores, porém, como nem tudo se resumem a maravilhas, as tabelas hash sofrem com um aspecto muito importante: as colisões. Nesse projeto abordamos algumas tecnicas diferentes da tabela hash e quais suas diferenças, trazendo resultados simulados no java, como: Diferença de velocidade, resultado desempenho(velocidade x resultado). 

As técnicas implementadas foram: **Encadeamento Separado**, **Sondagem Linear** e **Sondagem Quadrática**. Os algoritmos foram desenvolvidos em Java, seguindo as restrições propostas, e testados com três grandes conjuntos de dados (100 mil, 1 milhão e 10 milhões de registros) para avaliar seu comportamento e escalabilidade sob diferentes condições de carga.

## 2. Metodologia
Fiz um pequeno ajuste (dentro do possivel) pra termos um resultado mais condizente com um processo real.

### 2.1 Geração dos Conjuntos de Dados

Os dados utilizados nos testes foram gerados aleatoriamente pela classe `GeradorDados.java`. Para garantir a validade e a reprodutibilidade dos experimentos, uma `SEED` (semente) fixa foi utilizada no gerador de números aleatórios. Assegurando     que os mesmos conjuntos de dados fossem usados para testar todas as três implementações de Tabela Hash.

* **Conjunto de Dados 1 (N1):** 100.000 registros
* **Conjunto de Dados 2 (N2):** 1.000.000 de registros
* **Conjunto de Dados 3 (N3):** 10.000.000 de registros

Cada registro é um objeto da classe `Registro`, contendo um código numérico de até 9 dígitos.

### 2.2 Funções de Hash Implementadas

1.  **Encadeamento Separado:** Isso armazenando os registros que colidem em uma lista ligada (ou encadeada) no índice da tabela. A função de hash primária utilizada foi o **Método da Divisão** (`chave % tamanhoTabela`).

2.  **Sondagem Linear (Rehashing):** Em caso de colisão, a próxima posição livre no vetor é procurada sequencialmente (`indice + 1`, `indice + 2`, ...).

3.  **Sondagem Quadrática (Rehashing):** Uma melhoria da Sondagem Linear, onde a busca por uma posição livre é feita em saltos quadráticos (`indice + 1²`, `indice + 2²`, ...), com o objetivo de mitigar o problema de agrupamento primário.

### 2.3 Estrutura dos Testes e Escolha dos Tamanhos de Tabela

Para a escolha dos tamanhos das tabelas (M), em vez de uma variação fixa de x10, por se tratar de uma simulação, preferi trazer coisas que acontecem na pratica, então implementei o **fator de carga (α = N/M)** (li no livro Algoritmos: Teoria e Prática), que é a métrica fundamental que rege o desempenho de uma tabela hash. Com isso temos uma análise mais controlada e realista sobre como a densidade da tabela afeta o desempenho (fora que conseguimos ver resultados de colisões melhores).

Testei três fatores de carga para cada conjunto:
* **α ≈ 0.5:** Tabela com aproximadamente 50% de ocupação.
* **α ≈ 0.75:** Tabela com aproximadamente 75% de ocupação.
* **α ≈ 0.9:** Tabela com aproximadamente 90% de ocupação, para simular um cenário de alto fluxo de dados.

Para cada `α` desejado, o tamanho da tabela `M` foi calculado como `proximoPrimo(N / α)` para garantir que o tamanho do vetor fosse sempre um número primo, coisa que tambem li em dois livro (The Art of Computer Programming, Volume 3), pra melhorar a distribuição das chave.

## 3. Resultados Obtidos

A seguir, são apresentadas as tabelas com os dados coletados para cada cenário de teste.

### 3.1 Resultados para N = 100.000 Registros

<img width="1129" height="517" alt="image" src="https://github.com/user-attachments/assets/5e893239-f683-47f2-9da7-0b897802d66d" />
<img width="1105" height="386" alt="image" src="https://github.com/user-attachments/assets/8e758f21-a7bc-4731-b874-958562e38312" />
<img width="1100" height="393" alt="image" src="https://github.com/user-attachments/assets/303570fc-3733-4a3b-8d7d-59f93c15d5cc" />

### 3.2 Resultados para N = 1.000.000 Registros

<img width="1122" height="531" alt="image" src="https://github.com/user-attachments/assets/b8a96e25-90de-4bbf-9e28-3b40ca44072d" />
<img width="1097" height="393" alt="image" src="https://github.com/user-attachments/assets/f14e4147-3f1d-4eed-8f97-3b70a2d6e7a4" />
<img width="1100" height="386" alt="image" src="https://github.com/user-attachments/assets/23e4c341-0dcb-4fa0-a207-bbc0dedadce9" />


### 3.3 Resultados para N = 10.000.000 Registros

<img width="1128" height="528" alt="image" src="https://github.com/user-attachments/assets/d56d2fa7-12fd-46b7-a254-cee639c0f807" />
<img width="1095" height="391" alt="image" src="https://github.com/user-attachments/assets/c41bcc77-c127-4d0a-8ba7-dbfacd013a45" />
<img width="1099" height="393" alt="image" src="https://github.com/user-attachments/assets/33818059-ff14-41d0-a5c2-d43640d1fd5f" />


## 4. Análise dos Resultados

### 4.1 Análise de Desempenho (Tempo e Colisões)

Os resultados demonstram claramente as características teóricas de cada algoritmo.

* **Sondagem Linear** Como ela sempre procura o próximo espaço vazio andando de um em um, acabou criando blocos enormes de dados, todos amontoados. É o que chamam de "agrupamento primário", ela teve o maior número de colisões de longe.

* **Encadeamento Separado** Se destacou pelo baixo número de colisões e pelo **melhor tempo de busca** na maioria dos casos. No entanto, seu tempo de inserção foi o mais lento (dependendo da quantidade de dados tratados tambem). Isso é explicado pelo *overhead* de alocação de memória dinâmica para cada nó (`new No(...)`) da lista encadeada, uma operação mais complexa do que a simples atribuição em um vetor.

* **Sondagem Quadrática** Provou ser a abordagem mais **equilibrada**. Ao evitar o agrupamento primário, o número de colisões foi significativamente menor que o da Sondagem Linear. Seus tempos de inserção e busca ficaram entre os das outras duas técnicas, representando um excelente resultado entre velocidade e controle de colisões, sendo uma escolha mais recomendada para cenários de uso geral.


## 5. Conclusão

Com base nos dados coletados e na análise realizada, entendesse que não existe uma única técnica "melhor" para todas as situações; a escolha depende dos requisitos da aplicação.

* Para cenários onde a **velocidade de busca é a prioridade máxima** e o custo de memória não é um fator essêncial, o **Encadeamento Separado** é a escolha superior.
* A **Sondagem Linear**, apesar de sua simplicidade, mostrou ineficiente e não é recomendada para aplicações que utilizem fatores de carga moderados a altos.
* A **Sondagem Quadrática** foi feita como a **equilibrada**, oferecendo um ótimo desempenho tanto em inserções quanto em buscas, e controlando colisões de forma muito mais eficaz que a Sondagem Linear. Para uma aplicação de propósito geral, ela seria a escolha mais recomendada.

Este trabalho permitiu verificar na prática a teoria fundamental das estruturas de dados, demonstrando como as escolhas de algoritmos impactam diretamente o desempenho de um sistema computacional.


## 6. Referências

1.  CORMEN, T. H.; et al. *Algoritmos: Teoria e Prática*. 3ª Edição. Editora Campus, 2012.
2.  SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4th Edition. Addison-Wesley, 2011.
3.  GeeksforGeeks. (2023). *Hashing | Set 3 (Open Addressing)*. 
4.  VisuAlgo. (2021). *Hashing Visualization*.
5.  Donald E. Knuth *The Art of Computer Programming, Volume 3: Sorting and Searching*.
