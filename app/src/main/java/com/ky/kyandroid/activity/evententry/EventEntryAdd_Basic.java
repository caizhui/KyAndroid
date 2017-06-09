package com.ky.kyandroid.activity.evententry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.R2;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caizhui on 2017-6-9.
 */

public class EventEntryAdd_Basic extends Fragment{


    @BindView(R2.id.happen_time)
    EditText happenTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evententeradd_basic_fragment, container, false);
        ButterKnife.bind(this,view);
        initView(view);
        return view;
    }

    public void initView(View view) {
        happenTime.setText("aaaaa");
    }

    @OnClick(R.id.happen_time)
    public void onClick(View v) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.happen_time:
                Toast.makeText(EventEntryAdd_Basic.this.getActivity(), "aa", Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(EventEntryAdd_Basic.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        happenTime.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(EventEntryAdd_Basic.this.getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
