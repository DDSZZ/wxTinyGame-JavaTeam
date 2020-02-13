package top.phosky.mask.dto;

public class AccountDTO {
    private String wxID;
    private String nickName;

    public AccountDTO(String wxID, String nickName) {
        this.wxID = wxID;
        this.nickName = nickName;
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
}
