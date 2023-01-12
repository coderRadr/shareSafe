package babji.sharesafe.api.service;

import babji.sharesafe.api.models.FileMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

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
        return false;
    }

    public String downloadFile(String fileId) {
        return "hello.txt";
    }

    public void deleteFileById(String fileId) {

    }

}
