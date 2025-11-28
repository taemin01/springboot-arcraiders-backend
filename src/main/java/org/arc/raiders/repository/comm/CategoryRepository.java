package org.arc.raiders.repository.comm;

import org.arc.raiders.domain.GameItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<GameItem, Long> {
}
