package cn.jit.immessage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.socketio.callback.StringCallback;
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
    private ImageButton imbtn3;

    private TextView send_tv1;
    private TextView send_tv2;
    private TextView send_tv3;
    private TextView recv_tv1;
    private TextView recv_tv2;
    private TextView recv_tv3;




    public static Handler mhandler;
    String[] url=new String[2];
    String sendphone;
    String recvphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        final String content1 = pre.getString("sms_content", "");
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
        imbtn3=(ImageButton)findViewById(R.id.chat_imbtn3);

        //EditText悬浮
        resetSendMsgRl();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        recvphone = desc;
        Body1Activity.flag = 1;
        socketContent = sendphone + "," + "0" + "," + "alive" + recvphone + "\n";
        tv1.setText(name+"");


        String bql = "select * from uinfo where phone='" + content1 + "'or phone='" + desc + "'";
        BmobQuery<uinfo> query = new BmobQuery<uinfo>();
        //设置查询的SQL语句
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<uinfo>() {

            @Override
            public void done(BmobQueryResult<uinfo> result, BmobException e) {
                if (e == null) {
                    List<uinfo> list = (List<uinfo>) result.getResults();
                    for (uinfo uf1 : list) {
                        if (uf1.getPhone().equals(content1)) {
                            url[0] = uf1.getPhoto().getFileUrl();
                            Log.d("18989","url="+url[0]);
                        } else {
                            url[1] = uf1.getPhoto().getFileUrl();
                            Log.d("15989","url="+url[1]);

                        }

                    }


                    send.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                String content = inputText.getText().toString();

                                //System.out.println(url[1]+"");
                                if(!"".equals(content)) {
                                    Message mesg = new Message();
                                    mesg.what = 1;
                                    mesg.obj = new Mes(sendphone, recvphone, inputText.getText().toString());

                                    BodyService.bodyThread.revHandler.sendMessage(mesg);

                                    Msg msg = new Msg(content, Msg.TYPE_SENT,url[0]);
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
                                    BodyService.bodyThread.revHandler.sendMessage(mesg);


                                    Msg msg = new Msg(content, Msg.TYPE_SENT,url[0]);
                                    msgList.add(msg);
                                    adapter.notifyItemChanged(msgList.size() - 1);
                                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                    inputText.setText("");
                                }
                                return true;
                            }
                        });
                    mhandler = new Handler() {
                        @Override
                        public void handleMessage(final Message msg) {
                            if (msg.what == 0) {
                                 final Mes bb = new Mes((String) msg.obj);
                                if (bb.getRecv().equals(sendphone)) {
                                    if (bb.getSend().equals(recvphone)) {
                                        Log.d("8888",bb.getText()+"");
                                        String sss = bb.getText().substring(0,3);

                                        if (sss.equals("ftp")) {
                                            final String content=bb.getText();

//
                                            BmobQuery<fileurl> query = new BmobQuery<>();
                                            query.getObject(bb.getText().substring(4), new QueryListener<fileurl>() {
                                                @Override
                                                public void done(final fileurl object, BmobException e) {
                                                    if(e==null){
                                                        object.getBfile().download(new File(Environment.getExternalStorageDirectory(), object.getBfile().getFilename()), new DownloadFileListener() {

                                                            @Override
                                                            public void onStart() {
//                                                                toast("开始下载...");
                                                                Toast.makeText(ChatActivity.this,"开始下载...",Toast.LENGTH_SHORT).show();
                                                            }
                                                            @Override
                                                            public void done(String savePath,BmobException e) {
                                                                Log.d("18888", "url=" + url[1]);
                                                                if(e==null){
                                                                    Toast.makeText(ChatActivity.this,"下载成功，保存路径"+savePath,Toast.LENGTH_SHORT).show();
                                                                    Msg msg1 = new Msg(content, Msg.FILE_RECV, url[1],object.getBfile().getFilename(),object.getFilesize()+"","已接收");
                                                                    msgList.add(msg1);
                                                                }else{
                                                                    Toast.makeText(ChatActivity.this,"下载失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onProgress(Integer value, long newworkSpeed) {
                                                                Log.i("bmob","下载进度："+value+","+newworkSpeed);
                                                            }
                                                        });

                                                    }else{
                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                }

                                            });

                                        } else {
                                            Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED, url[1]);
                                            Log.d("18888", "url=" + url[1]);
                                            msgList.add(msg1);
                                        }
                                    }
                                }
                            }
                        }
                    };

                }

            }
        });




        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,Info4Activity.class);
                intent.putExtra("desc",desc);
                startActivity(intent);
            }
        });
        imbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This always works
                Intent i = new Intent(ChatActivity.this, FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, 2);
            }
        });

        initListener();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(ChatActivity.this,Body1Activity.class);
                startActivity(in);

               finish();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
//        send.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                String content = inputText.getText().toString();
//                if(!"".equals(content)) {
//                    Message mesg = new Message();
//                    mesg.what = 1;
//                    mesg.obj = new Mes(sendphone, recvphone, inputText.getText().toString());
//                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);
//                    Log.d("1111","url="+url[0]);
//
//                    Msg msg = new Msg(content, Msg.TYPE_SENT,url[0]);
//                    msgList.add(msg);
//                    adapter.notifyItemChanged(msgList.size() - 1);
//                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
//                    inputText.setText("");
//                }
//            }
//        });

//        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                String content = inputText.getText().toString();
//                if(!"".equals(content)) {
//                    Message mesg = new Message();
//                    mesg.what = 1;
//                    mesg.obj = new Mes(sendphone, recvphone, inputText.getText().toString());
//                    Body1Activity.bodyThread.revHandler.sendMessage(mesg);
//                    Log.d("1111","url="+url[1]);
//
//                    Msg msg = new Msg(content, Msg.TYPE_SENT,url[1]);
//                    msgList.add(msg);
//                    adapter.notifyItemChanged(msgList.size() - 1);
//                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
//                    inputText.setText("");
//                }
//                return true;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(ChatActivity.this,Body1Activity.class);
        startActivity(in);
        finish();
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

    //EditText悬浮
    private void resetSendMsgRl(){

        final View decorView=getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            final LinearLayout rlContent = (LinearLayout) findViewById(R.id.chat_layout);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(intent);
            for (Uri uri: files) {
                final File file = Utils.getFileForUri(uri);

                try {
                    long size=getFileSizes(file);
                    Log.d("456",FormentFileSize(size)+"");
                    //send_tv2.setText(FormentFileSize(size));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("555",file+"");

                final BmobFile icon = new BmobFile(file);
                //send_tv1.setText(icon.getFilename());


                icon.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        final fileurl fu1=new fileurl();
                        fu1.setBfile(icon);
                        try {
                            fu1.setFilesize(FormentFileSize(getFileSizes(file)));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        fu1.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Message mesg = new Message();
                                    mesg.what = 1;
                                    mesg.obj = new Mes(sendphone, recvphone, "ftp/"+fu1.getObjectId());
                                    BodyService.bodyThread.revHandler.sendMessage(mesg);
                                    Log.d("9999",url[0]);
                                    Msg msg = null;
                                    try {
                                        msg = new Msg("ftp/"+fu1.getObjectId(), Msg.FILE_SENT,url[0],icon.getFilename(),FormentFileSize(getFileSizes(file)),"已发送");
                                        msgList.add(msg);
                                        adapter.notifyItemChanged(msgList.size() - 1);
                                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                        inputText.setText("");
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


                                }else{
                                    Log.d("666","error");
                                }
                            }
                        });
                    }
                });


                // Do something with the result...
            }
        }
    }
    /*得到传入文件的大小*/
    public static long getFileSizes(File f) throws Exception {

        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }

        return s;
    }

    /**
     * 转换文件大小成KB  M等
     */
    public static String FormentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
