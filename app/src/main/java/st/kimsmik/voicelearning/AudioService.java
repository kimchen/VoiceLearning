package st.kimsmik.voicelearning;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by chenk on 2016/2/17.
 */
public class AudioService extends Service  {
    private AudioBinder mBinder = new AudioBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class AudioBinder extends Binder{
        public AudioService getService(){
            return AudioService.this;
        }
    }

    public void start(){


    }
}
