package ivshinaleksei.samples.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created on 08/05/2015.
 *
 * @author Aleksei_Ivshin
 */
public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyIntentService() {
        super(MyIntentService.class.getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, TAG + " starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "On handle intent");
        long entTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < entTime) {
            synchronized (this) {
                try {
                    wait(entTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "Service destroy");
        super.onDestroy();
    }
}
