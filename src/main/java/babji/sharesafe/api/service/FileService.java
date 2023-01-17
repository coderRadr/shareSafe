package babji.sharesafe.api.service;

import babji.sharesafe.api.models.FileMetadata;
import babji.sharesafe.api.models.FileObject;
import babji.sharesafe.api.repository.S3Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Service
public class FileService {

    private final S3Repository repository;


    @Autowired
    public FileService(S3Repository repository) {
        this.repository = repository;
    }

    public void createFile(FileMetadata fileMetadata) {
        this.repository.createFileMetaData(fileMetadata);
    }

    public List<FileMetadata> getAllFiles(Integer limit) {
        return this.repository.listAllFiles(limit);
    }

    public boolean uploadFile(String fileId, byte[] fileContent) {
        try {
            repository.uploadFile(fileId, fileContent);
            return true;
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            return false;
        }
    }

    public FileObject downloadFile(String fileId) throws FileNotFoundException {
        try {
            return repository.downloadFile(fileId);
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            throw new FileNotFoundException(exp.getMessage());
        }
    }

    public void deleteFileById(String fileId) {
        try {
            repository.deleteFile(fileId);
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
            throw new RestClientException(exp.getMessage());
        }
    }

}
