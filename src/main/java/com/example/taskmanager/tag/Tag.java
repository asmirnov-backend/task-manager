package com.example.taskmanager.tag;

import com.example.taskmanager.task.Task;
import com.example.taskmanager.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    private UUID id;

    @Column(length = 63, nullable = false)
    private String name;

    @Column(length = 63, nullable = false)
    private String color;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;

    @ManyToOne()
    private User creator;

    @ManyToMany()
    private Set<Task> tasks;
}
