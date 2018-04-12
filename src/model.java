import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;


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

    public static void deleteFromList(int ID){
        for(int i = 0; i < anime.size(); i++){
            if (anime.get(i).getId() == ID){
                anime.remove(i);
                break;
            }
        }
    }

    public static String saveList(){
        String xmlString = "";

        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("myanimelist");
            document.appendChild(rootElement);

            for(int i = 0; i < anime.size(); i++){
                Element e = document.createElement("anime");

                Element em = document.createElement("series_title");
                em.appendChild(document.createTextNode(anime.get(i).getName()));
                e.appendChild(em);

                em = document.createElement("series_episodes");
                em.appendChild(document.createTextNode("" + anime.get(i).getAllEp()));
                e.appendChild(em);

                em = document.createElement("my_watched_episodes");
                em.appendChild(document.createTextNode("" + anime.get(i).getSeenEp()));
                e.appendChild(em);

                em = document.createElement("my_score");
                em.appendChild(document.createTextNode("" + anime.get(i).getMyScore()));
                e.appendChild(em);

                em = document.createElement("my_status");
                em.appendChild(document.createTextNode(anime.get(i).getStatus()));
                e.appendChild(em);

                rootElement.appendChild(e);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);

            xmlString = result.getWriter().toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return xmlString;
    }
}
