package jfs.datagenerator.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lpuddu on 7-12-2015.
 */
public class GitHubDirectoryChooser {
    JFileChooser chooser;
    String choosertitle = "Select folder";

    public GitHubDirectoryChooser() {

    }

    public String showDialog() {
        String directory = "";

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            directory = chooser.getSelectedFile().getAbsolutePath();
        }

        return directory;
    }
}
