package com.example.s3ex.controller;


import com.example.s3ex.dto.BoardDTO;
import com.example.s3ex.service.BoardService;
import com.example.s3ex.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final S3Uploader s3Uploader;

    @GetMapping("/")
    public String list(Model model) {
        List<BoardDTO> list = boardService.getBoardList();
        log.info("list = {}", list);
        model.addAttribute("list", list);
        return "board/list";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "board/write";
    }

    @PostMapping("/write")
    public String write(BoardDTO dto, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileUrl = s3Uploader.uploadFile("upload", file.getInputStream(), file.getOriginalFilename());
            dto.setFilename(file.getOriginalFilename());
            dto.setFileUrl(fileUrl);
            log.info("dto : {}", dto);
        }
        boardService.saveBoard(dto);
        return "redirect:/";
    }
}