package org.arc.raiders.repository.comm;

import org.arc.raiders.domain.GameItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameItemRepository extends JpaRepository<GameItem, Long> {

    // 카테고리로 조회
    List<GameItem> findByCategory(String category);

    // 카테고리와 서브카테고리로 조회
    List<GameItem> findByCategoryAndSubCategory(String category, String subCategory);

    // 판매자로 조회
    List<GameItem> findBySeller(String seller);

    // 이름으로 검색 (부분 일치)
    List<GameItem> findByNameContaining(String name);

    // 가격 범위로 조회
    List<GameItem> findByPriceBetween(Integer minPrice, Integer maxPrice);
}
