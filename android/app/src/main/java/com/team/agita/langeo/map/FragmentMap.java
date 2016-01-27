package com.team.agita.langeo.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.agita.langeo.R;

/**
 * Created by agita on 27.01.16.
 */
public class FragmentMap extends Fragment {

    public FragmentMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        return view;
    }
}
