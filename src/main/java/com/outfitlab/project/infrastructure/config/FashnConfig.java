package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.FashnRepository;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.infrastructure.repositories.FashnRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FashnConfig {

    @Bean
    public FashnRepository fashnRepositoryImpl(RestTemplate restTemplate) {
        return new FashnRepositoryImpl(restTemplate);
    }

    @Bean
    public CombinePrendas combinePrendas(FashnRepository iFashnRepository){
        return new CombinePrendas(iFashnRepository);
    }
}
