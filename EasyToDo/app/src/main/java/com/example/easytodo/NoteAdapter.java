package com.example.easytodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//BaseAdapter所有适配器都要继承它，Filterable方便对内容进行排序
public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Note> backList;//用来存储原始数据
    private List<Note> noteList;//这个数据是会改变的，所以要有个变量来备份一下原始数据

    private MyFilter mFilter;//对笔记根据更新时间进行正序或倒序的排列
    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    //getCount()必须实现，不然报错
    @Override
    public int getCount() {
        return noteList.size();
    }
    //根据index返回相应的对象
    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }
    //根据id返回相应的对象
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //第一行内容，第二行时间
        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = v.findViewById(R.id.tv_content);
        TextView tv_time = v.findViewById(R.id.tv_time);

        //Set text for TextView

        String allText = noteList.get(position).getContent();
        /*if(sharedPreferences.getBoolean("noteTitle", true))
            tv_content.setText(allText.split("\n")[0]);*/
        tv_content.setText(allText);
        tv_time.setText(noteList.get(position).getTime());

        //Save note id to long
        v.setTag(noteList.get(position).getId());

        return v;


    }

// 当作筛选器，筛选想要的笔记并排序
    @Override
    public Filter getFilter() {
        if (mFilter == null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        //在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = backList;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Note note : backList) {
                    if (note.getContent().contains(charSequence)) {
                        list.add(note);
                    }

                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList = (List<Note>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }

}
