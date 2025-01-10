package br.com.jr.cloudsystemfile.domain.arquivo.service;

import br.com.jr.cloudsystemfile.infra.exception.InvalidOperationException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ArquivoServiceTest {

    private AutoCloseable closeable;

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private ArquivoService arquivoService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(arquivoService, "bucketName", "test-bucket");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void deve_realizar_upload_arquivo() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "conteúdo do arquivo".getBytes()
        );

        String fileName = arquivoService.upload(file);

        assertEquals("test.txt", fileName, "Arquivo com a extensão'test.txt'");

        verify(s3Client, times(1)).putObject(
                eq("test-bucket"),
                eq("test.txt"),
                any(),
                any(ObjectMetadata.class)
        );

        verify(s3Client, times(1)).putObject(
                anyString(),
                anyString(),
                any(),
                any()
        );

    }

    @Test
    void nao_deve_realizar_upload_arquivo() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "conteúdo do arquivo".getBytes()
        );

        doThrow(new RuntimeException("Erro ao acessar o bucket S3")).when(s3Client).putObject(
                anyString(),
                anyString(),
                any(),
                any(ObjectMetadata.class)
        );

        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> arquivoService.upload(file),
                "Deveria lançar InvalidOperationException"
        );

        assertTrue(exception.getMessage().contains("Erro ao subir o arquivo"));

        verify(s3Client, times(1)).putObject(
                anyString(),
                anyString(),
                any(),
                any(ObjectMetadata.class)
        );
    }


}