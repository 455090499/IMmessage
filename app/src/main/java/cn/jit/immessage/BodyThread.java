package cn.jit.immessage;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static cn.jit.immessage.Body1Activity.socketContent;



public class BodyThread implements Runnable {
    private Socket mSocket;
    private BufferedReader mBufferedReader = null;
    private OutputStream mOutputStream = null;
    private Handler mHandler;
    private Thread thread;
    public Handler revHandler;

    String sendphone;

    public void setSendphone(String sendphone) {
        this.sendphone = sendphone;
    }

    public BodyThread(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void run() {
        try {
            //mSocket = new Socket("139.196.138.200", 30002);
            mSocket = new Socket("192.168.1.125", 30003);
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mOutputStream = mSocket.getOutputStream();

            try {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(2 * 1000);//2s发送一次心跳
                                mOutputStream.write(socketContent.getBytes());
                                mOutputStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        String content = null;
                        while ((content = mBufferedReader.readLine()) != null) {
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = content;
                            if (Body1Activity.flag == 0)
                                mHandler.sendMessage(msg);
                            else if (Body1Activity.flag ==1)
                                ChatActivity.mhandler.sendMessage(msg);
                            else if (Body1Activity.flag ==2)
                                Chat2Activity.mhandler.sendMessage(msg);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();

            Looper.prepare();
            revHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        try {
                            mOutputStream.write((msg.obj.toString() + "\n").getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socketclose() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
