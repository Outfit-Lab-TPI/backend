package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.TripoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import com.outfitlab.project.domain.model.TripoModel;

public interface TripoJpaRepository extends JpaRepository<TripoEntity, Long> {

    TripoEntity findByTaskId(String taskId);

    List<TripoEntity> findByStatusIn(Collection<TripoModel.ModelStatus> statuses);
}
