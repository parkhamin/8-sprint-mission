package com.sprint.mission.discodeit.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
        // record는 Java가 생성자 자동 생성해줌.
        /*public BinaryContentCreateRequest(
                String fileName,
                String contentType,
                byte[] bytes
        ) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.bytes = bytes;
        }*/

        public static BinaryContentCreateRequest fileToRequest(MultipartFile file) {
            try{
                return new BinaryContentCreateRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e)  {
                throw new IllegalArgumentException("파일 변환 실패" , e);
            }
        }
}
