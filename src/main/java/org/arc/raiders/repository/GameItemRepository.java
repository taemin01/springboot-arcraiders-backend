package org.arc.raiders.repository;

import org.arc.raiders.domain.GameItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameItemRepository extends JpaRepository<GameItem, Long> {
}
