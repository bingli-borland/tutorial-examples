1、bes955配置server.config开启securitymanager
<jvm-options>-Djava.security.manager</jvm-options>
2、使用附件中server.policy
<jvm-options>-Djava.security.policy=<自定义路径>/server.policy</jvm-options>
3、启动bes，部署jacctest.ear， 勾选cdi，应用中permissions.xml 定义了应用所拥有的权限
4、正常测试：访问http://localhost:8088/jaccweb/test/TestJacc，返回信息中各项权限校验成功
5、异常测试：访问http://localhost:8088/jaccweb/test/TestJacc?exp=true, 返回信息中出现权限校验失败（不在permissions.xml定义中）