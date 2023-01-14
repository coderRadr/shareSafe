package babji.sharesafe.api.controller;

import babji.sharesafe.api.models.FileMetadata;
import babji.sharesafe.api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class FilesController {

    private final FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public FileMetadata createFile(@RequestBody FileMetadata fileMetadata) {
        return fileService.createFile(fileMetadata);
    }

    @GetMapping("/files")
    public List<FileMetadata> listFiles(@RequestParam(required = true) Integer limit) {
        return fileService.getAllFiles(limit);
    }


    @PostMapping(value = "/files/{fileId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean uploadFile(@PathVariable("fileId") String fileId, @RequestBody MultipartFile file) throws IOException {
        byte[] fileContent = file.getInputStream().readAllBytes();
        return fileService.uploadFile(fileId, fileContent);
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity downloadFile(@PathVariable("fileId") String fileId) {
        String filePath = fileService.downloadFile(fileId);
        File file = new File(filePath);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"").body(file);
    }

    @DeleteMapping("/files/{fileId}")
    public void deleteFileById(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        fileService.deleteFileById(fileId);
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }

}
