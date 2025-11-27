package org.arc.raiders.controller;

import lombok.Getter;
import lombok.Setter;
import org.arc.raiders.domain.GameItem;
import org.arc.raiders.repository.GameItemRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class GameItemController {

    private final GameItemRepository repository;

    public GameItemController(GameItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GameItem> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public GameItem create(@RequestBody GameItem gameItem) {
        // createdAt 직접 세팅
        if (gameItem.getCreatedAt() == null) {
            gameItem.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(gameItem);
    }
}
