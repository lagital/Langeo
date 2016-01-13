package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.appspot.myapplicationid.langeo.Langeo;
import com.appspot.myapplicationid.langeo.model.Coordinates;
import com.appspot.myapplicationid.langeo.model.User;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by agita on 12.01.16.
 */
public class LocalUser{
    private static String MY_PREFS_NAME = "LangeoPreferences";
    private static SharedPreferences prefs;

    public String id;
    public Coordinates coordinates;
    public Boolean isVisible;
    public Pair<String, Integer>[] languages;
    public Boolean showSlides;

    private static LocalUser user = new LocalUser( );

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private LocalUser(){ }

    /* Static 'instance' method */
    public static LocalUser getInstance( ) {
        return user;
    }

    public void initialize(Context context, String id) {
        InitUserAsyncTask task = new InitUserAsyncTask(context);
        task.execute(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Pair<String, Integer>[] getLanguages() {
        return languages;
    }

    public void setLanguages(Pair<String, Integer>[] languages) {
        this.languages = languages;
    }

    public Boolean getShowSlides() {
        return showSlides;
    }

    public void setShowSlides(Boolean showSlides) {
        this.showSlides = showSlides;
    }
}
