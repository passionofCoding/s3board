package com.example.s3ex.service;

import com.example.s3ex.dto.BoardDTO;
import com.example.s3ex.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public void saveBoard(BoardDTO dto) {
        boardMapper.insertBoard(dto);
    }

    public List<BoardDTO> getBoardList() {
        return boardMapper.getBoardList();
    }
}
