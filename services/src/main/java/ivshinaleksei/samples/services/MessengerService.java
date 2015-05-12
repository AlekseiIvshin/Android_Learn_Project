package ivshinaleksei.samples.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

public class MessengerService extends Service {

    public static final int MSG_SAY_HELLO = 1;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Binding",Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(),"Hello!",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
