package com.team.agita.langeo.achievements;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team.agita.langeo.LocalUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agita on 24.01.16.
 */
public class Achievement implements ParentListItem {

    public Boolean isReached;
    public String name;
    public String description;

    private List<Achievement> mChildItemList = new ArrayList<Achievement>();
    private boolean mInitiallyExpanded;

    public static ArrayList<Achievement> fill (ArrayList<Integer> reachedIds) {
        ArrayList<Achievement> resultList = new ArrayList<Achievement>();

        for (int i=0; i < mNames.length; i++) {
            Achievement a = new Achievement();
            a.name = mNames[i];
            if (LocalUser.getInstance().achievementsReached.contains(i)) {
                a.isReached = true;
            } else {
                a.isReached = false;
            }
            //PLease don't kill me.
            //its needed for expandadable list items:
            a.mChildItemList.add(new Achievement());
            a.mChildItemList.get(0).setDescription(mDescriptions[i]);

            resultList.add(a);
        }

        return resultList;
    }

    private static String [] mNames = {
            "Real HERO",
            "Hands up!",
            "Simple"};
    private static String [] mDescriptions = {
            "Write a pair of pages for your diploma.",
            "Put any hand up (not only yours).",
            "Close one eye."};

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }

    @Override
    public List<Achievement> getChildItemList() {
        return mChildItemList;
    }

    public Boolean getIsReached() {
        return isReached;
    }

    public void setIsReached(Boolean isReached) {
        this.isReached = isReached;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}