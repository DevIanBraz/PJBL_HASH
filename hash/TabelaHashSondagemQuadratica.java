package hash;

import model.Registro;

public class TabelaHashSondagemQuadratica {

    private Registro[] tabela;
    private int tamanhoTabela;
    private int numElementos = 0;

    public TabelaHashSondagemQuadratica(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new Registro[tamanho];
    }

    private int hash(int chave) {
        return chave % tamanhoTabela;
    }

    public int inserir(Registro registro) {
        if (numElementos >= tamanhoTabela * 0.95) {
            return -1; // Sinaliza erro de tabela cheia
        }

        int colisoes = 0;
        int hashInicial = hash(registro.getCodigo());
        int indice = hashInicial;
        int i = 1;

        while (tabela[indice] != null) {
            colisoes++;
            indice = (hashInicial + i * i) % tamanhoTabela;
            i++;
        }

        tabela[indice] = registro;
        numElementos++;
        return colisoes;
    }

    public Registro buscar(int codigo) {
        int hashInicial = hash(codigo);
        int indice = hashInicial;
        int i = 1;
        int tentativas = 0;

        while (tabela[indice] != null && tentativas < tamanhoTabela) {
            if (tabela[indice].getCodigo() == codigo) {
                return tabela[indice];
            }
            indice = (hashInicial + i * i) % tamanhoTabela;
            i++;
            tentativas++;
        }
        return null;
    }

    // --- MÉTODO GETTER ADICIONADO PARA ANÁLISE EXTERNA ---
    public Registro[] getTabela() {
        return this.tabela;
    }
}
