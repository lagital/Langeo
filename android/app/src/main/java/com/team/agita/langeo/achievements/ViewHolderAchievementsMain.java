package com.team.agita.langeo.achievements;

import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.team.agita.langeo.R;

/**
 * Created by agita on 24.01.16.
 */
public class ViewHolderAchievementsMain extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final long DEFAULT_ROTATE_DURATION_MS = 200;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    public TextView mAchievementTitleTextView;
    public ImageButton mParentDropDownArrow;
    public RelativeLayout mLayout;

    public ViewHolderAchievementsMain(View itemView) {
        super(itemView);

        mLayout = (RelativeLayout) itemView.findViewById(R.id.parent_list_item_layout);
        mAchievementTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_achievement);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }

    public void bind(String title, Boolean isReached) {
        if (isReached) {
            mLayout.setBackgroundResource(R.color.gold);
        }
        mAchievementTitleTextView.setText(title);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        if (expanded) {
            mParentDropDownArrow.setRotation(ROTATED_POSITION);
        } else {
            mParentDropDownArrow.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        RotateAnimation rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                INITIAL_POSITION,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE);
        rotateAnimation.setDuration(DEFAULT_ROTATE_DURATION_MS);
        rotateAnimation.setFillAfter(true);
        mParentDropDownArrow.startAnimation(rotateAnimation);
    }
}
