package com.bass.filemanager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreePath;
import java.io.File;

/**
 * Created by a.makarov on 23.05.2018.
 */ /* Я решил наследовать стандартный класс узла и добавить в него метод загрузки файлов и папок из узла, чтобы сразу
   у объекта этого класса можно было вызвать метод и на выходе сразу получить список файлов и папок и реализовать
   задержку в 2 секунды для каждого узла, чтобы их можно было сразу несколько открыть и они параллельно "загружались". */
class DefaultMutableTreeNode extends javax.swing.tree.DefaultMutableTreeNode{

    boolean bInProgress, bLoaded;
    static FileSystemView fileSystemView = MainClass.fileSystemView;

    public DefaultMutableTreeNode(Object object){
        super(object);
    }

    boolean isLoaded(){
        return bLoaded;
    }
    boolean inProgress(){
        return bInProgress;
    }
    void LoadNode(JTree tree, JList list){
        File[] newFiles = fileSystemView.getFiles((File)getUserObject(), true);
        if(!isLoaded() && !inProgress()) {
            bInProgress = true;
            new Thread() {
                long time = System.currentTimeMillis();
                @Override
                public void run() {
                    while (System.currentTimeMillis() - time < 2000L) {
                    }

                    for (File file : newFiles) {
                        if (file.isDirectory())
                            add(new DefaultMutableTreeNode(file));
                    }
                    tree.expandPath(new TreePath(getPath()));
                    tree.repaint();
                    bLoaded = true;
                    bInProgress = false;
                    list.setListData(newFiles);
                }
            }.start();

        }
        else if(isLoaded()){
            tree.expandPath(new TreePath(getPath()));
            list.setListData(newFiles);
        }

    }
}
