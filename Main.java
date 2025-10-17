
import model.No;
import model.Registro;
import utils.GeradorDados;
import hash.TabelaHashEncadeamento;
import hash.TabelaHashSondagemLinear;
import hash.TabelaHashSondagemQuadratica;

// Esta é a classe principal
public class Main {
    public static void main(String[] args) {


        // Os dados que vamos testar (tamanho, obvio)
        int[] tamanhosDados = {100_000, 1_000_000, 10_000_000};

        // 0.5 = 50% cheia (bem folgada)
        // 0.75 = 75% cheia (um bom teste de estresse)
        // 0.9 = 90% cheia (quase lotada, pra ver o caos acontecer)
        double[] fatoresDeCarga = {0.5, 0.75, 0.9};

        System.out.println("==========================================================");
        System.out.println("      INICIANDO ANÁLISE DE DESEMPENHO DE TABELAS HASH     ");
        System.out.println("==========================================================");

        // O primeiro loop passa por cada tamanho de dados (100k, 1M, 10M).
        for (int n : tamanhosDados) {
            // Imprime um título pra gente saber qual teste está rodando.
            System.out.println("\n\n--- INICIANDO TESTES PARA N = " + String.format("%,d", n) + " REGISTROS ---");

            // Gera os endereços pra inserir na tabela
            System.out.println("  Gerando dados... (Isso pode levar um tempo (pra N=10M))");
            Registro[] dados = GeradorDados.gerarRegistros(n);
            System.out.println("  Dados gerados com sucesso.");

            // O segundo loop passa por cada "nível de lotação" da tabela.
            for (double alfa : fatoresDeCarga) {
                // calcula o tamanho ideal da tabela pra dar o fator de carga que a gente quer.
                // próximo número primo pra espalhar melhor os dados.
                int tamanhoTabela = proximoPrimo((int) (n / alfa));

                // Imprime outro título pra organizar a saída (aprendi a fazer isso em um video no youtube na matéria de java ano passado)
                System.out.println("\n  ------------------------------------------------------");
                System.out.println("  Testando com Fator de Carga α ≈ " + alfa + " (Tabela M = " + String.format("%,d", tamanhoTabela) + ")");
                System.out.println("  ------------------------------------------------------");

                // Monta o cabeçalho da nossa tabela de resultados pra ficar bonito no console
                System.out.printf("  %-28s | %-15s | %-18s | %-18s%n", "Técnica de Hashing", "Colisões", "Tempo Inserção(ms)", "Tempo Busca(ms)");
                System.out.println("  -----------------------------+-----------------+--------------------+--------------------");

                // Cada função abaixo vai testar uma técnica e imprimir sua linha de resultado.
                testarEncadeamento(dados, tamanhoTabela);
                testarSondagemLinear(dados, tamanhoTabela);
                testarSondagemQuadratica(dados, tamanhoTabela);
            }
        }
        // Fim de todos os testes
        System.out.println("\n\n==========================================================");
        System.out.println("                 ANÁLISE FINALIZADA                 ");
        System.out.println("==========================================================");
    }

    // Cada função abaixo é pra testar UMA técnica de hash.

    // Testa o Encadeamento Separado
    public static void testarEncadeamento(Registro[] dados, int tamanhoTabela) {
        // 1. Cria a tabela
        TabelaHashEncadeamento tabela = new TabelaHashEncadeamento(tamanhoTabela);

        // 2. Mede o tempo e as colisões pra INSERIR
        long startTime = System.nanoTime(); // Inicia o cronômetro
        long totalColisoes = 0;
        for (Registro reg : dados) {
            totalColisoes += tabela.inserir(reg);
        }
        long tempoInsercao = (System.nanoTime() - startTime) / 1_000_000; // Para o cronômetro e converte pra milissegundos.

        // 3. Mede o tempo pra BUSCAR da volta.
        startTime = System.nanoTime();
        for (Registro reg : dados) {
            tabela.buscar(reg.getCodigo());
        }
        long tempoBusca = (System.nanoTime() - startTime) / 1_000_000;

        // 4. Imprime os resultados bonitinhos na tela.
        System.out.printf("  %-28s | %-15s | %-18d | %-18d%n", "Encadeamento Separado", String.format("%,d", totalColisoes), tempoInsercao, tempoBusca);
        // 5. Função pra ver quais foram as maiores listas.
        analisarListas(tabela);
    }

    // Testa a Sondagem Linear.
    public static void testarSondagemLinear(Registro[] dados, int tamanhoTabela) {
        TabelaHashSondagemLinear tabela = new TabelaHashSondagemLinear(tamanhoTabela);

        long startTime = System.nanoTime();
        long totalColisoes = 0;
        for (Registro reg : dados) {
            totalColisoes += tabela.inserir(reg);
        }
        long tempoInsercao = (System.nanoTime() - startTime) / 1_000_000;

        startTime = System.nanoTime();
        for (Registro reg : dados) {
            tabela.buscar(reg.getCodigo());
        }
        long tempoBusca = (System.nanoTime() - startTime) / 1_000_000;

        System.out.printf("  %-28s | %-15s | %-18d | %-18d%n", "Sondagem Linear", String.format("%,d", totalColisoes), tempoInsercao, tempoBusca);
        // Mede os buracos entre os dados.
        analisarGaps(tabela.getTabela());
    }

    // Testa Sondagem Quadrática.
    public static void testarSondagemQuadratica(Registro[] dados, int tamanhoTabela) {
        TabelaHashSondagemQuadratica tabela = new TabelaHashSondagemQuadratica(tamanhoTabela);

        long startTime = System.nanoTime();
        long totalColisoes = 0;
        for (Registro reg : dados) {
            totalColisoes += tabela.inserir(reg);
        }
        long tempoInsercao = (System.nanoTime() - startTime) / 1_000_000;

        startTime = System.nanoTime();
        for (Registro reg : dados) {
            tabela.buscar(reg.getCodigo());
        }
        long tempoBusca = (System.nanoTime() - startTime) / 1_000_000;

        System.out.printf("  %-28s | %-15s | %-18d | %-18d%n", "Sondagem Quadrática", String.format("%,d", totalColisoes), tempoInsercao, tempoBusca);
        // Testa os espaços pra essa tabela também.
        analisarGaps(tabela.getTabela());
    }

    // o tamanho das 3 maiores "filas" (listas) que se formaram.
    public static void analisarListas(TabelaHashEncadeamento tabela) {
        int[] top3Listas = {0, 0, 0}; // Um array pra guardar os 3 maiores tamanhos.
        for (int i = 0; i < tabela.getTamanhoTabela(); i++) {
            int tamanhoAtual = 0;
            No atual = tabela.getNo(i);
            // Caminha pela lista contando quantos nós tem
            while (atual != null) {
                tamanhoAtual++;
                atual = atual.getProximo();
            }
            // Lógica simples pra manter sempre os 3 maiores no nosso pódio.
            if (tamanhoAtual > top3Listas[0]) {
                top3Listas[2] = top3Listas[1];
                top3Listas[1] = top3Listas[0];
                top3Listas[0] = tamanhoAtual;
            } else if (tamanhoAtual > top3Listas[1]) {
                top3Listas[2] = top3Listas[1];
                top3Listas[1] = tamanhoAtual;
            } else if (tamanhoAtual > top3Listas[2]) {
                top3Listas[2] = tamanhoAtual;
            }
        }
        System.out.printf("  %-28s | 3 Maiores Listas: %d, %d, %d%n", "  └─ Análise de Listas", top3Listas[0], top3Listas[1], top3Listas[2]);
    }

    // verifica os espaços da parada
    public static void analisarGaps(Registro[] tabela) {
        int maxGap = 0, minGap = Integer.MAX_VALUE, numGaps = 0, gapAtual = 0;
        long totalGap = 0;
        boolean encontrouElemento = false;
        // Percorre a tabela inteira.
        for (int i = 0; i < tabela.length; i++) {
            // Se achou um elemento...
            if (tabela[i] != null) {
                // ...e a gente já tinha visto outro antes, então o espaço entre eles era um gap.
                if (encontrouElemento && gapAtual > 0) {
                    if (gapAtual > maxGap) maxGap = gapAtual;
                    if (gapAtual < minGap) minGap = gapAtual;
                    totalGap += gapAtual;
                    numGaps++;
                }
                gapAtual = 0; // Zera o contador de gap.
                encontrouElemento = true;
            } else {
                gapAtual++; // Se o espaço tá vazio, aumenta o tamanho do gap atual.
            }
        }
        double mediaGap = (numGaps > 0) ? (double) totalGap / numGaps : 0;
        if (minGap == Integer.MAX_VALUE) minGap = 0; // Pra não mostrar um número gigante se não houver gaps.
        System.out.printf("  %-28s | Gap Mín/Méd/Máx: %d / %.2f / %d%n", "  └─ Análise de Gaps", minGap, mediaGap, maxGap);
    }

    // Função pra achar o próximo primo
    private static int proximoPrimo(int numero) {
        while (true) {
            if (isPrimo(numero)) return numero;
            numero++;
        }
    }

    // Função checkar se um número é primo.
    private static boolean isPrimo(int numero) {
        if (numero <= 1) return false;
        // (isso aqui eu colei) testa até a raiz quadrada do número, pq ai n precisa ficar testando tudo
        for (int i = 2; i * i <= numero; i++) {
            if (numero % i == 0) return false; // Se for divisível não é primo.
        }
        return true;
    }
}
