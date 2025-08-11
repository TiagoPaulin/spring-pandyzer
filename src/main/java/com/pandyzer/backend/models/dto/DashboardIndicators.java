package com.pandyzer.backend.models.dto;

public class DashboardIndicators {
    int avaliacoesCriadas;
    int avaliacoesFeitas;
    int avaliacoesEmAndamento;

    public DashboardIndicators() {
    }

    public DashboardIndicators(int avaliacoesCriadas, int avaliacoesFeitas, int avaliacoesEmAndamento) {
        this.avaliacoesCriadas = avaliacoesCriadas;
        this.avaliacoesFeitas = avaliacoesFeitas;
        this.avaliacoesEmAndamento = avaliacoesEmAndamento;
    }

    public int getAvaliacoesCriadas() {
        return avaliacoesCriadas;
    }

    public void setAvaliacoesCriadas(int avaliacoesCriadas) {
        this.avaliacoesCriadas = avaliacoesCriadas;
    }

    public int getAvaliacoesFeitas() {
        return avaliacoesFeitas;
    }

    public void setAvaliacoesFeitas(int avaliacoesFeitas) {
        this.avaliacoesFeitas = avaliacoesFeitas;
    }

    public int getAvaliacoesEmAndamento() {
        return avaliacoesEmAndamento;
    }

    public void setAvaliacoesEmAndamento(int avaliacoesEmAndamento) {
        this.avaliacoesEmAndamento = avaliacoesEmAndamento;
    }
}
