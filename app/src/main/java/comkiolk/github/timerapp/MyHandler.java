package comkiolk.github.timerapp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static comkiolk.github.timerapp.Constants.TAGS;

public class MyHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d(TAGS, "Handler get message");
    }
}
