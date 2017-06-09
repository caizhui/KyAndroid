package com.ky.kyandroid.activity.evententry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ky.kyandroid.R;

/**
 * Created by Caizhui on 2017-6-9.
 */

public class EventEntryAdd_Attachment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.evententeradd_attachment_fragment,container,false);
        return view;
    }
}
