package com.bass.filemanager;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by a.makarov on 06.07.2017.
 */
public class MainClass {

    /* Главная панель, которая возвращается в методе getGUI()*/
    JPanel gui;
    /* Правая часть окна, список папок и файлов в выбранной директории*/
    JList list;
    /* Левая часть панели, дерево */
    JTree tree;
    /* Скролл панели */
    JScrollPane scrollTree, scrollGrid;
    /* Контекстое меню, вызываемое ПКМ на правую часть(лист) программы */
    PopupMenu pm = new PopupMenu("Actions");
    /* Класс, методы которого используются для извлечения нужных значков и имен для файлов и папок*/
    public static FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    /* Корневая точка дерева, взят нулевой элемент в массиве, поскольку файловый менеджер для Windows и у меня
    не было других элементов в выдаваевом массиве, для других ОС возможно нужно будет доработать и импортировать
    все элементы из массива возвращаемого методом getRoots()  */
    public static DefaultMutableTreeNode root = new DefaultMutableTreeNode(fileSystemView.getRoots()[0]), node;
    /* Класс используемый для открытия файлов нужным ПО заданным в Windows по умолчанию */
    Desktop desktop = Desktop.getDesktop();

    /* Возвращает интерфейс программы */
    JPanel getGUI(){
        if(gui == null){
            gui = new JPanel(new BorderLayout(3,3));

            /* Добавляем пункт в контекстное меню и добавляем листенер для всего меню,
            поскольку один пункт, не мучаемся с конкретной идентификацией эвента и просто
            создаём диалоговое окно с кнопкой и полем для ввода текста, кнопку ставим по
            умолчанию, чтобы нажималась на Enter и пишем код, который создаёт файлы и
            убирает флаги для повторной загрузки папки, чтобы мы могли видеть новую
            папку.
             */
            pm.add(new MenuItem("Создать папку"));
            pm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog newFolderDialog = new JDialog();
                    JButton b = new JButton("Создать");
                    newFolderDialog.getRootPane().setDefaultButton(b);  // Кнопка по умолчанию для нажатия по Enter'у
                    newFolderDialog.setSize(200, 100);
                    JTextField jtf = new JTextField("Новая папка");
                    jtf.selectAll();
                    newFolderDialog.add(jtf, BorderLayout.NORTH);
                    newFolderDialog.add(b, BorderLayout.CENTER);
                    newFolderDialog.setVisible(true);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String currentDir = ((File) node.getUserObject()).getPath();
                            String folderName = jtf.getText();

                            /* Здесь алгоритм который находит совпадения имен существующих папок, если имя новой папки
                            будет совпадать с существующей, имя изменится на "Новая папка (1)", "Новая папка (2)" и т.д.
                            В общем как в винде.*/
                            int n = 1;
                            while(isSameFolderName(fileSystemView.getFiles((File)node.getUserObject(), true), folderName)){
                                if(folderName.matches(".*\\(\\d+\\)")) {
                                    for (int a = folderName.length() - 1; a > 0; a--)
                                        if (folderName.charAt(a) == '(') {
                                            folderName = folderName.substring(0, a) + "(" + n++ + ")";
                                            break;
                                        }
                                }
                                else
                                    folderName = folderName + " (" + n + ")";
                            }
                            new File(currentDir + "\\" + folderName).mkdir();
                            node.bInProgress = false;
                            node.bLoaded = false;
                            node.LoadNode(tree, list);
                            newFolderDialog.setVisible(false);
                        }
                        // Метод, проверяющий наличие файла с таким же именем как в строке name.
                        public boolean isSameFolderName(File[] files, String name){
                            for(File f : files)
                                if(f.getName().toLowerCase().equals(name.toLowerCase()))
                                    return true;
                            return false;
                        }
                    });

                }
            });
            /* Настраиваем дерево папок */
            DefaultTreeModel dTreeModel = new DefaultTreeModel(root);
            tree = new JTree(dTreeModel);
            tree.setCellRenderer(new myTreeCellRenderer());
            TreeSelectionListener tsListener = new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent tse){
                    node = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
                    node.LoadNode(tree, list);
                    gui.repaint();  // Чтобы обновить значок загрузки папки.
                }
            };
            tree.addTreeSelectionListener(tsListener);
            scrollTree = new JScrollPane(tree);

            /* Настраиваем лист в правой части окна, добавляя сюда контекстное меню, вызываемое по щелчку ПКМ.
               Также добавляем функцию открытия папки в нашем менеджере по двойному щелчку ЛКМ. */
            list = new JList();
            list.setCellRenderer(new myListCellRenderer());
            scrollGrid = new JScrollPane(list);
            list.add(pm);
            list.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if(e.getClickCount() >= 2 && e.getButton() == 1) {
                        File fileInList = (File)((JList) e.getComponent()).getSelectedValue();
                        if(fileInList.isDirectory()) {
                            tree.setSelectionPath(new TreePath(findNodeByFile(fileInList)));
                        }
                        else{
                            try { desktop.open(fileInList); }
                            catch(IOException ioe){ ioe.printStackTrace(); }
                        }
                    }
                    else if(e.getButton() == 3){
                        pm.show(list,e.getX(),e.getY());
                    }
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
            //Панель-разделитель, чтобы можно было менять размеры левой, правой части.
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, scrollGrid);
            //Расширяем левую часть.
            splitPane.setDividerLocation(300);
            gui.add(splitPane, BorderLayout.CENTER);

        }
        return gui;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Преображаем UI подходящий Windows
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch(Exception e) {
                }

                // Загружаем иконки, находящиеся внутри jar файла.
                myTreeCellRenderer.II_OPEN = new ImageIcon(getClass().getClassLoader().getResource("open_16.png"));
                myTreeCellRenderer.II_CLOSED = new ImageIcon(getClass().getClassLoader().getResource("closed_16.png"));
                myTreeCellRenderer.II_LOAD = new ImageIcon(getClass().getClassLoader().getResource("load_16.png"));

                JFrame mainF = new JFrame("File Manager");
                MainClass mc = new MainClass();
                mainF.setContentPane(mc.getGUI());
                mainF.setBounds(100, 100, 800, 480);
                mainF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainF.setLocationByPlatform(true);
                mainF.setMinimumSize(mainF.getSize());
                mainF.pack();
                mainF.setVisible(true);

                /* Выделяем первый корневой узел дерева, чтобы вызвать метод в листенере и загрузить элементы внутри
                него */
                mc.tree.setSelectionRow(0);
            }
        });

    }
    /* Метод для нахождения объекта Node по файлу, сделал его специально для того, чтобы можно было сделать двойной
    щелчок мыши, чтобы знать, какой узел развернуть(выделить).
     */
    public static DefaultMutableTreeNode findNodeByFile(File file){
        Enumeration<DefaultMutableTreeNode> ee = root.postorderEnumeration();
        ArrayList<DefaultMutableTreeNode> nodesaa = new ArrayList<DefaultMutableTreeNode>();
        while (ee.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) ee.nextElement();
            File f = (File) node.getUserObject();
            if (f.equals(file)) {
                return node;
            }
        }
        return null;

    }
}

