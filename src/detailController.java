import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class detailController {
    @FXML
    private Label detAnime, detEpisodes;

    @FXML
    private Hyperlink detMAL, detKitsu, detMasterani;

    @FXML
    private ChoiceBox<anime> detStatus;

    @FXML
    private TextField detEpisode;

    @FXML
    private Slider detScore;

    public void display(String title, String aName, String aStatus, int aEpisode, int aEpisodes, int aScore) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/detail.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            detAnime.setText(aName);
            detEpisodes.setText("/" + aEpisodes);
            detScore.setValue(aScore);
            detEpisode.setText("" + aEpisode);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
