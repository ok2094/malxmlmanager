import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import java.io.File;

public class controller {
    @FXML
    private TableView<anime> tblAnime;

    @FXML
    private TableColumn<anime, String> colAnime, colStatus, colEpisodes;

    @FXML
    private TableColumn<anime, anime> colEdit;

    @FXML
    private TableColumn<anime, Integer> colRating, colID;

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
        colAnime.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEpisodes.setCellValueFactory(new PropertyValueFactory<>("episodes"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("myScore"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));

        colEdit.setCellFactory(col -> {
            final Button editButton = new Button("edit");
            TableCell<anime, anime> cell = new TableCell<anime, anime>() {
                @Override
                public void updateItem(anime anime, boolean empty) {
                    super.updateItem(anime, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
            editButton.setOnAction((event) -> {
                editButtonClick(cell.getTableRow().getIndex());
            });

            return cell ;
        });

        tblAnime.getItems().setAll(model.getAnime());
    }

    private void editButtonClick(int cellIndex){
        ObservableList<anime> animelist = model.getAnime();

        int aID = colID.getCellObservableValue(cellIndex).getValue();

        String aName = animelist.get(aID).getName();
        String aStatus = animelist.get(aID).getStatus();
        int aEpisode =  animelist.get(aID).getSeenEp();
        int aEpisodes = animelist.get(aID).getAllEp();
        int aScore = animelist.get(aID).getMyScore();


        detAnime.setText("TEST");
        detEpisodes.setText("/" + aEpisodes);
        detScore.setValue(aScore);
        detEpisode.setText("" + aEpisode);

        openDetails(aName);
    }

    private void openDetails(String title){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/detail.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
