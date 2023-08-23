package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
