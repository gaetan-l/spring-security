package com.training.demo;

import com.training.demo.model.Role;
import com.training.demo.model.RoleName;
import com.training.demo.model.Tutorial;
import com.training.demo.model.User;
import com.training.demo.repository.RoleRepository;
import com.training.demo.repository.TutorialRepository;
import com.training.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {
	private final TutorialRepository tutorialRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			Role roleAdmin = Role.builder()
					.roleName(RoleName.ROLE_ADMIN)
					.build();
			Role roleUser = Role.builder()
					.roleName(RoleName.ROLE_USER)
					.build();
			roleRepository.save(roleAdmin);
			roleRepository.save(roleUser);

			User user1 = User.builder()
					.firstName("Gaetan")
					.lastName("L.")
					.email("fake_user@mail.com")
					.password(passwordEncoder.encode("qsdfgh"))
					.roles(List.of(roleUser, roleAdmin))
					.enabled(true)
					.accountExpired(false)
					.accountLocked(false)
					.credentialsExpired(false)
					.build();

			userRepository.save(user1);

			Tutorial tuto1 = Tutorial.builder()
					.title("Spring madness")
					.content("Spring and Spring Boot")
					.description("Introduction to Spring and Spring Boot")
					.createAt(LocalDateTime.now())
					.author(user1)
					.build();

			Tutorial tuto2 = Tutorial.builder()
					.title("Java")
					.content("Java 23")
					.description("New functionalities of Java 23")
					.createAt(LocalDateTime.now())
					.author(user1)
					.build();

			tutorialRepository.save(tuto1);
			tutorialRepository.save(tuto2);
		};
	}
}