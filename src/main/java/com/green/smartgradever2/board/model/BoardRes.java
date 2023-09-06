package com.green.smartgradever2.board.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
public class BoardRes {
    private PagingUtils page;
    private List<BoardVo> list;
}
