package de.whoopwh00p.taskapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    private String id;

    @OneToMany
    private List<Project> projects;

    private String name;
}
