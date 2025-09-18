package main.java.com.aluguel.carros.dto;

@Data
@Getters
@Setters
public class ContratanteRequestDTO {
    private String nome;
    private String cpf;
    private String profissao;
    private String entidade;

    // Constructors
    public ContratanteRequestDTO() {}

    public ContratanteRequestDTO(String nome, String cpf, String profissao, String entidade) {
        this.nome = nome;
        this.cpf = cpf;
        this.profissao = profissao;
        this.entidade = entidade;
    }


}