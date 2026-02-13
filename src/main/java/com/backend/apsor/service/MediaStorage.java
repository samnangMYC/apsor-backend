package com.backend.apsor.service;
import com.backend.apsor.entities.StorageProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaStorage {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final StorageProps props;

    public PutResult put(String objectKey, MultipartFile file) {
        try  {
            String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();

            var req = PutObjectRequest.builder()
                    .bucket(props.bucket())
                    .key(objectKey)
                    .contentType(contentType)
                    .build();

            var body = RequestBody.fromContentProvider(() -> {
                try { return file.getInputStream(); }
                catch (IOException e) { throw new UncheckedIOException(e); }
            }, file.getSize(), contentType);

            var resp = s3.putObject(req, body);
            return new PutResult(resp.eTag(), file.getSize(), contentType);
        } catch (S3Exception e) {
            // This will show: 403 AccessDenied, 404 NoSuchBucket, etc.
            log.error("S3 putObject failed: status={} code={} msg={} bucket={} key={}",
                    e.statusCode(),
                    e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "n/a",
                    e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage(),
                    props.bucket(),
                    objectKey,
                    e
            );
            throw e; // let your services map it to FILE_002 with details
        } catch (SdkClientException e) {
            // Connection / DNS / SSL issues land here
            log.error("S3 client error (network/ssl): bucket={} key={} msg={}",
                    props.bucket(), objectKey, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Upload failed (unexpected): bucket={} key={} msg={}",
                    props.bucket(), objectKey, e.getMessage(), e);
            throw new RuntimeException("Upload failed", e);
        }
    }

    public void delete(String objectKey) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(props.bucket())
                .key(objectKey)
                .build());
    }

    public String publicUrl(String objectKey) {
        String base = (props.publicBaseUrl() == null || props.publicBaseUrl().isBlank())
                ? props.endpoint()
                : props.publicBaseUrl();
        return base.replaceAll("/$", "") + "/" + props.bucket() + "/" + objectKey;
    }

    public String presignedGetUrl(String objectKey, Duration ttl) {
        var getReq = GetObjectRequest.builder()
                .bucket(props.bucket())
                .key(objectKey)
                .build();

        var presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getReq)
                .build();

        return presigner.presignGetObject(presignReq).url().toString();
    }

    public record PutResult(String etag, long sizeBytes, String contentType) {}
}
