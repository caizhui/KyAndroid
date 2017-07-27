package com.ky.kyandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ky.kyandroid.R;
import com.ky.kyandroid.entity.GwpjEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GwpjListAdapter extends BaseAdapter {
    public List<GwpjEntity> list;
    public Context context;


    public GwpjListAdapter(Context context) {
        super();
        list = new ArrayList<GwpjEntity>();
        this.context = context;
    }

    public GwpjListAdapter(List<GwpjEntity> list, Context context) {
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
            convertView = View.inflate(context, R.layout.activity_gwpj_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);// 绑定ViewHolder对象
            holder.bmName.setText(list.get(position).getBPJR());
            holder.pjdjText.setText(list.get(position).getDJ());
            holder.dzsText.setText(list.get(position).getDZS()==null? "0":list.get(position).getDZS()+"");
            holder.sjzsText.setText(list.get(position).getSJS() == null ?"0":list.get(position).getSJS()+"");

            holder.zfText.setText(list.get(position).getZF() == null ? "99.55":formateRate(list.get(position).getZF())+"");
            holder.pjfText.setText(list.get(position).getFS()== null ?"99.55":formateRate(list.get(position).getFS())+"");
            holder.sjlrjsText.setText(list.get(position).get_$59205fe0a5b2b1610460435b()==null?
                    "99.55":formateRate(list.get(position).get_$59205fe0a5b2b1610460435b())+"");
            holder.sjlrzqwwText.setText(list.get(position).get_$59205fd4a5b2b1610460435a()==null?
                    "99.55":formateRate(list.get(position).get_$59205fd4a5b2b1610460435a())+"");
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

    public void notifyDataSetChanged(List<GwpjEntity> list) {
        this.list = list;
        super.notifyDataSetChanged();
    }

    /**
     * @param addList
     */
    public void addDataSetChanged(List<GwpjEntity> addList) {
        this.list.addAll(addList);
        this.notifyDataSetChanged(list);
    }

    public List<GwpjEntity> getList() {
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
        @BindView(R.id.sjlrjs_text)
        TextView sjlrjsText;
        @BindView(R.id.sjlrzqww_text)
        TextView sjlrzqwwText;

        @BindView(R.id.item_view)
        View itemView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
