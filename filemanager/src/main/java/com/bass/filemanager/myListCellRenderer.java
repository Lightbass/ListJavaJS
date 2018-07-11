package com.bass.filemanager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

/**
 * Created by a.makarov on 23.05.2018.
 */ // Почти тоже самое делаем для списка
class myListCellRenderer extends DefaultListCellRenderer {

    FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel labSuper = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        File file = (File) value;
        labSuper.setText(fileSystemView.getSystemDisplayName(file));
        if(!file.isDirectory()){
            labSuper.setIcon(fileSystemView.getSystemIcon(file));
        }
        else {
            DefaultMutableTreeNode node = MainClass.findNodeByFile(file);
            if(!node.inProgress())
                labSuper.setIcon(myTreeCellRenderer.II_CLOSED);
            else
                labSuper.setIcon(myTreeCellRenderer.II_LOAD);
        }
        return labSuper;

    }
}
