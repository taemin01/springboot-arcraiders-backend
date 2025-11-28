package org.arc.raiders.service.comm;

import lombok.RequiredArgsConstructor;
import org.arc.raiders.domain.GameItem;
import org.arc.raiders.repository.comm.GameItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameItemService {
    private final GameItemRepository gameItemRepository;

    public List<GameItem> findAll() {
        return gameItemRepository.findAll();
    }

}
