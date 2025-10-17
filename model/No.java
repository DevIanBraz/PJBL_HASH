package model;

// Esta classe representa um "elo" da nossa lista encadeada.
// É usado apenas na Tabela Hash com Encadeamento.
public class No {

    private Registro registro; // O dado que queremos guardar
    private No proximo;      // A referência (ponteiro) para o próximo nó na lista

    public No(Registro registro) {
        this.registro = registro;
        this.proximo = null; // Quando um nó é criado, ele ainda não aponta pra ninguém.
    }

    // Métodos para pegar e definir os valores (getters e setters)
    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public No getProximo() {
        return proximo;
    }

    public void setProximo(No proximo) {
        this.proximo = proximo;
    }
}
