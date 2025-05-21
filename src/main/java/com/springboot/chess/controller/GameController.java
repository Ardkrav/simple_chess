package com.springboot.chess.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.chess.model.Game;
import com.springboot.chess.repository.GameRepository;

@RestController
public class GameController {
    private GameRepository repository;

    public GameController(GameRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/games")
    public List<Game> getGames() {
        return this.repository.findAll();
    }

    @GetMapping("/api/game/{id}/board")
    public ResponseEntity<Game> getGameBoard(@PathVariable int id) {
        Optional<Game> og = this.repository.findById(id);
        ResponseEntity<Game> response;
        if (og.isEmpty()) {
            response = ResponseEntity.badRequest().build();
        } else {
            response = ResponseEntity.ok(og.get());
        }
        return response;
    }

    @PostMapping("/api/games")
    public ResponseEntity<Integer> startGame() {
        Game newGame = new Game();
        newGame.initializeBoard(); 

        Game savedGame = repository.save(newGame);
        return ResponseEntity.ok(savedGame.getId());
    }
}
