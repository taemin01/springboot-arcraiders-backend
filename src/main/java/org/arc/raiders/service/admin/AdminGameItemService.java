package org.arc.raiders.service.admin;

import org.arc.raiders.domain.GameItem;
import org.arc.raiders.repository.comm.GameItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminGameItemService {

    private final GameItemRepository gameItemRepository;

    @Autowired
    public AdminGameItemService(GameItemRepository gameItemRepository) {
        this.gameItemRepository = gameItemRepository;
    }

    // 전체 아이템 조회
    @Transactional(readOnly = true)
    public List<GameItem> getAllItems() {
        return gameItemRepository.findAll();
    }

    // ID로 아이템 조회
    @Transactional(readOnly = true)
    public Optional<GameItem> getItemById(Long id) {
        return gameItemRepository.findById(id);
    }

    // 카테고리로 조회
    @Transactional(readOnly = true)
    public List<GameItem> getItemsByCategory(String category) {
        return gameItemRepository.findByCategory(category);
    }

    // 카테고리와 서브카테고리로 조회
    @Transactional(readOnly = true)
    public List<GameItem> getItemsByCategoryAndSubCategory(String category, String subCategory) {
        return gameItemRepository.findByCategoryAndSubCategory(category, subCategory);
    }

    // 판매자로 조회
    @Transactional(readOnly = true)
    public List<GameItem> getItemsBySeller(String seller) {
        return gameItemRepository.findBySeller(seller);
    }

    // 이름 검색
    @Transactional(readOnly = true)
    public List<GameItem> searchItemsByName(String name) {
        return gameItemRepository.findByNameContaining(name);
    }

    // 가격 범위로 조회
    @Transactional(readOnly = true)
    public List<GameItem> getItemsByPriceRange(Integer minPrice, Integer maxPrice) {
        return gameItemRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // 아이템 생성
    public GameItem createItem(String name, String category, String subCategory,
                               Integer price, String description, String imageUrl, String seller) {
        GameItem item = new GameItem();
        item.setName(name);
        item.setCategory(category);
        item.setSubCategory(subCategory);
        item.setPrice(price);
        item.setDescription(description);
        item.setImageUrl(imageUrl);
        item.setSeller(seller);
        return gameItemRepository.save(item);
    }

    // 아이템 수정
    public GameItem updateItem(Long id, String name, String category, String subCategory,
                               Integer price, String description, String imageUrl, String seller) {
        GameItem item = gameItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다: " + id));

        item.setName(name);
        item.setCategory(category);
        item.setSubCategory(subCategory);
        item.setPrice(price);
        item.setDescription(description);
        item.setImageUrl(imageUrl);
        item.setSeller(seller);

        return gameItemRepository.save(item);
    }

    // 아이템 삭제
    public void deleteItem(Long id) {
        if (!gameItemRepository.existsById(id)) {
            throw new RuntimeException("아이템을 찾을 수 없습니다: " + id);
        }
        gameItemRepository.deleteById(id);
    }
}
