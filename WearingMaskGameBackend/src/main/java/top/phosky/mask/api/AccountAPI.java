package top.phosky.mask.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.phosky.mask.dto.AccountDTO;
import top.phosky.mask.service.LoginService;
import top.phosky.mask.service.RankService;

@Controller
public class AccountAPI {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RankService rankService;

    /**
     * 玩家登录游戏的时候访问的API，将会更新玩家的昵称(如果微信名称被修改了)
     * <p>
     * 新用户会创建数据
     * <p>
     * 传入参数: 带有wxID(String)，nickName(String)的封装类JSON字符串
     * <p>
     * 返回信息字符串
     * 登录成功且为老用户返回OLD_ACCOUNT
     * 登录成功且为新用户NEW_ACCOUNT
     * 传入参数错误返回PARAM_FAULT
     * 传入JSON格式错误返回JSON_CONVERT_FAULT
     * 其他错误返回UNKNOWN_FAULT
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam(name = "accountObj") String accountObj) {
        try {
            JSONObject obj = JSON.parseObject(accountObj);
            String wxID = obj.getString("wxID");
            String nickName = obj.getString("nickName");
            if (wxID == null || wxID.trim().equals("") || nickName == null || nickName.trim().equals("")) {
                return "PARAM_FAULT";
            }
            AccountDTO accountDTO = new AccountDTO(wxID, nickName);
            int status = loginService.login(accountDTO);
            if (status == 1) {
                return "NEW_ACCOUNT";
            } else if (status == 2) {
                return "OLD_ACCOUNT";
            } else if (status == 0) {
                return "PARAM_FAULT";
            }
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
            return "JSON_CONVERT_FAULT";
        } catch (Exception err) {
            err.printStackTrace();
        }
        return "UNKNOWN_FAULT";
    }
}
