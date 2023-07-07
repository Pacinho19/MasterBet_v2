package pl.pacinho.MasterBet.utils;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class MediaResponseEntityUtils {

    public ResponseEntity get(String fileName, MediaType mediaType) {

        try {
            return ResponseEntity.ok().contentType(mediaType)
                    .body(Files.readAllBytes(new File(
                            getClass()
                                    .getClassLoader()
                                    .getResource(fileName)
                                    .getFile())
                            .toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
