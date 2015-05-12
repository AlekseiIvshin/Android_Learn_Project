package ivshinaleksei.samples.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final String TAG =  MainActivity.class.getSimpleName();

    private LocalBoundService mLocalBoundService;
    private boolean mLocalBound = false;
    private ServiceConnection mLocalServiceConnection = new ServiceConnection() {
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

    Messenger mMessenger = null;
    boolean mMessengerBound = false;
    private ServiceConnection mMessengerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            mMessengerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            mMessengerBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button intentServiceExecutor = (Button) findViewById(R.id.start_intent_service);
        Button serviceExecutor = (Button) findViewById(R.id.start_service);
        Button foregroundServiceExecutor = (Button) findViewById(R.id.start_foreground_service);
        Button bindLocalServiceExecutor = (Button) findViewById(R.id.bind_local_service);
        Button unbindLocalServiceExecutor = (Button) findViewById(R.id.unbind_local_service);
        Button callLocalBoundService = (Button) findViewById(R.id.unbind_local_service_call);
        Button bindMessengerServiceExecutor = (Button) findViewById(R.id.bind_messenger_service);
        Button unbindMessengerServiceExecutor = (Button) findViewById(R.id.unbind_messenger_service);
        Button callMessengerBoundService = (Button) findViewById(R.id.bound_messenger_service_call);

        final TextView callLocalBoundServiceResult = (TextView) findViewById(R.id.unbind_local_service_call_result);

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

        bindLocalServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalBoundService.class);
                bindService(intent, mLocalServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        unbindLocalServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindLocalService();
            }
        });

        callLocalBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocalBound) {
                    callLocalBoundServiceResult.setText(getString(R.string.bound_local_service_call_result, mLocalBoundService.getRandomNumber()));
                }
            }
        });


        bindMessengerServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessengerService.class);
                bindService(intent, mMessengerServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        unbindMessengerServiceExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindMessengerService();
            }
        });

        callMessengerBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMessengerBound) {
                    Message message = Message.obtain(null, MessengerService.MSG_SAY_HELLO,0,0);
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        Log.e(TAG, e.getMessage(),e);
                    }
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindLocalService();
        unbindMessengerService();
    }

    private void unbindLocalService() {
        if(mLocalBound) {
            unbindService(mLocalServiceConnection);
            mLocalBound = false;
        }
    }


    private void unbindMessengerService() {
        if(mMessengerBound) {
            unbindService(mMessengerServiceConnection);
            mMessengerBound = false;
        }
    }
}
