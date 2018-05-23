package com.callname.project;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class CallNameFrame extends JFrame{
	private static final long serialVersionUID = 1L;
    private static int MAX_WIDTH = 800, MAX_HEIGHT = 800;
    private JFileChooser mFileChooser;
    private JButton mStartButton, mStopButton;
    private JLabel mNameText;
    private Map<String, List<String>> students = new HashMap<String, List<String>>();
    private List<String> allStudents = new ArrayList<String>();
    private static boolean isStart = false;
    private static String callName = "开始";
    private static int lastRadomStudent = -1;
    private boolean isSpeak = false;
    public CallNameFrame() {
        init();
    }
    private void init() {
        setLayout(null);
        setTitle("点名系统");
        this.setResizable(false);
        this.setSize(MAX_WIDTH, MAX_HEIGHT);
        setLocation(400, 50);
        JMenu menu = new JMenu("File");
        JMenuItem opemItem = new JMenuItem("Open");
        opemItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        menu.add(opemItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(exitItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
        mNameText = new JLabel("开始");
        mNameText.setBounds(130, 10, 670, 640);
        mNameText.setFont(new Font("宋体", Font.BOLD, 120));
        mNameText.setHorizontalAlignment(SwingConstants.CENTER);
        add(mNameText);
        mStartButton = new JButton("开始");
        mStopButton = new JButton("停止");
        mStartButton.setEnabled(false);
        mStopButton.setEnabled(false);
        mStartButton.setBounds(360, 680, 80, 30);
        mStopButton.setBounds(440, 680, 80, 30);
        mStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mStopButton.setEnabled(true);
                mStartButton.setEnabled(false);
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws InterruptedException {
                        isStart = true;
                        do{
                            isSpeak = false;
                            if (allStudents != null && (allStudents.size() > 0)) {
                                int radomStudent = (int) (Math.random() * allStudents.size());
                                if(radomStudent==lastRadomStudent) {
                                    continue;
                                }
                                System.out.println("index = " + radomStudent);
                                Thread.sleep(50);
                                lastRadomStudent = radomStudent;
                                callName = allStudents.get(radomStudent);
                                System.out.println("callName = " + callName);
                                mNameText.setText(callName);
                            } else {
                                isStart = false;
                            }
                        }while (isStart); 
                        return callName;
                    }
                    public void done() {
                        lastRadomStudent = -1;
                        try {
                            if(allStudents.size()>0) {
                                callName = get();
                                String name = getClassName(callName)+ callName;
                                mNameText.setText(name);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(isSpeak) {
                                            VoiceUtils.strat(name,0);
                                        }
                                    }
                                }).start();
                                removeStudent(callName);
                                refreshDisplay();
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
            }
        });
        mStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStart = false;
                isSpeak = true;
                mStopButton.setEnabled(false);
                mStartButton.setEnabled(true);
            }
        });
        add(mStartButton);
        add(mStopButton);
    }

    private String getClassName(String callName) {
        String className = "";
        if (students != null && !students.isEmpty()) {
            Iterator<String> keys = students.keySet().iterator();
            while (keys.hasNext()) {
                className = keys.next();
                List<String> name = students.get(className);
                if (name.contains(callName)) {
                    return className;
                }
            }
        }
        return className;
    }

    private void removeStudent(String callName) {
        String className = "";
        List<String> tempNames = new ArrayList<String>();
        if (students != null && !students.isEmpty()) {
            Iterator<String> keys = students.keySet().iterator();
            while (keys.hasNext()) {
                className = keys.next();
                List<String> name = students.get(className);
                if(name.contains(callName)) {
                    name.remove(callName);
                    tempNames.clear();
                    tempNames.addAll(name);
                    name.clear();
                    students.get(className).addAll(tempNames);
                    allStudents.clear();
                    return;
                }
            }
        }
        return;
    }

    private void refreshDisplay() {
        if (students != null && students.isEmpty()) {
            return;
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("点名册");
        Iterator<String> keys = students.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            allStudents.addAll(students.get(key));
            System.out.println("=====name :" +allStudents);
            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(key);
            for (String list : students.get(key)) {
                node2.add(new DefaultMutableTreeNode(list));
            }
            top.add(node2);
        }
        JTree tree = new JTree(top);
        JScrollPane treePanel = new JScrollPane(tree);
        treePanel.setBounds(10, 10, 120, 640);
        add(treePanel);
        expandAll(tree,new TreePath(top),true);
        validate();
    }

    private void openFile() {
        mFileChooser = new JFileChooser();
        mFileChooser.setCurrentDirectory(new File("csv"));

        mFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".csv");
            }

            public String getDescription() {
                return "CSV Files";
            }
        });

        int r = mFileChooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File file = mFileChooser.getSelectedFile();
        new SwingWorker<Map<String, List<String>>, Void>() {
            @Override
            protected Map<String, List<String>> doInBackground() throws Exception {
                return FileUtils.parseStudent(file);
            }
            public void done() {
                try {
                    students = get();
                    if (students.isEmpty()) {
                        return;
                    }
                    refreshDisplay();
                    mStartButton.setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CallNameFrame.this, e.getMessage());
                }
            }
        }.execute();
    }
    @SuppressWarnings("rawtypes")
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
