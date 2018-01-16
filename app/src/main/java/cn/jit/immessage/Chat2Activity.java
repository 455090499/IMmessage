package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

import static cn.jit.immessage.Body1Activity.socketContent;

public class Chat2Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView tv1;
    private EditText et1;
    private MsgAdapter adapter;
    boolean visibility_Flag = false;
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private RelativeLayout layout;
    private String desc;
    private ImageButton imbtn1;

    String sendphone;
    String groupphone;
    public static Handler mhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content1 = pre.getString("sms_content", "");
        sendphone = content1;

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Chat2Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Chat2Activity.this,true);

        btn1=(Button)findViewById(R.id.chat2_btn1);
        btn2=(Button)findViewById(R.id.chat2_btn2);
        btn3=(Button)findViewById(R.id.chat2_btn3);
        et1=(EditText)findViewById(R.id.chat2_et1);
        layout = (RelativeLayout)findViewById(R.id.layout1);
        msgRecyclerView = (RecyclerView) findViewById(R.id.chat2_rv1);
        imbtn1=(ImageButton)findViewById(R.id.chat2_imbtn1);
        tv1=(TextView) findViewById(R.id.chat2_tv1);

        //EditText悬浮
        resetSendMsgRl();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        groupphone = desc;
        Body1Activity.flag = 2;
        socketContent = sendphone + "," + "0" + "," + "alive" + groupphone + "\n";
        tv1.setText(name);

        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Mes bb = new Mes((String) msg.obj);
                    if (!bb.getSend().equals(sendphone)) {
                        if (bb.getRecv().equals(groupphone)) {
                            Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED);
                            msgList.add(msg1);
                        }
                    }
                }
            }
        };

        initListener();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat2Activity.this,Info5Activity.class);
                intent.putExtra("desc",desc);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        btn3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String content = et1.getText().toString();
                if(!"".equals(content)) {
                    Message mesg = new Message();
                    mesg.what = 1;
                    mesg.obj = new Mes(sendphone, groupphone, et1.getText().toString());
                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);

                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    et1.setText("");
                }
            }
        });

        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = et1.getText().toString();
                if(!"".equals(content)) {
                    Message mesg = new Message();
                    mesg.what = 1;
                    mesg.obj = new Mes(sendphone, groupphone, et1.getText().toString());
                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);

                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    et1.setText("");
                }
                return true;
            }
        });

    }

    private void initListener() {
        imbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibility_Flag) {
                    layout.setVisibility(View.VISIBLE);
                    visibility_Flag = false;
                } else {
                    layout.setVisibility(View.GONE);
                    visibility_Flag = true;
                }
            }
        });
    }

    //EditText悬浮
    private void resetSendMsgRl(){

        final View decorView=getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            final LinearLayout rlContent = (LinearLayout) findViewById(R.id.chat2_layout);

            @Override
            public void onGlobalLayout() {
                Rect rect=new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = getScreenHeight();
                int heightDifference = screenHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) rlContent.getLayoutParams();
                layoutParams.setMargins(0,0,0,heightDifference);//设置rlContent的marginBottom的值为软键盘占有的高度即可
                rlContent.requestLayout();
            }
        });
    }


    private int getScreenHeight(){
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return  height;
    }


}



