package by.it.academy.grodno.elibrary.service.utils;

import by.it.academy.grodno.elibrary.api.utils.DownloadFileType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
@Slf4j
public class FileUploader {

    @Value("${upload-file-to-source-folder}")
    private static final boolean UPLOAD_FILE_TO_SOURCE_FOLDER = true;

    private static final String IMAGE_EXTENSION = ".png";

    private static final String SYSTEM_SEPARATOR = getSeparator();
    private static final String SOURCE_STATIC_RESOURCE_DIRECTORY;

    static {
        SOURCE_STATIC_RESOURCE_DIRECTORY = getSourcePathToProject() +
                "elibrary-controller" + SYSTEM_SEPARATOR +
                "src" + SYSTEM_SEPARATOR +
                "main" + SYSTEM_SEPARATOR +
                "resources" + SYSTEM_SEPARATOR +
                "static";
    }

    private static final String BOOK_COVER_PATH = "img/books/covers/";
    private static final String BOOK_COVER_FOLDER_PATH_SOURCE = SYSTEM_SEPARATOR + SOURCE_STATIC_RESOURCE_DIRECTORY +
            SYSTEM_SEPARATOR + "img" + SYSTEM_SEPARATOR + "books" + SYSTEM_SEPARATOR + "covers" + SYSTEM_SEPARATOR;

    private static final String USER_AVATAR_PATH = "img/users/avatars/";
    private static final String USER_AVATAR_FOLDER_PATH_SOURCE = SYSTEM_SEPARATOR + SOURCE_STATIC_RESOURCE_DIRECTORY +
         SYSTEM_SEPARATOR + "img" + SYSTEM_SEPARATOR + "users" + SYSTEM_SEPARATOR + "avatars" + SYSTEM_SEPARATOR;


    public URL uploadFile(MultipartFile file, DownloadFileType type, String fileName) throws IOException {
        String pathToTargetDirectory = getPathToTargetDirectory(type);
        uploadFile(file, pathToTargetDirectory, fileName);
        if (UPLOAD_FILE_TO_SOURCE_FOLDER){
            uploadFile(file, getPathToSourceDirectory(type), fileName);
        }
        String url = '/' +  getPathToContent(type) + fileName + IMAGE_EXTENSION;
        return new URL(url);
    }

    private static final String UNKNOWN_DOWNLOAD_FILE_TYPE_MESSAGE = "Unknown type of download file";

    private String getPathToTargetDirectory(DownloadFileType type) {
        if (type == DownloadFileType.BOOK_COVER) {
            return ResourceUtils.CLASSPATH_URL_PREFIX + "static/" + BOOK_COVER_PATH;
        } else if (type == DownloadFileType.USER_AVATAR){
            return ResourceUtils.CLASSPATH_URL_PREFIX + "static/" + USER_AVATAR_PATH;
        } else {
            throw new IllegalArgumentException(UNKNOWN_DOWNLOAD_FILE_TYPE_MESSAGE + type);
        }
    }

    private String getPathToSourceDirectory(DownloadFileType type) {
        if (type == DownloadFileType.BOOK_COVER) {
            return BOOK_COVER_FOLDER_PATH_SOURCE;
        } else if (type == DownloadFileType.USER_AVATAR){
            return USER_AVATAR_FOLDER_PATH_SOURCE;
        } else {
            throw new IllegalArgumentException(UNKNOWN_DOWNLOAD_FILE_TYPE_MESSAGE + type);
        }
    }

    private String getPathToContent(DownloadFileType type){
        if (type == DownloadFileType.BOOK_COVER) {
            return BOOK_COVER_PATH;
        } else if (type == DownloadFileType.USER_AVATAR){
            return USER_AVATAR_PATH;
        } else {
            throw new IllegalArgumentException(UNKNOWN_DOWNLOAD_FILE_TYPE_MESSAGE + type);
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
