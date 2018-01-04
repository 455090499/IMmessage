package cn.jit.immessage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 2018/1/3.
 */

public class DemoFragment extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_message, container, false);
        int type = getArguments().getInt("type", 0);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(type == 0 ? "1st Fragment" : type == 1 ? "2nd Fragment" : type == 2 ? "3rd Fragment" : "4th Fragment");
        return view;
    }
}