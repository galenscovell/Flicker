
/**
 * MONSTERPARSER CLASS
 * Deserializes monster XML data.
 */

package galenscovell.util;

import com.badlogic.gdx.Gdx;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class MonsterParser {

    public MonsterParser() {
        try {
            File xmlFile = new File("data/monsters.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList1 = doc.getElementsByTagName("Monster");
            for (int i = 0; i < nList1.getLength(); i++) {
                Node node = nList1.item(i);
                System.out.println("\nElement: " + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("Type: " + element.getAttribute("Type"));
                    System.out.println("AIType: " + element.getAttribute("AIType"));
                    System.out.println("Intelligent: " + element.getAttribute("Intelligent"));
                }
            }

            NodeList nList2 = doc.getElementsByTagName("BaseStats");
            for (int i = 0; i < nList2.getLength(); i++) {
                Node node = nList2.item(i);
                System.out.println("\nElement: " + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("Base Level: " + element.getAttribute("BaseLevel"));
                    System.out.println("Base Vision: " + element.getAttribute("BaseVision"));
                    System.out.println("Base HP: " + element.getAttribute("BaseHP"));
                    System.out.println("Base Defense: " + element.getAttribute("BaseDefense"));
                    System.out.println("Base Evade: " + element.getAttribute("BaseEvade"));
                    System.out.println("Base Damage: " + element.getAttribute("BaseDamage"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
