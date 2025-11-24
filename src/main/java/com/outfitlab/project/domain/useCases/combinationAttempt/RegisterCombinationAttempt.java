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
        UserModel user = null;

        if (userEmail != null) {
            try {
                user = userRepository.findUserByEmail(userEmail);
            } catch (UserNotFoundException ignored) {
            }
        }

        var attempt = new CombinationAttemptModel(
                user,
                combination,
                LocalDateTime.now(),
                imageUrl
        );

        return attemptRepository.save(attempt);
    }
}
