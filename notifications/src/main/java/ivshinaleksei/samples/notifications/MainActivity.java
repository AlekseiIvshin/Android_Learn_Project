package ivshinaleksei.samples.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ivshinaleksei.samples.notifications.services.NotificationService;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showProgressNotification = (Button) findViewById(R.id.show_progress_bar);
        showProgressNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.SERVICE_ACTIONS, NotificationService.SHOW_PROGRESS_BAR);
                startService(intent);
            }
        });

        Button showSimpleNotification = (Button) findViewById(R.id.show_simple_notification);
        showSimpleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.SERVICE_ACTIONS,NotificationService.SHOW_SIMPLE_NOTIFICATION);
                startService(intent);
            }
        });

        Button showSimpleActionNotification = (Button) findViewById(R.id.show_simple_action_notification);
        showSimpleActionNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.SERVICE_ACTIONS,NotificationService.SHOW_SIMPLE_ACTION_NOTIFICATION);
                startService(intent);
            }
        });

        Button showAdvancedActionNotification = (Button) findViewById(R.id.show_advanced_action_notification);
        showAdvancedActionNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.SERVICE_ACTIONS,NotificationService.SHOW_ADVANCED_ACTION_NOTIFICATION);
                startService(intent);
            }
        });

        Button showCustomNotification = (Button) findViewById(R.id.show_custom_notification);
        showCustomNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                intent.putExtra(NotificationService.SERVICE_ACTIONS,NotificationService.SHOW_CUSTOM_NOTIFICATION);
                startService(intent);
            }
        });

        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);
    }
}
