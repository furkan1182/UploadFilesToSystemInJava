package com.ets.challenge.dto;

import com.ets.challenge.entity.FileEntity;

public class FileDTO {

    private final Long id;

    private final String name;

    private final String path;

    private final Long size;

    private final String extension;

    public FileDTO(Long id, String name, String path, Long size, String extension) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
        this.extension = extension;
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Long getSize() {
        return size;
    }

    public String getExtension() {
        return extension;
    }


    public Long getId() {
        return id;
    }

    public static FileDTO toDTO(FileEntity fileEntity) {
        return new FileDTO(fileEntity.getId(), fileEntity.getName(), fileEntity.getPath(), fileEntity.getSize(), fileEntity.getExtension());
    }

    @Override
    public String toString() {
        return "FileDTO{" + "name='" + name + '\'' + ", path='" + path + '\'' + ", size=" + size + ", extension='" + extension + '\'' + '}';
    }


}
