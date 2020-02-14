package top.phosky.mask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import top.phosky.mask.dao.file.UserDAO;
import top.phosky.mask.dto.SelfRankDTO;
import top.phosky.mask.entity.User;
import top.phosky.mask.util.adt.AVLTree;

import java.util.ArrayList;
import java.util.LinkedList;

@Component
@Order(1)
public class RankService {
    @Autowired
    private UserDAO userDAO;
    /**
     * 用户的排名顺序，在内存中存储，需要持续更新数据
     */
    public AVLTree<User> usersOrdered;

    private RankService() {
        initUserOrdered();//DEBUG
    }

    public SelfRankDTO getRankAndScore(String wxID) {
        User user = userDAO.select(wxID);
        if (user == null) {
            return null;
        }
        return new SelfRankDTO(user.getMaxMarks(), getRank(wxID));
    }

    //返回-1表示未找到，返回其他数字表示排名
    public int getRank(String wxID) {
        int index = 0;
        for (User user : usersOrdered) {
            if (user.getWxID().equals(wxID)) {
                return index + 1;
            }
            index++;
        }
        return -1;
    }

    //-1表示未找到，1表示修改成功，0表示分数没有刷新记录，-2表示未知错误
    public int setRank(String wxID, int marks) {
        User user = userDAO.select(wxID);
        if (user == null) {
            return -1;
        }
        int preMarks = user.getMaxMarks();
        if (marks > preMarks) {
            user.setMaxMarks(marks);
            int status = userDAO.update(wxID, user);
            if (status == 1) {
                //更改在AVL树中的数据，即移动AVl树
                user.setMaxMarks(preMarks);//DEBUG
                usersOrdered.remove(user);//方法内部根据id值比较user的不同,前提是id值相同
                user.setMaxMarks(marks);
                usersOrdered.insert(user);//再添加
                return 1;
            } else if (status == -1) {
                return -1;
            } else if (status == 0) {
                return -2;
            }
        }
        return 0;
    }

    public AVLTree<User> getUsersOrdered() {
        return usersOrdered;
    }

    public void initUserOrdered() {
        usersOrdered = new AVLTree<>();
        if (userDAO == null) {//TODO
            userDAO = new UserDAO();
        }
        LinkedList<User> users = userDAO.getAllUsers();
        for (User u : users) {
            usersOrdered.insert(u);
        }
    }

    public ArrayList<User> getRankTop5() {
        ArrayList<User> list = new ArrayList<>();
        int i = 0;
        for (User u : usersOrdered) {
            if (i < 5) {
                list.add(u);
            } else {
                break;
            }
            i++;
        }
        return list;
    }
}
