package com.example.taskmanager.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;



import java.util.Date;
import java.util.UUID;


@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 127)
    @NotBlank
    @Column(length = 127, nullable = false)
    private String name;

    @NotNull
    @Size(max = 4095)
    @Column(length = 4095, nullable = false)
    private String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(updatable = false, nullable = false)
    private Date created_at;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(nullable = false)
    private Date updated_at;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status = TaskStatus.OPEN;
}

