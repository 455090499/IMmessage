package cn.jit.immessage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

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
        listView = (ListView) view.findViewById(R.id.body_message_lv);
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
            String bql ="select * from uinfo where phone in (select fphone from pfriend where phone = '"+Body1Activity.p1.getPhone()+"')";
            BmobQuery<uinfo> query=new BmobQuery<uinfo>();
//设置查询的SQL语句
            query.setSQL(bql);
            query.doSQLQuery(new SQLQueryListener<uinfo>(){

                @Override
                public void done(BmobQueryResult<uinfo> result, BmobException e) {
                    if(e ==null){
                        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

                        List<uinfo> list = (List<uinfo>) result.getResults();

                            for (uinfo uf1 : list) {
                                Map<String, Object> listem = new HashMap<String, Object>();
                                String name =uf1.getNiconame();
                                String desc =uf1.getPhone();
                                Log.e(TAG, "done:"+name);
                                listem.put("name", name);
                                listem.put("desc", desc);
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

                    }else{
                        Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
                }
            });









//
//            BmobQuery<pfriend> bmobQuery = new BmobQuery<>();
//            bmobQuery.addWhereEqualTo("phone",Body1Activity.p1.getPhone());
//            bmobQuery.findObjects(new FindListener<pfriend>() {
//                @Override
//                public void done(List<pfriend> list, BmobException e) {
//                    int j = 0;
//                    for (pfriend pf1 : list) {
//
//                        Body1Activity.ffd[j++] = pf1.getFphone();
//                    }
//                    final List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
//
//                    for (int i = 0; i < j; i++) {
//                        BmobQuery<uinfo> bmobQuery = new BmobQuery<>();
//                        bmobQuery.addWhereEqualTo("phone", Body1Activity.ffd[i]);
//                        bmobQuery.findObjects(new FindListener<uinfo>() {
//                            @Override
//                            public void done(List<uinfo> list, BmobException e) {
//                                final Map<String, Object> listem = new HashMap<String, Object>();
//
//                                for (uinfo uf1 : list) {
//                                    String name =uf1.getNiconame();
//                                    String desc =uf1.getPhone();
//
//
//                                        listem.put("name", name);
//                                        listem.put("desc", desc);
//                                    listems.add(listem);
//                                }
//
//
//
//                            }
//                        });
//                    }
//
//                    SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
//                            R.layout.haoyou, new String[] { "name", "head", "desc" },
//                            new int[] {R.id.name,R.id.head,R.id.desc});
//                    simplead.notifyDataSetChanged();
//
//                    listView.setAdapter(simplead);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                            Intent intent;
//                            intent = new Intent(getActivity(),ChatActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                }
//            });

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