package ivshinaleksei.samples.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private LocalBoundService mLocalBoundService;
    private boolean mLocalBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBoundService.LocalBinder binder = (LocalBoundService.LocalBinder) service;
            mLocalBoundService = binder.getService();
            mLocalBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocalBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button intentServiceExecutor = (Button) findViewById(R.id.start_intent_service);
        Button serviceExecutor = (Button) findViewById(R.id.start_service);
        Button foregroundServiceExecutor = (Button) findViewById(R.id.start_foreground_service);
        Button bindServiceExecutor = (Button) findViewById(R.id.bind_service);
        Button unbindServiceExecutor = (Button) findViewById(R.id.unbind_service);
        Button callLocalBoundService = (Button) findViewById(R.id.unbind_service_call);

        final TextView callLocalBoundServiceResult = (TextView) findViewById(R.id.unbind_service_call_result);

        intentServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyIntentService.class);
                startService(intent);
            }
        });

        serviceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });

        foregroundServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForegroundService.class);
                startService(intent);
            }
        });

        bindServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalBoundService.class);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        unbindServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindLocalService();
            }
        });

        callLocalBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLocalBound){
                    callLocalBoundServiceResult.setText(getString(R.string.bound_service_call_result,mLocalBoundService.getRandomNumber()));
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindLocalService();
    }

    private void unbindLocalService(){
        unbindService(mServiceConnection);
        mLocalBound = false;
    }
}
