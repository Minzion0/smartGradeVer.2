package com.green.smartgradever2.board.repository;

import com.green.smartgradever2.config.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByTitleContainingAndImportanceAndDelYn(String keyword, int importance, int delYn, Pageable page);
    Page<BoardEntity> findByImportanceAndDelYn( int importance, int delYn, Pageable page);
    List<BoardEntity> findAllByImportanceAndDelYn(int importance,int delYn, Sort sort );
    @Modifying
    @Query(" update BoardEntity p" +
            " set p.boardView = p.boardView + 1 " +
            " where p.iboard = :iboard")
    int updateView(Long iboard);

//    @Query(" SELECT COUNT(*) " +
//            "FROM BoardEntity p " +
//            "WHERE p.title LIKE :keyword " +
//            "and p.importance = 0 " +
//            "and p.delYn = 0")
    long countByTitleAndImportanceAndDelYn(String keyword, int importance, int delYn);

    long countByImportanceAndDelYn(int importance, int delYn );
}
