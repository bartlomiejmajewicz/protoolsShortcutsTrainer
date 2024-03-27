import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Main implements KeyListener {

    List<String> commands = new ArrayList<>();
    List<String> winKeys = new ArrayList<>();
    List<String> macKeys = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    List<String> tagsList = new ArrayList<>();
    List <Boolean> macCmdPress = new ArrayList<>();
    List<Boolean> macOptPress = new ArrayList<>();
    List<Boolean> macCtrlPress = new ArrayList<>();
    List<Boolean> macShiftPress = new ArrayList<>();


    Main(){
        JFrame window = new JFrame();
        JPanel panel = new JPanel();

        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(panel);
        panel.setPreferredSize(new Dimension(500,500));

        window.addKeyListener(this);



        window.setVisible(true); // TODO odblokuj na true
        window.pack();
        window.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDefaultDataFromXml();
            }
        }).start();


    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        new Main();
    }

    private void loadDefaultDataFromXml(){

        String fileName = "src/Keyboard Shortcuts.xml";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        try {
            Document doc = db.parse(fileName);
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("Binding");

            Pattern pCmdPress = Pattern.compile(".*Command.*");
            Pattern pShiftPress = Pattern.compile(".*Shift.*");
            Pattern pOptPress = Pattern.compile(".*Option.*");
            Pattern pControlPress = Pattern.compile(".*Control.*");

            for (int i=0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String command = element.getElementsByTagName("Command").item(0).getTextContent();
                    String winKey = element.getElementsByTagName("WindowsKey").item(0).getTextContent();
                    String macKey = element.getElementsByTagName("MacKey").item(0).getTextContent();
                    String tag = element.getElementsByTagName("Tags").item(0).getTextContent();
                    // wszystkie parametry z XML do hashMaps
                    commands.add(command);
                    winKeys.add(winKey);
                    macKeys.add(macKey);
                    tags.add(tag);
                    boolean tagUnique = true;
                    for (int a=0; a < tagsList.size(); a++){
                        if (Objects.equals(tagsList.get(a), tag)){
                            tagUnique = false;
                        }
                    }
                    if (tagUnique){
                        tagsList.add(tag); // jeśli to pierwsze wystąpienie tego taga, to dodaj go do mapy
                    }

                    // modyfikatory
                    if (pCmdPress.matcher(macKey).matches()){
                        macCmdPress.add(true);
                    } else {
                        macCmdPress.add(false);
                    }
                    if (pShiftPress.matcher(macKey).matches()){
                        macShiftPress.add(true);
                    } else {
                        macShiftPress.add(false);
                    }
                    if (pOptPress.matcher(macKey).matches()){
                        macOptPress.add( true);
                    } else {
                        macOptPress.add(false);
                    }
                    if (pControlPress.matcher(macKey).matches()){
                        macCtrlPress.add(true);
                    } else {
                        macCtrlPress.add(false);
                    }

                }
            }

        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    // KEYBOARD LISTENER
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TUTAJ nie czytają się skróty z CMD o dziwo
        System.out.print(e.getKeyChar());// mogą być problemy z enterami, spacjami etc.
        System.out.print(" - ");
        System.out.print(e.getModifiersEx()); // ctrl 128; opt 512; cmd 256; shift 64
        System.out.print(" - ");
        System.out.print(e.getExtendedKeyCode()); // to jest kod klawisza
        System.out.print(" - ");
        System.out.print(e.getKeyCode()); // TODO to jest dobre - nie zmienia się niezależnie od modyfikatorów
        System.out.println();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}