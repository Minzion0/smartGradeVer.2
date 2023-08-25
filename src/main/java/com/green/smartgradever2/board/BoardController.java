package com.green.smartgradever2.board;

import com.green.smartgradever2.board.model.BoardInsDto;
import com.green.smartgradever2.board.model.BoardRes;
import com.green.smartgradever2.board.model.BoardUpdDto;
import com.green.smartgradever2.board.model.BoardVo;
import com.green.smartgradever2.entity.BoardEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Tag(name ="게시판 - 공지사항")
public class BoardController {
    private final BoardService SERVICE;

    /** insert **/
    @PostMapping(value = "/pics", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "게시판 등록")
    public BoardEntity insBoard(@RequestPart BoardInsDto dto,
                                @RequestPart List<MultipartFile> pics) {
        return SERVICE.insBoard(dto, pics);
    }

    /** select **/
    @GetMapping("/keyword")
    @Operation(summary = "전체게시판 리스트 출력 & 제목검색")
    public BoardRes selBoard(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC,size = 10) Pageable page,
             @RequestParam(required = false) String keyword) {
        return SERVICE.selBoard(page, keyword);
    }

    /** select **/
    @GetMapping
    @Operation(summary = "중요공지리스트 출력")
    public  List<BoardVo> selBoard() {
        return SERVICE.selImportanceBoard();
    }

    /** update **/
    @PutMapping
    @Operation(summary = "중요공지 수정")
    public BoardEntity updBoard(@RequestBody BoardUpdDto dto) {
        return SERVICE.updBoard(dto);
    }
}
