package br.com.jr.cloudsystemfile.domain.arquivo.controller;


import br.com.jr.cloudsystemfile.domain.arquivo.service.ArquivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
class ArquivoController {

    private final ArquivoService arquivoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             UriComponentsBuilder uriBuilder) {
        String fileName = arquivoService.upload(file);

        URI location = uriBuilder
                .path("/v1/roles/{id}")
                .buildAndExpand(fileName)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(fileName);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        arquivoService.delete(fileName);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStream fileStream = arquivoService.download(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> listFiles() {
        List<String> files = arquivoService.listFiles();
        return ResponseEntity.ok(files);
    }

}
