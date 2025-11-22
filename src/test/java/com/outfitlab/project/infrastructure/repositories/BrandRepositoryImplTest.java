//package com.outfitlab.project.infrastructure.repositories;
//
//import com.outfitlab.project.domain.interfaces.repositories.BrandRepository;
//import com.outfitlab.project.infrastructure.model.MarcaEntity;
//import com.outfitlab.project.infrastructure.repositories.interfaces.BrandJpaRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test") // IMPORTANTISIMO GENTE ESTE ACTIVATE PROFILE PARA NO AFECTAR LA BDD DE RENDER, PRESTAR ATENCIÓN
//class BrandRepositoryImplTest {
//
//    @Autowired
//    private BrandJpaRepository brandJpaRepository;
//
//    @Autowired
//    private BrandRepository brandRepository;
//
//    @Test
//    @Transactional
//    @Rollback
//    void testFindByBrandCode() {
//        MarcaEntity entity = new MarcaEntity();
//        entity.setCodigoMarca("raviolito");
//        entity.setNombre("Raviolito");
//        entity.setLogoUrl("https://raviolito-logo.png");
//        brandJpaRepository.save(entity);
//
//        var result = brandRepository.findByBrandCode("raviolito");
//
//        assertNotNull(result);
//        assertEquals("raviolito", result.getCodigoMarca());
//        assertEquals("Raviolito", result.getNombre());
//    }
//}
//
//
