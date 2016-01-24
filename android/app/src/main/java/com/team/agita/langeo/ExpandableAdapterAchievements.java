package com.team.agita.langeo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by agita on 24.01.16.
 */
public class ExpandableAdapterAchievements extends ExpandableRecyclerAdapter<ViewHolderAchievementsMain,
        ViewHolderAchievementsSub> {

    private LayoutInflater mInflater;

    public ExpandableAdapterAchievements(Context context, List<? extends Achievement> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderAchievementsMain onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item_achievement_parent, parent, false);
        return new ViewHolderAchievementsMain(view);
    }

    @Override
    public ViewHolderAchievementsSub onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item_achievement_child, parent, false);
        return new ViewHolderAchievementsSub(view);
    }

    @Override
    public void onBindParentViewHolder(ViewHolderAchievementsMain parentViewHolder, int position,
                                       ParentListItem achievementListItem) {
        Achievement item = (Achievement) achievementListItem;
        parentViewHolder.bind(item.getName(), item.getIsReached());
    }

    @Override
    public void onBindChildViewHolder(ViewHolderAchievementsSub childViewHolder, int position,
                                      Object childListItem) {
        Achievement item = (Achievement) childListItem;
        childViewHolder.bind(item.getDescription());
    }
}