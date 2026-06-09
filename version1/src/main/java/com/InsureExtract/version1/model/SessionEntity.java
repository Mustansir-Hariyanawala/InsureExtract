package com.InsureExtract.version1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;


    String title;
    String category;
}
