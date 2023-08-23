package com.green.smartgradever2.board;

import com.green.smartgradever2.board.model.BoardInsDto;
import com.green.smartgradever2.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService SERVICE;

    @PostMapping
    public BoardEntity insBoard(@RequestBody BoardInsDto dto) {
        return SERVICE.insBoard(dto);
    }
}
