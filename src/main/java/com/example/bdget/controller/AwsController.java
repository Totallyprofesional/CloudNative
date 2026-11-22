package com.example.bdget.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bdget.model.Asset;
import com.example.bdget.service.AwsService;
import com.example.bdget.service.CompraService;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping(value = "/s3")
public class AwsController {
    private AwsService awsService;
    private CompraService compraService;
    private final String bucket;

    @Autowired
    public AwsController(AwsService awsService, CompraService compraService, @Value("${aws.s3.bucket}") String bucket) {
        this.awsService = awsService;
        this.compraService = compraService;
        this.bucket = bucket;
    }

    @GetMapping("/getS3FileContent")
    public ResponseEntity<String> getS3FileContent(
            @RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "fileName") String fileName) throws IOException {

        if (bucketName == null || bucketName.isBlank() || fileName == null || fileName.isBlank()) {
            return new ResponseEntity<>("bucketName y fileName son obligatorios", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(awsService.getS3FileContent(bucketName, fileName), HttpStatus.OK);
    }

    @GetMapping("/listS3Files")
    public ResponseEntity<List<Asset>> getS3Files(@RequestParam(value = "bucketName") String bucketName)
            throws IOException {

        if (bucketName == null || bucketName.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        List<Asset> list = new ArrayList<>();
        HttpStatus status = HttpStatus.OK;
        try {
            list = awsService.getS3Files(bucketName);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(list, status);
    }

    @GetMapping("/downloadS3File")
    public ResponseEntity<ByteArrayResource> downloadS3File(@RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "filePath") String filePath,
            @RequestParam(value = "fileName") String fileName)
            throws IOException {

        if (bucketName == null || bucketName.isBlank() || filePath == null || filePath.isBlank()
                || fileName == null || fileName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        byte[] data = awsService.downloadFile(bucketName, filePath + fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/deleteObject")
    public ResponseEntity<String> deleteFile(@RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "fileName") String fileName) {

        if (bucketName == null || bucketName.isBlank() || fileName == null || fileName.isBlank()) {
            return new ResponseEntity<>("bucketName y fileName son obligatorios", HttpStatus.BAD_REQUEST);
        }
        awsService.deleteObject(bucketName, fileName);
        return new ResponseEntity<>("File deleted", HttpStatus.OK);
    }

    @GetMapping("/moveFile")
    public ResponseEntity<String> moveFile(@RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "fileName") String fileKey,
            @RequestParam(value = "fileNameDest") String fileKeyDest) {

        if (bucketName == null || bucketName.isBlank() || fileKey == null || fileKey.isBlank()
                || fileKeyDest == null || fileKeyDest.isBlank()) {
            return new ResponseEntity<>("bucketName, fileName y fileNameDest son obligatorios", HttpStatus.BAD_REQUEST);
        }
        awsService.moveObject(bucketName, fileKey, fileKeyDest);
        return new ResponseEntity<>("File moved", HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "bucketName") String bucketName,
            @RequestParam(value = "filePath") String filePath,
            @RequestParam(value = "file") MultipartFile file) {

        if (bucketName == null || bucketName.isBlank() || filePath == null || filePath.isBlank()
                || file == null || file.isEmpty()) {
            return new ResponseEntity<>("bucketName, filePath y file son obligatorios", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(awsService.uploadFile(bucketName, filePath, file), HttpStatus.OK);
    }

    @PostMapping("/uploadResumen")
    public ResponseEntity<String> uploadResumen(@RequestParam Long compraId, @RequestParam String usuario) {

        if (compraId == null || usuario == null || usuario.isBlank()) {
            return new ResponseEntity<>("compraId y usuario son obligatorios", HttpStatus.BAD_REQUEST);
        }
        File archivo = compraService.generarResumen(compraId);
        Long eventoId = compraService.obtenerEventoIdPorCompra(compraId);
        String ruta = "evento" + eventoId + "/" + usuario + "/";
        return new ResponseEntity<>(awsService.uploadFile(bucket, ruta, archivo), HttpStatus.OK);
    }
}