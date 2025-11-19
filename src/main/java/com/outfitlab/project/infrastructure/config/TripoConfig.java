package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository;
import com.outfitlab.project.domain.useCases.tripo.*;
import com.outfitlab.project.infrastructure.repositories.UploadImageRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.TripoJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class TripoConfig {

    @Bean
    public TripoRepositoryImpl tripoRepositoryImpl(RestTemplate restTemplate, TripoJpaRepository iJpaTripoRepository) {
        return new TripoRepositoryImpl(restTemplate, iJpaTripoRepository);
    }

    @Bean
    public TripoRepository iTripoRepository(TripoRepositoryImpl tripoRepositoryImpl) {
        return tripoRepositoryImpl;
    }

    @Bean
    public UploadImageRepository iAwsRepository(S3Client s3Client) {
        return new UploadImageRepositoryImpl(s3Client);
    }

    @Bean
    public UploadImageToTripo uploadImageToTripo(TripoRepository iTripoRepository) {
        return new UploadImageToTripo(iTripoRepository);
    }

    @Bean
    public GenerateImageToModelTrippo generateImageToModelTrippo(TripoRepository iTripoRepository) {
        return new GenerateImageToModelTrippo(iTripoRepository);
    }

    @Bean
    public SaveTripoModel saveTripoModel(TripoRepository iTripoRepository) {
        return new SaveTripoModel(iTripoRepository);
    }

    @Bean
    public CheckTaskStatus checkTaskStatus(TripoRepository iTripoRepository) {
        return new CheckTaskStatus(iTripoRepository);
    }

    @Bean
    public UpdateTripoModel updateTripoModel(TripoRepository iTripoRepository) {
        return new UpdateTripoModel(iTripoRepository);
    }

    @Bean
    public FindTripoModelByTaskid findTripoModelByTaskid(TripoRepository iTripoRepository) {
        return new FindTripoModelByTaskid(iTripoRepository);
    }
}
