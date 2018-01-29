package cn.jit.immessage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fyf on 2018/1/17.
 */

public class CommomDialog2 extends Dialog implements View.OnClickListener {

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private Chronometer timer;
    private TextView timer1;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CommomDialog2(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog2(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommomDialog2(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CommomDialog2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog2 setTitle(String title){
        this.title = title;
        return this;
    }

    public CommomDialog2 setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommomDialog2 setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promptbox2);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
//        contentTxt = (TextView)findViewById(R.id.prombox2_content);
        titleTxt = (TextView)findViewById(R.id.prombox2_title);
        timer=(Chronometer)findViewById(R.id.voice_timer);
        timer1=(TextView)findViewById(R.id.voice_timer1) ;

        submitTxt = (TextView)findViewById(R.id.prombox2_submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.prombox2_cancel);
        cancelTxt.setOnClickListener(this);

//        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prombox2_cancel:
                timer.stop();
                if(listener != null){

                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.prombox2_submit:
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                timer.setFormat("0"+String.valueOf(hour)+":%s");
                timer.start();
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer1.setText(millisUntilFinished / 1000 + "秒");
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                if(listener != null){


                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }



}
