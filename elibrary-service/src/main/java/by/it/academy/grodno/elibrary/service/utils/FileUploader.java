package by.it.academy.grodno.elibrary.service.utils;

import by.it.academy.grodno.elibrary.api.utils.DownloadFileType;
import by.it.academy.grodno.elibrary.api.utils.WebFileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileUploader implements WebFileUploader {

    @Value("${upload-file-to-source-folder}")
    private static boolean uploadFileToSourceFolder;

    private static final String STATIC_CONTENT_ROOT_URL = "http://localhost:8080";

    private static final String IMAGE_EXTENSION = ".png";

    private static final String SYSTEM_SEPARATOR = getSeparator();
    private static final String SOURCE_STATIC_RESOURCE_DIRECTORY;

    static {
        SOURCE_STATIC_RESOURCE_DIRECTORY = getSourcePathToProject() +
                "final-controller" + SYSTEM_SEPARATOR +
                "src" + SYSTEM_SEPARATOR +
                "main" + SYSTEM_SEPARATOR +
                "resources" + SYSTEM_SEPARATOR +
                "static";
    }

    private static final String BOOK_COVER_FOLDER_PATH = "static/img/books/covers/";
    private static final String BOOK_COVER_FOLDER_PATH_SOURCE = SYSTEM_SEPARATOR + SOURCE_STATIC_RESOURCE_DIRECTORY +
            "img" + SYSTEM_SEPARATOR + "books" + SYSTEM_SEPARATOR + "covers" + SYSTEM_SEPARATOR;

    private static final String USER_AVATAR_FOLDER_PATH = "static/img/users/avatars/";
    private static final String USER_AVATAR_FOLDER_PATH_SOURCE = SYSTEM_SEPARATOR + SOURCE_STATIC_RESOURCE_DIRECTORY +
            "img" + SYSTEM_SEPARATOR + "users" + SYSTEM_SEPARATOR + "avatars" + SYSTEM_SEPARATOR;


    @Override
    public URL uploadFile(MultipartFile file, DownloadFileType type, String fileName) throws IOException {
        String sourcePathToDirectory = getSourcePathToDirectory(type);
        uploadFile(file, sourcePathToDirectory, fileName);
        if (uploadFileToSourceFolder){
            uploadFile(file, getTargetPathToDirectory(type), fileName);
        }
        String url = STATIC_CONTENT_ROOT_URL + sourcePathToDirectory + fileName + IMAGE_EXTENSION;
        return new URL(url);
    }

    private String getSourcePathToDirectory(DownloadFileType type) {
        if (type == DownloadFileType.BOOK_COVER) {
            return ResourceUtils.CLASSPATH_URL_PREFIX + BOOK_COVER_FOLDER_PATH;
        } else if (type == DownloadFileType.USER_AVATAR){
            return ResourceUtils.CLASSPATH_URL_PREFIX + USER_AVATAR_FOLDER_PATH;
        } else {
            throw new IllegalArgumentException("Unknown type of download file" + type);
        }
    }

    private String getTargetPathToDirectory(DownloadFileType type) {
        if (type == DownloadFileType.BOOK_COVER) {
            return BOOK_COVER_FOLDER_PATH_SOURCE;
        } else if (type == DownloadFileType.USER_AVATAR){
            return USER_AVATAR_FOLDER_PATH_SOURCE;
        } else {
            throw new IllegalArgumentException("Unknown type of download file" + type);
        }
    }


    @Override
    public void deleteFile(URL url) {
        String sourcePath = ResourceUtils.CLASSPATH_URL_PREFIX + url.getPath();
        try {
            URL resourceUrl = ResourceUtils.getURL(sourcePath);
            Path path = Paths.get(resourceUrl.toString());
            Files.delete(path);
        } catch (FileNotFoundException e) {
            log.error("Resource [{}] doesn't find to deleting.", url);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void uploadFile(MultipartFile multipartFile, String pathToDirectory, String fileName) throws IOException {
        String filePath = pathToDirectory + fileName + IMAGE_EXTENSION;
        File file;
        try {
            file = ResourceUtils.getFile(filePath);
        } catch (FileNotFoundException e) {
            URL fileUrl = ResourceUtils.getURL(pathToDirectory);
            file = new File(fileUrl.getPath() + fileName + IMAGE_EXTENSION);
        }
        Path pathToFile = Paths.get(file.getPath());
        writeFile(multipartFile, pathToFile);
    }

    private void writeFile(MultipartFile multipartFile, Path destinationPath) throws IOException {
        try (InputStream in = multipartFile.getInputStream();
            OutputStream out = Files.newOutputStream(destinationPath)) {
            byte[] buffer = new byte[4 * 1024];
            while (in.read(buffer) > 0){
                out.write(buffer);
            }
        }
    }

    private static String getSourcePathToProject(){
        return System.getProperty("user.dir") + getSeparator();
    }

    private static String getSeparator(){
        return System.getProperty("file.separator");
    }
}
