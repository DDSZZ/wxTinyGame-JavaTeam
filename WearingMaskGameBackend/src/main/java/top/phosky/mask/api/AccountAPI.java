package top.phosky.mask.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.phosky.mask.dto.AccountDTO;
import top.phosky.mask.service.LoginService;

@Controller
public class AccountAPI {
    @Autowired
    private LoginService loginService;

    /**
     * 玩家登录游戏的时候访问的API，将会更新玩家的昵称(如果微信名称被修改了)
     * <p>
     * 新用户会创建数据
     * <p>
     * 传入参数: 带有wxID(String)，nickName(String)的封装类JSON字符串
     * <p>
     * 返回数字int
     * 登录成功且为老用户返回2
     * 登录成功且为新用户1
     * 传入参数错误返回0
     * 其他错误返回-1
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public int login(@RequestParam(name = "accountObj") String accountObj) {
        try {
            JSONObject obj = JSON.parseObject(accountObj);
            String wxID = obj.getString("wxID");
            String nickName = obj.getString("nickName");
            AccountDTO accountDTO = new AccountDTO(wxID, nickName);
            int status = loginService.login(accountDTO);
            return status;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return -1;
    }
}
