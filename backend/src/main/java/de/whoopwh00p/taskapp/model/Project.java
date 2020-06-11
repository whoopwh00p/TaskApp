package de.whoopwh00p.taskapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private Set<Task> tasks;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String shortName;

    private String name;
}
