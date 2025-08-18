package com.pandyzer.backend.models.dto;

public class DashboardIndicators {
    private Integer avaliacoesEmAndamento;
    private Integer avaliacoesConcluidas;
    private Integer avaliacoesNaoIniciadas;

    public DashboardIndicators() {
    }

    public DashboardIndicators(Integer avaliacoesEmAndamento, Integer avaliacoesConcluidas, Integer avaliacoesNaoIniciadas) {
        this.avaliacoesEmAndamento = avaliacoesEmAndamento;
        this.avaliacoesConcluidas = avaliacoesConcluidas;
        this.avaliacoesNaoIniciadas = avaliacoesNaoIniciadas;
    }

    public Integer getAvaliacoesEmAndamento() {
        return avaliacoesEmAndamento;
    }

    public void setAvaliacoesEmAndamento(Integer avaliacoesEmAndamento) {
        this.avaliacoesEmAndamento = avaliacoesEmAndamento;
    }

    public Integer getAvaliacoesConcluidas() {
        return avaliacoesConcluidas;
    }

    public void setAvaliacoesConcluidas(Integer avaliacoesConcluidas) {
        this.avaliacoesConcluidas = avaliacoesConcluidas;
    }

    public Integer getAvaliacoesNaoIniciadas() {
        return avaliacoesNaoIniciadas;
    }

    public void setAvaliacoesNaoIniciadas(Integer avaliacoesNaoIniciadas) {
        this.avaliacoesNaoIniciadas = avaliacoesNaoIniciadas;
    }
}
