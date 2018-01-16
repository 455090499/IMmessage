package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

import static cn.jit.immessage.Body1Activity.socketContent;

public class ChatActivity extends AppCompatActivity  {

    private ImageButton plus;
    private RelativeLayout layout;
    boolean visibility_Flag = false;
    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;
    private EditText inputText;
    private Button send;
    private Button back;
    private RecyclerView msgRecyclerView;
    private TextView tv1;
    private ImageButton imbtn;
    public static Handler mhandler;

    String sendphone;
    String recvphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content1 = pre.getString("sms_content", "");
        sendphone = content1;

        //透明状态栏
        StatusBarCompat.translucentStatusBar(ChatActivity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(ChatActivity.this,true);

        plus = (ImageButton) findViewById(R.id.chat_imbtn1);
        layout = (RelativeLayout)findViewById(R.id.layout);
        inputText = (EditText) findViewById(R.id.chat_et1);
        back=(Button)findViewById(R.id.chat_btn1);
        send = (Button) findViewById(R.id.chat_btn2);
        msgRecyclerView = (RecyclerView) findViewById(R.id.chat_rv1);
        tv1=(TextView)findViewById(R.id.chat_tv1);
        imbtn=(ImageButton)findViewById(R.id.chat_imbtn);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        recvphone = desc;
        Body1Activity.flag = 1;
        socketContent = sendphone + "," + "0" + "," + "alive" + recvphone + "\n";

        tv1.setText(name+"");

        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Mes bb = new Mes((String) msg.obj);
                    if (bb.getRecv().equals(sendphone)) {
                        Msg msg1 = new Msg(bb.getText(),Msg.TYPE_RECEIVCED);
                        msgList.add(msg1);
                    }
                }
            }
        };

        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,Info4Activity.class);
                intent.putExtra("desc",desc);
                startActivity(intent);
            }
        });

        initListener();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Message mesg = new Message();
                    mesg.what = 1;
                    mesg.obj = new Mes(sendphone, recvphone, inputText.getText().toString());
                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);

                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");
                }
            }
        });

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Message mesg = new Message();
                    mesg.what = 1;
                    mesg.obj = new Mes(sendphone, recvphone, inputText.getText().toString());
                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);

                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");
                }
                return true;
            }
        });
    }


    private void initListener() {
        plus.setOnClickListener(new View.OnClickListener() {
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

}
