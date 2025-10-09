package com.aluguel.carros.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.dto.AutomovelDto;
import com.aluguel.carros.dto.SaveAutomovelRequest;
import com.aluguel.carros.model.Automovel;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long>{
    Optional<Automovel> findByPlaca(String placa);
    
    @Query("SELECT new com.aluguel.carros.dto.AutomovelDto(a.matricula, a.marca, a.modelo, a.ano, a.placa, a.valorMensal) FROM tb_automovel a")
    List<AutomovelDto> findAllDTO();

}
