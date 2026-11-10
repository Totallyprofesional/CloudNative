package com.example.bdget.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.bdget.model.Asset;

public interface S3Repository {
    List<Asset> listObjectsInBucket(String bucket) throws IOException;

    S3ObjectInputStream getObject(String bucketName, String fileName) throws IOException;

    byte[] downloadFile(String bucketName, String fileName) throws IOException;

    void moveObject(String bucketName, String fileName, String destinationFileKey);

    void deleteObject(String bucketName, String fileName);

    String uploadFile(String bucketName, String fileName, File fileOb);
}