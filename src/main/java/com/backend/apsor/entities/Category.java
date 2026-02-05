package com.backend.apsor.entities;

import com.backend.apsor.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Display name: "Cleaning & Maid Services"
    @Column(nullable = false, length = 120)
    private String name;

    // URL-friendly unique identifier: "cleaning-maid-services"
    @Column(nullable = false, length = 140, unique = true)
    private String slug;

    @Column(length = 500)
    private String description;

    // For custom ordering in UI lists
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        status = Status.ACTIVE;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>();
}
