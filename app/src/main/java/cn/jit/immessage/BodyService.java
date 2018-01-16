package cn.jit.immessage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BodyService extends Service {
    public static BodyThread bodyThread;

    public BodyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bodyThread = new BodyThread();
        new Thread(bodyThread).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
