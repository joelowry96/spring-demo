package com.joe.demo;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConfigMapController {

    @PostMapping("/update-configmap")
    public String updateConfigMap(@RequestParam String namespace, @RequestParam String name, @RequestParam String key, @RequestParam String value) {
        try {
            ApiClient client = Config.defaultClient();
            CoreV1Api api = new CoreV1Api(client);

            V1ConfigMap configMap = api.readNamespacedConfigMap(name, namespace, null, null, null);
            configMap.getData().put(key, value);
            api.replaceNamespacedConfigMap(name, namespace, configMap, null, null, null);
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
}