package top.phosky.mask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import top.phosky.mask.dao.file.UserDAO;
import top.phosky.mask.dto.AccountDTO;
import top.phosky.mask.entity.User;

@Controller
public class LoginService {
    @Autowired
    private UserDAO userDAO;

    //老用户返回2，新用户返回1，错误返回0
    public int login(AccountDTO accountDTO) {
        User user = userDAO.select(accountDTO.getWxID());
        if (user == null) {//新用户，添加到数据库
            int status = userDAO.insert(new User(accountDTO.getWxID(), accountDTO.getNickName(), 0));
            if (status == 1) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }
}