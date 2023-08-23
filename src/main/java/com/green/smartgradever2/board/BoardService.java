package com.green.smartgradever2.board;

import com.green.smartgradever2.board.model.BoardInsDto;
import com.green.smartgradever2.board.repository.BoardRepository;
import com.green.smartgradever2.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository BOARD_REP;

    public BoardEntity insBoard (BoardInsDto dto) {
        BoardEntity entity = new BoardEntity();

        entity.setIadmin(dto.getIadmin());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setImportance(dto.getImportance());
        return BOARD_REP.save(entity);
    }
}
