package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByTitleContainingAndImportanceAndDelYn(String keyword, int importance, int delYn, Pageable page);
    Page<BoardEntity> findByImportanceAndDelYn( int importance, int delYn, Pageable page);
    List<BoardEntity> findAllByImportanceAndDelYn(int importance,int delYn, Sort sort );
}
