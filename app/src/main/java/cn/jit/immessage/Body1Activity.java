package cn.jit.immessage;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import io.github.leibnik.wechatradiobar.WeChatRadioGroup;
import qiu.niorgai.StatusBarCompat;

import static java.lang.Thread.sleep;

public class Body1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    public static boolean rff=true;
    private WeChatRadioGroup gradualRadioGroup;
    private TextView header_tv1;
    private TextView header_tv2;
    public static String[] ffd = new String[60];  ;
    private ImageView im;
    public static TextView tv;
    //获取个人信息页面
    private NavigationView navigationView ;

    static boolean islogin=false;
    pp p2=new pp();
    static pp p1;

    private Handler mHandler;
    public static BodyThread bodyThread;
    public static String socketContent;
    public static int flag = 0;
    String sendphone;

    @Override
    protected void onResume() {
        try {
            sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String bql ="select * from uinfo where phone in (select fphone from pfriend where phone = '"+Body1Activity.p1.getPhone()+"')";
        BmobQuery<uinfo> query=new BmobQuery<uinfo>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<uinfo>(){

            @Override
            public void done(BmobQueryResult<uinfo> result, BmobException e) {
                if(e ==null){
                    final List<DemoFragment> lista = new ArrayList<>();
                    if(lista.size()!=0)
                        for(DemoFragment ll: lista)
                        lista.remove(ll);
                    DemoFragment fragment0 = new DemoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",0);
//                bundle.putInt("count",i);
//                bundle.putStringArrayList("name",i);
//                bundle.putStringArrayList("desc",i);
//                bundle.putStringArrayList("head",i);
//
                    fragment0.setArguments(bundle);
                    lista.add(fragment0);


                    List<uinfo> list = (List<uinfo>) result.getResults();

                    DemoFragment fragment1 = new DemoFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("type",1);
                    bundle1.putInt("count",list.size());
                    String[] name=new String[list.size()];
                    String[] desc=new String[list.size()];
                    String[] head=new String[list.size()];
                    int i=0;
                    for (uinfo uf1 : list) {

                        name[i]=uf1.getNiconame();
                        desc[i]=uf1.getPhone();
                        head[i]=uf1.getPhoto().getFileUrl();
                        Log.d("aaa",head[i]);
                        i++;
                    }
                    bundle1.putStringArray("name",name);
                    bundle1.putStringArray("desc",desc);
                    bundle1.putStringArray("head",head);

                    fragment1.setArguments(bundle1);
                    lista.add(fragment1);


                    String bq2 ="select * from  ginfo where gid in (select gid from gphone where phone = '"+Body1Activity.p1.getPhone()+"')";
                    BmobQuery<ginfo> query=new BmobQuery<ginfo>();
                    query.setSQL(bq2);
                    query.doSQLQuery(new SQLQueryListener<ginfo>(){

                        @Override
                        public void done(BmobQueryResult<ginfo> result, BmobException e) {
                            if(e ==null){
                                List<ginfo> list = (List<ginfo>) result.getResults();
                                DemoFragment fragment2 = new DemoFragment();
                                Bundle bundle2 = new Bundle();
                                bundle2.putInt("type",2);
                                bundle2.putInt("count",list.size());
                                String[] name2=new String[list.size()];
                                String[] desc2=new String[list.size()];
                                String[] head2=new String[list.size()];
                                int i=0;
                                for (ginfo uf1 : list) {
                                    name2[i]=uf1.getGname();
                                    desc2[i]=uf1.getGid();
                                    head2[i]=uf1.getPhoto().getFileUrl();
                                    i++;
                                }
                                bundle2.putStringArray("name",name2);
                                bundle2.putStringArray("desc",desc2);
                                bundle2.putStringArray("head",head2);

                                fragment2.setArguments(bundle2);
                                lista.add(fragment2);

                                DemoPagerAdapter demo=new DemoPagerAdapter(getSupportFragmentManager(), lista);
                                viewPager.setOffscreenPageLimit(2);
                                viewPager.setAdapter(demo);

                                gradualRadioGroup.setViewPager(viewPager);
                            }else{
                                Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    });




//                bundle.putInt("count",i);
//                bundle.putStringArrayList("name",i);
//                bundle.putStringArrayList("desc",i);
//                bundle.putStringArrayList("head",i);
//

                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });


        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content1 = pre.getString("sms_content", "");
        BmobQuery<uinfo> bmobQuery3 = new BmobQuery<>();
        p1.setPhone(content1);
        bmobQuery3.addWhereEqualTo("phone", content1);
        bmobQuery3.findObjects(new FindListener<uinfo>() {
            @Override
            public void done(List<uinfo> list, BmobException e) {
                for (uinfo u4 : list) {
                    BmobFile bmobfile = u4.getPhoto();
                    if (bmobfile != null)
                        try {

                            String url = bmobfile.getFileUrl();
                            Log.e("1", url);
                            Bitmap bitmap = getBitmap(url);
                            im.setImageBitmap(bitmap);
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                    header_tv1.setText(u4.getNiconame());
                    header_tv2.setText(u4.getPs());

                }
            }
        });

        super.onResume();
        flag = 0;
        socketContent = sendphone + "," + "0" + "," + "alive" + "\n";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body1);

        p1 = new pp();


        //透明状态栏
        StatusBarCompat.translucentStatusBar(Body1Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Body1Activity.this,true);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv=(TextView)findViewById(R.id.toolbar_text);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        gradualRadioGroup = (WeChatRadioGroup) findViewById(R.id.radiogroup);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_body1);
        header_tv1=(TextView) headerLayout.findViewById(R.id.header_tv1);
        header_tv2=(TextView) headerLayout.findViewById(R.id.header_tv2);
        im=(ImageView)headerLayout.findViewById(R.id.imageView);

        //图片设置
        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        //获取login的登陆手机号
        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content1 = pre.getString("sms_content", "");

        sendphone = content1;
        socketContent = sendphone + "," + "0" + "," + "alive" + "\n";






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout)
;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SharedPreferences pre = getSharedPreferences("user", Context.MODE_PRIVATE);
        islogin="1".equals(pre.getString("islogin","0").toString());
        if(islogin == false) {
            Intent intent = new Intent(Body1Activity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }else
        {
            Bmob.initialize(this, "a2994aaba430f692b3d442a44b73a089");
            p1.setPhone(content1);
            BmobQuery<uinfo> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("phone", content1);
            bmobQuery.findObjects(new FindListener<uinfo>() {
                @Override
                public void done(List<uinfo> list, BmobException e) {
                    for (uinfo u3 : list) {
                        BmobFile bmobfile = u3.getPhoto();
                        if (bmobfile != null)
                            try {

                                String url = bmobfile.getFileUrl();
                                Log.e("1", url);
                                Bitmap bitmap = getBitmap(url);
                                im.setImageBitmap(bitmap);
                            } catch (IOException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                            }
                        header_tv1.setText(u3.getNiconame());
                        header_tv2.setText(u3.getPs());
                    }
                }
            });
//            List<DemoFragment> lista = new ArrayList<>();

//            for (int i = 0; i < 3; i++) {
//            List<DemoFragment> lista = new ArrayList<>();
//                DemoFragment fragment0 = new DemoFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("type",0);
//               bundle.putInt("count",i);
//                bundle.putStringArrayList("name",i);
//                bundle.putStringArrayList("desc",i);
//                bundle.putStringArrayList("head",i);
//
//                fragment0.setArguments(bundle);
//                lista.add(fragment0);

        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Mes bb = new Mes((String) msg.obj);
                    if (!bb.getRecv().equals("0")) {
                        if (bb.getSend().equals("0")) {
                            if (bb.getText().length() == 11)
                                Toast.makeText(Body1Activity.this, bb.getText() + "请求添加好友", Toast.LENGTH_SHORT).show();
                            else {
                                String[] array = bb.getText().split("/");
                                Toast.makeText(Body1Activity.this, array[1] + "请求加群" + array[0], Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(Body1Activity.this, bb.getSend() + "发来消息", Toast.LENGTH_SHORT).show();
                    }
                }
                super.handleMessage(msg);
            }
        };

        bodyThread = new BodyThread(mHandler);
        bodyThread.setSendphone(sendphone);
        new Thread(bodyThread).start();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.body1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.body1_settings1) {
            Intent intent = new Intent(Body1Activity.this,AddActivity.class);
            startActivity(intent);
            // Toast.makeText(Body1Activity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.body2_settings2) {
            //Toast.makeText(Body1Activity.this,"SUCCESS2",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Body1Activity.this,CreatActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.body3_settings3) {
            //Toast.makeText(Body1Activity.this,"SUCCESS3",Toast.LENGTH_SHORT).show();
            String state = Environment.getExternalStorageState();// 获取内存卡可用状态
            if(state.equals(Environment.MEDIA_MOUNTED)) {
                Intent intent =new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent,1);
            } else {
                Toast.makeText(Body1Activity.this,"内存不可用", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
                    Intent intent=new Intent(Body1Activity.this,Info2Activity.class);
                    startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(Body1Activity.this,LoginActivity.class);
            Body1Activity.islogin=false;
            SharedPreferences.Editor editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
            editor.putString("islogin", "0");
            editor.putString("isRem","0");
            editor.commit();
            startActivity(intent);
            bodyThread.Socketclose();
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class DemoPagerAdapter extends FragmentStatePagerAdapter {
        List<DemoFragment> mData;
        private  FragmentManager fm;
        public DemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public DemoPagerAdapter(FragmentManager fm, List<DemoFragment> data) {
            super(fm);
            this.fm=fm;
            this.mData = data;
        }
        @Override
        public Fragment getItem(int position) {

           Log.d("88888","position="+position);

            return mData.get(position);
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//
//

        @Override
        public int getCount() {
            Log.d("77777","mData.size()="+mData.size());
            return mData.size();
        }


    }
    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射，设置menu显示icon
     *
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

}



