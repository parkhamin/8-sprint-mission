package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Properties s3Properties;
  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final Long presignedUrlExpiration;

  public S3BinaryContentStorage(S3Properties s3Properties) {
    this.s3Properties = s3Properties;
    this.accessKey = s3Properties.getAccessKey();
    this.secretKey = s3Properties.getSecretKey();
    this.region = s3Properties.getRegion();
    this.bucket = s3Properties.getBucket();
    this.presignedUrlExpiration = s3Properties.getPresignedUrlExpiration();
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();

    try {
      S3Client s3Client = getS3Client();

      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

      return binaryContentId;
    } catch (RuntimeException e) {
      throw new RuntimeException("Failed to put binary content", e);
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    try {
      S3Client s3Client = getS3Client();

      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();

      byte[] bytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
      return new ByteArrayInputStream(bytes);
    } catch (RuntimeException e) {
      throw new RuntimeException("Failed to get binary content", e);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto file) {
    String key = file.id().toString();
    String contentType = file.contentType();
    String presignedUrl = generatePresignedUrl(key, contentType);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(presignedUrl));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    try (S3Presigner s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build()) {

      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .responseContentType(contentType)
          .build();

      GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
          getObjectPresignRequest);
      return presignedGetObjectRequest.url().toString();
    } catch (RuntimeException e) {
      throw new RuntimeException("Failed to presign url", e);
    }
  }
}
