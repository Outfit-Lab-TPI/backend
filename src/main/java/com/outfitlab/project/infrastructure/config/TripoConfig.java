package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.service.TrippoService;
import com.outfitlab.project.domain.useCases.tripo.*;
import com.outfitlab.project.infrastructure.repositories.UploadImageRepository;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.TripoJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class TripoConfig {


    // ---------- repos, Impl y AWS ---------------------------------------------------------------------------------
    @Bean
    public TripoRepositoryImpl tripoRepositoryImpl(RestTemplate restTemplate, TripoJpaRepository iJpaTripoRepository) {
        return new TripoRepositoryImpl(restTemplate, iJpaTripoRepository);
    }

    @Bean
    public TripoRepository iTripoRepository(TripoRepositoryImpl tripoRepositoryImpl) {
        return tripoRepositoryImpl;
    }

    @Bean
    public com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository iAwsRepository(S3Client s3Client) {
        return new UploadImageRepository(s3Client);
    }

    @Bean
    public SaveImage uploadImageToAws(com.outfitlab.project.domain.interfaces.repositories.UploadImageRepository iAwsRepository) {
        return new SaveImage(iAwsRepository);
    }
    // ---------------------------------------------------------------------------------------------------------



    // ---------- USE CASES ----------------------------------------------------------------------------------
    @Bean
    public GetFileExtension getFileExtension() {
        return new GetFileExtension();
    }

    @Bean
    public GetImageResource getImageResource() {
        return new GetImageResource();
    }

    @Bean
    public ValidateExtension validateExtension() {
        return new ValidateExtension();
    }

    @Bean
    public UploadImageToTripo uploadImageToTripo(ValidateExtension validateExtension, GetFileExtension getFileExtension, TripoRepository iTripoRepository) {
        return new UploadImageToTripo(validateExtension, getFileExtension, iTripoRepository);
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
    //---------------------------------------------------------------------------------------------------------------------------



    // ---------- service con tooooodos los useCases inyectados -----------------------------------------------------------------
    @Bean
    public TrippoService trippoService(
            UploadImageToTripo uploadImageToTripo, SaveImage uploadImageToAws,
            GenerateImageToModelTrippo generateImageToModelTrippo, SaveTripoModel saveTripoModel,
            CheckTaskStatus checkTaskStatus, UpdateTripoModel updateTripoModel, FindTripoModelByTaskid findTripoModelByTaskid
    ) {
        return new TrippoService(
                uploadImageToTripo, uploadImageToAws, generateImageToModelTrippo,
                saveTripoModel, checkTaskStatus, updateTripoModel, findTripoModelByTaskid
        );
    }
    //---------------------------------------------------------------------------------------------------------------------------
}
