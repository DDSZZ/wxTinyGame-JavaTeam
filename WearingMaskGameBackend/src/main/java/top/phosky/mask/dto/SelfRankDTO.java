package top.phosky.mask.dto;

public class SelfRankDTO {
    private int marks;
    private int rank;

    public SelfRankDTO(int marks, int rank) {
        this.marks = marks;
        this.rank = rank;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
