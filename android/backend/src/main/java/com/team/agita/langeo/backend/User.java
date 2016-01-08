package com.team.agita.langeo.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
    @Id public String id;
    public Coordinates coordinates;
    public Boolean isVisible;
}
