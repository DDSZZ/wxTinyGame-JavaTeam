package top.phosky.mask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WearMaskGameApplication {
    public final static String PATH = System.getProperty("user.dir");

    public static void main(String[] args) {
        SpringApplication.run(WearMaskGameApplication.class, args);
    }

}
