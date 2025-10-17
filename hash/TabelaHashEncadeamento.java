package hash;

import model.No;
import model.Registro;

public class TabelaHashEncadeamento {

    private No[] tabela;
    private int tamanhoTabela;

    public TabelaHashEncadeamento(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new No[tamanho];
    }

    private int hash(int chave) {
        return chave % tamanhoTabela;
    }

    public int inserir(Registro registro) {
        int indice = hash(registro.getCodigo());
        int colisoes = 0;

        if (tabela[indice] == null) {
            tabela[indice] = new No(registro);
        } else {
            colisoes = 1;
            No noAtual = tabela[indice];
            while (noAtual.getProximo() != null) {
                colisoes++;
                noAtual = noAtual.getProximo();
            }
            noAtual.setProximo(new No(registro));
        }
        return colisoes;
    }

    public Registro buscar(int codigo) {
        int indice = hash(codigo);
        No noAtual = tabela[indice];
        while (noAtual != null) {
            if (noAtual.getRegistro().getCodigo() == codigo) {
                return noAtual.getRegistro();
            }
            noAtual = noAtual.getProximo();
        }
        return null;
    }

    // --- MÉTODOS GETTER ADICIONADOS PARA ANÁLISE EXTERNA ---
    public int getTamanhoTabela() {
        return this.tamanhoTabela;
    }

    public No getNo(int indice) {
        return this.tabela[indice];
    }
}
