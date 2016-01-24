package com.team.agita.langeo;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by agita on 24.01.16.
 */
public class ViewHolderAchievementsSub extends ChildViewHolder {

    public TextView mDescription;

    public ViewHolderAchievementsSub(View itemView) {
        super(itemView);

        mDescription = (TextView) itemView.findViewById(R.id.child_list_item_achievement_description);
    }

    public void bind(String description) {
        mDescription.setText(description);
    }
}
