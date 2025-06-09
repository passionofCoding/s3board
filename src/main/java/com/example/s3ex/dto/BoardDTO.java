package com.example.s3ex.dto;

    import lombok.Data;

    @Data
    public class BoardDTO {
        private Long id;
        private String title;
        private String content;
        private String filename;
        private String fileUrl;
    }

