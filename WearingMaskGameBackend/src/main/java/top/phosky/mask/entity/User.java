package top.phosky.mask.entity;

import org.jetbrains.annotations.NotNull;

public class User implements Comparable<User> {
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
            return 0;
        } else {
            return -1;
        }
    }
}
