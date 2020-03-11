package com.young.newsgathering.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.young.newsgathering.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {

    public NewFragment() {
        // Required empty public constructor
    }

    public static NewFragment newInstance() {
        return new NewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
