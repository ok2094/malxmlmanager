import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;


public class model {
    private static ObservableList<anime> anime = FXCollections.observableArrayList();
    private static Document mallist;

    public static ObservableList<anime> getAnime() {
        return anime;
    }

    public static void initializeList(File xmlDoc) {
        mallist = getDocument(xmlDoc);
        mallist.getDocumentElement().normalize();
        NodeList animelist = mallist.getElementsByTagName("anime");

        for (int i = 0; i < animelist.getLength(); i++) {
            anime a = new anime();
            Node node = animelist.item(i);
            Element elem = (Element) node;

            a.setId(i);
            a.setName(elem.getElementsByTagName("series_title").item(0).getTextContent());
            a.setStatus(elem.getElementsByTagName("my_status").item(0).getTextContent());
            a.setSeenEp(Integer.parseInt(elem.getElementsByTagName("my_watched_episodes").item(0).getTextContent()));
            a.setAllEp(Integer.parseInt(elem.getElementsByTagName("series_episodes").item(0).getTextContent()));
            a.setMyScore(Integer.parseInt(elem.getElementsByTagName("my_score").item(0).getTextContent()));
            a.setMalId(Integer.parseInt(elem.getElementsByTagName("series_animedb_id").item(0).getTextContent()));

            anime.add(a);
        }

    }

    private static Document getDocument(File xmlDoc) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlDoc);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return null;
    }
}
