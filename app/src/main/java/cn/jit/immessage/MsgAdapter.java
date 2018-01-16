package cn.jit.immessage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by liweiwei on 2018/1/5.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
         LinearLayout leftLayout;
         LinearLayout rightLayout;
         TextView leftMsg;
         TextView rightMsg;
         ImageView leftImg;
         ImageView rightImg;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = (LinearLayout) itemView.findViewById(R.id.chat_item_left);
            rightLayout = (LinearLayout) itemView.findViewById(R.id.chat_item_right);
            leftMsg = (TextView) itemView.findViewById(R.id.chat_item_left_msg);
            rightMsg = (TextView) itemView.findViewById(R.id.chat_item_right_msg);
            leftImg=(ImageView)itemView.findViewById(R.id.chat_item_left_imag);
            rightImg=(ImageView)itemView.findViewById(R.id.chat_item_right_imag);
        }
    }
    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.activity_chat_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgAdapter.ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);


        if (msg.getType() == Msg.TYPE_RECEIVCED) {
            String url1=msg.getUrl();
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            Bitmap bitmap1 = getBitmap(url1);
            holder.leftImg.setImageBitmap(bitmap1);

        } else if(msg.getType() == Msg.TYPE_SENT) {
            String url2=msg.getUrl();

            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            Bitmap bitmap2 = getBitmap(url2);
            holder.rightImg.setImageBitmap(bitmap2);
        }
    }

    private Bitmap getBitmap(String url1) {
        try {
            URL url = new URL(url1);
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


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }



}
