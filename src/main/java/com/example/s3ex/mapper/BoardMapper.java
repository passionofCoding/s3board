package com.example.s3ex.mapper;

import com.example.s3ex.dto.BoardDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {
    @Insert("INSERT INTO board (title, content, filename, file_url) \n" +
            "        VALUES (#{title}, #{content}, #{filename}, #{fileUrl})")
    void insertBoard(BoardDTO board);

    @Select("SELECT id, title, content, filename, file_url AS fileUrl FROM board")
    List<BoardDTO> getBoardList();
}

