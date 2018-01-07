package cn.jit.immessage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by fyf on 2018/1/6.
 */

public class TopTitle extends LinearLayout {
    //加载布局文件
    public TopTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.toptitle,this);
    }
}
