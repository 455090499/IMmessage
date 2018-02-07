package cn.jit.immessage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by liweiwei on 2018/1/5.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> mMsgList;

    private Bitmap sendimg;
    byte[] sendb;
    private Bitmap recvimg;
    byte[] recvb;

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
         TextView recv_tv4;

        LinearLayout voicerecv;
        LinearLayout voicesend;

        TextView voicesend_tv1;
        TextView voicesend_tv2;
        TextView voicerecv_tv1;
        TextView voicerecv_tv2;




        ImageView leftImg;
        ImageView rightImg;

        ImageView leftImg1;
        ImageView rightImg1;

        ImageView leftImg2;
        ImageView rightImg2;

        ImageView leftfileImg;
        private final ImageView Imgrecvimg1;
        private final ImageView Imgrecvimg2;
        private final ImageView Imgsendimg1;
        private final ImageView Imgsendimg2;
        private final LinearLayout Imgleft;
        private final LinearLayout Imgright;
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
            recv_tv4=(TextView)itemView.findViewById(R.id.file_recv_tv4) ;
            leftfileImg=(ImageView)itemView.findViewById(R.id.file_recv_img2);

            voicerecv = (LinearLayout) itemView.findViewById(R.id.voice_recv_layout);
            voicesend = (LinearLayout) itemView.findViewById(R.id.voice_send_layout);

            voicesend_tv1=(TextView)itemView.findViewById(R.id.chat_item_right_voicemsg) ;
            voicesend_tv2=(TextView)itemView.findViewById(R.id.chat_item_right_voicemsg2) ;

            voicerecv_tv1=(TextView)itemView.findViewById(R.id.chat_item_left_voicemsg) ;
            voicerecv_tv2=(TextView)itemView.findViewById(R.id.chat_item_left_voicemsg2) ;


            leftImg2=(ImageView)itemView.findViewById(R.id.chat_item_left_voiceimag);
            rightImg2=(ImageView)itemView.findViewById(R.id.chat_item_right_voiceimag);
            //图片
            Imgleft = (LinearLayout)itemView.findViewById(R.id.img_recv_left);
            Imgright = (LinearLayout)itemView.findViewById(R.id.img_send_right);
            Imgrecvimg1=(ImageView)itemView.findViewById(R.id.img_recv_img1);
            Imgrecvimg2=(ImageView)itemView.findViewById(R.id.img_recv_img2);
            Imgsendimg1=(ImageView)itemView.findViewById(R.id.img_send_img2);
            Imgsendimg2=(ImageView)itemView.findViewById(R.id.img_send_img1);


        }
    }
    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.activity_chat_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.leftfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.recv_tv3.getText().toString().equals("等待下载")) {
                    BmobQuery<fileurl> query = new BmobQuery<>();
                    query.getObject(holder.recv_tv4.getText().toString(), new QueryListener<fileurl>() {

                        @Override
                        public void done(final fileurl object, BmobException e) {
                            object.getBfile().download(new File(Environment.getExternalStorageDirectory(), object.getBfile().getFilename()), new DownloadFileListener() {
                                @Override
                                public void onStart() {
//                                    Toast.makeText(view.getContext(), "开始下载...", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void done(String savePath, BmobException e) {
                                    if (e == null) {
//                                        Toast.makeText(view.getContext(), "下载成功，保存路径" + savePath, Toast.LENGTH_LONG).show();
//                                    Msg msg5 = new Msg(null, Msg.FILE_RECV, url[1], object.getBfile().getFilename(), object.getFilesize() + "", "已接收");
//                                    msgList.add(msg5);

                                    } else {
                                        Toast.makeText(view.getContext(), "下载失败：" + e.getErrorCode() + "," + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

//                                    object.setObjectId(object.getObjectId());
//                                    object.delete(new UpdateListener() {
//
//                                        @Override
//                                        public void done(BmobException e) {
//                                            if (e == null) {
////                                                Toast.makeText(view.getContext(), "删除FILE成功", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(view.getContext(), "删除FILE失败", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                    });


                                }

                                @Override
                                public void onProgress(Integer value, long newworkSpeed) {

                                }
                            });

                        }
                    });
                    holder.recv_tv3.setText("下载完成");
                }else{

                }

            }
        });
        holder.voicesend_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File recordFile1 = new File("/storage/sdcard0","kk_"+ holder.voicesend_tv2.getText().toString()+".amr");
                Log.e("888888","kk_"+ holder.voicesend_tv2.getText().toString()+".amr");
                Log.e("888888","recordfile="+recordFile1);
                Log.e("888888","V="+v);
                RecordPlayer player = new RecordPlayer(v.getContext());
                player.playRecordFile(recordFile1);


            }
        });
        holder.voicerecv_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File recordFile = new File("/storage/sdcard0","kk_"+holder.voicerecv_tv2.getText().toString() +".amr");
                RecordPlayer player = new RecordPlayer(view.getContext());
                player.playRecordFile(recordFile);



            }
        });
        final Context context=parent.getContext();
        holder.Imgsendimg2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ShowImgActivity.class);

                intent.putExtra("picture", sendb);

                context.startActivity(intent);
                

            }
        });
        holder.Imgrecvimg2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ShowImgActivity.class);
                intent.putExtra("picture", recvb);
                context.startActivity(intent);
            }
        });

        return holder;
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
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            Bitmap bitmap1 = getBitmap(url1);
            holder.leftImg.setImageBitmap(bitmap1);

        } else if(msg.getType() == Msg.TYPE_SENT) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            Bitmap bitmap2 = getBitmap(url2);
            holder.rightImg.setImageBitmap(bitmap2);
        } else if(msg.getType() == Msg.FILE_SENT) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.VISIBLE);
            holder.filerecv.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
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
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);

            holder.recv_tv1.setText(msg.getFilename());
            holder.recv_tv2.setText(msg.getFilesize());
            holder.recv_tv3.setText(msg.getFilestate());
            holder.recv_tv4.setText(msg.getFileobject());
            Bitmap bitmap2 = getBitmap(url2);
            holder.leftImg1.setImageBitmap(bitmap2);
        } else if(msg.getType() == Msg.VOICE_SENT) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.VISIBLE);
            Bitmap bitmap2 = getBitmap(url2);
            holder.rightImg2.setImageBitmap(bitmap2);
            holder.voicesend_tv2.setText(msg.getFileobject());


        } else if(msg.getType() == Msg.VOICE_RECV) {
            String url2=msg.getUrl();
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.VISIBLE);
            holder.voicesend.setVisibility(View.GONE);
            Bitmap bitmap2 = getBitmap(url2);
            holder.leftImg2.setImageBitmap(bitmap2);
            holder.voicerecv_tv2.setText(msg.getFileobject());


        }else if (msg.getType() == Msg.IMG_SENT) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.GONE);
            holder.Imgright.setVisibility(View.VISIBLE);
            holder.filerecv.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);

            String url = msg.getUrl();
            Bitmap bitmap = getBitmap(url);
            holder.Imgsendimg1.setImageBitmap(bitmap);

            String ur2 = msg.getImg();
            sendimg = getBitmap(ur2);
            holder.Imgsendimg2.setImageBitmap(sendimg);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            sendimg.compress(Bitmap.CompressFormat.PNG, 100, baos);
            sendb = baos.toByteArray();


        } else if (msg.getType() == Msg.IMG_RECV) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.filesend.setVisibility(View.GONE);
            holder.Imgleft.setVisibility(View.VISIBLE);
            holder.Imgright.setVisibility(View.GONE);
            holder.filerecv.setVisibility(View.GONE);
            holder.voicerecv.setVisibility(View.GONE);
            holder.voicesend.setVisibility(View.GONE);
            String url = msg.getUrl();
            Bitmap bitmap = getBitmap(url);
            holder.Imgrecvimg1.setImageBitmap(bitmap);

            String ur2 = msg.getImg();
            recvimg = getBitmap(ur2);
            holder.Imgrecvimg2.setImageBitmap(recvimg);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            recvimg.compress(Bitmap.CompressFormat.PNG, 100, baos);
            recvb = baos.toByteArray();

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
