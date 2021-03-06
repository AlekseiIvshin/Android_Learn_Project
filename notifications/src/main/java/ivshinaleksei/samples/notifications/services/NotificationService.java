package ivshinaleksei.samples.notifications.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import ivshinaleksei.samples.notifications.R;
import ivshinaleksei.samples.notifications.SpecialNotificationActivity;

public class NotificationService extends Service {

    public static final String SERVICE_ACTIONS = NotificationService.class.getSimpleName() + ".actions";

    public static final int NONE = 0;
    public static final int SHOW_PROGRESS_BAR = 1;
    public static final int SHOW_SIMPLE_NOTIFICATION = 2;
    public static final int SHOW_SIMPLE_ACTION_NOTIFICATION = 3;
    public static final int SHOW_ADVANCED_ACTION_NOTIFICATION = 4;
    public static final int SHOW_CUSTOM_NOTIFICATION = 5;


    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_PROGRESS_ID = 2;

    private static final String TAG = NotificationService.class.getSimpleName();

    private ServiceHandler mServiceHandler;

    public NotificationService() {
    }

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
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        @Override
        public void handleMessage(Message msg) {
            mNotificationBuilder = new NotificationCompat.Builder(NotificationService.this);
            mNotificationBuilder.setSmallIcon(R.drawable.notification_icon);
            switch (msg.what) {
                case SHOW_PROGRESS_BAR:
                    showProgressBarNotification();
                    break;
                case SHOW_SIMPLE_NOTIFICATION:
                    showSimpleNotification();
                    break;
                case SHOW_SIMPLE_ACTION_NOTIFICATION:
                    showSimpleActionNotification();
                    break;

                case SHOW_ADVANCED_ACTION_NOTIFICATION:
                    showAdvancedActionNotification();
                    break;
                case SHOW_CUSTOM_NOTIFICATION:
                    showCustomNotification();
                    break;
            }
        }

        private void showProgressBarNotification() {
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
        }


        private void showProgress(int currentValue) {
            mNotificationBuilder.setProgress(MAX_PROGRESS_VALUE, currentValue, false);
            mNotificationManager.notify(NOTIFICATION_PROGRESS_ID, mNotificationBuilder.build());
        }

        private void hideProgress() {
            mNotificationBuilder.setProgress(0, 0, false);
            mNotificationBuilder.setContentText("Completed");
            mNotificationManager.notify(NOTIFICATION_PROGRESS_ID, mNotificationBuilder.build());
        }

        private void showSimpleNotification() {
            mNotificationBuilder.setContentTitle("Greeting!")
                    .setContentText("I'm a simple notification!")
                    .setVisibility(NotificationCompat.VISIBILITY_SECRET);
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }

        private void showSimpleActionNotification() {
            Intent notifyIntent = new Intent(NotificationService.this, SpecialNotificationActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(NotificationService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mNotificationBuilder
                    .setContentTitle("Call")
                    .setContentText("The Robot")
                    .setContentIntent(notifyPendingIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
        }

        private void showAdvancedActionNotification() {
            if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
                Intent notifyIntent = new Intent(NotificationService.this, SpecialNotificationActivity.class);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(NotificationService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotificationBuilder
                        .setContentTitle("Call")
                        .setContentText("The Robot")
                        .addAction(R.drawable.notification_icon, "Call", notifyPendingIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
            } else{
                mNotificationBuilder
                        .setContentTitle("Can't show notification")
                        .setContentText("Cause: need api level = " + Build.VERSION_CODES.LOLLIPOP+", but current api level = "+Build.VERSION.SDK_INT);
                mNotificationManager.notify(NOTIFICATION_ID,mNotificationBuilder.build());
            }
        }

        private void showCustomNotification(){
            Intent notifyIntent = new Intent(NotificationService.this, SpecialNotificationActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(NotificationService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(getPackageName(),R.layout.notification_custom_layout);
            views.setOnClickPendingIntent(R.id.custom_notification_action0,notifyPendingIntent);
            mNotificationBuilder.setContent(views);
            mNotificationManager.notify(NOTIFICATION_ID,mNotificationBuilder.build());
        }

    }
}
