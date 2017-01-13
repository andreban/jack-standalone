package com.classyshark.github;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import static com.classyshark.github.JFilePicker.MODE_OPEN;

public class JackPanel extends JFrame {

    private JFilePicker srcFilePicker = new JFilePicker("Pick sources root:", "browse");
    private JFilePicker classpathFilePicker = new JFilePicker("Pick classpath root:", "browse");

    private JButton buttonCompile = new JButton("Compile");

    public JackPanel() {
        super("Jack Standalone Compiler");

        srcFilePicker.setMode(MODE_OPEN);
        srcFilePicker.addFileTypeFilter("jar", "jars");

        classpathFilePicker.setMode(MODE_OPEN);
        classpathFilePicker.addFileTypeFilter("jar", "jars");

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(srcFilePicker, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(classpathFilePicker, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonCompile, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;

        newPanel.add(new TextArea(20,60), constraints);


        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Jack Compiler"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JackPanel().setVisible(true);
            }
        });
    }
}