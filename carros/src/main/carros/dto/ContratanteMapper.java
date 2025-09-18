package com.aluguel.carros.dto;

import com.aluguel.carros.model.Contratante;
import java.util.List;
import java.util.stream.Collectors;

public class ContratanteMapper {

    public static ContratanteResponseDTO toResponseDTO(Contratante contratante) {
        if (contratante == null) {
            return null;
        }
        return new ContratanteResponseDTO(
            contratante.getId(),
            contratante.getNome(),
            contratante.getCpf(),
            contratante.getProfissao(),
            contratante.getEntidade()
        );
    }

    public static List<ContratanteResponseDTO> toResponseDTOList(List<Contratante> contratantes) {
        return contratantes.stream()
                .map(ContratanteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static Contratante toEntity(ContratanteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Contratante contratante = new Contratante();
        contratante.setNome(requestDTO.getNome());
        contratante.setCpf(requestDTO.getCpf());
        contratante.setProfissao(requestDTO.getProfissao());
        contratante.setEntidade(requestDTO.getEntidade());
        return contratante;
    }

    public static Contratante toEntity(ContratanteUpdateDTO updateDTO) {
        if (updateDTO == null) {
            return null;
        }
        Contratante contratante = new Contratante();
        contratante.setId(updateDTO.getId());
        contratante.setNome(updateDTO.getNome());
        contratante.setCpf(updateDTO.getCpf());
        contratante.setProfissao(updateDTO.getProfissao());
        contratante.setEntidade(updateDTO.getEntidade());
        return contratante;
    }
}