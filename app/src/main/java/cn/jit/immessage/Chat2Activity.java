package cn.jit.immessage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

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
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import qiu.niorgai.StatusBarCompat;

import static cn.jit.immessage.Body1Activity.socketContent;

public class Chat2Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView tv1;
    private EditText et1;
    private  static int filedownb=0;
    String[] url2=new String[2];
    private MsgAdapter adapter;
    boolean visibility_Flag = false;
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private RelativeLayout layout;
    private String desc;
    private ImageButton imbtn1;
    private ImageButton imbtn3;
    private ImageButton imbtn2;
    private ImageButton imbtn5;
    List<Uri> mSelected;
    public static final int REQUEST_CODE_CHOOSE = 3;

    // 录音类
    private MediaRecorder mediaRecorder;
    // 以文件的形式保存
    private File recordFile;
    private RecordPlayer player;


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
        imbtn2=(ImageButton)findViewById(R.id.chat2_imbtn2);
        imbtn3=(ImageButton)findViewById(R.id.chat2_imbtn3);
        imbtn5=(ImageButton)findViewById(R.id.chat2_imbtn5);


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

        String bql = "select * from uinfo where phone='" + sendphone+ "'";
        BmobQuery<uinfo> query = new BmobQuery<uinfo>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<uinfo>() {
            @Override
            public void done(BmobQueryResult<uinfo> result, BmobException e) {
                if (e == null) {
                    List<uinfo> list = (List<uinfo>) result.getResults();
                    for (uinfo uf1 : list) {
                         url2[0] = uf1.getPhoto().getFileUrl();

                        btn3.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                String content = et1.getText().toString();
                                if(!"".equals(content)) {
                                    Message mesg = new Message();
                                    mesg.what = 1;
                                    mesg.obj = new Mes(sendphone, groupphone, et1.getText().toString());
                                    BodyService.bodyThread.revHandler.sendMessage(mesg);

                                    Msg msg = new Msg(content, Msg.TYPE_SENT,url2[0]);
                                    msgList.add(msg);
                                    adapter.notifyItemChanged(msgList.size() - 1);
                                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                    et1.setText("");
                                }
                            }
                        });

                    }
                }
            }
        });
        imbtn2.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                Matisse.from(Chat2Activity.this)
                        .choose(MimeType.allOf()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(1) // 图片选择的最多数量
                        //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码

            }


        });

        imbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Chat2Activity.this, FilePickerActivity.class);

                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, 2);
            }
        });

        imbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player = new RecordPlayer(Chat2Activity.this);
                new CommomDialog2(Chat2Activity.this, R.style.dialog, "点击按钮即可开始录音", new CommomDialog2.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {


                        final File recordFile = new File("/storage/sdcard0", "kk.amr");
                        if(confirm){
                            Toast.makeText(Chat2Activity.this,"111111",Toast.LENGTH_SHORT).show();
                            mediaRecorder = new MediaRecorder();
//                            // 判断，若当前文件已存在，则删除
//                            if (recordFile.exists()) {
//                                recordFile.delete();
//                            }
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                            mediaRecorder.setOutputFile(recordFile.getAbsolutePath());

                            try {
                                // 准备好开始录音
                                mediaRecorder.prepare();

                                mediaRecorder.start();
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }else{
                            if (recordFile != null) {
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                final BmobFile voice = new BmobFile(recordFile);

                                voice.uploadblock(new UploadFileListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        final fileurl fu2=new fileurl();
                                        fu2.setBfile(voice);
                                        fu2.setFilesize("kk_"+fu2.getObjectId()+".amr");
                                        fu2.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if(e==null){
                                                    recordFile.renameTo(new File("/storage/sdcard0", "kk_"+fu2.getObjectId()+".amr"));
                                                    Message mesg = new Message();
                                                    mesg.what = 1;
                                                    mesg.obj = new Mes(sendphone, groupphone, "voi/"+fu2.getObjectId());
                                                    BodyService.bodyThread.revHandler.sendMessage(mesg);
                                                    Msg msg = null;
                                                    try {
                                                        msg = new Msg("voi/"+fu2.getObjectId(), Msg.VOICE_SENT,url2[0],fu2.getObjectId());
                                                        msgList.add(msg);
                                                        adapter.notifyItemChanged(msgList.size() - 1);
                                                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                                        et1.setText("");
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }


                                                }else{
                                                    Toast.makeText(Chat2Activity.this,"123456",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                });
                            }

                            dialog.dismiss();
                        }

                    }
                }).setTitle("录音").show();

            }
        });


        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    final Mes bb = new Mes((String) msg.obj);
                    String recv_phone=bb.getSend();
                String bql = "select * from uinfo where phone='" + recv_phone+ "'";
                BmobQuery<uinfo> query = new BmobQuery<uinfo>();
                //设置查询的SQL语句
                query.setSQL(bql);
                query.doSQLQuery(new SQLQueryListener<uinfo>() {

                    @Override
                    public void done(BmobQueryResult<uinfo> result, BmobException e) {
                        if (e == null) {
                            List<uinfo> list = (List<uinfo>) result.getResults();
                            for (uinfo uf1 : list) {
                                url2[1] = uf1.getPhoto().getFileUrl();
                                if (!bb.getSend().equals(sendphone)) {
                                    if (bb.getRecv().equals(groupphone)) {
                                        if (bb.getText().length() > 4) {

                                            String sss = bb.getText().substring(0, 3);
                                            if (sss.equals("ftp")) {

//
                                                BmobQuery<fileurl> query = new BmobQuery<>();
                                                query.getObject(bb.getText().substring(4), new QueryListener<fileurl>() {
                                                    @Override
                                                    public void done( fileurl object, BmobException e) {
                                                        if (e == null) {
                                                            Msg msg1 = new Msg(null, Msg.FILE_RECV, url2[1] , object.getBfile().getFilename(), object.getFilesize() + "", "等待下载",object.getObjectId());
                                                            msgList.add(msg1);
//

                                                        } else {
                                                            Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED, url2[1] );


                                                            msgList.add(msg1);
                                                        }
                                                    }

                                                });

                                            }else if(sss.equals("img")) {
                                                BmobQuery<fileurl> query = new BmobQuery<>();
                                                query.getObject(bb.getText().substring(4), new QueryListener<fileurl>() {
                                                    @Override
                                                    public void done(final fileurl object, BmobException e) {
                                                        if (e == null) {
                                                            Msg msg1 = new Msg(null, Msg.IMG_RECV, url2[1], object.getBfile().getFileUrl(),0);
                                                            msgList.add(msg1);

                                                        } else {
                                                            Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED, url2[1]);
                                                            msgList.add(msg1);
                                                        }
                                                    }
                                                });
                                            }else if (sss.equals("voi")) {
                                                BmobQuery<fileurl> query = new BmobQuery<>();
                                                query.getObject(bb.getText().substring(4), new QueryListener<fileurl>() {

                                                    @Override
                                                    public void done(final fileurl object, BmobException e) {
                                                        object.getBfile().download(new File("/storage/sdcard0","kk_"+ object.getObjectId()+".amr"), new DownloadFileListener() {
                                                            @Override
                                                            public void onStart() {
//
                                                            }

                                                            @Override
                                                            public void done(String savePath, BmobException e) {
                                                                if (e == null) {
                                                                    Msg msg1 = new Msg(null, Msg.VOICE_RECV, url2[1], object.getObjectId());
                                                                    msgList.add(msg1);
                                                                } else {
                                                                    Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED, url2[1]);
                                                                    msgList.add(msg1);
                                                                }

                                                                object.setObjectId(object.getObjectId());
                                                                object.delete(new UpdateListener() {

                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        if (e == null) {
                                                                        } else {
                                                                            Toast.makeText(Chat2Activity.this, "删除FILE失败", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                });


                                                            }

                                                            @Override
                                                            public void onProgress(Integer value, long newworkSpeed) {

                                                            }
                                                        });

                                                    }
                                                });


                                            } else {
                                                Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED,url2[1] );
                                                msgList.add(msg1);
                                            }
                                        } else {
                                            Msg msg1 = new Msg(bb.getText(), Msg.TYPE_RECEIVCED, url2[1] );
                                            msgList.add(msg1);

                                        }
                                    }
                                }

                            }
                        }

                    }
                });



                }
            }
        };

        initListener();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Chat2Activity.this,Body1Activity.class);
                startActivity(in);
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


        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = et1.getText().toString();
                if(!"".equals(content)) {
                    Message mesg = new Message();
                    mesg.what = 1;
                    mesg.obj = new Mes(sendphone, groupphone, et1.getText().toString());
                    BodyService.bodyThread.revHandler.sendMessage(mesg);

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
    public void onBackPressed() {
        Intent in = new Intent(Chat2Activity.this,Body1Activity.class);
        startActivity(in);
        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(intent);
            for (Uri uri: files) {
                final File file = Utils.getFileForUri(uri);

                try {
                    long size=getFileSizes(file);
                    //send_tv2.setText(FormentFileSize(size));

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                                    mesg.obj = new Mes(sendphone, groupphone, "ftp/"+fu1.getObjectId());
                                    BodyService.bodyThread.revHandler.sendMessage(mesg);

                                    Msg msg = null;
                                    try {
                                        msg = new Msg("ftp/"+fu1.getObjectId(), Msg.FILE_SENT,url2[0],icon.getFilename(),FormentFileSize(getFileSizes(file)),"     已发送");
                                        msgList.add(msg);
                                        adapter.notifyItemChanged(msgList.size() - 1);
                                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                        et1.setText("");
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


                                }else{

                                }
                            }
                        });
                    }
                });
            }
        }
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(intent);
            Log.e("Matisse", "mSelected: " + mSelected);
            //            回调要显示的图片

            for(int i=0; i<mSelected.size(); i++) {

                Uri uri = (Uri) mSelected.get(i);
                Log.e("Matisse", "i:    "+ mSelected.get(i));
                String img_path = getImagePath(uri,null);
                final File img_file = new File(img_path);

                final BmobFile icon = new BmobFile(img_file);
                Log.e("Matisse", "icon: " + icon );
                // Log.e("Matisse", "fileurl " + icon.getFileUrl() );

                icon.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        final fileurl fu2=new fileurl();
                        fu2.setBfile(icon);
                        try {
                            fu2.setFilesize(FormentFileSize(getFileSizes(img_file)));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        fu2.save(new SaveListener<String>() {

                            @Override
                            public void done(String s, BmobException e) {

                                if(e==null){
                                    Message mesg = new Message();
                                    mesg.what = 1;
                                    mesg.obj = new Mes(sendphone, groupphone, "img/"+fu2.getObjectId());
                                    BodyService.bodyThread.revHandler.sendMessage(mesg);
                                    Msg msg = null;
                                    try {
                                        msg = new Msg("img/"+fu2.getObjectId(), Msg.IMG_SENT,url2[0],icon.getFileUrl(),0);
                                        Log.e("Matisse", "fileurl " + icon.getFileUrl() );
                                        msgList.add(msg);
                                        adapter.notifyItemChanged(msgList.size() - 1);
                                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                        et1.setText("");
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


                                }else{
                                    //Log.e("Matisse", "失败: "+e.getMessage());
                                }
                            }
                        });
                    }
                });
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
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }


}



