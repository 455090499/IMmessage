package cn.jit.immessage;

import java.io.Serializable;


public class Mes implements Serializable {
    private String send;
    private String recv;
    private String text;
    private static final long serialVersionUID = -5809782578272943999L;
    private void setSend(String send) {
        this.send = send;
    }

    public String getSend() {
        return send;
    }

    private void setRecv(String recv) {
        this.recv = recv;
    }

    public String getRecv() {
        return recv;
    }

    private void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Mes(String send, String recv, String text) {
        setSend(send);
        setRecv(recv);
        setText(text);
    }

    public String toString() {
        return send+","+recv+","+text;
    }

    public Mes(String a) {
       String[]aa = a.split(",");
        setSend(aa[0]);
        setRecv(aa[1]);
        setText(aa[2]);
    }
}
