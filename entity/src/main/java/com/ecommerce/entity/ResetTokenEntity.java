package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Entity
@Table(name="reset_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",
            nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Column(name = "created_date",
            nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "token_valid_till")
    private LocalDateTime tokenValidTill;
}
