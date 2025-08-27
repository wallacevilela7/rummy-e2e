package tech.buildrun.rummye2e.config;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope
public class ScenarioContext {
    private final Map<String, Object> data = new HashMap<>();
    public void put(String key, Object value) {
        data.put(key, value);
    }
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalStateException("No value found in context for key: " + key);
        }
        return type.cast(value);
    }
    public boolean has(String key) {
        return data.containsKey(key);
    }
    public void remove(String key) {
        data.remove(key);
    }
    public void clear() {
        data.clear();
    }
}