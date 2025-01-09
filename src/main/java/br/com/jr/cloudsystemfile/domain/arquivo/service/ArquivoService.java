package br.com.jr.cloudsystemfile.domain.arquivo.service;


import br.com.jr.cloudsystemfile.infra.exception.InvalidOperationException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String upload(MultipartFile file) {

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata);

            return file.getOriginalFilename();
        } catch (Exception e) {
            throw new InvalidOperationException("Erro ao subir o arquivo: " + e.getMessage());
        }

    }

    public void delete(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            throw new InvalidOperationException("Erro ao deletar o arquivo: " + e.getMessage());
        }
    }


    public InputStream download(String fileName) {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new InvalidOperationException("Erro ao baixar o arquivo: " + e.getMessage());
        }
    }

    public List<String> listFiles() {
        try {
            ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
            List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();

            return objectSummaries.stream()
                    .map(S3ObjectSummary::getKey)
                    .toList();
        } catch (Exception e) {
            throw new InvalidOperationException("Erro ao listar arquivos: " + e.getMessage());
        }
    }
}
