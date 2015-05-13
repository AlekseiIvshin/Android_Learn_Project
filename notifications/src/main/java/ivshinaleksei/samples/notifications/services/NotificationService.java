package ivshinaleksei.samples.notifications.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ivshinaleksei.samples.notifications.R;
import ivshinaleksei.samples.notifications.SpecialNotificationActivity;

public class NotificationService extends Service {

    public static final String SERVICE_ACTIONS = NotificationService.class.getSimpleName() + ".actions";

    public static final int NONE = 0;
    public static final int SHOW_PROGRESS_BAR = 1;
    public static final int SHOW_SIMPLE_NOTIFICATION = 2;
    public static final int SHOW_SIMPLE_ACTION_NOTIFICATION = 3;


    private static final int NOTIFICATION_ID = 1;

    private static final String TAG = NotificationService.class.getSimpleName();

    private ServiceHandler mServiceHandler;

    public NotificationService() { }

    @Override
    public void onCreate() {
        Log.v(TAG, "Creating");
        HandlerThread thread = new HandlerThread("NotificationServiceArguments");
        thread.start();

        mServiceHandler = new ServiceHandler(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Starting execute command");
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.what = intent.getIntExtra(SERVICE_ACTIONS, NONE);
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final class ServiceHandler extends Handler {

        private static final int MAX_PROGRESS_VALUE = 100;

        private NotificationCompat.Builder mNotificationBuilder;
        private NotificationManager mNotificationManager;

        public ServiceHandler(Looper looper) {
            super(looper);
            mNotificationBuilder = new NotificationCompat.Builder(NotificationService.this);
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        @Override
        public void handleMessage(Message msg) {
            mNotificationBuilder.setSmallIcon(R.drawable.notification_icon);
            switch (msg.what) {
                case SHOW_PROGRESS_BAR:
                    mNotificationBuilder
                            .setContentTitle("Progress title")
                            .setContentText("Omnomnom!");
                    for (int i = 0; i < MAX_PROGRESS_VALUE; i++) {
                        showProgress(i);
                        try {
                            Thread.sleep(100l);
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    hideProgress();
                    break;
                case SHOW_SIMPLE_NOTIFICATION:
                    mNotificationBuilder.setContentTitle("Greeting!")
                            .setContentText("I'm a simple notification!");
                    mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
                    break;
                case SHOW_SIMPLE_ACTION_NOTIFICATION:

                    Intent notifyIntent = new Intent(NotificationService.this, SpecialNotificationActivity.class);
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(NotificationService.this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                    mNotificationBuilder
                            .setContentTitle("Call")
                            .setContentText("The Robot")
                            .setContentIntent(notifyPendingIntent);
                    mNotificationManager.notify(NOTIFICATION_ID,mNotificationBuilder.build());
            }
        }

        private void showProgress(int currentValue) {
            mNotificationBuilder.setProgress(MAX_PROGRESS_VALUE, currentValue, false);
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }

        private void hideProgress() {
            mNotificationBuilder.setProgress(0, 0, false);
            mNotificationBuilder.setContentText("Completed");
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }

    }
}
