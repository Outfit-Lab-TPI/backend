package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.IMarcaRepository;
import com.outfitlab.project.domain.service.MarcaService;
import com.outfitlab.project.domain.useCases.marca.GetAllMarcas;
import com.outfitlab.project.domain.useCases.marca.GetMarcaByCodigoMarca;
import com.outfitlab.project.infrastructure.repositories.MarcaRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.IJpaMarcaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarcaConfig {

    @Bean
    public IMarcaRepository marcaRepository(IJpaMarcaRepository jpaRepo) {
        return new MarcaRepositoryImpl(jpaRepo);
    }

    @Bean
    public GetMarcaByCodigoMarca getMarcaByCodigoMarca(IMarcaRepository marcaRepository) {
        return new GetMarcaByCodigoMarca(marcaRepository);
    }

    @Bean
    public GetAllMarcas getAllMarcas(IMarcaRepository marcaRepository) {
        return new GetAllMarcas(marcaRepository);
    }

    @Bean
    public MarcaService marcaService(GetAllMarcas getAllMarcas, GetMarcaByCodigoMarca getMarcaByCodigoMarca) {
        return new MarcaService(getAllMarcas, getMarcaByCodigoMarca);
    }
}
