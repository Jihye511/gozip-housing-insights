package com.ssafy.local.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {
    private final Path uploadDir = Paths.get("uploads");

    @PostConstruct
    public void init() throws IOException {
        if (Files.notExists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    /**
     * 업로드한 파일을 저장하고, 접근 가능한 URL 경로를 반환합니다.
     * 예: "/uploads/uuid-원본이름.jpg"
     */
    public String store(MultipartFile file) throws IOException {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename;
    }
    
    /**
     * 주어진 filename 을 실제 저장소에서 찾아서 Resource 로 반환
     */
    public Resource loadFileAsResource(String storedPath) {
        try {
            // 1) DB 에 저장된 전체 경로(절대 or 상대)에서
            //    마지막 요소(순수 파일명)만 꺼냅니다.
            String filename = Paths.get(storedPath)
                                   .getFileName()
                                   .toString();  // → "61c48835-41d2-4793-b082-a49d2243ed48.png"

            // 2) 실제 업로드 루트 + 순수 파일명 → 실제 저장된 파일 경로
            Path filePath = uploadDir
                                .resolve(filename)
                                .normalize();

            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("파일 경로가 잘못되었습니다: " + storedPath, ex);
        }
    }
}
