package com.green.smartgradever2.board;

import com.green.smartgradever2.board.model.*;
import com.green.smartgradever2.config.entity.BoardEntity;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Tag(name ="게시판 - 공지사항")
public class BoardController {
    private final BoardService SERVICE;

    /** insert **/
    @PostMapping(value = "/pics", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "게시판 등록")
    public BoardInsRes insBoard(@RequestPart BoardInsParam param,
                                @RequestPart(required = false) List<MultipartFile> pics,
            @AuthenticationPrincipal MyUserDetails details ) {
        BoardInsDto dto = new BoardInsDto();
        dto.setCtnt(param.getCtnt());
        dto.setIadmin(details.getIuser());
        dto.setImportance(param.getImportance());
        dto.setTitle(param.getTitle());
        return SERVICE.insBoard(dto, pics);
    }

    /** select **/
    @GetMapping("/keyword")
    @Operation(summary = "전체게시판 리스트 출력 & 제목검색")
    public BoardRes selBoard(@ParameterObject @PageableDefault(sort = "iboard", direction = Sort.Direction.DESC, size = 10) Pageable page,
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
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "공지 수정")
    public BoardInsVo updBoard(@RequestPart BoardUpdParam param, @RequestPart(required = false) List<MultipartFile> pics,
            @AuthenticationPrincipal MyUserDetails details) {
        BoardUpdDto dto = new BoardUpdDto();
        dto.setIboard(param.getIboard());
        dto.setIadmin(details.getIuser());;
        dto.setTitle(param.getTitle());
        dto.setCtnt(param.getCtnt());
        dto.setImportance(param.getImportance());
        dto.setIpic(param.getIpic());
        dto.setUpdatedAt(LocalDateTime.now());

        return SERVICE.updBoard(dto, pics);
    }

    /** update DelYn **/
    // Todo 리턴 타입 박스갈이
    @DeleteMapping
    @Operation(summary = "공지삭제", description = "ibaord : pk값 , 1 = 삭제")
    public BoardEntity delBoardYn (BoardDelYnDto dto){
        return SERVICE.delBoardYn(dto);
    }

    /** detail select Board **/
    @Transactional
    @GetMapping("/{iboard}")
    @Operation(summary = "디테일 공지사항 보기")
    public BoardDetailVo selDetailBoard(@PathVariable Long iboard) {
        return SERVICE.selDetailBoard(iboard);
    }
}
