package top.phosky.mask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import top.phosky.mask.dao.file.UserDAO;
import top.phosky.mask.dto.AccountDTO;
import top.phosky.mask.entity.User;

@Component
public class LoginService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RankService rankService;

    //老用户返回2，新用户返回1，错误返回0
    public int login(AccountDTO accountDTO) {
        User user = userDAO.select(accountDTO.getWxID());
        if (user == null) {//新用户，添加到数据库
            User userNew = new User(accountDTO.getWxID(), accountDTO.getNickName(), 0);
            int status = userDAO.insert(userNew);
            if (status == 1) {
                rankService.getUsersOrdered().insert(userNew);
                return 1;
            } else {
                return 0;
            }
        } else {//更新昵称
            if (!accountDTO.getNickName().equals(user.getNickName())) {
                user.setNickName(accountDTO.getNickName());
                userDAO.update(accountDTO.getWxID(), user);
            }
            return 2;
        }
    }
}
