package org.example.Multiplication;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class HazelcastManager {
    private static HazelcastInstance hazelcastInstance;

    // Inicializa la instancia de Hazelcast
    public static void initialize() {
        if (hazelcastInstance == null) {
            hazelcastInstance = Hazelcast.newHazelcastInstance();
        }
    }

    // Devuelve la instancia de Hazelcast
    public static HazelcastInstance getInstance() {
        if (hazelcastInstance == null) {
            throw new IllegalStateException("Hazelcast instance is not initialized");
        }
        return hazelcastInstance;
    }

    // Devuelve el servicio de ejecución para la multiplicación
    public static IExecutorService getExecutorService(String serviceName) {
        return getInstance().getExecutorService(serviceName);
    }

    // Finaliza Hazelcast al apagar la aplicación
    public static void shutdown() {
        if (hazelcastInstance != null) {
            hazelcastInstance.shutdown();
        }
    }
}