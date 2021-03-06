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
import org.json.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class controller {
    private double version = 1.0;

    private boolean refresh = false;

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
    private void scrollAction(){
        if(refresh){
            fillTable();
            refresh = false;
        }
    }

    @FXML
    private void refreshButtonAction(){
        fillTable();
    }

    @FXML
    private void saveListButtonAction(){
        if(true){
            String xml = model.saveList();

            FileChooser myFile = new FileChooser();
            myFile.setTitle("Save List");
            myFile.setInitialFileName("animelist.xml");
            File dest = myFile.showSaveDialog(null);
            if (dest != null) {
                saveTextToFile(xml, dest);
            }
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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

        int index = 0;

        for(int i = 0; i < animelist.size(); i++){
            if (animelist.get(i).getId() == aID){
                index = i;
                break;
            }
        }

        String aName = animelist.get(index).getName();
        String aStatus = animelist.get(index).getStatus();
        int aEpisode =  animelist.get(index).getSeenEp();
        int aEpisodes = animelist.get(index).getAllEp();
        int aScore = animelist.get(index).getMyScore();
        Image aImage = null;
        String aDesc = "";

        try {
            JSONObject kitsuJson = readJsonFromUrl("https://kitsu.io/api/edge/anime?filter%5Btext%5D=" + aName.replaceAll("\\s+","%20"));
            JSONObject attributes = kitsuJson.getJSONArray("data").getJSONObject(0).getJSONObject("attributes");

            aImage = getImageFromUrl(attributes.getJSONObject("posterImage").getString("medium"));

            aDesc = attributes.getString("synopsis");
        }catch(Exception e){
            System.out.println("JSON STUFF");
            System.out.println(e.getMessage());
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/detail.fxml"));
            Parent root1 = fxmlLoader.load();

            detailController controller = fxmlLoader.getController();
            //add links maybe later
            //controller.setDetMAL();
            //controller.setDetKitsu();
            //controller.setDetMasterani();
            if(aImage != null){
                controller.setDetImage(aImage);
            }else {
                controller.setDetImage(new Image("null.png"));
            }
            controller.setDetID(aID);
            controller.setDetStatus(aStatus);
            controller.setDetEpisode(aEpisode);
            controller.setDetEpisodes(aEpisodes);
            controller.setDetAnime(aName);
            controller.setDetScore(aScore);
            controller.setDetDesc(aDesc);

            Stage stage = new Stage();
            stage.setTitle("Details - " + aName);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            System.out.println("FXML STUFF");
            System.out.println(e.getMessage());
        }

        refresh = true;
    }

    private Image getImageFromUrl(String url) throws Exception {
        URL imgurl = new URL(url);
        URLConnection imgcon = imgurl.openConnection();
        imgcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        InputStream is = imgcon.getInputStream();
        return new Image(is);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        URL obj = new URL(url);
        URLConnection con = obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String jsonText = readAll(in);
        JSONObject json = new JSONObject(jsonText);
        return json;
    }
}
