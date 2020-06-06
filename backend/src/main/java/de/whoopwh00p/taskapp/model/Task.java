package de.whoopwh00p.taskapp.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private boolean completed;
    private String description;
}
