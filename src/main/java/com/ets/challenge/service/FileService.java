package com.ets.challenge.service;

import com.ets.challenge.dto.FileDTO;
import com.ets.challenge.entity.FileEntity;
import com.ets.challenge.exception.FileCopyException;
import com.ets.challenge.exception.FileInvalidExtensionException;
import com.ets.challenge.exception.FileNotFoundException;
import com.ets.challenge.exception.FileStoreException;
import com.ets.challenge.repository.FileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Path fileStorageLocation;
    private final FileRepository fileRepository;

    private static final Set<String> ACCEPTABLE_EXTENSIONS = Set.of("png", "jpeg", "docx", "pdf", "jpg", "xlsx");

    public FileService(FileRepository fileRepository) {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStoreException(fileStorageLocation.toString());
        }
    }

    public void upload(MultipartFile file) {
        checkExtension(file);
        String fileNameWithExtension = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(fileNameWithExtension);
        String fileName = FilenameUtils.removeExtension(fileNameWithExtension);
        Path savedFilePath = saveFileToSystem(file, fileNameWithExtension);
        FileEntity fileEntity = new FileEntity(fileName, savedFilePath.toString(), file.getSize(), extension);
        fileRepository.save(fileEntity);
    }

    private void checkExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!ACCEPTABLE_EXTENSIONS.contains(extension)) {
            throw new FileInvalidExtensionException(extension);
        }
    }

    private Path saveFileToSystem(MultipartFile file, String fileNameWithExtension) {
        Path targetLocation = this.fileStorageLocation.resolve(fileNameWithExtension);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileCopyException(file.getOriginalFilename());
        }
        return targetLocation;
    }


    public List<FileDTO> getFiles() {
        return fileRepository.findAll().stream().map(FileDTO::toDTO).collect(Collectors.toList());
    }

    public byte[] getFileByteArray(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(fileId));
        File file = FileUtils.getFile(fileEntity.getPath());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(fileId));
        File file = FileUtils.getFile(fileEntity.getPath());
        file.delete();
        fileRepository.delete(fileEntity);
    }

    public void replaceFile(Long oldFileId, MultipartFile newFile) {
        checkExtension(newFile);
        FileEntity oldFileEntity = fileRepository.findById(oldFileId).orElseThrow(() -> new FileNotFoundException(oldFileId));

        File oldFile = FileUtils.getFile(oldFileEntity.getPath());
        oldFile.delete();

        String fileNameWithExtension = StringUtils.cleanPath(newFile.getOriginalFilename());
        String extension = FilenameUtils.getExtension(fileNameWithExtension);
        String fileName = FilenameUtils.removeExtension(fileNameWithExtension);
        Path savedFilePath = saveFileToSystem(newFile, fileNameWithExtension);

        oldFileEntity.setName(fileName);
        oldFileEntity.setExtension(extension);
        oldFileEntity.setPath(savedFilePath.toString());
        oldFileEntity.setSize(newFile.getSize());
        fileRepository.save(oldFileEntity);
    }
}
