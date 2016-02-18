package st.kimsmik.voicelearning;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MicFragment extends Fragment {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private AudioService service = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    public MicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mic, container, false);
        TabHost tabHost = (TabHost)root.findViewById(R.id.tabHost);
        tabHost.setup();

//        TextView t1 = new TextView(getActivity());
//        t1.setText("Mic");
        TabHost.TabSpec spec1 = tabHost.newTabSpec("mic").setContent(R.id.micTab).setIndicator("Mic");
        TabHost.TabSpec spec2 = tabHost.newTabSpec("speaker").setContent(R.id.speakerTab).setIndicator("Speaker");
        TabHost.TabSpec spec3 = tabHost.newTabSpec("setting").setContent(R.id.settingTab).setIndicator("Setting");
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);

        View micView = tabHost.getChildAt(0);
        Button micBtn = (Button)micView.findViewById(R.id.micBtn);
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText("On");
            }
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if(service == null){
            Intent serviceIt = new Intent(getActivity(),AudioService.class);
            getActivity().bindService(serviceIt, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    service = ((AudioService.AudioBinder) binder).getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    service = null;
                }
            }, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
