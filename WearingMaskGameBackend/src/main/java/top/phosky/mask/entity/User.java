package top.phosky.mask.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
    private String wxID;
    private String nickName;
    private int maxMarks;

    public User(String wxID, String nickName, int maxMarks) {
        this.wxID = wxID;
        this.nickName = nickName;
        this.maxMarks = maxMarks;
    }

    public String getWxID() {
        return wxID;
    }

    public void setWxID(String wxID) {
        this.wxID = wxID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    @Override
    public int compareTo(@NotNull User o) {
        int mark1 = this.maxMarks;
        int mark2 = o.getMaxMarks();
        if (mark1 < mark2) {
            return 1;
        } else if (mark1 == mark2) {
            if (this.wxID.equals(o.getWxID())) {//等于的要求很严苛
                return 0;
            } else {//二级比较，根据昵称比较
                return this.getNickName().compareTo(o.getNickName());
            }
        } else if (mark1 > mark2) {
            return -1;
        }
        return 1;//分数值相等，但是不是一个元素
    }
}
