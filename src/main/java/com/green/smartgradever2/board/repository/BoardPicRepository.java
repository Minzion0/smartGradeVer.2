package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.config.entity.BoardEntity;
import com.green.smartgradever2.config.entity.BoardPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPicRepository extends JpaRepository<BoardPicEntity, Long> {
    List<BoardPicEntity> findByBoardEntity(BoardEntity entity);
}
