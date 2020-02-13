package top.phosky.mask.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountAPI {
    /**
     * 玩家登录游戏的时候访问的API，将会更新玩家的昵称(如果微信名称被修改了)
     * <p>
     * 新用户会创建数据
     * <p>
     * 传入参数: 带有wxID(String)，nickName(String)的封装类JSON字符串
     * <p>
     * 返回状态类(state:int，content:String)的JSON字符串
     * <p>
     * 登录成功
     * 登录成功且为新用户
     * 传入参数错误
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam(name = "accountObj") String accountObj) {
        //TODO
        return null;
    }
}
