package org.arc.raiders.controller;

import org.arc.raiders.domain.GameItem;
import org.arc.raiders.service.admin.AdminGameItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game-items")
public class GameItemController {

    private final AdminGameItemService gameItemService;

    @Autowired
    public GameItemController(AdminGameItemService gameItemService) {
        this.gameItemService = gameItemService;
    }

    // 전체 아이템 조회
    @GetMapping
    public ResponseEntity<List<GameItem>> getAllItems() {
        List<GameItem> items = gameItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // ID로 아이템 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        return gameItemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 카테고리로 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GameItem>> getItemsByCategory(@PathVariable String category) {
        List<GameItem> items = gameItemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    // 카테고리와 서브카테고리로 조회
    @GetMapping("/category/{category}/{subCategory}")
    public ResponseEntity<List<GameItem>> getItemsByCategoryAndSubCategory(
            @PathVariable String category, @PathVariable String subCategory) {
        List<GameItem> items = gameItemService.getItemsByCategoryAndSubCategory(category, subCategory);
        return ResponseEntity.ok(items);
    }

    // 판매자로 조회
    @GetMapping("/seller/{seller}")
    public ResponseEntity<List<GameItem>> getItemsBySeller(@PathVariable String seller) {
        List<GameItem> items = gameItemService.getItemsBySeller(seller);
        return ResponseEntity.ok(items);
    }

    // 이름 검색
    @GetMapping("/search")
    public ResponseEntity<List<GameItem>> searchItems(@RequestParam String name) {
        List<GameItem> items = gameItemService.searchItemsByName(name);
        return ResponseEntity.ok(items);
    }

    // 가격 범위로 조회
    @GetMapping("/price")
    public ResponseEntity<List<GameItem>> getItemsByPriceRange(
            @RequestParam Integer minPrice, @RequestParam Integer maxPrice) {
        List<GameItem> items = gameItemService.getItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    // 아이템 생성
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody GameItemRequest request) {
        try {
            GameItem created = gameItemService.createItem(
                    request.getName(),
                    request.getCategory(),
                    request.getSubCategory(),
                    request.getPrice(),
                    request.getDescription(),
                    request.getImageUrl(),
                    request.getSeller()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 아이템 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody GameItemRequest request) {
        try {
            GameItem updated = gameItemService.updateItem(
                    id,
                    request.getName(),
                    request.getCategory(),
                    request.getSubCategory(),
                    request.getPrice(),
                    request.getDescription(),
                    request.getImageUrl(),
                    request.getSeller()
            );
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 아이템 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            gameItemService.deleteItem(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "아이템이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 요청 DTO
    public static class GameItemRequest {
        private String name;
        private String category;
        private String subCategory;
        private Integer price;
        private String description;
        private String imageUrl;
        private String seller;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }
    }
}