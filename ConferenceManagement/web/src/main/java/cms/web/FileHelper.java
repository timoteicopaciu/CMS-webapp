package cms.web;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHelper {
    public static String storeFile(MultipartFile file, String location) {
        File file1 = new File(location);
        try {
            Files.copy(file.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file1.toPath().toString();
        } catch (IOException ex) {
            System.out.println(ex);

        }
        return null;
    }

    public static Resource loadFileAsResource(String path) {
        try {
            Path filePath = Paths.get(path).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + path);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + path, ex);
        }
    }
}

