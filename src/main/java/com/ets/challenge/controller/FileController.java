package com.ets.challenge.controller;

import com.ets.challenge.dto.FileDTO;
import com.ets.challenge.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) {
        fileService.upload(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getFiles() {
        List<FileDTO> fileDTOs = fileService.getFiles();
        return ResponseEntity.ok(fileDTOs);
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFileByteArray(@PathVariable Long fileId) {
        byte[] fileByteArray = fileService.getFileByteArray(fileId);
        return ResponseEntity.ok(fileByteArray);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<?> replaceFile(@PathVariable Long fileId,
                                         @RequestParam MultipartFile file) {
        fileService.replaceFile(fileId,file);
        return ResponseEntity.ok().build();
    }
}
