package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.appspot.id.app.langeo.model.Coordinates;
import com.appspot.id.app.langeo.model.GetCurrentUserResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

/**
 * Created by agita on 12.01.16.
 */
public class LocalUser{
    private static String MY_PREFS_NAME = "LangeoPreferences";
    private static SharedPreferences prefs;

    public Boolean initialized = false;
    public ArrayList<Integer> achievementsReached = new ArrayList<Integer>();
    public Long id = null;
    public String eMail = "example@email.com";
    public Coordinates coordinates;
    public Boolean isVisible = true;
    public Pair<String, Integer>[] languages;
    public Boolean showSlides = true;
    public UserType userType;
    public String cityId;

    private static LocalUser mUser = null;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private LocalUser(){ }

    /* Static 'instance' method */
    public static LocalUser getInstance( ) {
        if (mUser == null) {
            mUser = new LocalUser();
        }
        return mUser;
    }

    public static void updateStorages(Context context) {
        AsyncTaskUpdateStorages task = new AsyncTaskUpdateStorages(context);
        task.execute();
    }

    public void fill(String eMail, Boolean showSlides, Boolean isVisible) {
        mUser.setShowSlides(showSlides);
        mUser.setIsVisible(isVisible);
        mUser.seteMail(eMail);
    }

    public GetCurrentUserResponse extractAPIUser() {
        GetCurrentUserResponse user = new GetCurrentUserResponse();
        user.setIsVisible(isVisible);
        user.setId(id);
        return user;
    }

    public void initialize(Context context, GoogleSignInAccount acct) {
        //tests without API
        achievementsReached.add(1);
        achievementsReached.add(2);

        AsyncTaskInitUser task = new AsyncTaskInitUser(context);
        task.execute(acct.getEmail());
    }

    public void setAchievementsReached(ArrayList<Integer> achievementsReached) {
        this.achievementsReached = achievementsReached;
    }

    public void reachAchievement (Integer number) {
        achievementsReached.add(number);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    public ArrayList<Integer> getAchievementsReached() {
        return achievementsReached;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
