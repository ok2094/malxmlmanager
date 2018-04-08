import javafx.scene.control.Button;

public class anime {
    private int id;
    private String name;
    private String status;
    private int seenEp;
    private int allEp;
    private int myRating;
    private Button btnEdit = new Button("edit");

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

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

    public int getMyRating() {
        return myRating;
    }

    public void setMyRating(int myRating) {
        this.myRating = myRating;
    }

    public String getEpisodes() {
        return seenEp + "/" + allEp;
    }

}
