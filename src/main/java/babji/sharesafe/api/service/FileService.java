package babji.sharesafe.api.service;

import babji.sharesafe.api.models.FileMetadata;
import babji.sharesafe.api.models.FileObject;
import babji.sharesafe.api.repository.S3Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FileService {

    private final S3Repository repository;


    @Autowired
    public FileService(S3Repository repository) {
        this.repository = repository;
    }

    public FileMetadata createFile(FileMetadata fileMetadata) {
        return fileMetadata;
    }

    public List<FileMetadata> getAllFiles(Integer limit) {
        List<FileMetadata> response = List.of(FileMetadata.builder().id(1L)
                .descr("Sample description")
                .ownedBy(123L).name("Hello.txt").build());
        return response;
    }

    public boolean uploadFile(String fileId, byte[] fileContent) {
        try {
            repository.uploadFile(fileId, fileContent);
            return true;
        }catch (IOException exp){
            log.error(exp.getMessage(), exp);
            return false;
        }
    }

    public FileObject downloadFile(String fileId) throws FileNotFoundException {
        try {
            FileObject fileObject = repository.downloadFile(fileId);
            return fileObject;
        }catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            throw new FileNotFoundException(exp.getMessage());
        }
    }

    public void deleteFileById(String fileId) {
        try {
            repository.deleteFile(fileId);
        }catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            throw new RestClientException(exp.getMessage());
        }
    }

}
