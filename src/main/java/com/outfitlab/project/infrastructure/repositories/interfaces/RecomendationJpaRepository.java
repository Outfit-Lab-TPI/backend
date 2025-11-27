package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.GarmentRecomendationEntity;
import com.outfitlab.project.infrastructure.model.PrendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecomendationJpaRepository extends JpaRepository<GarmentRecomendationEntity, Long> {

    List<GarmentRecomendationEntity> findByTopGarment(PrendaEntity garment);
    List<GarmentRecomendationEntity> findByBottomGarment(PrendaEntity garment);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM GarmentRecomendationEntity gr
    WHERE gr.topGarment.garmentCode = :garmentCode
       OR gr.bottomGarment.garmentCode = :garmentCode
    """)
    void deleteAllByGarmentCode(@Param("garmentCode") String garmentCode);



    @Modifying
    @Transactional
    @Query("""
        delete from GarmentRecomendationEntity gr 
        where gr.bottomGarment.garmentCode = :primaryCode
          and gr.topGarment.garmentCode = :secondaryCode
    """)
    void deleteWhenPrimaryIsBottom(String primaryCode, String secondaryCode);


    @Modifying
    @Transactional
    @Query("""
        delete from GarmentRecomendationEntity gr 
        where gr.topGarment.garmentCode = :primaryCode
          and gr.bottomGarment.garmentCode = :secondaryCode
    """)
    void deleteWhenPrimaryIsTop(String primaryCode, String secondaryCode);
}
