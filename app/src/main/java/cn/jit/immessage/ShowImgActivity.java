package cn.jit.immessage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bm.library.PhotoView;

/**
 * Created by liweiwei on 2018/1/26.
 */

public class ShowImgActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimg);


        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        PhotoView photoView = (PhotoView) findViewById(R.id.show_img);


        photoView.setImageBitmap(bmp);
        // 启用图片缩放功能
        photoView.enable();

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
