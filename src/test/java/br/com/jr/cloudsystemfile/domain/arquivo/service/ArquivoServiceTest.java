package br.com.jr.cloudsystemfile.domain.arquivo.service;

import br.com.jr.cloudsystemfile.infra.exception.InvalidOperationException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ArquivoServiceTest {

    private AutoCloseable closeable;

    @Mock
    private S3Object s3Object;

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private ListObjectsV2Result listObjectsV2Result;

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

        // Cenários
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "conteúdo do arquivo".getBytes()
        );

        // Acao
        String fileName = arquivoService.upload(file);

        // Verificacoes
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

        // Cenarios
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


        // Ação
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> arquivoService.upload(file),
                "Deveria lançar InvalidOperationException"
        );


        // Verificaoes
        assertTrue(exception.getMessage().contains("Erro ao subir o arquivo"));
        verify(s3Client, times(1)).putObject(
                anyString(),
                anyString(),
                any(),
                any(ObjectMetadata.class)
        );
    }

    @Test
    void deve_realizar_delete_arquivo() {

        // Cenario
        String fileName = "arquivo-teste.txt";
        doNothing().when(s3Client).deleteObject("test-bucket", fileName);

        // Acao
        arquivoService.delete(fileName);

        // Verificacao
        verify(s3Client, times(1)).deleteObject("test-bucket", fileName);
    }

    @Test
    void nao_deve_realizar_delete_arquivo() {

        String fileName = "arquivo-inexistente.txt";
        doThrow(new RuntimeException("Erro ao acessar S3")).when(s3Client).deleteObject("test-bucket", fileName);

        // Acao
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> arquivoService.delete(fileName),
                "Deveria lançar InvalidOperationException"
        );

        assertTrue(exception.getMessage().contains("Erro ao deletar o arquivo"));
        verify(s3Client, times(1)).deleteObject("test-bucket", fileName);
    }

    @Test
    void deve_realizar_download_arquivo() {

        // Cenário
        String fileName = "arquivo-teste.txt";
        InputStream mockInputStream = new ByteArrayInputStream("conteúdo do arquivo".getBytes());
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(mockInputStream, null);

        when(s3Client.getObject("test-bucket", fileName)).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);

        // Ação
        InputStream result = arquivoService.download(fileName);

        // Verificacao
        assertNotNull(result, "O resultado tem que ser um stream");
        verify(s3Client, times(1)).getObject("test-bucket", fileName);
    }

    @Test
    void nao_deve_realizar_download_arquivo() {

        // Cenário
        String fileName = "arquivo-inexistente.txt";

        when(s3Client.getObject("test-bucket", fileName))
                .thenThrow(new RuntimeException("Erro ao acessar S3"));

        // Ação
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> arquivoService.download(fileName),
                "Deveria lançar InvalidOperationException"
        );

        // Verificacoes
        assertTrue(exception.getMessage().contains("Erro ao baixar o arquivo"));
        verify(s3Client, times(1)).getObject("test-bucket", fileName);

    }

    @Test
    void deve_realizar_listagem_arquivo() {

        // Cenarios
        S3ObjectSummary file1 = new S3ObjectSummary();
        file1.setKey("file1.txt");

        S3ObjectSummary file2 = new S3ObjectSummary();
        file2.setKey("file2.txt");

        List<S3ObjectSummary> mockSummaries = List.of(file1, file2);

        when(s3Client.listObjectsV2("test-bucket")).thenReturn(listObjectsV2Result);
        when(listObjectsV2Result.getObjectSummaries()).thenReturn(mockSummaries);

        // Ação
        List<String> result = arquivoService.listFiles();

        // Verificacoes
        assertNotNull(result, "A lista de arquivos não deve ser nula");
        assertEquals(2, result.size(), "O tamanho da lista de arquivos deve ser igual a 2");
        assertTrue(result.contains("file1.txt"), "A lista deve retornar o arquivo 'file1.txt'");
        assertTrue(result.contains("file2.txt"), "A lista deve retornar o arquivo 'file2.txt'");

        verify(s3Client, times(1)).listObjectsV2("test-bucket");

    }

    @Test
    void nao_deve_realizar_listagem_arquivo_quando_nao_houver_arquivos() {

        // Cenarios
        when(s3Client.listObjectsV2("test-bucket")).thenReturn(listObjectsV2Result);
        when(listObjectsV2Result.getObjectSummaries()).thenReturn(Collections.emptyList());

        // Ação
        List<String> result = arquivoService.listFiles();

        // Verificacoes
        assertNotNull(result, "A lista de arquivos não deve ser nula");
        assertTrue(result.isEmpty(), "A lista de arquivos deve estar vazia");

        verify(s3Client, times(1)).listObjectsV2("test-bucket");

    }

    @Test
    void nao_deve_realizar_listagem_arquivo_quando_falhar_a_listagem_de_arquivos() {

        // Cenario
        when(s3Client.listObjectsV2("test-bucket"))
                .thenThrow(new RuntimeException("Erro ao acessar S3"));

        // Ação
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                arquivoService::listFiles,
                "Deveria lançar InvalidOperationException"
        );

        // Verificações
        assertTrue(exception.getMessage().contains("Erro ao listar arquivos"));
        verify(s3Client, times(1)).listObjectsV2("test-bucket");

    }


}