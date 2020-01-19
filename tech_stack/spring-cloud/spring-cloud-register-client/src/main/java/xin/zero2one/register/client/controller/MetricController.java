package xin.zero2one.register.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhoujundong
 * @data 1/16/2020
 * @description
 */
@RestController
@RequestMapping("/api/metric")
public class MetricController {

    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/services")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    @GetMapping("/instance/{serviceId}")
    public List<ServiceInstance> getServiceInstance(@PathVariable("serviceId") String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

}
