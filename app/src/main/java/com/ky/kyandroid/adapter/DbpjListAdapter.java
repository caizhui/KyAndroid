package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DbpjListAdapter extends BaseAdapter {
    public List<Map<String,String>> list;
    public Context context;


    public DbpjListAdapter(Context context) {
        super();
        list = new ArrayList<Map<String,String>>();
        this.context = context;
    }

    public DbpjListAdapter(List<Map<String,String>> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_bmpj_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
            holder.bmName.setText(list.get(position).get("BPJR"));
            holder.pjdjText.setText(list.get(position).get("DJ"));
            holder.dzsText.setText(list.get(position).get("DZS")==null? "0":list.get(position).get("DZS")+"");
            holder.sjzsText.setText(list.get(position).get("SJS") == null ?"0":list.get(position).get("SJS")+"");

            holder.zfText.setText(list.get(position).get("ZF") == null ? "99.55":formateRate(list.get(position).get("ZF"))+"");
            holder.pjfText.setText(list.get(position).get("FS")== null ?"99.55":formateRate(list.get(position).get("FS"))+"");
            holder.sjsfjsczText.setText(list.get(position).get("59205ff2a5b2b1610460435c")==null?
                    "99.55":formateRate(list.get(position).get("59205ff2a5b2b1610460435c"))+"");
            holder.sjsftsczText.setText(list.get(position).get("59206000a5b2b1610460435d")==null?
                    "99.55":formateRate(list.get(position).get("59206000a5b2b1610460435d"))+"");
            holder.sjclfwzlText.setText(list.get(position).get("595c77d14f75180274e11dfc")==null?
                    "99.55":formateRate(list.get(position).get("595c77d14f75180274e11dfc"))+"");
            holder.sjclfwtdText.setText(list.get(position).get("595c77894f75180274e11dfa")==null?
                    "99.55":formateRate(list.get(position).get("595c77894f75180274e11dfa"))+"");
            holder.sjclfwsxText.setText(list.get(position).get("595c77bc4f75180274e11dfb")==null?
                    "99.55": formateRate(list.get(position).get("595c77bc4f75180274e11dfb"))+"");
            holder.sjsfyfczText.setText(list.get(position).get("595c77664f75180274e11df9")==null?
                    "99.55":formateRate(list.get(position).get("595c77664f75180274e11df9"))+"");

        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        return convertView;
    }

    //格式化 电子化移交完成率 保留两位
    public String formateRate(String rateStr){
        if(rateStr.indexOf(".") != -1){
            //获取小数点的位置
            int num = 0;
            num = rateStr.indexOf(".");

            //获取小数点后面的数字 是否有两位 不足两位补足两位
            String dianAfter = rateStr.substring(0,num+1);
            String afterData = rateStr.replace(dianAfter, "");
            if(afterData.length() < 2){
                afterData = afterData + "0" ;
            }else{
                afterData = afterData;
            }
            return rateStr.substring(0,num) + "." + afterData.substring(0,2);
        }else{
            if(rateStr == "1"){
                return "100";
            }else{
                return rateStr;
            }
        }
    }

    /**
     * 存放控件
     */

    public void notifyDataSetChanged(List<Map<String,String>> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<Map<String,String>> addList) {
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<Map<String,String>> getList() {
        return list;
    }


    static class ViewHolder {
        @BindView(R.id.bm_name)
        TextView bmName;
        @BindView(R.id.pjdj_text)
        TextView pjdjText;
        @BindView(R.id.dzs_text)
        TextView dzsText;
        @BindView(R.id.sjzs_text)
        TextView sjzsText;
        @BindView(R.id.zf_text)
        TextView zfText;
        @BindView(R.id.pjf_text)
        TextView pjfText;
        @BindView(R.id.sjsfjscz_text)
        TextView sjsfjsczText;
        @BindView(R.id.sjsftscz_text)
        TextView sjsftsczText;
        @BindView(R.id.sjclfwzl_text)
        TextView sjclfwzlText;
        @BindView(R.id.sjclfwtd_text)
        TextView sjclfwtdText;
        @BindView(R.id.sjclfwsx_text)
        TextView sjclfwsxText;
        @BindView(R.id.sjsfyfcz_text)
        TextView sjsfyfczText;
        @BindView(R.id.item_view)
        View itemView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
