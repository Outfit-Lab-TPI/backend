package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionJpaRepository extends JpaRepository<UserSubscriptionEntity, Long> {
}