lib/cart-common.jar 中存在变量javaeetutorial.cart.util.Constants.TEST_LIB_CONSTANT1
cart-war1.war中存在变量javaeetutorial.cart.war1.Constants.TEST_WAR1_CONSTANT1
1、如开启多个war使用同一个classloader，访问http://localhost:9080/cart-war1/和http://localhost:9080/cart-war2看到上面两个变量值相同，且
一个页面修改后，另一个页面也可见
2、META-INF/jars.txt 给ear配置了附加类库


```dtd
/root/middleware/apache-tomcat-8.5.100/webapps/examples/WEB-INF/classes/
+${com.bes.installRoot}/test/*.jar
+/root/middleware/apache-tomcat-8.5.100/webapps/docs/

+/mnt/d/project/spring-boot-websocket-chat-demo/target/test-classes
+/mnt/d/project/spring-boot-websocket-chat-demo/target/BOOT-INF/lib/*.jar
+/mnt/d/project/spring-boot-websocket-chat-demo/
```