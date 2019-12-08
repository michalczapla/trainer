package com.czaplon.trainer.service.storage;

import com.czaplon.trainer.config.StorageProperties;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    private Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    private final Set<String> allowedFiles = new HashSet<>(Arrays.asList(
            "image/bmp",
            "image/cgm",
            "image/gif",
            "image/ief",
            "image/jpeg",
            "image/tiff",
            "image/png",
            "image/svg+xml",
            "image/vnd.djvu",
            "image/vnd.wap.wbmp",
            "image/x-cmu-raster",
            "image/x-icon",
            "image/x-portable-anymap",
            "image/x-portable-bitmap",
            "image/x-portable-graymap",
            "image/x-portable-pixmap",
            "image/x-rgb"));

    @Override
    public String store(MultipartFile file) throws IOException, StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        logger.info(file.getBytes().toString());
        String hashedFilename = DigestUtils.md5Hex(file.getBytes().toString()) + "." + FilenameUtils.getExtension(filename);
//        logger.info(file.getContentType());
//        logger.info("TIKA: "+TIKAFileAnalizer.checkMIMEType(file));
        if (allowedFiles.contains(TIKAFileAnalizer.checkMIMEType(file))) {


            try {
                if (file.isEmpty()) {
                    logger.warn("Failed to store empty file " + filename);
                    throw new StorageException("Failed to store empty file " + filename);
                }
                if (filename.contains("..")) {
                    // This is a security check
                    throw new StorageException(
                            "Cannot store file with relative path outside current directory "
                                    + filename);
                }
                try (InputStream inputStream = file.getInputStream()) {
                    logger.info("File was successfully uploaded. Filename "+ hashedFilename);
                    Files.copy(inputStream, this.rootLocation.resolve(hashedFilename),
                            StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                logger.warn("Failed to store file " + filename);
                throw new StorageException("Failed to store file " + filename, e);
            }
        } else {
            logger.warn("Unsupported file");
            throw new StorageException("Unsupported file type!");
        }
        return hashedFilename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String filename) {
//        Path file = load(this.rootLocation.resolve(filename));
//        FileSystemUtils.deleteRecursively(File. filename);
        try {
            Files.delete(this.rootLocation.resolve(filename));
        } catch (IOException e) {
            logger.warn("Error during file delete");
            throw new StorageException("Error during file delete");
        }
    }

    @Override
    public void markArchive(String filename) {
        try {
            Path originalPath = this.rootLocation.resolve(filename);
            Path archivedPath = this.rootLocation.resolve("(archived)"+filename);
            logger.info("File "+originalPath+" was archived");
            Files.move(originalPath,archivedPath);
        } catch (IOException e) {
            logger.warn("Error during renaming the file");
            throw new StorageException("Error during renaming the file");
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


}
