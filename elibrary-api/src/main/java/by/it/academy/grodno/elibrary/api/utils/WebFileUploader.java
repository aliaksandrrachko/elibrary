package by.it.academy.grodno.elibrary.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface WebFileUploader {

    URL uploadFile(MultipartFile file, DownloadFileType type, String fileName) throws IOException;
    void deleteFile(URL url);
}
