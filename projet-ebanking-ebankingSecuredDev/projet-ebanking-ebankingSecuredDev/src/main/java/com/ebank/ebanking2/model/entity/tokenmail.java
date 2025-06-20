package com.ebank.ebanking2.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class tokenmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private boolean used;
    LocalDateTime expriryDate;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
