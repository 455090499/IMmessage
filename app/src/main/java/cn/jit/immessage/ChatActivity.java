package cn.jit.immessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity  {

    private ImageButton plus;
    private RelativeLayout layout;
    boolean visibility_Flag = false;
    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        initListener();
        initMsgs();

        inputText = (EditText) findViewById(R.id.chat_message);
        send = (Button) findViewById(R.id.chat_send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.chat_body);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("hello!",Msg.TYPE_RECEIVCED);
        msgList.add(msg1);
        Msg msg2 = new Msg("hello,who",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("this id mark",Msg.TYPE_RECEIVCED);
        msgList.add(msg3);
    }

    private void initView() {
        plus = (ImageButton) findViewById(R.id.chat_plus);
        layout = (RelativeLayout)findViewById(R.id.layout);
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
