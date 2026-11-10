package com.example.bdget.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.example.bdget.model.Asset;
import com.example.bdget.repository.S3Repository;

@Service
public class AwsServiceImpl implements AwsService {
    private static final Logger log = LoggerFactory.getLogger(AwsServiceImpl.class);
    private S3Repository s3Repository;

    @Autowired
    public AwsServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    @Override
    public List<Asset> getS3Files(String bucket) throws IOException {
        return s3Repository.listObjectsInBucket(bucket);
    }

    @Override
    public String getS3FileContent(String bucketName, String fileName) throws IOException {
        return getAsString(s3Repository.getObject(bucketName, fileName));
    }

    private static String getAsString(InputStream is) throws IOException {
        if (is == null)
            return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StringUtils.UTF8));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            is.close();
        }
        return sb.toString();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) throws IOException {
        return s3Repository.downloadFile(bucketName, fileName);
    }

    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey) {
        s3Repository.moveObject(bucketName, fileKey, destinationFileKey);
    }

    @Override
    public void deleteObject(String bucketName, String fileKey) {
        s3Repository.deleteObject(bucketName, fileKey);
    }

    @Override
    public String uploadFile(String bucketName, String filePath, MultipartFile file) {
        File fileObj = convertMultiPartFiletoFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        return s3Repository.uploadFile(bucketName, filePath + fileName, fileObj);
    }

    @Override
    public String uploadFile(String bucketName, String carpeta, File archivo) {
        return s3Repository.uploadFile(bucketName, carpeta + archivo.getName(), archivo);
    }

    private File convertMultiPartFiletoFile(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertFile;
    }
}