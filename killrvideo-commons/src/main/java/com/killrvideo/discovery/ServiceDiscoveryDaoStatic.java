package com.killrvideo.discovery;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.killrvideo.conf.KillrVideoConfiguration;

/**
 * There is no explicit access to 
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Component("killrvideo.discovery.network")
@Profile(KillrVideoConfiguration.PROFILE_DISCOVERY_STATIC)
public class ServiceDiscoveryDaoStatic implements ServiceDiscoveryDao {
   
    /** Initialize dedicated connection to ETCD system. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscoveryDaoStatic.class);
    
    @Value("${killrvideo.discovery.services.kafka:kafka}")
    private String kafkaServiceName;
    
    @Value("${killrvideo.discovery.static.kafka.port:8082}")
    private int kafkaPort;
    
    @Value("${killrvideo.discovery.static.kafka.host:kafka}")
    private String kafkaHost;
    
    @Value("${killrvideo.discovery.services.cassandra:cassandra}")
    private String cassandraServiceName;
    
    @Value("${killrvideo.discovery.static.cassandra.port:9042}")
    private int cassandraPort;
    
    @Value("${killrvideo.discovery.static.cassandra.host:dse}")
    private String cassandraHost;
    
    /** {@inheritDoc} */
    @Override
    public List<String> lookup(String serviceName) {
        List< String > endPointList = new ArrayList<>();
        LOGGER.info(" List (static) endpoints for key '{}':", serviceName);
        if (kafkaServiceName.trim().equalsIgnoreCase(serviceName.trim())) {
            endPointList.add(kafkaHost + ":" + kafkaPort);
            LOGGER.info(" + Adding '{}':", kafkaHost + ":" + kafkaPort);
        } else if (cassandraServiceName.trim().equalsIgnoreCase(serviceName.trim())) {
            endPointList.add(cassandraHost + ":" + cassandraPort);
            LOGGER.info(" + Adding '{}':", cassandraHost + ":" + cassandraPort);
        } else {
            throw new IllegalArgumentException("End point list cannot be empty");
        }
        LOGGER.info(" + [OK] Endpoints retrieved '{}':", endPointList);
        return endPointList;
    }
    
    /** {@inheritDoc} */
    @Override
    public String register(String serviceName, String hostName, int portNumber) {
        // Do nothing in k8s service are registered through DNS
        return serviceName;
    }

    /** {@inheritDoc} */
    @Override
    public void unregister(String serviceName) {}

    /** {@inheritDoc} */
    @Override
    public void unregisterEndpoint(String serviceName, String hostName, int portNumber) {}

}