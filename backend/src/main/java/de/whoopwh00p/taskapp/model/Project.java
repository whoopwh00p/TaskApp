package de.whoopwh00p.taskapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String shortName;

    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

}
