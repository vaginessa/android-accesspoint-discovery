package poc.wifi.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alejandro.wifi_discover_poc.R;

import java.util.ArrayList;
import java.util.List;

import poc.wifi.scan.model.AccessPoint;
import poc.wifi.scan.model.AccessPointContainer;

public class AccessPointScanLogger extends AppCompatActivity {

    // View
    List<String> accessPointIds = new ArrayList<>();
    ArrayAdapter<String> scanningListAdapter;

    // Data
    WifiManager wifi;
    AccessPointContainer historical = new AccessPointContainer();
    private BroadcastReceiver scanResultBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bssidlogger);

        initialiseWifi();

        createScanButton();

        createListDataBiding();

        createOnScanResultsReceiver();
        registerReceiver(scanResultBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private void createOnScanResultsReceiver() {
        scanResultBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                // On new scanResults
                List<AccessPoint> result = AccessPoint.batchFromScan(wifi.getScanResults());

                Log.i("SCANNER", "ScanResults: " + result.size());
                int repeated = updateListedIds(result);

                if (repeated != 0) {
                    Log.i("SCANNER", "Repetitions: " + repeated);
                }

                historical.addAll(result);
                scanningListAdapter.notifyDataSetChanged();
            }
        };
    }

    private void createScanButton() {
        ((Button) findViewById(R.id.scanBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.startScan();
                Toast.makeText(v.getContext(), "Scanning....", Toast.LENGTH_SHORT).show();
                accessPointIds.clear();
                scanningListAdapter.notifyDataSetChanged();
            }
        });

        ((Button) findViewById(R.id.logBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListedIds(historical);
                scanningListAdapter.notifyDataSetChanged();
            }
        });
    }

    private int updateListedIds(Iterable<AccessPoint> result) {
        int repeated = 0;
        for (AccessPoint ap : result) {
            if (!accessPointIds.contains(ap.getId())){
                accessPointIds.add(ap.getId());
            } else {
                repeated++;
            }
        }
        return repeated;
    }

    private void initialiseWifi() {
        wifi = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);

        if (!wifi.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "enabling Wifi", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
    }

    private void createListDataBiding() {
        // Binding AP dataset with view
        scanningListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accessPointIds);
        ListView listView = (ListView) findViewById(R.id.dinaList);
        listView.setAdapter(scanningListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bssidlogger, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(scanResultBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.upload_log:
                uploadLog();
                return true;

            case R.id.clear_log:
                clearLog();
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearLog() {
        historical.clear();
        accessPointIds.clear();
        scanningListAdapter.notifyDataSetChanged();
    }

    private void uploadLog() {
        // This will hit a server which can the store all the retrieved AP`s data and
        // make some statistics with it
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //FIXME
        if (network != null && network.isConnected()) {
            // Do whatever
        }
    }

}
