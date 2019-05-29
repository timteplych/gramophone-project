package com.geekbrains.gramophone.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class FileUploader {

    public boolean upload(String username, MultipartFile file) {
        String path = "uploads/" + username;
        File uploadFile = new File(path);

        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        try (FileChannel channel = (FileChannel) Files.newByteChannel(
                Paths.get(path + "/" + file.getOriginalFilename()),
                StandardOpenOption.WRITE,
                StandardOpenOption.READ,
                StandardOpenOption.CREATE)
        ) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.getSize());
            buffer.put(file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
