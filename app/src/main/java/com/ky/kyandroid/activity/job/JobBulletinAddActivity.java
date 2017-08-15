package com.ky.kyandroid.activity.job;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ky.kyandroid.R;
import com.ky.kyandroid.adapter.GroupAdapter;
import com.ky.kyandroid.bean.CodeValue;
import com.ky.kyandroid.db.dao.DescEntityDao;
import com.ky.kyandroid.util.DateTimePickDialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by Caizhui on 2017/8/15.
 * 工作简报新增页面
 */

public class JobBulletinAddActivity extends AppCompatActivity {

    /**
     * 标题栏左边按钮
     */
    @BindView(R.id.left_btn)
    ImageView leftBtn;

    /**
     * 标题栏中间标题
     */
    @BindView(R.id.center_text)
    TextView centerText;

    /**
     * 标题栏右边按钮
     */
    @BindView(R.id.right_btn)
    Button rightBtn;

    /**
     * 标题
     */
    @BindView(R.id.job_name_edt)
    EditText jobNameEdt;
    /**
     * 时间
     */
    @BindView(R.id.job_time_edt)
    EditText jobTimeEdt;
    /**
    * 上报部门
     */
    @BindView(R.id.job_departmen_edt)
    TextView jobDepartmenEdt;
    @BindView(R.id.field_departmen_img)
    ImageView fieldDepartmenImg;
    @BindView(R.id.job_departmen_layout)
    LinearLayout jobDepartmenLayout;

    /**
     * 全局LinearLayout
     */
    @BindView(R.id.linear_evententry)
    LinearLayout linearEvententry;
    /**
     * 内容
     */
    @BindView(R.id.job_content_edt)
    EditText jobContentEdt;
    /**
     * 上报领导
     */
    @BindView(R.id.reporting_leadership_btn)
    Button reportingLeadershipBtn;


    View showPupWindow = null; // 选择区域的view

    /**
     * 一级菜单名称数组
     **/
    String[][] GroupNameArray;
    /**
     * 二级菜单名称数组
     **/
    String[][] childNameArray;
    /**
     * 三级菜单名称数组
     **/
    String[][] child2NameArray;

    ListView groupListView = null;
    ListView childListView = null;

    TranslateAnimation animation;// 出现的动画效果
    // 屏幕的宽高
    public static int screen_width = 0;
    public static int screen_height = 0;

    private boolean[] tabStateArr = new boolean[4];// 标记tab的选中状态，方便设置

    PopupWindow mPopupWindow = null;

    public DescEntityDao descEntityDao;

    /**
     * 上报部门
     */
    private String spinnerType;

    Button btnCancel, btnConfirm;

    TextView btntext;

    GroupAdapter groupAdapter = null;

    GroupAdapter childAdapter = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobjy_add);
        ButterKnife.bind(this);
        descEntityDao = new DescEntityDao();
        initToolbar();
    }

    /**
     * 初始化标题栏e
     */
    private void initToolbar() {

        /** 设置toolbar标题 **/
        centerText.setText("工作简报录入");

    }

    @OnTouch({R.id.job_time_edt})
    public boolean OnTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            /** 点击发生时间控件 **/
            case R.id.job_time_edt:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    jobTimeEdt.clearFocus();
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            JobBulletinAddActivity.this, "");
                    dateTimePicKDialog.dateTimePicKDialog(jobTimeEdt);
                    return false;
                }
                break;
        }
        return false;
    }

    @OnClick({R.id.left_btn,R.id.job_departmen_layout})
    public void onClick(View v) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置,方便展示Popupwindow
        jobDepartmenLayout.getLocationOnScreen(location);
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.left_btn:
                onBackPressed();
                break;
            // 到场部门
            case R.id.field_departmen_layout:
                spinnerType = "dcbm";
                break;
        }
        animation = null;
        // location[1] 改成 0
        animation = new TranslateAnimation(0, 0, -700, 0);
        animation.setDuration(500);
        List<CodeValue> codeValueList = descEntityDao.queryPidList(spinnerType);
        /** 一级菜单名称数组 **/
        GroupNameArray = new String[codeValueList.size()][];
        if (codeValueList != null && codeValueList.size() > 0) {
            for (int i = 0; i < codeValueList.size(); i++) {
                String[] cg = new String[2];
                // 0 是未选中,1 是选中
                cg[0] = "0";
                cg[1] = codeValueList.get(i).getValue();
                GroupNameArray[i] = cg;
            }
            CodeValue cv = codeValueList.get(0);
            /** 二级菜单名称数组 **/
            List<CodeValue> childCodeValueList = descEntityDao.queryValueListByPid(spinnerType, cv.getCode());
            childNameArray = new String[childCodeValueList.size()][];
            if (childCodeValueList != null && childCodeValueList.size() > 0) {
                for (int i = 0; i < childCodeValueList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = childCodeValueList.get(i).getValue();
                    childNameArray[i] = cg;
                }
            }
        }
        showPupupWindow();
    }

    /**
     * 初始化 PopupWindow
     *
     * @param view
     */
    public void initPopuWindow(View view) {
        /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(view, screen_width, screen_height);
        /* 设置背景显示 */
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.mypop_bg));
        /* 设置触摸外面时消失 */
        // mPopupWindow.setOutsideTouchable(true);
        // 设置动画
        mPopupWindow.setAnimationStyle(R.style.pop_menu);

        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        /**
         * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
         */
        view.setFocusableInTouchMode(true);
    }

    /**
     * 展示区域选择的对话框
     */
    private void showPupupWindow() {
        showPupWindow = LayoutInflater.from(JobBulletinAddActivity.this).inflate(
                R.layout.bottom_layout, null);
        initPopuWindow(showPupWindow);
        // 初始化三个ListView
        groupListView = showPupWindow.findViewById(R.id.listView1);
        childListView = showPupWindow.findViewById(R.id.listView2);

        // 初始化点击事件 - 取消
        btnCancel = showPupWindow.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        // 初始化点击事件 - 确定
        btnConfirm = showPupWindow.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 填充选择框
                fillListValues();
            }
        });

        // 标题
        btntext = showPupWindow.findViewById(R.id.btn_text);

        // 设置视图 一级菜单
        groupAdapter = new GroupAdapter(JobBulletinAddActivity.this, GroupNameArray);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
        groupListView.setOnItemClickListener(new MyItemClick());

        btntext.setText("上报部门列表");

        mPopupWindow.showAtLocation(linearEvententry, Gravity.CENTER, 0, 0);
    }

    /**
     * 填充列表框
     */
    private void fillListValues() {
        StringBuffer sb = new StringBuffer();
        // 菜单列表项
        List<GroupAdapter> adapterList = new ArrayList<GroupAdapter>();
        adapterList.add((GroupAdapter) groupListView.getAdapter());
        adapterList.add((GroupAdapter) childListView.getAdapter());

        // 判断是否有选中
        if (checkGroupAdapter(sb, adapterList)) {
            Toast.makeText(JobBulletinAddActivity.this, "请选择任意一项", Toast.LENGTH_LONG).show();
            return;
        } else {
            jobDepartmenEdt.setText(sb.deleteCharAt(sb.length() - 1).toString());
            mPopupWindow.dismiss();
        }
    }


    private boolean checkGroupAdapter(StringBuffer selectedStr, List<GroupAdapter> adapters) {
        boolean flag = true;
        if (adapters != null && adapters.size() > 0) {
            for (GroupAdapter adap : adapters) {
                String[][] mitems = adap.getmGroupItems();
                for (int i = 0; i < mitems.length; i++) {
                    if ("1".equals(mitems[i][0])) {
                        flag = false;
                        selectedStr.append(mitems[i][1]).append(",");
                    }
                }
            }
        }
        return flag;
    }

    class MyItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            groupAdapter.setSelectedPosition(position);
            String[] adCg = (String[]) groupAdapter.getItem(position);
            String pidCode = descEntityDao.queryCodeByName(spinnerType, adCg[1]);
            // 获取列表集合
            List<CodeValue> childCodeValueList = descEntityDao.queryValueListByPid(spinnerType, pidCode);
            childNameArray = new String[childCodeValueList.size()][];
            String[][] groupItems = childAdapter.getmGroupItems();
            boolean flag = true;
            if (childCodeValueList.size() > 0) {
                for (int i = 0; i < childCodeValueList.size(); i++) {
                    String[] cg = new String[2];
                    // 0 是未选中,1 是选中
                    cg[0] = "0";
                    cg[1] = childCodeValueList.get(i).getValue();
                    // 如果名称有一个一样就换值不刷新
                    if (groupItems != null && i < groupItems.length) {
                        if (cg[1].equals(groupItems[i][1])) {
                            flag = false;
                            break;
                        }

                    }
                    childNameArray[i] = cg;
                }
            } else {
                flag = true;
            }
            if (flag) {
                childAdapter.setChildData(childNameArray);
            }
            handler.sendEmptyMessage(20);
        }

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    // 刷新二级菜单列表
                    childAdapter.notifyDataSetChanged();
                    groupAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }

        ;
    };
}
