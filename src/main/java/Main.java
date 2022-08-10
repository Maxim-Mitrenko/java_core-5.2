import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        File file = new File("data.xml");
        File result = new File("data2.json");
        listToJSON(result, parseXML(file));
    }

    public static List<Employee> parseXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            List<Employee> list = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    long id = Long.parseLong(getElement(element, "id"));
                    String firstName = getElement(element, "firstName");
                    String lastName = getElement(element, "lastName");
                    String country = getElement(element, "country");
                    int age = Integer.parseInt(getElement(element, "age"));
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    list.add(employee);
                }
            }
            return list;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void listToJSON (File file, List<Employee> list) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(gson.toJson(list, listType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getElement(Element element, String name) {
        return element.getElementsByTagName(name).item(0).getTextContent();
    }
}