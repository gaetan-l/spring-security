package com.training.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tutorial implements AnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long          id;

    private String        title;
    private String        description;
    private String        content;
    private LocalDateTime createAt;

    /**
     * O à n Tuto écrit par 1 User
     */
    @ManyToOne
    private User          author;

    private String        category;
    // private List<String>  comments;
}