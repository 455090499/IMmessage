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

        LinearLayout filesend;
        LinearLayout filerecv;

         TextView send_tv1;
         TextView send_tv2;
         TextView send_tv3;
         TextView recv_tv1;
         TextView recv_tv2;
         TextView recv_tv3;

        ImageView leftImg;
        ImageView rightImg;

        ImageView leftImg1;
        ImageView rightImg1;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = (LinearLayout) itemView.findViewById(R.id.chat_item_left);
            rightLayout = (LinearLayout) itemView.findViewById(R.id.chat_item_right);
            leftMsg = (TextView) itemView.findViewById(R.id.chat_item_left_msg);
            rightMsg = (TextView) itemView.findViewById(R.id.chat_item_right_msg);
            leftImg=(ImageView)itemView.findViewById(R.id.chat_item_left_imag);
            rightImg=(ImageView)itemView.findViewById(R.id.chat_item_right_imag);
            leftImg1=(ImageView)itemView.findViewById(R.id.chat_item_left_fileimag);
            rightImg1=(ImageView)itemView.findViewById(R.id.chat_item_right_fileimag);
            filesend=(LinearLayout) itemView.findViewById(R.id.file_send_layout);
            filerecv=(LinearLayout) itemView.findViewById(R.id.file_recv_layout);
            send_tv1=(TextView)itemView.findViewById(R.id.file_send_tv1) ;
            send_tv2=(TextView)itemView.findViewById(R.id.file_send_tv2) ;
            send_tv3=(TextView)itemView.findViewById(R.id.file_send_tv3) ;
            recv_tv1=(TextView)itemView.findViewById(R.id.file_recv_tv1) ;
            recv_tv2=(TextView)itemView.findViewById(R.id.file_recv_tv2) ;
            recv_tv3=(TextView)itemView.findViewById(R.id.file_recv_tv3) ;

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
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            Bitmap bitmap1 = getBitmap(url1);
            holder.leftImg.setImageBitmap(bitmap1);

        } else if(msg.getType() == Msg.TYPE_SENT) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            Bitmap bitmap2 = getBitmap(url2);
            holder.rightImg.setImageBitmap(bitmap2);
        } else if(msg.getType() == Msg.FILE_SENT) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.VISIBLE);
            holder.filerecv.setVisibility(View.GONE);
            holder.send_tv1.setText(msg.getFilename());
            holder.send_tv2.setText(msg.getFilesize());
            holder.send_tv3.setText(msg.getFilestate());
            Bitmap bitmap2 = getBitmap(url2);
            holder.rightImg1.setImageBitmap(bitmap2);


        } else if(msg.getType() == Msg.FILE_RECV) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.VISIBLE);
            holder.recv_tv1.setText(msg.getFilename());
            holder.recv_tv2.setText(msg.getFilesize());
            holder.recv_tv3.setText(msg.getFilestate());
            Bitmap bitmap2 = getBitmap(url2);
            holder.leftImg1.setImageBitmap(bitmap2);
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
