package de.whoopwh00p.taskapp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String shortName;

    private String name;
}
