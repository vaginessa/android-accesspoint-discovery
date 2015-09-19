package poc.wifi.scan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alejandro.wifi_discover_poc.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccessPointScanLoggerFragment extends Fragment {

    public AccessPointScanLoggerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bssidlogger, container, false);
    }
}
