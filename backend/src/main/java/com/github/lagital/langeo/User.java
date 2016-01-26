package com.github.lagital.langeo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
    @Id public String id;
    public Coordinates coordinates;
    public Boolean isVisible;
    public Achievement[] achievements;
    public UserType userType;
    public Boolean isOnline;
    public String cityId;
}
