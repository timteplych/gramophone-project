package com.geekbrains.gramophone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class UploadService {

    private GenreService genreService;

    @Autowired
    public void setGenreService(GenreService genreService) {
        this.genreService = genreService;
    }

    public boolean upload(String username, MultipartFile file, String pathPartDestination) {
        String path = pathPartDestination + username;
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
            return false;
        }
        return true;
    }

    public boolean remove(String username, MultipartFile file, String pathPartDestination) {
        String path = pathPartDestination + username + "/" + file.getOriginalFilename();
        File uploadFile = new File(path);

        if (uploadFile.exists()) {
            uploadFile.delete();
            return true;
        }
        return false;
    }


}
