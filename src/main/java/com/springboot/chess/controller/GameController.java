package com.springboot.chess.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.chess.model.Game;
import com.springboot.chess.repository.GameRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    public ResponseEntity<?> getGameBoard(@PathVariable Integer id) {
        Optional<Game> og = this.repository.findById(id);
        ResponseEntity<?> response;
        if (og.isEmpty()) {
            response = ResponseEntity.badRequest().body("Game does not exist");
        } else {
            response = ResponseEntity.ok(og.get());
        }
        return response;
    }

    @PostMapping("/api/games")
    public ResponseEntity<Integer> startGame(@RequestBody Map<String, String> data) {
        Game newGame = new Game(data.get("name"), data.get("password"));

        Game savedGame = repository.save(newGame);
        return ResponseEntity.ok(savedGame.getId());
    }

    @PutMapping("/api/game/{id}")
    public ResponseEntity<String> movePiece(@PathVariable Integer id, @RequestBody Map<String, String> move) {
        ResponseEntity<String> response;
        String startPos = move.get("startPos");
        String endPos = move.get("endPos");

        if (startPos == null || endPos == null) {
            return ResponseEntity.badRequest().body("Missing arguments startPos or endPos");
        }

        Optional<Game> og = this.repository.findById(id);
        if (og.isEmpty()) {
            response = ResponseEntity.badRequest().body("Game does not exist");
            return response;
        } 
        if (og.get().movePiece(startPos, endPos)) {
            response = ResponseEntity.ok().build();
            repository.save(og.get());
        }
        else{
            response = ResponseEntity.badRequest().body("Invalid move");
        }

        return response;
    }

    @DeleteMapping("/api/game/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable Integer id){
        ResponseEntity<String> response;
        Optional<Game> og = this.repository.findById(id);
        if(og.isEmpty()){
            response = ResponseEntity.badRequest().body("Game does not exist");
        } else {
            this.repository.deleteById(id);  
            response = ResponseEntity.ok().build();
        }
        return response;
    }

}
