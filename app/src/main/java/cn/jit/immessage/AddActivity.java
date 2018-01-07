package cn.jit.immessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private String[] name = { "王小二"};
    private String[] desc = { "15150010"};
    private ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        lv1 = (ListView) findViewById(R.id.add_lv1);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            //listem.put("head", imageids[i]);
            listem.put("name", name[i]);
            listem.put("desc", desc[i]);
            listems.add(listem);
        }

        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.additem, new String[] { "name", "head", "desc" },
                new int[] {R.id.name,R.id.head,R.id.desc});

        lv1=(ListView)findViewById(R.id.add_lv1);
        lv1.setAdapter(simplead);

    }
}
