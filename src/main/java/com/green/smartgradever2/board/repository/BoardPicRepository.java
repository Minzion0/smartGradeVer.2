package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.entity.BoardPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;

public interface BoardPicRepository extends JpaRepository<BoardPicEntity, Long> {
}
