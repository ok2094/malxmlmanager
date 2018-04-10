import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class detailController implements Initializable {
    @FXML
    private Label detAnime, detEpisodes, detID;

    @FXML
    private Hyperlink detMAL, detKitsu, detMasterani;

    @FXML
    private ChoiceBox<String> detStatus;

    @FXML
    private TextField detEpisode;

    @FXML
    private Slider detScore;

    @FXML
    private ImageView detImage;

    @FXML
    private Button btnCancel;

    @FXML
    private void cancelButtonAction(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveButtonAction(){
        int aID = Integer.parseInt(detID.getText());

        ObservableList<anime> animelist = model.getAnime();
        animelist.get(aID).setStatus(detStatus.getValue());
        animelist.get(aID).setMyScore((int)detScore.getValue());
        animelist.get(aID).setSeenEp(Integer.parseInt(detEpisode.getText()));

        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setDetImage(Image image) {
        this.detImage.setImage(image);
    }

    public void setDetStatus(String aStatus) {
        this.detStatus.getItems().addAll("Watching", "Completed", "Plan to Watch", "On-Hold", "Dropped");
        this.detStatus.setValue(aStatus);
    }

    public void setDetID(int aID) {
        this.detID.setText("" + aID);
    }

    public void setDetAnime(String aName) {
        this.detAnime.setText(aName);
    }

    public void setDetEpisodes(int aEpisodes) {
        this.detEpisodes.setText("/" + aEpisodes);
    }

    public void setDetEpisode(int aEpisode) {
        this.detEpisode.setText("" + aEpisode);
    }

    public void setDetScore(int aScore) {
        this.detScore.setValue(aScore);
    }

    public void setDetMAL(Hyperlink detMAL) {
        this.detMAL = detMAL;
    }

    public void setDetKitsu(Hyperlink detKitsu) {
        this.detKitsu = detKitsu;
    }

    public void setDetMasterani(Hyperlink detMasterani) {
        this.detMasterani = detMasterani;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
