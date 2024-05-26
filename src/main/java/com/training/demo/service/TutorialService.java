package com.training.demo.service;

import com.training.demo.dto.TutorialDto;
import com.training.demo.exception.EntityNotFoundException;
import com.training.demo.exception.IllegalEntityArgument;
import com.training.demo.model.Tutorial;
import com.training.demo.model.User;
import com.training.demo.repository.TutorialRepository;
import com.training.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorialService {
    private final TutorialRepository tutorialRepository;
    private final UserRepository userRepository;

    public Optional<TutorialDto> get(final Long id) {
        return tutorialRepository.findById(id).map(TutorialDto::fromPojo);
    }

    public List<TutorialDto> getAll() {
        return tutorialRepository.findAll().stream().map(TutorialDto::fromPojo).toList();
    }

    public TutorialDto create(final TutorialDto dto) {
        final Tutorial tutorial = dto.toPojo();

        if (dto.getUserId() == null) {
            User newUser = User.builder()
                    .firstName(dto.getUserFirstName())
                    .lastName(dto.getUserLastName())
                    .email(dto.getUserEmail())
                    .build();
            userRepository.save(newUser);
            tutorial.setAuthor(newUser);
        }
        else {
            final Optional<User> user = userRepository.findById(dto.getUserId());
            if (user.isPresent()) {
                if ((dto.getUserFirstName() != null)
                        ||  (dto.getUserLastName() != null)
                        ||  (dto.getUserEmail() != null)) {
                    throw new IllegalEntityArgument(
                            User.class,
                            "Trying to redefine User with id=" + dto.getUserId(),
                            "{}");
                }
                tutorial.setAuthor(user.get());
            }
            else {
                throw new EntityNotFoundException(User.class, dto.getUserId(), "{}");
            }
        }

        return TutorialDto.fromPojo(tutorialRepository.save(tutorial));
    }

    public void delete(final Long id) {
        tutorialRepository.deleteById(id);
    }

    public TutorialDto update(final TutorialDto dto) {
        return TutorialDto.fromPojo(tutorialRepository.save(dto.toPojo()));
    }
}
