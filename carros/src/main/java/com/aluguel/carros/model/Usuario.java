package com.aluguel.carros.model;


import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.aluguel.carros.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario role;

    public Usuario(){}
    public Usuario(TipoUsuario tipo, String email, String senha){
        this.email=email;
        this.role = tipo;
        this.senha=senha;
    }
}
