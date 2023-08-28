package com.green.smartgradever2.board;

import com.green.smartgradever2.admin.AdminRepository;
import com.green.smartgradever2.board.model.BoardInsDto;
import com.green.smartgradever2.board.model.BoardRes;
import com.green.smartgradever2.board.model.BoardUpdDto;
import com.green.smartgradever2.board.model.BoardVo;
import com.green.smartgradever2.board.repository.BoardPicRepository;
import com.green.smartgradever2.board.repository.BoardRepository;
import com.green.smartgradever2.entity.AdminEntity;
import com.green.smartgradever2.entity.BoardEntity;
import com.green.smartgradever2.entity.BoardPicEntity;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    @Value("${file.dir}")
    private String fileDir;

    private final AdminRepository ADMIN_REP;
    private final BoardRepository  BOARD_REP;
    private final BoardPicRepository BOARD_PIC_REP;

    /** insert with pics **/
    public BoardEntity insBoard (BoardInsDto dto, List<MultipartFile> pics) {
        BoardEntity entity = new BoardEntity();
        BoardPicEntity picResult = new BoardPicEntity();

        Optional<AdminEntity> adminOpt = ADMIN_REP.findById(dto.getIadmin());
        entity.setAdminEntity(adminOpt.get());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setImportance(dto.getImportance());

        BoardEntity result = BOARD_REP.save(entity);

        if (result != null) {
            if (pics != null) {
                String centerPath = String.format("boardPic/%d", result.getIboard());
                String targetPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir),centerPath);

                File targetFile = new File(targetPath);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }

                for (int i = 0; i < pics.size(); i++) {
                    String originFile = pics.get(i).getOriginalFilename();
                    String saveName = FileUtils.makeRandomFileNm(originFile);

                    File targetFilePath = new File(targetPath + "/" + saveName);
                    try {
                        pics.get(i).transferTo(targetFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException("파일저장을 실패했습니다.");
                    }
                    BoardPicEntity picEntity = new BoardPicEntity();
                    picEntity.setBoardEntity(result);
                    picEntity.setPic(saveName);

                    BOARD_PIC_REP.save(picEntity);
                }
            }
        }
        return result;
    }

    int delYn = 0;
    int importance = 0;

    /** 전체리스트 출력  및 제목 검색 **/
    public BoardRes selBoard(Pageable page, String title) {
        int row = 10;
        int importanceRow = 3;

        Page<BoardEntity> list;

        if (title == null){
           if (selImportanceBoard().size() < importanceRow) {
               page.withPage(row - selImportanceBoard().size());
               list = BOARD_REP.findByImportance(page, importance,delYn);
           } else {
               page.withPage(7);
               list = BOARD_REP.findByImportance(page, importance,delYn);
           }
        } else {
            page.withPage(10);
            list = BOARD_REP.findByTitleContaining(title, page, importance,delYn);
        }

       List<BoardVo> result = list.stream().map(item -> BoardVo.builder()
                .title(item.getTitle())
                .importance(item.getImportance())
                .boardView(item.getBoardView())
                .iboard(item.getIboard())
                .createdAt(item.getCreatedAt())
                .iadmin(item.getAdminEntity().getIadmin())
                .build()
        ).toList();

        return BoardRes.builder()
                .list(result)
                .page(page)
                .build();
    }

    /** 중요공지 리스트 출력 **/
    public List<BoardVo> selImportanceBoard() {
        importance = 1;
       List<BoardEntity> list = BOARD_REP.findAllByImportance(importance, Sort.by(Sort.Direction.DESC, "createdAt"), delYn);

        return list.stream().map(item -> BoardVo.builder()
                .title(item.getTitle())
                .importance(item.getImportance())
                .boardView(item.getBoardView())
                .iboard(item.getIboard())
                .createdAt(item.getCreatedAt())
                .iadmin(item.getAdminEntity().getIadmin())
                .build()
        ).toList();
    }

    /** 공지사항 삭제(del_yn) **/
    public int delBoardYn() {
        return 1;
    }


    /** 공지사항 수정 **/
    public BoardEntity updBoard (BoardUpdDto dto) {
        BoardEntity entity = BOARD_REP.getReferenceById(dto.getIboard());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setImportance(dto.getImportance());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
