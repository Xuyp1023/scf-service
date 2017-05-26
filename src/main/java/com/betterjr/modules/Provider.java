package com.betterjr.modules;

import java.net.URL;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

public class Provider {

    public static void main(String[] args) throws Exception {
        URL url = Provider.class.getClassLoader().getSystemResource("log4j.properties");
        System.out.println(url.toString());
        Log4jConfigurer.initLogging(url.getFile());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-context-scf-dubbo-provider.xml" });
        context.start();
<<<<<<< HEAD
        System.out.println("scf service  starting......");
=======
        System.out.println("scf-service 已经启动");
>>>>>>> refs/remotes/xuyp.scf-service/bug-185(2)
        System.in.read();
        context.close();
        System.exit(0);
    }

}
