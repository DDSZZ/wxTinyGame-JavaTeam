package top.phosky.mask.test;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import top.phosky.mask.api.AccountAPI;
import top.phosky.mask.dto.AccountDTO;

public class AccountTest {
    @Autowired
    private static AccountAPI accountAPI;

    public static void main(String[] args) {
        AccountDTO accountDTO = new AccountDTO("1425539139", "Phosky");

        System.out.println(accountAPI.login(JSON.toJSONString(accountDTO)));

    }
}
