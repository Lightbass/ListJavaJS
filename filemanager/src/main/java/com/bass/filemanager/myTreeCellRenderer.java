package com.bass.filemanager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Created by a.makarov on 23.05.2018.
 */ /* Наследуем дефолтный рендерер, чтобы добавить другие иконки для папок, включая иконку мнимой загрузки папки.
   А также добавляем стандартные виндовые иконки для файлов из ассоциаций Windows.
 */
class myTreeCellRenderer extends DefaultTreeCellRenderer {

    FileSystemView fileSystemView = MainClass.fileSystemView;

    public static ImageIcon II_OPEN, II_CLOSED, II_LOAD;

    private JLabel label;

    myTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        File file = (File)node.getUserObject();
        if(file.isDirectory()){
            if(expanded){
                if(node.inProgress())
                    label.setIcon(II_LOAD);
                else
                    label.setIcon(II_OPEN);
            }
            else if(leaf){
                    if(node.inProgress())
                        label.setIcon(II_LOAD);
                    else
                        label.setIcon(II_CLOSED);
            }
            else{
                label.setIcon(II_CLOSED);
            }
        }
        else label.setIcon(fileSystemView.getSystemIcon(file));


        label.setText(fileSystemView.getSystemDisplayName(file));
        label.setToolTipText(file.getPath());
        label.setBackground(selected ? backgroundSelectionColor : backgroundNonSelectionColor );
        label.setForeground(selected ? textSelectionColor : textNonSelectionColor);

        return label;
    }
}
