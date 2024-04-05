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
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main implements KeyListener {

    List<String> tags = new ArrayList<>();
    List<String> tagsList = new ArrayList<>();

    List<JCheckBox> tagsCheckboxList = new ArrayList<>();

    String[] keyNrAscii = new String[256];


    String[] commandsTable;
    String[] keyMacTable;
    String[] keyWinTable;
    String[] tagsTable;
    int currentGuessId=0;

    JFrame window;
    JPanel panel;
    JLabel jLabelTitle = new JLabel("Witaj w programie do nauki skrótów ProTools-a!");
    JLabel jLabelInstruction = new JLabel("Wybierz kategorie skrótów, które chcesz ćwiczyć:");
    GridBagConstraints constraints = new GridBagConstraints();
    JButton buttonShow;


    Main(){

        window = new JFrame();
        panel = new JPanel();
        JButton buttonStartSkip = new JButton("Start!");
        JButton buttonExit = new JButton("Wyjdź!");
        buttonShow = new JButton("");
        buttonShow.setFocusable(false);
        buttonShow.setVisible(false);

        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(panel);
        window.setTitle("Protools shortcuts trainer 2024.04");




        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500,500));
        setConstraintsForLayout(constraints,0,0);
        constraints.gridwidth = 3;
        constraints.weighty = 2;

        panel.add(jLabelTitle, constraints);
        constraints.weighty = 1;
        panel.add(jLabelInstruction, setConstraintsForLayout(constraints, 0,1));
        constraints.gridwidth = 1;


        buttonExit.setFocusable(false);
        buttonStartSkip.setFocusable(false);
        panel.add(buttonStartSkip, setConstraintsForLayout(constraints, 0,2));
        panel.add(buttonShow, setConstraintsForLayout(constraints, 1,2));
        panel.add(buttonExit, setConstraintsForLayout(constraints, 2,2));


        buttonStartSkip.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (checkIfAnyTagIsEnabled()){
                    newRandom();
                    buttonStartSkip.setText("Pomiń skrót");
                    buttonShow.setText("Pokaż odpowiedź");
                    buttonShow.setVisible(true);
                    jLabelInstruction.setVisible(false);
                } else {
                    jLabelInstruction.setText("Musisz wybrać przynajmniej jedną kategorię!");
                    jLabelInstruction.setVisible(true);

                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        buttonShow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jLabelInstruction.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        buttonExit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        window.addKeyListener(this);


        window.setVisible(true); // TODO odblokuj na true
        window.pack();
        window.repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDefaultDataFromXml();
                fillTheKeyboardMap();
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

            commandsTable = new String[nodeList.getLength()];
            keyMacTable = new String[nodeList.getLength()];
            keyWinTable = new String[nodeList.getLength()];
            tagsTable = new String[nodeList.getLength()];

            for (int i=0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String command = element.getElementsByTagName("Command").item(0).getTextContent();
                    String winKey = element.getElementsByTagName("WindowsKey").item(0).getTextContent();
                    String macKey = element.getElementsByTagName("MacKey").item(0).getTextContent();
                    String tag = element.getElementsByTagName("Tags").item(0).getTextContent();
                    macKey = macKey.toLowerCase();
                    winKey = winKey.toLowerCase();

                    // wszystkie parametry z XML do tablic
                    commandsTable[i] = command;
                    keyMacTable[i] = macKey;
                    keyWinTable[i] = winKey;
                    tagsTable[i] = tag;

                    // wszystkie parametry z XML do hashMaps
                    tags.add(tag);
                    String[] currentTags = tag.split(", ");

                    for (int b=0; b< currentTags.length; b++){
                        boolean tagUnique = true;
                        for (int a=0; a < tagsList.size(); a++){
                            if (Objects.equals(tagsList.get(a), currentTags[b])){
                                tagUnique = false;
                            }
                        }
                        if (tagUnique && currentTags[b] != ""){
                            tagsList.add(currentTags[b]); // jeśli to pierwsze wystąpienie tego taga, to dodaj go do mapy
                            tagsCheckboxList.add(new JCheckBox(currentTags[b]));
                        }
                    }
                }
            }
            for (String s : tagsList) {
                System.out.println(s);
            }
            setConstraintsForLayout(constraints, -1,4);
            for (JCheckBox checkBox : tagsCheckboxList){
                checkBox.setFocusable(false);
                checkBox.setSelected(true);
                panel.add(checkBox, setConstraintsForLayout(constraints));

                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getModifiers() == 24){
                            for (JCheckBox checkBoxModify : tagsCheckboxList){
                                checkBoxModify.setSelected(checkBox.isSelected());
                            }
                        }
                    }
                });
            }
            constraints.gridy += 1;
            constraints.gridx = 0;
            constraints.gridwidth = 3;
            JLabel podpis1 = new JLabel("Program stworzony na potrzeby AMFN w Bydgoszczy.");
            panel.add(podpis1,constraints);
            constraints.gridy += 1;
            JLabel podpis2 = new JLabel("Copyright Bartłomiej Majewicz 2024.");
            panel.add(podpis2,constraints);

            window.pack();
            window.repaint();

        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void fillTheKeyboardMap(){

        // fill all
        for (int a=0;a<256;a++){
            keyNrAscii[a] = Character.getName(((char) a)).toLowerCase();
            keyNrAscii[a] = keyNrAscii[a].replace("digit ", "");
            keyNrAscii[a] = keyNrAscii[a].replace("latin capital letter ", "");
        }
        for (int a=33;a<58;a++){
            keyNrAscii[a] = Character.toString(((char) a)).toLowerCase();
        }
        for (int a=91;a<94;a++){
            keyNrAscii[a] = Character.toString(((char) a)).toLowerCase();
        }

        // F1-F12
        for (int a=112;a<124;a++){
            keyNrAscii[a] = "f"+(a-111);
        }

        //arrows
        keyNrAscii[37] = "left arrow";
        keyNrAscii[38] = "up arrow";
        keyNrAscii[39] = "right arrow";
        keyNrAscii[40] = "down arrow";

        // klawisze opisane tekstem
        keyNrAscii[61] = "equals";
        keyNrAscii[45] = "minus";
        keyNrAscii[46] = "period";
        keyNrAscii[222] = "quote";
        keyNrAscii[9] = "tab";
        keyNrAscii[10] = "return";

        int a = 0;
        for (String keyDesc : keyNrAscii){
            System.out.print(a+" ");
            System.out.println(keyDesc);
            a++;
        }

        // TODO add numpad keycodes
        // TODO add page up / down keycodes

    }

    boolean checkCorrect(KeyEvent ke, int functionId){
        String[] macKeyIndividual = keyMacTable[functionId].split(" \\+ ");
        int modifierSum = 0; // ctrl 128; opt 512; cmd 256; shift 64
        int keySum = 0;
        for (String individualKey : macKeyIndividual){
            if (Objects.equals(individualKey, "shift")){
                modifierSum+=64;
                continue;
            }
            if (Objects.equals(individualKey, "control")){
                modifierSum+=128;
                continue;
            }
            if (Objects.equals(individualKey, "option")){
                modifierSum+=512;
                continue;
            }
            if (Objects.equals(individualKey, "command")){
                modifierSum+=256;
                continue;
            }
            for (int a=0; a<keyNrAscii.length;a++){
                if (Objects.equals(keyNrAscii[a], individualKey)){
                    keySum+=a;
                }
            }
        }
        System.out.print(modifierSum);
        System.out.print(" = ");
        System.out.println(keySum);
        if (ke.getModifiersEx() == modifierSum && ke.getKeyCode() == keySum){
            return true;
        } else {
            return false;
        }
    }

    void newRandom(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                currentGuessId = random.nextInt(commandsTable.length);
                while (Objects.equals(keyMacTable[currentGuessId], "") || !checkTag(currentGuessId)){
                    currentGuessId = random.nextInt(commandsTable.length);
                }
                jLabelTitle.setText(commandsTable[currentGuessId]);
                jLabelInstruction.setText(keyMacTable[currentGuessId]);
            }
        }).start();

    }

    boolean checkTag(int selectedShortcutId){
        for (JCheckBox jCheckBox : tagsCheckboxList){
            String[] currentTags = tagsTable[selectedShortcutId].split(", ");
            for (String tag : currentTags){
                if (Objects.equals(jCheckBox.getText(), tag) && jCheckBox.isSelected()){
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkIfAnyTagIsEnabled(){
        for (JCheckBox jCheckBox : tagsCheckboxList){
            if (jCheckBox.isSelected()){
                return true;
            }
        }
        return false;
    }

    GridBagConstraints setConstraintsForLayout(GridBagConstraints gridBagConstraints, int x, int y){
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        return gridBagConstraints;
    }
    GridBagConstraints setConstraintsForLayout(GridBagConstraints gridBagConstraints){
        if (gridBagConstraints.gridx == 2){
            gridBagConstraints.gridy += 1;
            gridBagConstraints.gridx = 0;
        } else {
            gridBagConstraints.gridx += 1;
        }

        return gridBagConstraints;
    }



    // KEYBOARD LISTENER
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (checkCorrect(e, currentGuessId)){
            jLabelInstruction.setVisible(false);
            newRandom();
        }
        System.out.print(e.getModifiersEx()); // ctrl 128; opt 512; cmd 256; shift 64
        System.out.print(" - ");
        System.out.print(e.getExtendedKeyCode()); // to jest kod klawisza
        System.out.print(" - ");
        System.out.print(e.getKeyCode()); // to jest dobre - nie zmienia się niezależnie od modyfikatorów
        System.out.println();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
// TODO odrzucić "commands focus" chociaż i tak działa
}
