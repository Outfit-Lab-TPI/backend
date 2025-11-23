package com.outfitlab.project.infrastructure.config;

import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
import com.outfitlab.project.domain.interfaces.repositories.GarmentRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.useCases.brand.*;
import com.outfitlab.project.infrastructure.repositories.BrandRepositoryImpl;
import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrandConfig {

    @Bean
    public BrandRepository marcaRepository(BrandJpaRepository jpaRepo) {
        return new BrandRepositoryImpl(jpaRepo);
    }

    @Bean
    public GetBrandAndGarmentsByBrandCode getMarcaByCodigoMarca(BrandRepository marcaRepository, GarmentRepository garmentRepository) {
        return new GetBrandAndGarmentsByBrandCode(marcaRepository, garmentRepository);
    }

    @Bean
    public GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers(UserRepository userRepository) {
        return new GetAllBrandsWithRelatedUsers(userRepository);
    }

    @Bean
    public CreateBrand createBrand(BrandRepository marcaRepository) {
        return new CreateBrand(marcaRepository);
    }

    @Bean
    public GetNotificationsNewBrands getNotificationsNewBrands(UserRepository userRepository) {
        return new GetNotificationsNewBrands(userRepository);
    }

    @Bean
    public ActivateBrand activateBrand(BrandRepository marcaRepository, UserRepository userRepository) {
        return new ActivateBrand(marcaRepository, userRepository);
    }

    @Bean
    public DesactivateBrand desactivateBrand(BrandRepository marcaRepository, UserRepository userRepository) {
        return new DesactivateBrand(marcaRepository, userRepository);
    }

    @Bean
    public GetAllBrands getAllMarcas(BrandRepository marcaRepository) {
        return new GetAllBrands(marcaRepository);
    }
}
