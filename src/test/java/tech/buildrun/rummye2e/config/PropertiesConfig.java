package tech.buildrun.rummye2e.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfig {

    @Value("${backend.url}")
    private String backendUrl;

    public String getBackendUrl() {
        return backendUrl;
    }
}
