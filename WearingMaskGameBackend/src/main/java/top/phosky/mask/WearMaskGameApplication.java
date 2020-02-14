package top.phosky.mask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import top.phosky.mask.test.AccountTest;
import top.phosky.mask.service.RankService;

@SpringBootApplication
public class WearMaskGameApplication {
    public final static String PATH = System.getProperty("user.dir");
    @Autowired
    public static RankService rankService;

    public static void main(String[] args) {
        SpringApplication.run(WearMaskGameApplication.class, args);
        //test
        // AccountTest.main(null);


    }

}
