package ivshinaleksei.samples.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalBoundService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private final Random mRandom = new Random();

    public LocalBoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getRandomNumber() {
        return mRandom.nextInt(100);
    }

    public class LocalBinder extends Binder {
        LocalBoundService getService() {
            return LocalBoundService.this;
        }
    }
}
