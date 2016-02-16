package st.kimsmik.voicelearning;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MicActivity extends ActionBarActivity {
    private static int ratehz = 44100;
    private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private Button micBtn = null;
    private int recBufSize,playBufSize;
    private AudioRecord recorder = null;
    private AudioTrack track = null;
    private boolean recording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic);
        micBtn = (Button)findViewById(R.id.micBtn);

        recBufSize = AudioRecord.getMinBufferSize(ratehz,
                channelConfig, audioEncoding);

        playBufSize = AudioTrack.getMinBufferSize(ratehz,
                channelConfig, audioEncoding);

        recorder  = new AudioRecord(MediaRecorder.AudioSource.MIC,ratehz,AudioFormat.CHANNEL_IN_MONO,audioEncoding,recBufSize);
        track = new AudioTrack(AudioManager.STREAM_MUSIC,ratehz,AudioFormat.CHANNEL_OUT_MONO,audioEncoding,playBufSize,AudioTrack.MODE_STREAM);
        track.setStereoVolume(1f,1f);

        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording){

                    micBtn.setText("Record");
                    recording = false;
                }else{
                    micBtn.setText("Stop");
                    recording = true;
                    RecordThread thread = new RecordThread();
                    thread.start();
                }
            }
        });
    }

    class RecordThread extends Thread{
        @Override
        public void run() {
            byte[] buffer = new byte[recBufSize];
            recorder.startRecording();
            track.play();

            while (recording){
                int res = recorder.read(buffer,0,recBufSize);
                byte[] tmpBuf = new byte[res];
                System.arraycopy(buffer, 0, tmpBuf, 0, res);
                track.write(tmpBuf, 0, tmpBuf.length);
            }

            recorder.stop();
            track.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
