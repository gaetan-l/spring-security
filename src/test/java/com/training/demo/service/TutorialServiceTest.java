package com.training.demo.service;

import com.training.demo.dto.TutorialDto;
import com.training.demo.exception.EntityNotFoundException;
import com.training.demo.model.Tutorial;
import com.training.demo.model.User;
import com.training.demo.repository.TutorialRepository;
import com.training.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorialServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialService tutorialServiceMock;

    private TutorialDto unsavedTutorialDto, savedTutorialDto;
    private Tutorial unsavedTutorial, savedTutorial;
    private User unsavedUser, savedUser;

    @BeforeEach
    void setUp() {
        // Mocking objects
        unsavedTutorialDto = TutorialDto
                .builder()
                .id(null)
                .title("Title")
                .description("Description")
                .content("Content")
                .createAt(LocalDateTime.now())
                .userFirstName("First Name")
                .userLastName("Last Name")
                .userEmail("email@mail.mail")
                .build();

        savedTutorialDto = TutorialDto
                .builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .content("Content")
                .createAt(LocalDateTime.now())
                .userId(1L)
                .userFirstName("First Name")
                .userLastName("Last Name")
                .userEmail("email@mail.mail")
                .build();

        unsavedUser = User.builder()
                .firstName("First Name")
                .lastName("Last Name")
                .email("email@mail.mail")
                .build();

        savedUser = User.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .email("email@mail.mail")
                .build();

        unsavedTutorial = unsavedTutorialDto.toPojo();
        savedTutorial = savedTutorialDto.toPojo();
        savedTutorial.setAuthor(savedUser);
    }

    @Test
    @DisplayName("Given valid TutorialDto with valid new User without id, when create() then id and userId filled")
    void createWithValidNewUser() {
        // Mocking methods
        when(userRepository.save(unsavedUser)).thenReturn(savedUser);
        when(tutorialRepository.save(unsavedTutorial)).thenReturn(savedTutorial);
        // TODO Remove: when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        // Execution
        TutorialDto result = tutorialServiceMock.create(unsavedTutorialDto);

        // Assertions
        // TODO: Remove: verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(unsavedUser);
        verify(tutorialRepository, times(1)).save(unsavedTutorial);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("Given valid TutorialDto with new User with inexistant id, when create() then EntityNotFoundException thrown")
    void createWithInvalidNewUserWithId() {
        // Mocking objects
        unsavedTutorialDto.setUserId(1L);
        unsavedTutorial = unsavedTutorialDto.toPojo();

        // Mocking methods
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Execution
        Throwable result = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            tutorialServiceMock.create(unsavedTutorialDto);
        });

        // Assertions
        Assertions.assertEquals(EntityNotFoundException.class, result.getClass());
    }
}