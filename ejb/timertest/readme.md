1、部署timertest.war，并勾选web发布ejb
2、访问http://localhost:8080/timertest/TestEJB，最终返回true，表示ejb定时任务调用成功
3、查看server.log，会看到定时任务每隔5s执行一次的日志
##|2024-08-05 12:08:27.025|INFO|com.bes.ejb.test.apitest.TimerBean|_ThreadID=401;_ThreadName=EjbTimerPool - 10|Timer execute: test2|##
##|2024-08-05 12:08:32.033|INFO|com.bes.ejb.test.apitest.TimerBean|_ThreadID=402;_ThreadName=EjbTimerPool - 11|Timer execute: test2|##
##|2024-08-05 12:08:37.032|INFO|com.bes.ejb.test.apitest.TimerBean|_ThreadID=403;_ThreadName=EjbTimerPool - 12|Timer execute: test2|##
##|2024-08-05 12:08:42.026|INFO|com.bes.ejb.test.apitest.TimerBean|_ThreadID=401;_ThreadName=EjbTimerPool - 10|Timer execute: test2|##