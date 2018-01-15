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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.github.leibnik.wechatradiobar.WeChatRadioGroup;
import qiu.niorgai.StatusBarCompat;

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
    static pp p1=new pp();

    @Override
    protected void onResume() {

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body1);

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



        List<DemoFragment> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DemoFragment fragment = new DemoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        DemoPagerAdapter demo=new DemoPagerAdapter(getSupportFragmentManager(), list);
         viewPager.setAdapter(demo);

        gradualRadioGroup.setViewPager(viewPager);



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
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

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