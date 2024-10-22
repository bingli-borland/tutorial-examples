package javaeetutorial.esp.resource;

import java.util.HashSet;
import java.util.Set;

public class HelloWorldAppConfig extends javax.ws.rs.core.Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(HelloWorldResource.class);
        return classes;
    }
}