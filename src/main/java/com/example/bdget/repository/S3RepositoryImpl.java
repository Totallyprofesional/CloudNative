package com.example.bdget.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.example.bdget.model.Asset;

@Repository
public class S3RepositoryImpl implements S3Repository {
    private AmazonS3 amazonS3;

    @Autowired
    public S3RepositoryImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static final Logger log = LoggerFactory.getLogger(S3RepositoryImpl.class);

    @Override
    public List<Asset> listObjectsInBucket(String bucket) throws IOException {
        List<Asset> items = amazonS3.listObjectsV2(bucket).getObjectSummaries().stream()
                .parallel()
                .map(S3ObjectSummary::getKey)
                .map(key -> mapS3ToObject(bucket, key))
                .collect(Collectors.toList());
        log.info("se encontraron" + items.size() + "objectos en el bucket" + bucket);
        return items;
    }

    private Asset mapS3ToObject(String bucket, String key) {
        return Asset.builder()
                .name(amazonS3.getObjectMetadata(bucket, key).getUserMetaDataOf("name"))
                .key(key)
                .url(amazonS3.getUrl(bucket, key))
                .build();
    }

    @Override
    public S3ObjectInputStream getObject(String bucketName, String fileName) throws IOException {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            log.error("No hay buckets existentes");
            return null;
        }
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        return s3Object.getObjectContent();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(inputStream);
    }

    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, fileKey, bucketName, destinationFileKey);
        amazonS3.copyObject(copyObjectRequest);
        deleteObject(bucketName, fileKey);
    }

    @Override
    public void deleteObject(String bucketName, String fileKey) {
        amazonS3.deleteObject(bucketName, fileKey);
    }

    @Override
    public String uploadFile(String bucketName, String fileName, File fileObject) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObject));
        fileObject.delete();
        return "Archivo subido: " + fileName;
    }
}