package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByTitleContaining(String keyword, Pageable page, int importance, int delYn);
    Page<BoardEntity> findByImportance(Pageable page, int importance, int delYn);
    List<BoardEntity> findAllByImportance(int importance, Sort sort, int delYn);
}
