package com.aluguel.carros.dto;

public class ContratanteResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String profissao;
    private String entidade;

    // Constructors
    public ContratanteResponseDTO() {}

    public ContratanteResponseDTO(Long id, String nome, String cpf, String profissao, String entidade) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.profissao = profissao;
        this.entidade = entidade;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }
}