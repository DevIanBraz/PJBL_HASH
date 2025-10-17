package com.seu_grupo;

import com.seu_grupo.model.No;
import com.seu_grupo.model.Registro;
import com.seu_grupo.utils.GeradorDados;
import com.seu_grupo.hash.TabelaHashEncadeamento;
import com.seu_grupo.hash.TabelaHashSondagemLinear;
import com.seu_grupo.hash.TabelaHashSondagemQuadratica;

public class Main {

    public static void main(String[] args) {
        int[] tamanhosDados = {100_000, 1_000_000, 10_000_000};
        double[] fatoresDeCarga = {0.5, 0.75, 0.9};

        System.out.println("==========================================================");
        System.out.println("      INICIANDO ANÁLISE DE DESEMPENHO DE TABELAS HASH     ");
        System.out.println("==========================================================");

        for (int n : tamanhosDados) {
            System.out.println("\n\n--- INICIANDO TESTES PARA N = " + String.format("%,d", n) + " REGISTROS ---");

            System.out.println("  Gerando dados... (Isso pode levar um tempo para N=10M)");
            Registro[] dados = GeradorDados.gerarRegistros(n);
            System.out.println("  Dados gerados com sucesso.");

            for (double alfa : fatoresDeCarga) {
                int tamanhoTabela = proximoPrimo((int) (n / alfa));
                
                System.out.println("\n  ------------------------------------------------------");
                System.out.println("  Testando com Fator de Carga α ≈ " + alfa + " (Tabela M = " + String.format("%,d", tamanhoTabela) + ")");
                System.out.println("  ------------------------------------------------------");

                System.out.printf("  %-28s | %-15s | %-18s | %-18s%n", "Técnica de Hashing", "Colisões", "Tempo Inserção(ms)", "Tempo Busca(ms)");
                System.out.println("  -----------------------------+-----------------+--------------------+--------------------");

                testarEncadeamento(dados, tamanhoTabela);
                testarSondagemLinear(dados, tamanhoTabela);
                testarSondagemQuadratica(dados, tamanhoTabela);
            }
        }
        System.out.println("\n\n==========================================================");
        System.out.println("                 ANÁLISE FINALIZADA                 ");
        System.out.println("==========================================================");
    }
    
    public static void testarEncadeamento(Registro[] dados, int tamanhoTabela) {
        TabelaHashEncadeamento tabela = new TabelaHashEncadeamento(tamanhoTabela);
        
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
        
        System.out.printf("  %-28s | %-15s | %-18d | %-18d%n", "Encadeamento Separado", String.format("%,d", totalColisoes), tempoInsercao, tempoBusca);
        analisarListas(tabela);
    }

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
        analisarGaps(tabela.getTabela());
    }

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
        analisarGaps(tabela.getTabela());
    }
    
    public static void analisarListas(TabelaHashEncadeamento tabela) {
        int[] top3Listas = {0, 0, 0};
        for (int i = 0; i < tabela.getTamanhoTabela(); i++) {
            int tamanhoAtual = 0;
            No atual = tabela.getNo(i);
            while (atual != null) {
                tamanhoAtual++;
                atual = atual.getProximo();
            }
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

    public static void analisarGaps(Registro[] tabela) {
        int maxGap = 0, minGap = Integer.MAX_VALUE, numGaps = 0, gapAtual = 0;
        long totalGap = 0;
        boolean encontrouElemento = false;
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                if (encontrouElemento && gapAtual > 0) {
                    if (gapAtual > maxGap) maxGap = gapAtual;
                    if (gapAtual < minGap) minGap = gapAtual;
                    totalGap += gapAtual;
                    numGaps++;
                }
                gapAtual = 0;
                encontrouElemento = true;
            } else {
                gapAtual++;
            }
        }
        double mediaGap = (numGaps > 0) ? (double) totalGap / numGaps : 0;
        if (minGap == Integer.MAX_VALUE) minGap = 0;
        System.out.printf("  %-28s | Gap Mín/Méd/Máx: %d / %.2f / %d%n", "  └─ Análise de Gaps", minGap, mediaGap, maxGap);
    }
    
    private static int proximoPrimo(int numero) {
        while (true) { 
            if (isPrimo(numero)) return numero; 
            numero++; 
        }
    }

    private static boolean isPrimo(int numero) {
        if (numero <= 1) return false;
        for (int i = 2; i * i <= numero; i++) { 
            if (numero % i == 0) return false; 
        }
        return true;
    }
}
