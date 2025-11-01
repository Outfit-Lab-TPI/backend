package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.IFashnRepository;
import com.outfitlab.project.domain.service.FashnService;
import com.outfitlab.project.domain.useCases.fashn.CheckRequestCombine;
import com.outfitlab.project.domain.useCases.fashn.CombinePrendas;
import com.outfitlab.project.domain.useCases.fashn.PollStatusUntilCombineComplete;
import com.outfitlab.project.infrastructure.repositories.FashnRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FashnConfig {

    @Bean
    public IFashnRepository fashnRepositoryImpl(RestTemplate restTemplate) {
        return new FashnRepositoryImpl(restTemplate);
    }

    @Bean
    public CheckRequestCombine checkRequestCombine(){
        return new CheckRequestCombine();
    }

    @Bean
    public CombinePrendas combinePrendas(IFashnRepository iFashnRepository){
        return new CombinePrendas(iFashnRepository);
    }

    @Bean
    public PollStatusUntilCombineComplete pollStatusUntilCombineComplete(IFashnRepository iFashnRepository){
        return new PollStatusUntilCombineComplete(iFashnRepository);
    }

    @Bean
    public FashnService fashnService(CheckRequestCombine checkRequestCombine, CombinePrendas combinePrendas, PollStatusUntilCombineComplete pollStatusUntilCombineComplete){
        return new FashnService(checkRequestCombine, combinePrendas, pollStatusUntilCombineComplete);
    }
}
