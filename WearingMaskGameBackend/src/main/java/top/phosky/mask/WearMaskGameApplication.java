package top.phosky.mask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.phosky.mask.service.RankService;

@SpringBootApplication
public class WearMaskGameApplication {
    public final static String PATH = System.getProperty("user.dir");
    @Autowired
    private static RankService rankService;

    public static void main(String[] args) {
        rankService.initUserOrdered();
        SpringApplication.run(WearMaskGameApplication.class, args);
    }

}
