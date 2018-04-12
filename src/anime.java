
public class anime {
    private int id;
    private String name;
    private String status;
    private int seenEp;
    private int allEp;
    private int myScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeenEp() {
        return seenEp;
    }

    public void setSeenEp(int seenEp) {
        this.seenEp = seenEp;
    }

    public int getAllEp() {
        return allEp;
    }

    public void setAllEp(int allEp) {
        this.allEp = allEp;
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public String getEpisodes() {
        return seenEp + "/" + allEp;
    }
}
