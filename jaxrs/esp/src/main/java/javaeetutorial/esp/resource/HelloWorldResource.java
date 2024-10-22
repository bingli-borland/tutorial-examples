package javaeetutorial.esp.resource;

@javax.ws.rs.Path("/helloworld")
public class HelloWorldResource {

    @javax.ws.rs.GET
    public String sayHelloWorld() {
        return "Hello World!";
    }
}