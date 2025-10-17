package utils;

import model.Registro;
import java.util.Random;

// Classe responsável por gerar nossos conjuntos de dados.
public class GeradorDados {

    // A SEED é um número "semente" para o gerador aleatório.
    // Usar a mesma SEED garante que os números gerados serão SEMPRE OS MESMOS,
    // o que é ESSENCIAL para um teste justo entre as diferentes tabelas hash.
    private static final long SEED = 42L;
    private static Random random = new Random(SEED);

    /**
     * Gera um vetor de Registros com códigos aleatórios de até 9 dígitos.
     */
    public static Registro[] gerarRegistros(int quantidade) {
        Registro[] registros = new Registro[quantidade];
        int maxCodigo = 999_999_999;

        for (int i = 0; i < quantidade; i++) {
            // Gera um código aleatório entre 1 e 999.999.999
            int codigo = random.nextInt(maxCodigo) + 1;
            registros[i] = new Registro(codigo);
        }

        // IMPORTANTE: Reinicia o gerador com a mesma seed.
        // Isso garante que a próxima vez que você chamar `gerarRegistros`,
        // a sequência de números será a mesma do início.
        random = new Random(SEED);

        return registros;
    }
}
