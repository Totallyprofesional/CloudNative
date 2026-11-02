package com.example.bdget.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdget.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
      List<Evento> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);    
}
