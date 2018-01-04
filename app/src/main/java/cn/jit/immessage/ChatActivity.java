package cn.jit.immessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ChatActivity extends AppCompatActivity  {

    private ImageButton plus;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        initListener();
    }

    private void initView() {
        plus = (ImageButton) findViewById(R.id.chat_plus);
        layout = (RelativeLayout)findViewById(R.id.layout);
    }

    private void initListener() {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 layout.setVisibility(View.VISIBLE);
            }
        });
    }

}
