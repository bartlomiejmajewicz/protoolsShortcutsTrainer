import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EntryMenu {
    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel();
    GridBagConstraints c = new GridBagConstraints();

    EntryMenu(){
        jFrame.setTitle("Protools shortcuts trainer 2024.04 - settings");
        jFrame.setPreferredSize(new Dimension(400,200));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-200, Toolkit.getDefaultToolkit().getScreenSize().height/2-100);

        jPanel.setLayout(new GridBagLayout());
        JLabel jLabel = new JLabel("Wybierz źródło pliku ze skrótami: ");
        JButton jButtonInternal = new JButton("skróty domyślne");
        JButton jButtonUpload = new JButton("dodaj plik xml ze skrótami...");


        jButtonInternal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                jFrame.dispose();
                new ProToolsTrainerMain(null);
            }
        });

        jButtonUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });



        c.gridwidth = 2;
        jPanel.add(jLabel, setConstraintsInGrid(c, 0,0));
        c.gridwidth = 1;
        jPanel.add(jButtonInternal, setConstraintsInGrid(c, 0,1));
        jPanel.add(jButtonUpload, setConstraintsInGrid(c, 1,1));



        jFrame.add(jPanel);
        jFrame.setVisible(true);
        jFrame.pack();


    }

    GridBagConstraints setConstraintsInGrid(GridBagConstraints constraints, int x, int y){
        constraints.gridx = x;
        constraints.gridy = y;
        return constraints;
    }

    void loadFile(){
        FileDialog fileDialog = new FileDialog(jFrame, "dodaj plik xml ze skrótami...");
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setFile("*.xml");
        fileDialog.setVisible(true);
        String file = fileDialog.getDirectory()+fileDialog.getFile();
        fileDialog.dispose();
        String filetype = fileDialog.getFile().split("\\.")[1];
        System.out.println(filetype);
        if (!Objects.equals(filetype, "xml")){
            loadFile();
        } else {
            jFrame.setVisible(false);
            jFrame.dispose();
            new ProToolsTrainerMain(file);
        }

    }
}
