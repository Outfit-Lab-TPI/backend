package com.outfitlab.project.domain.useCases.combinationAttempt;

import com.outfitlab.project.domain.exceptions.UserNotFoundException;
import com.outfitlab.project.domain.interfaces.repositories.CombinationAttemptRepository;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.CombinationAttemptModel;
import com.outfitlab.project.domain.model.CombinationModel;
import com.outfitlab.project.domain.model.UserModel;
import java.time.LocalDateTime;

public class RegisterCombinationAttempt {

    private final CombinationAttemptRepository attemptRepository;
    private final UserRepository userRepository;

    public RegisterCombinationAttempt(
            CombinationAttemptRepository attemptRepository,
            UserRepository userRepository
    ) {
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    public Long execute(String userEmail, CombinationModel combination, String imageUrl) {
        if (userEmail == null) {
            throw new IllegalArgumentException("El email del usuario no puede ser null");
        }

        UserModel user = userRepository.findUserByEmail(userEmail);

        if (combination == null || combination.getId() == null) {
            throw new IllegalArgumentException("La combinación debe existir y tener un ID válido");
        }

        CombinationAttemptModel attempt = new CombinationAttemptModel(
                user,
                combination,
                LocalDateTime.now(),
                imageUrl
        );
        
        return attemptRepository.save(attempt);
    }

}
