package poc.wifi.scan.model;


import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;

public class AccessPoint {

    private final String id;
    private ScanResult latestScan;

    public static List<AccessPoint> batchFromScan(List<ScanResult> results){
        List<AccessPoint> aps = new LinkedList<>();
        for (ScanResult scan : results) {
            aps.add(new AccessPoint(scan));
        }
        return aps;
    }

    public AccessPoint(ScanResult apScan) {
        latestScan = apScan;
        id = createId(apScan);
    }

    public void update(ScanResult apScan){
        if (TextUtils.equals(id, createId(apScan))){
            latestScan = apScan;
        }
    }

    @NonNull
    private String createId(ScanResult scan) {
        return scan.SSID + "_-_" + scan.BSSID;
    }

    public String getId() {
        return id;
    }

    public ScanResult getLatestScan() {
        return latestScan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessPoint that = (AccessPoint) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
