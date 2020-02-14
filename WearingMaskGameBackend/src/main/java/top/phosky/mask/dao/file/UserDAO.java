package top.phosky.mask.dao.file;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import top.phosky.mask.WearMaskGameApplication;
import top.phosky.mask.entity.User;
import top.phosky.mask.interfaces.CRUDInterface;
import top.phosky.mask.util.FileUtil;

import java.util.LinkedList;

@Component
@Order(-1)
public class UserDAO implements CRUDInterface<User> {
    private final static String PATH = WearMaskGameApplication.PATH + "\\data\\Users.dat";

    //插入成功返回1，未知原因错误返回0
    @Override
    public int insert(User obj) {
        try {
            LinkedList<User> users = FileUtil.getSingleton().readFile(PATH);
            if (users == null) {
                users = new LinkedList<>();
            }
            users.add(obj);
            FileUtil.getSingleton().saveFile(PATH, users);
            return 1;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return 0;
    }

    @Override
    public <K> int delete(K key) {
        //TODO
        return 0;
    }

    //修改成功返回1，未找到对象返回-1，未知错误原因返回0
    @Override
    public <K> int update(K key, User obj) {
        try {
            boolean isFound = false;
            LinkedList<User> users = FileUtil.getSingleton().readFile(PATH);
            if (users == null) {
                users = new LinkedList<>();
            }
            int i = 0;//修改用迭代其中的失败
            for (User u : users) {
                if (u.getWxID().equals(key)) {
                    isFound = true;
                    break;
                }
                i++;
            }
            if (isFound) {
                users.set(i, obj);
                FileUtil.getSingleton().saveFile(PATH, users);
                return 1;
            } else {
                return -1;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return 0;
    }

    //找到了返回对象，查找失败返回null
    @Override
    public <K> User select(K key) {
        try {
            LinkedList<User> users = FileUtil.getSingleton().readFile(PATH);
            if (users == null) {
                users = new LinkedList<>();
            }
            for (User u : users) {
                if (u.getWxID().equals(key)) {
                    return u;
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    //返回所有的数据
    public LinkedList<User> getAllUsers() {
        LinkedList<User> users = FileUtil.getSingleton().readFile(PATH);
        if (users == null) {
            users = new LinkedList<>();
        }
        return users;
    }
}
