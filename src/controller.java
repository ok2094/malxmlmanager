import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class controller {
    private double version = 0.4;

    @FXML
    private TableView<anime> tblAnime;

    @FXML
    private TableColumn<anime, String> colAnime, colStatus, colEpisodes;

    @FXML
    private TableColumn<anime, anime> colEdit;

    @FXML
    private TableColumn<anime, Integer> colRating, colID;

    @FXML
    private Label statTotal, statWatch, statHold, statDrop, statComplete, statPlan;

    @FXML
    private void refreshButtonAction(){
        fillTable();
    }

    @FXML
    private void aboutButtonAction(){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(5);
        dialogVbox.getChildren().add(new Text("MyAnimeList List Manager Version: " + version));
        dialogVbox.getChildren().add(new Text("by Jen"));
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox, 250, 70);
        dialog.setScene(dialogScene);
        dialog.show();
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
        ObservableList<anime> animelist = model.getAnime();

        tblAnime.getItems().setAll(animelist);

        //Stats

        statTotal.setText("" + animelist.size());

        int watch=0, complete=0, hold=0, drop=0, plan=0;

        for(int i = 0; i < animelist.size(); i++) {
            switch (animelist.get(i).getStatus()) {
                case "Completed": complete++;
                    break;
                case "On-Hold": hold++;
                    break;
                case "Dropped": drop++;
                    break;
                case "Plan to Watch": plan++;
                    break;
                case "Watching": watch++;
                    break;
            }
        }

        statWatch.setText("" + watch);
        statComplete.setText("" + complete);
        statDrop.setText("" + drop);
        statHold.setText("" + hold);
        statPlan.setText("" + plan);
    }

    private void editButtonClick(int cellIndex){
        ObservableList<anime> animelist = model.getAnime();

        int aID = colID.getCellObservableValue(cellIndex).getValue();

        String aName = animelist.get(aID).getName();
        String aStatus = animelist.get(aID).getStatus();
        int aEpisode =  animelist.get(aID).getSeenEp();
        int aEpisodes = animelist.get(aID).getAllEp();
        int aScore = animelist.get(aID).getMyScore();
        Image aImage = null;

        try {
            JSONObject kitsuJson = readJsonFromUrl("https://kitsu.io/api/edge/anime?page[limit]=1?filter[text]=" + aName);
            System.out.println(kitsuJson);
            aImage = new Image(kitsuJson.get(".data[\"0\"].attributes.posterImage.medium").toString());
        }catch(Exception e){
            System.out.println("JSON STUFF");
            System.out.println(e.getMessage());
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/detail.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            detailController controller = fxmlLoader.getController();
            //controller.setDetMAL();
            //controller.setDetKitsu();
            //controller.setDetMasterani();
            if(aImage != null){
                controller.setDetImage(aImage);
            }
            controller.setDetID(aID);
            controller.setDetStatus(aStatus);
            controller.setDetEpisode(aEpisode);
            controller.setDetEpisodes(aEpisodes);
            controller.setDetAnime(aName);
            controller.setDetScore(aScore);

            Stage stage = new Stage();
            stage.setTitle("Details - " + aName);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            System.out.println("FXML STUFF");
            System.out.println(e.getMessage());
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
