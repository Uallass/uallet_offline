package com.example.uallas.uallet.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.uallas.uallet.R;

/**
 * Created by Uallas on 24/06/2017.
 */

public class IntroFragment extends Fragment {

    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String PAGE = "page";
    private OnButtonClickListener onButtonClickListener;

    private int page;

    public static IntroFragment newInstance(int page) {
        IntroFragment frag = new IntroFragment();
        Bundle b = new Bundle();
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onButtonClickListener = (OnButtonClickListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        page = getArguments().getInt(PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int layoutResId;
        switch (page) {
            case 0:
                layoutResId = R.layout.intro_fragment_1;
                break;
            default:
                layoutResId = R.layout.intro_fragment_2;
        }

        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);
        view.setTag(page);

        if (page == 0) {

            LinearLayout llBtNext = (LinearLayout) view.findViewById(R.id.bt_next);
            llBtNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClickListener.onButtonClicked(view, page + 1, false);
                }
            });
        } else if (page == 1) {

            LinearLayout llBtStart = (LinearLayout) view.findViewById(R.id.bt_start);
            llBtStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClickListener.onButtonClicked(view, page + 1, true);
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    interface OnButtonClickListener{
        void onButtonClicked(View view, int page, boolean start);
    }

}
