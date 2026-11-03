package com.example.bdget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdget.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    
}
