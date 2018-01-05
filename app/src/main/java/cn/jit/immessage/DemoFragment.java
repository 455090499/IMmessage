package cn.jit.immessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2018/1/3.
 */

public class DemoFragment extends Fragment {
   private ListView listView;
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_message, container, false);
        int type = getArguments().getInt("type", 0);
        listView = (ListView) view.findViewById(R.id.listview);
        //textView.setText(type == 0 ? "1st Fragment" : type == 1 ? "2nd Fragment" : type == 2 ? "3rd Fragment" : "4th Fragment");
        if (type == 0)
        {
            Log.e(TAG, "onCreateView: xiaoxi" );
            String[] name = { "李四", "张三"};
            String[] desc = { "怎么不回我信息", "在哪呢"};
            //int[] imageids ={};
            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < name.length; i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
              //  listem.put("head", imageids[i]);
                listem.put("name", name[i]);
                listem.put("desc", desc[i]);
                listems.add(listem);
            }
           SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                   R.layout.xiaoxi, new String[] { "name", "head", "desc" },
                   new int[] {R.id.name,R.id.head,R.id.desc});
            listView.setAdapter(simplead);



        }
        if (type == 1)
        {
            String[] name = { "王二", "张三"};
            String[] desc = { "15150010", "15150011"};
            //int[] imageids ={};
            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < name.length; i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
                //  listem.put("head", imageids[i]);
                listem.put("name", name[i]);
                listem.put("desc", desc[i]);
                listems.add(listem);
            }
            SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                    R.layout.haoyou, new String[] { "name", "head", "desc" },
                    new int[] {R.id.name,R.id.head,R.id.desc});
            listView.setAdapter(simplead);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent;
                    intent = new Intent(getActivity(),ChatActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (type == 2)
        {
            //Log.e(TAG, "onCreateView: qunzu");
            String[] name = { "金科一班群", "金科二班群"};
            //String[] desc = { "15150010", "15150011"};
            //int[] imageids ={};
            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < name.length; i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
                //  listem.put("head", imageids[i]);
                listem.put("name", name[i]);
              //  listem.put("desc", desc[i]);
                listems.add(listem);
            }
            SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                    R.layout.qunzu, new String[] { "name", "head", "desc" },
                    new int[] {R.id.name,R.id.head,R.id.desc});
            listView.setAdapter(simplead);
        }

        return view;
    }



}