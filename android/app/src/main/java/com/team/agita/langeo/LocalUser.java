package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.appspot.myapplicationid.langeo.model.Coordinates;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by agita on 12.01.16.
 */
public class LocalUser{
    private static String MY_PREFS_NAME = "LangeoPreferences";
    private static SharedPreferences prefs;

    public Integer initialized = 0;
    public String id = "0";
    public String eMail = "example@email.com";
    public Coordinates coordinates;
    public Boolean isVisible = true;
    public Pair<String, Integer>[] languages;
    public Boolean showSlides = true;

    private static LocalUser user = null;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private LocalUser(){ }

    /* Static 'instance' method */
    public static LocalUser getInstance( ) {
        if (user == null) {
            user = new LocalUser();
        }
        return user;
    }

    public void initialize(Context context, GoogleSignInAccount acct) {
        AsyncTaskInitUser task = new AsyncTaskInitUser(context);
        task.execute(acct.getId());
        eMail = acct.getEmail();
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

    public Integer getInitialized() {
        return initialized;
    }

    public void setInitialized(Integer initialized) {
        this.initialized = initialized;
    }

}
