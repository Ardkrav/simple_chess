package com.springboot.chess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.springboot.chess.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer>{
    
}
