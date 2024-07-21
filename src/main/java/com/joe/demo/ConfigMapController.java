package com.joe.demo;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@RestController
public class ConfigMapController {

    @PostMapping("/update-configmap")
    public String updateConfigMap(@RequestParam String namespace, @RequestParam String name, @RequestParam String key, @RequestParam String value) {
        try {
            ApiClient client = Config.defaultClient();
            CoreV1Api api = new CoreV1Api(client);

            V1ConfigMap configMap = api.readNamespacedConfigMap(name, namespace).execute();

            String applicationYmlContent = configMap.getData().get("application.yml");

            applicationYmlContent = updateYamlConfig(applicationYmlContent, key, value);

            // Put the updated application.yml back into the ConfigMap data
            configMap.getData().put("application.yml", applicationYmlContent);
            
            // Replace the ConfigMap in Kubernetes with the updated one
            api.replaceNamespacedConfigMap(name, namespace, configMap).execute();
            return "ConfigMap updated";
        } catch (io.kubernetes.client.openapi.ApiException e) {
            log.error("API call failed with status code: " + e.getCode());
            log.error("Response body: " + e.getResponseBody());
            e.printStackTrace();
            return "Failed to update ConfigMap\n" + e.getCode() + ": " + e.getResponseBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update ConfigMap";
        }

    }

    public static String updateYamlConfig(String yamlString, String propertyPath, Object newValue) {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(yamlString);

        // Assuming the property path is something like "parent.child.property"
        String[] pathSegments = propertyPath.split("\\.");
        Map<String, Object> current = data;
        for (int i = 0; i < pathSegments.length - 1; i++) {
            current = (Map<String, Object>) current.get(pathSegments[i]);
        }
        current.put(pathSegments[pathSegments.length - 1], newValue);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        return yaml.dump(data);
    }
}