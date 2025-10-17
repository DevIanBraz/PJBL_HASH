package hash;

import model.Registro;

public class TabelaHashSondagemLinear {

    private Registro[] tabela;
    private int tamanhoTabela;
    private int numElementos = 0;

    public TabelaHashSondagemLinear(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new Registro[tamanho];
    }

    private int hash(int chave) {
        return chave % tamanhoTabela;
    }

    public int inserir(Registro registro) {
        // Fator de carga máximo de 95% para evitar loop infinito em tabelas muito cheias
        if (numElementos >= tamanhoTabela * 0.95) {
            return -1; // Sinaliza erro de tabela cheia
        }

        int colisoes = 0;
        int indice = hash(registro.getCodigo());

        while (tabela[indice] != null) {
            colisoes++;
            indice = (indice + 1) % tamanhoTabela;
        }

        tabela[indice] = registro;
        numElementos++;
        return colisoes;
    }

    public Registro buscar(int codigo) {
        int indice = hash(codigo);
        int tentativas = 0;
        while (tabela[indice] != null && tentativas < tamanhoTabela) {
            if (tabela[indice].getCodigo() == codigo) {
                return tabela[indice];
            }
            indice = (indice + 1) % tamanhoTabela;
            tentativas++;
        }
        return null;
    }

    // --- MÉTODO GETTER ADICIONADO PARA ANÁLISE EXTERNA ---
    public Registro[] getTabela() {
        return this.tabela;
    }
}
