package com.green.smartgradever2.board;

import com.green.smartgradever2.admin.AdminRepository;
import com.green.smartgradever2.board.model.BoardInsDto;
import com.green.smartgradever2.board.repository.BoardPicRepository;
import com.green.smartgradever2.board.repository.BoardRepository;
import com.green.smartgradever2.entity.AdminEntity;
import com.green.smartgradever2.entity.BoardEntity;
import com.green.smartgradever2.entity.BoardPicEntity;
import com.green.smartgradever2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    @Value("${file.dir}")
    private String fileDir;

    private final AdminRepository ADMIN_REP;
    private final BoardRepository BOARD_REP;
    private final BoardPicRepository BOARD_PIC_REP;

    /** insert with pics **/
    public BoardEntity insBoard (BoardInsDto dto, List<MultipartFile> pics) {
        BoardEntity entity = new BoardEntity();
        BoardPicEntity picResult = new BoardPicEntity();

        Optional<AdminEntity> adminOpt = ADMIN_REP.findById(dto.getIadmin());
        entity.setIadmin(adminOpt.get());
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
}
