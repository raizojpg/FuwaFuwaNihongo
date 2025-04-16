import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary<T extends Comparable<T>> {
    private Map<String, T> itemMap;

    public Dictionary() {
        this.itemMap = new HashMap<>();
    }

    public void addItem(String key, T item) {
        itemMap.put(key, item);
    }

    public T getItem(String key) {
        return itemMap.get(key);
    }

    public void removeItem(String key) {
        itemMap.remove(key);
    }

    public boolean containsItem(String key) {
        return itemMap.containsKey(key);
    }

    public List<T> getAllItems() {
        List<T> itemList = new ArrayList<>(itemMap.values());
        Collections.sort(itemList);
        return itemList;
    }

    public void printAllItems() {
        for (T item : itemMap.values()) {
            System.out.println(item);
        }
    }

    public int size() {
        return itemMap.size();
    }
}