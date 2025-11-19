package com.outfitlab.project.infrastructure.config.s3;

import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import com.outfitlab.project.domain.useCases.bucketImages.DeleteImage;
import com.outfitlab.project.domain.useCases.bucketImages.SaveImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretAccessKey;

    @Value("${AWS_REGION}")
    private String region;
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey
        );
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public DeleteImage deleteImage(UploadImageRepository iAwsRepository){
        return new DeleteImage(iAwsRepository);
    }

    @Bean
    public SaveImage uploadImageToAws(com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository iAwsRepository) {
        return new SaveImage(iAwsRepository);
    }
}
