package org.arc.raiders.controller;

import org.arc.raiders.domain.GameItem;
import org.arc.raiders.repository.comm.GameItemRepository;
import org.arc.raiders.service.comm.GameItemService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class GameItemController {

    private final GameItemRepository repository;
    private final GameItemService gameItemService;

    public GameItemController(GameItemRepository repository, GameItemService gameItemService) {
        this.repository = repository;
        this.gameItemService = gameItemService;
    }

    @GetMapping
    public List<GameItem> findAll() {
        return gameItemService.findAll();
    }

    @GetMapping("/")

    @PostMapping
    public GameItem create(@RequestBody GameItem gameItem) {
        // createdAt 직접 세팅
        if (gameItem.getCreatedAt() == null) {
            gameItem.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(gameItem);
    }
}
