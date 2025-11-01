package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.IAwsRepository;
import com.outfitlab.project.domain.interfaces.repositories.ITripoRepository;
import com.outfitlab.project.domain.service.TrippoService;
import com.outfitlab.project.domain.useCases.tripo.*;
import com.outfitlab.project.infrastructure.repositories.AwsRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.TripoRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.IJpaTripoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class TripoConfig {


    // ---------- repos, Impl y AWS ---------------------------------------------------------------------------------
    @Bean
    public TripoRepositoryImpl tripoRepositoryImpl(RestTemplate restTemplate, IJpaTripoRepository iJpaTripoRepository) {
        return new TripoRepositoryImpl(restTemplate, iJpaTripoRepository);
    }

    @Bean
    public ITripoRepository iTripoRepository(TripoRepositoryImpl tripoRepositoryImpl) {
        return tripoRepositoryImpl;
    }

    @Bean
    public IAwsRepository iAwsRepository(S3Client s3Client) {
        return new AwsRepositoryImpl(s3Client);
    }

    @Bean
    public UploadImageToAws uploadImageToAws(IAwsRepository iAwsRepository) {
        return new UploadImageToAws(iAwsRepository);
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
    public UploadImageToTripo uploadImageToTripo(ValidateExtension validateExtension, GetFileExtension getFileExtension, ITripoRepository iTripoRepository) {
        return new UploadImageToTripo(validateExtension, getFileExtension, iTripoRepository);
    }

    @Bean
    public GenerateImageToModelTrippo generateImageToModelTrippo(ITripoRepository iTripoRepository) {
        return new GenerateImageToModelTrippo(iTripoRepository);
    }

    @Bean
    public SaveTripoModel saveTripoModel(ITripoRepository iTripoRepository) {
        return new SaveTripoModel(iTripoRepository);
    }

    @Bean
    public CheckTaskStatus checkTaskStatus(ITripoRepository iTripoRepository) {
        return new CheckTaskStatus(iTripoRepository);
    }

    @Bean
    public UpdateTripoModel updateTripoModel(ITripoRepository iTripoRepository) {
        return new UpdateTripoModel(iTripoRepository);
    }

    @Bean
    public FindTripoModelByTaskid findTripoModelByTaskid(ITripoRepository iTripoRepository) {
        return new FindTripoModelByTaskid(iTripoRepository);
    }
    //---------------------------------------------------------------------------------------------------------------------------



    // ---------- service con tooooodos los useCases inyectados -----------------------------------------------------------------
    @Bean
    public TrippoService trippoService(
            UploadImageToTripo uploadImageToTripo, UploadImageToAws uploadImageToAws,
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
