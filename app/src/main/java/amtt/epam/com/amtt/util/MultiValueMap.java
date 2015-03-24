package amtt.epam.com.amtt.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artsiom_Kaliaha on 19.03.2015.
 */
public class MultiValueMap<K, V> {

    private Map<K, List<V>> mMap = new LinkedHashMap<>();

    public List<V> put(K key, V value) {
        if (mMap.get(key) == null) {
            List<V> list = new ArrayList<>();
            list.add(value);
            return mMap.put(key, list);
        } else {
            List<V> list = mMap.get(key);
            list.add(value);
            return list;
        }
    }

    public List<V> put(K key, List<V> values) {
        if (mMap.get(key) == null) {
            return mMap.put(key, values);
        } else {
            List<V> list = mMap.get(key);
            list.addAll(values);
            return list;
        }
    }

    public List<V> get(K key) {
        return mMap.get(key);
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return mMap.entrySet();
    }

    public void clear() {
        mMap.clear();
    }

}
