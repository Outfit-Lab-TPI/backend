package com.outfitlab.project.domain.interfaces.repositories;
import com.outfitlab.project.domain.model.CombinationAttemptModel;

public interface CombinationAttemptRepository {

    Long save(CombinationAttemptModel attempt);
}