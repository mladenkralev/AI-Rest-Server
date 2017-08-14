package application;


import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by mladen on 8/14/2017.
 */
public class Demo {
    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    public static void main (String[] args) {
        ApplicationContext ctx =
                SpringApplication.run(Demo.class, args);
        MyBean bean = ctx.getBean(MyBean.class);
        bean.doSomething();
    }

    private static class MyBean {

        public void doSomething () {

            System.out.println("Doing something in MyBean");
       }
    }
}
