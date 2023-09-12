package com.green.smartgradever2.board;

import com.green.smartgradever2.admin.AdminRepository;
import com.green.smartgradever2.board.model.*;
import com.green.smartgradever2.board.repository.BoardPicRepository;
import com.green.smartgradever2.board.repository.BoardRepository;
import com.green.smartgradever2.config.entity.AdminEntity;
import com.green.smartgradever2.config.entity.BoardEntity;
import com.green.smartgradever2.config.entity.BoardPicEntity;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BoardInsRes insBoard (BoardInsDto dto, List<MultipartFile> pics) {
        BoardEntity entity = new BoardEntity();
        BoardPicEntity picResult = new BoardPicEntity();

        Optional<AdminEntity> adminOpt = ADMIN_REP.findById(dto.getIadmin());
        entity.setAdminEntity(adminOpt.get());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setImportance(dto.getImportance());
        entity.setBoardView(0L);

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
        return BoardInsRes.builder()
                .iadmin(result.getAdminEntity().getIadmin())
                .ctnt(result.getCtnt())
                .title(result.getTitle())
                .importance(result.getImportance())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .boardView(0)
                .build();
    }

    int delYn = 0;
    int importance = 0;

    /** 전체리스트 출력  및 제목 검색 **/
    public BoardRes selBoard(Pageable page, String title) {

        page = PageRequest.of(page.getPageNumber(), page.getPageSize(),Sort.by(Sort.Direction.DESC, "iboard"));

        int pageSize = page.getPageSize();

        int row = 10;
        int importanceRow = 3;

        Page<BoardEntity> list;

        if (title == null){
           if (selImportanceBoard().size() < importanceRow) {
               pageSize = row - selImportanceBoard().size();
               page = PageRequest.of(page.getPageNumber() ,pageSize, Sort.by(Sort.Direction.DESC, "iboard"));
               list = BOARD_REP.findByImportanceAndDelYn(0,0,page);
           } else {
               page = PageRequest.of(page.getPageNumber(), pageSize - 3 , Sort.by(Sort.Direction.DESC, "iboard"));
               list = BOARD_REP.findByImportanceAndDelYn( 0,0,page);
           }
        } else {
            pageSize = row - selImportanceBoard().size();
            page = PageRequest.of(page.getPageNumber(), pageSize, Sort.by(Sort.Direction.DESC,"iboard"));
            list = BOARD_REP.findByTitleContainingAndImportanceAndDelYn(title, 0,0, page);
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

        PagingUtils utils = new PagingUtils(page.getPageNumber(), (int)list.getTotalElements(), pageSize);

        return BoardRes.builder()
                .page(utils)
                .list(result)
                .build();
    }

    /** 중요공지 리스트 출력 **/
    public List<BoardVo> selImportanceBoard() {
        importance = 1;
       List<BoardEntity> list = BOARD_REP.findAllByImportanceAndDelYn(1,0, Sort.by(Sort.Direction.DESC, "createdAt"));

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
    public BoardEntity delBoardYn(BoardDelYnDto dto) {
        BoardEntity entity = BOARD_REP.findById(dto.getIboard()).get();
        entity.setDelYn(1);
        return BOARD_REP.save(entity);

    }

    /** 공지사항 수정 **/
    public BoardInsVo updBoard (BoardUpdDto dto, List<MultipartFile> pics) {
        BoardEntity entity = BOARD_REP.getReferenceById(dto.getIboard());
        entity.setTitle(dto.getTitle());
        entity.setCtnt(dto.getCtnt());
        entity.setImportance(dto.getImportance());
        entity.setUpdatedAt(LocalDateTime.now());


        BoardEntity result = BOARD_REP.save(entity);


        List<Long> ipic = dto.getIpic();

        List<BoardPicEntity> picList = new ArrayList<>();

        if (result != null) {
            if (pics != null) {
                for (Long picPk : ipic) {
                    BoardPicEntity pic = BOARD_PIC_REP.findById(picPk).get();
                    BOARD_PIC_REP.delete(pic);
                }

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
                    picList.add(picEntity);
                    BOARD_PIC_REP.save(picEntity);
                }
            } else if (pics == null) {
                for (Long picPk : ipic) {
                    BoardPicEntity pic = BOARD_PIC_REP.findById(picPk).get();
                    BOARD_PIC_REP.delete(pic);
                }
            }
        }
        return BoardInsVo.builder()
                .iboard(entity.getIboard())
                .iadmin(entity.getAdminEntity().getIadmin())
                .title(entity.getTitle())
                .ctnt(entity.getCtnt())
                .updatedAt(entity.getUpdatedAt())
                .importance(entity.getImportance())
                .pisc(picList)
                .build();
    }

    /** 공지사항 디테일 및 조회수 업로드 **/
    public BoardDetailVo selDetailBoard (Long iboard) {
        BoardEntity entity = BOARD_REP.findById(iboard).get();
        List<BoardPicEntity> picEntity = BOARD_PIC_REP.findByBoardEntity(entity);

        List<BoardPicVo> picList = new ArrayList<>();
        for (int i = 0; i < picEntity.size(); i++) {
         BoardPicVo vo = new BoardPicVo();
         vo.setIpic(picEntity.get(i).getIpic());
         vo.setPic(picEntity.get(i).getPic());
         picList.add(vo);

        }

        BOARD_REP.updateView(entity.getIboard());

        BoardDetailVo vo = BoardDetailVo.builder()
                .iboard(entity.getIboard())
                .iadmin(entity.getAdminEntity().getIadmin())
                .title(entity.getTitle())
                .ctnt(entity.getCtnt())
                .importance(entity.getImportance())
                .pisc(picList)
                .build();
        return vo;
    }
}
