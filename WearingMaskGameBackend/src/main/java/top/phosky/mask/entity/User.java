package top.phosky.mask.entity;

public class User {
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
}
