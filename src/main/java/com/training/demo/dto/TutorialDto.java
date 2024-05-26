package com.training.demo.dto;

import com.training.demo.model.Tutorial;
import com.training.demo.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorialDto {
    private Long          id;
    private String        title;
    private String        description;
    private String        content;
    private LocalDateTime createAt;
    private String        category;
    private Long          userId;
    private String        userFirstName;
    private String        userLastName;
    private String        userEmail;
    private String        userPassword;

    public static TutorialDto fromPojo(Tutorial pojo) {
        final TutorialDto dto = new TutorialDto();
        dto.setId(pojo.getId());
        dto.setTitle(pojo.getTitle());
        dto.setDescription(pojo.getDescription());
        dto.setContent(pojo.getContent());
        dto.setCreateAt(pojo.getCreateAt());
        dto.setCategory(pojo.getCategory());
        dto.setUserId(pojo.getAuthor() == null ? null : pojo.getAuthor().getId());
        dto.setUserFirstName(pojo.getAuthor() == null ? null : pojo.getAuthor().getFirstName());
        dto.setUserLastName(pojo.getAuthor() == null ? null : pojo.getAuthor().getLastName());
        dto.setUserEmail(pojo.getAuthor() == null ? null : pojo.getAuthor().getEmail());
        dto.setUserPassword(pojo.getAuthor() == null ? null : pojo.getAuthor().getPassword());
        return dto;
    }

    public Tutorial toPojo() {
        final Tutorial tutorial = new Tutorial();
        final User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .lastName(userLastName)
                .email(userEmail)
                .password(userPassword)
                .build();
        tutorial.setId(id);
        tutorial.setTitle(title);
        tutorial.setDescription(description);
        tutorial.setContent(content);
        tutorial.setCreateAt(createAt);
        tutorial.setCategory(category);
        tutorial.setAuthor(user);
        return tutorial;
    }
}
