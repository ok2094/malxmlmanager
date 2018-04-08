import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import java.io.File;

public class controller {
    @FXML
    private TableView<anime> tblAnime;

    @FXML
    private TableColumn<anime, String> colAnime, colStatus, colEpisodes;

    @FXML
    private TableColumn<anime, Button> colEdit;

    @FXML
    private TableColumn<anime, Integer> colRating, colID;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("test");
    }

    public void openFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Animelist");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            model.initializeList(selectedFile);
            fillTable();
        }
    }

    public void fillTable() {
        colAnime.setCellValueFactory(new PropertyValueFactory<anime, String>("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<anime, String>("status"));
        colEpisodes.setCellValueFactory(new PropertyValueFactory<anime, String>("episodes"));
        colRating.setCellValueFactory(new PropertyValueFactory<anime, Integer>("myRating"));
        colID.setCellValueFactory(new PropertyValueFactory<anime, Integer>("id"));
        colEdit.setCellValueFactory(new PropertyValueFactory<anime, Button>("btnEdit"));

        tblAnime.getItems().setAll(model.getAnime());
    }
}
