package discordbot.manager;

import java.util.Map;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractManager<K, V> {

    private final Map<K, V> VALUES = new HashMap<>();

    public final void register(K key, V value) {
        VALUES.put(key, value);
    }

    public final void unregister(K key) {
        VALUES.remove(key);
    }

    public final Map<K, V> getValues() {
        return Collections.unmodifiableMap(this.VALUES);
    }

    public final V getValue(K key) {
        return this.VALUES.get(key);
    }

    public final V getValue(K key, V defaultValue){
        return this.VALUES.getOrDefault(key, defaultValue);
    }
    
    public final List<V> getFilteredValues(Predicate<V> predicate) {
        return this.VALUES.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
