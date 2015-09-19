package poc.wifi.scan.model;

import android.util.Log;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AccessPointContainer implements Iterable {

    private Map<String, AccessPoint> accessPoints = new HashMap();

    public void addAll(List<AccessPoint> aps){
        for (AccessPoint ap : aps) {
            add(ap);
        }
    }

    public void add(AccessPoint ap){
        if (accessPoints.containsKey(ap.getId())){
            Log.d("ACCESS_POINT", "Access point " + ap.getId() + " already existed");
        } else {
          accessPoints.put(ap.getId(), ap);
        }
    }

    public boolean contains(String id){
        return accessPoints.containsKey(id);
    }

    public List<String> getIds(){
        return new ArrayList<>(accessPoints.keySet());
    }

    public Optional<AccessPoint> get(String id){
        return Optional.fromNullable(accessPoints.get(id));
    }

    public Optional<AccessPoint> remove(String id){
        return Optional.fromNullable(accessPoints.remove(id));
    }

    public void clear(){
        accessPoints.clear();
    }

    public void update(AccessPoint ap){
        // This can be extended to save multiple scanned versions of the same accesspoint
        if (!accessPoints.containsKey(ap.getId())){
            Log.d("ACCESS_POINT", "Missing AP [" + ap.getId() + "] on UPDATE. Added instead");
        }

        accessPoints.put(ap.getId(), ap);
    }

    public int size() {
        return accessPoints.keySet().size();
    }

    @Override
    public Iterator<AccessPoint> iterator() {
        return accessPoints.values().iterator();
    }
}
