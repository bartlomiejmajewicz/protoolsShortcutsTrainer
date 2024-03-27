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
import java.util.HashMap;
import java.util.regex.Pattern;

public class Main implements KeyListener {

    HashMap commands = new HashMap();
    HashMap winKeys = new HashMap();
    HashMap macKeys = new HashMap();
    HashMap tags = new HashMap();
    HashMap macCmdPress = new HashMap();
    HashMap macOptPress = new HashMap();
    HashMap macCtrlPress = new HashMap();
    HashMap macShiftPress = new HashMap();


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
                    commands.put(i, command);
                    winKeys.put(i, winKey);
                    macKeys.put(i, macKey);
                    tags.put(i, tag);

                    // modyfikatory
                    if (pCmdPress.matcher(macKey).matches()){
                        macCmdPress.put(i, true);
                    } else {
                        macCmdPress.put(i, false);
                    }
                    if (pShiftPress.matcher(macKey).matches()){
                        macShiftPress.put(i, true);
                    } else {
                        macShiftPress.put(i, false);
                    }
                    if (pOptPress.matcher(macKey).matches()){
                        macOptPress.put(i, true);
                    } else {
                        macOptPress.put(i, false);
                    }
                    if (pControlPress.matcher(macKey).matches()){
                        macCtrlPress.put(i, true);
                    } else {
                        macCtrlPress.put(i, false);
                    }

                }
            }
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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