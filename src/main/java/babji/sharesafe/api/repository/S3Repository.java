package babji.sharesafe.api.repository;

import babji.sharesafe.api.models.FileObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Repository
public class S3Repository {

    private final AmazonS3 s3Client;

    //TODO Update below bucket name
    private static final String BUCKET_NAME = "Document/";


    @Autowired
    public S3Repository(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String fileName, byte[] fileContent) throws IOException {
        String docPath = BUCKET_NAME + fileName;
        File file = new File(fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(fileContent);
        PutObjectResult response = s3Client.putObject(BUCKET_NAME, docPath,file);
    }

    public void deleteFile(String fileId) {
        s3Client.deleteObject(BUCKET_NAME, fileId);
    }

    public FileObject downloadFile(String fileId) throws IOException {
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileId);
        S3ObjectInputStream s3Content = s3Object.getObjectContent();
        byte[] fileContent = IOUtils.toByteArray(s3Content);
        FileObject fileObject = new FileObject();
        fileObject.setFileName(fileId);
        fileObject.setFileContent(fileContent);
        return fileObject;
    }
}
