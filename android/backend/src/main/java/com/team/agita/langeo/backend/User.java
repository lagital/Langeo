package com.team.agita.langeo.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

@Entity
public class User {
    @Id public String id;   // email
    public Coordinates coordinates;
    public Boolean isVisible;
    public Achievement[] achievements;
    public UserType userType;
    @Index public DateTime lastRequestTime;
    @Index public String cityId;
}