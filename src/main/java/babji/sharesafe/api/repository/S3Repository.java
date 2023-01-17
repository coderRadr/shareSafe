package babji.sharesafe.api.repository;

import babji.sharesafe.api.models.FileMetadata;
import babji.sharesafe.api.models.FileObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
public class S3Repository {

    //TODO Update below bucket name
    private static final String BUCKET_NAME = "Document/";
    private final AmazonS3 s3Client;


    @Autowired
    public S3Repository(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void createFileMetaData(FileMetadata fileMetadata) {
        int hashCode = fileMetadata.hashCode();
        SetObjectTaggingRequest objectTagRequest = createObjectTaggingRequest(fileMetadata, hashCode);
        SetObjectTaggingResult response = this.s3Client.setObjectTagging(objectTagRequest);
        log.info("Created File MetaData, Name: {}, Signature: {}, Id: {} (HashCode: {}), responseId: {}",
                fileMetadata.getName(), fileMetadata.getSignature(), fileMetadata.getId(),
                hashCode, response.getVersionId());
    }


    public List<FileMetadata> listAllFiles(Integer limit) {
        VersionListing filesList = this.s3Client.listVersions(BUCKET_NAME, null,
                null, null, null, limit);
        List<S3VersionSummary> versionSummaries = filesList.getVersionSummaries();

        ObjectMapper mapper = new ObjectMapper();

        VersionSummaryToFileMetadata s3SummaryToMetadata = (s3VersionSummary) -> {
            String key = s3VersionSummary.getKey();
            ObjectMetadata objectMetadata = s3Client.getObjectMetadata(BUCKET_NAME, key);
            Map<String, String> userMetadata = objectMetadata.getUserMetadata();
            return mapper.convertValue(userMetadata, FileMetadata.class);
        };
        return versionSummaries.stream().map(s3SummaryToMetadata::accept).toList();
    }

    public void uploadFile(String fileId, byte[] fileContent) {
        ObjectMetadata objectMetadata = this.s3Client.getObjectMetadata(BUCKET_NAME, fileId);
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        PutObjectResult response = this.s3Client.putObject(BUCKET_NAME, fileId, inputStream, objectMetadata);
        log.info("Successfully uploaded file with fileId: {}, responseId-versionId: {}", fileId, response.getVersionId());
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

    private SetObjectTaggingRequest createObjectTaggingRequest(FileMetadata fileMetadata, int hashCode) {
        List<Tag> tagSet = new ArrayList<>();
        tagSet.add(new Tag("Desc", fileMetadata.getDescr()));
        tagSet.add(new Tag("Name", fileMetadata.getName()));
        tagSet.add(new Tag("Signature", fileMetadata.getSignature()));
        tagSet.add(new Tag("Id", String.valueOf(fileMetadata.getId())));
        tagSet.add(new Tag("OwndedBy", String.valueOf(fileMetadata.getOwnedBy())));
        tagSet.add(new Tag("MalwareVerdict", fileMetadata.getMalwareVerdict().getValue()));
        ObjectTagging objectTagging = new ObjectTagging(tagSet);


        String uuid = String.valueOf(hashCode);
        return new SetObjectTaggingRequest(BUCKET_NAME, uuid, objectTagging);
    }


    @FunctionalInterface
    public interface VersionSummaryToFileMetadata {
        FileMetadata accept(S3VersionSummary s3VersionSummary);
    }


}
