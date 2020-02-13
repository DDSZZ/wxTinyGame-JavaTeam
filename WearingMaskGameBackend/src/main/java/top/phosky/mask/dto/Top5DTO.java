package top.phosky.mask.dto;

import top.phosky.mask.entity.User;

public class Top5DTO {
    private User top1;
    private User top2;
    private User top3;
    private User top4;
    private User top5;

    public Top5DTO(User top1, User top2, User top3, User top4, User top5) {
        this.top1 = top1;
        this.top2 = top2;
        this.top3 = top3;
        this.top4 = top4;
        this.top5 = top5;
    }

    public User getTop1() {
        return top1;
    }

    public void setTop1(User top1) {
        this.top1 = top1;
    }

    public User getTop2() {
        return top2;
    }

    public void setTop2(User top2) {
        this.top2 = top2;
    }

    public User getTop3() {
        return top3;
    }

    public void setTop3(User top3) {
        this.top3 = top3;
    }

    public User getTop4() {
        return top4;
    }

    public void setTop4(User top4) {
        this.top4 = top4;
    }

    public User getTop5() {
        return top5;
    }

    public void setTop5(User top5) {
        this.top5 = top5;
    }
}

