package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.BmpjEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DbpjListAdapter extends BaseAdapter {
    public List<BmpjEntity> list;
    public Context context;


    public DbpjListAdapter(Context context) {
        super();
        list = new ArrayList<BmpjEntity>();
        this.context = context;
    }

    public DbpjListAdapter(List<BmpjEntity> list, Context context) {
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
            holder.bmName.setText(list.get(position).getBPJR());
            holder.pjdjText.setText(list.get(position).getDJ());
            holder.dzsText.setText(list.get(position).getDZS()==null? "0":list.get(position).getDZS()+"");
            holder.sjzsText.setText(list.get(position).getSJS() == null ?"0":list.get(position).getSJS()+"");
           /* holder.zfText.setText(list.get(position).getZF() == null ? "99.55":isNumber(list.get(position).getZF())+"");
            holder.pjfText.setText(list.get(position).getFS()== null ?"99.55":isNumber(list.get(position).getFS())+"");
            holder.sjsfjsczText.setText(list.get(position).get_$59205ff2a5b2b1610460435c()==null?
                    "99.55":isNumber(list.get(position).get_$59205ff2a5b2b1610460435c())+"");
            holder.sjsftsczText.setText(list.get(position).get_$59206000a5b2b1610460435d()==null?
                    "99.55":isNumber(list.get(position).get_$59206000a5b2b1610460435d())+"");
            holder.sjclfwzlText.setText(list.get(position).get_$595c77d14f75180274e11dfc()==null?
                    "99.55":isNumber(list.get(position).get_$595c77d14f75180274e11dfc())+"");
            holder.sjclfwtdText.setText(list.get(position).get_$595c77894f75180274e11dfa()==null?
                    "99.55":isNumber(list.get(position).get_$595c77894f75180274e11dfa())+"");
            holder.sjclfwsxText.setText(list.get(position).get_$595c77bc4f75180274e11dfb()==null?
                    "99.55": isNumber(list.get(position).get_$595c77bc4f75180274e11dfb())+"");
            holder.sjsfyfczText.setText(list.get(position).get_$595c77664f75180274e11df9()==null?
                    "99.55":isNumber(list.get(position).get_$595c77664f75180274e11df9())+"");*/

            holder.zfText.setText(list.get(position).getZF() == null ? "99.55":isNumber(list.get(position).getZF())+"");
            holder.pjfText.setText(list.get(position).getFS()== null ?"99.55":isNumber(list.get(position).getFS())+"");
            holder.sjsfjsczText.setText(list.get(position).get_$59205ff2a5b2b1610460435c()==null?
                    "99.55":isNumber(list.get(position).get_$59205ff2a5b2b1610460435c())+"");
            holder.sjsftsczText.setText(list.get(position).get_$59206000a5b2b1610460435d()==null?
                    "99.55":isNumber(list.get(position).get_$59206000a5b2b1610460435d())+"");
            holder.sjclfwzlText.setText(list.get(position).get_$595c77d14f75180274e11dfc()==null?
                    "99.55":isNumber(list.get(position).get_$595c77d14f75180274e11dfc())+"");
            holder.sjclfwtdText.setText(list.get(position).get_$595c77894f75180274e11dfa()==null?
                    "99.55":isNumber(list.get(position).get_$595c77894f75180274e11dfa())+"");
            holder.sjclfwsxText.setText(list.get(position).get_$595c77bc4f75180274e11dfb()==null?
                    "99.55": isNumber(list.get(position).get_$595c77bc4f75180274e11dfb())+"");
            holder.sjsfyfczText.setText(list.get(position).get_$595c77664f75180274e11df9()==null?
                    "99.55":isNumber(list.get(position).get_$595c77664f75180274e11df9())+"");

        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        return convertView;
    }

    /**
     * 判断字符串是否是数字
     */
    public static String  isNumber(String value) {
        String str = "";
        if(isInteger(value) || isDouble(value)){
            double vals = roundForNumber(Double.valueOf((String) value),2);
            str = String.valueOf(vals);
        }else{
            str = (String) value;
        }
        return str;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     *
     * 提供精确的小数位四舍五入处理。
     *
     * @param v
     *            需要四舍五入的数字
     *
     * @param scale
     *            小数点后保留几位
     *
     * @return 四舍五入后的结果
     *
     */

    public static double roundForNumber(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 存放控件
     */

    public void notifyDataSetChanged(List<BmpjEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<BmpjEntity> addList) {
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<BmpjEntity> getList() {
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
