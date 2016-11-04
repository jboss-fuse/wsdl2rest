package org.slosc.wsdl2rest.ui;

/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.service.*;
import org.slosc.wsdl2rest.Wsdl2Rest;

public class Wsdl2RestForm extends JPanel
                      implements TreeSelectionListener, ActionListener {

    private Wsdl2Rest wsdl2rest;
    private JTree serviceMethods;
    private JTable methodDetails;

    protected List<ClassDefinition> svcClasses = null;
    protected ClassDefinition clazzDef = null;
    private static String lineStyle = "Horizontal";
    protected JTextField fileName;
    protected JTextField outputLocation;
    private JFileChooser fc;
    private JButton openButton;
    private JButton generateButton;
    private JButton outputLocationSelectButton;
    private DefaultMutableTreeNode topServiceTree;

    private JComboBox resources;
	private JComboBox httpMethod;
	private JComboBox mimeType;

    private Object currentTreeNode = null;



    public Wsdl2RestForm() {

        super(new GridLayout(1,0));
        
        fileName = new JTextField();
        fileName.addActionListener(this);
        JLabel textFieldLabel = new JLabel("WSDL File Name: ");
        textFieldLabel.setLabelFor(fileName);

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel(new BorderLayout());
//        textControlsPane.add(textFieldLabel, BorderLayout.WEST);
//        textControlsPane.add(fileName, BorderLayout.CENTER);

        //Create a file chooser
        fc = new JFileChooser();
        FileFilter filter = new WSDLFileFilter();
        fc.setFileFilter(filter);
        openButton = new JButton("Browse");
        openButton.addActionListener(this);

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.X_AXIS));
        textPane.add(textFieldLabel);
        textPane.add(fileName);
        textPane.add(openButton);

        textControlsPane.add(textPane, BorderLayout.NORTH);

        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("WSDL"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));

        topServiceTree =  new DefaultMutableTreeNode("Webservice Methods");
        //Create a tree that allows one selection at a time.
        serviceMethods = new JTree(topServiceTree);
        serviceMethods.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        serviceMethods.addTreeSelectionListener(this);
        //TODO
        serviceMethods.putClientProperty("JTree.lineStyle", lineStyle);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(serviceMethods);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setLeftComponent(treeView);
        mainSplitPane.setRightComponent(addEditPane());
        mainSplitPane.setDividerLocation(300);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        splitPane.setTopComponent(textControlsPane);
        splitPane.setBottomComponent(mainSplitPane);

        Dimension minimumSize = new Dimension(300, 300);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); //XXX: ignored in some releases
                                           //of Swing. bug 4101306
        //workaround for bug 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100));

        splitPane.setPreferredSize(new Dimension(700, 400));

        //Add the split pane to this panel.
        add(splitPane);
    }

    private Component addEditPane() {
        JPanel mainEditPane = new JPanel(new BorderLayout());
        JPanel editPane = new JPanel(new GridLayout(3,2)){
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        JLabel lbl1 = new JLabel("Resources: ");
        lbl1.setLabelFor(resources);
        JLabel lbl2 = new JLabel("HTTP Method: ");
        lbl2.setLabelFor(httpMethod);
        JLabel lbl3 = new JLabel("MIME type: ");
        lbl3.setLabelFor(mimeType);
        resources = new JComboBox();
        resources.setEditable(true);
        httpMethod = new JComboBox();
        httpMethod.setEditable(true);
        mimeType = new JComboBox();
        mimeType.setEditable(true);


        editPane.add(lbl1);
        editPane.add(resources);
        editPane.add(lbl2);
        editPane.add(httpMethod);
        editPane.add(lbl3);
        editPane.add(mimeType);
        editPane.setMaximumSize(new Dimension(200, 100));
        editPane.setPreferredSize(new Dimension(200, 100));
        editPane.setBorder(new TitledBorder("REST definitions"));
        mainEditPane.add(editPane, BorderLayout.NORTH);

        // Create the table
        methodDetails = getParamTable();
        TableColumn col = methodDetails.getColumn("Path Param");
        col.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        col.setMaxWidth(col.getPreferredWidth());
        JScrollPane scrollpane= new JScrollPane(methodDetails);
        mainEditPane.add(scrollpane, BorderLayout.CENTER);

        JPanel actionPanel =  new JPanel(new BorderLayout());
        actionPanel.setBorder(new TitledBorder("Generate REST Classes"));
        JPanel outputPanel =  new JPanel(new BorderLayout());
        outputLocation = new JTextField();
        outputLocation.addActionListener(this);
        JLabel textFieldLabel = new JLabel("Output Location: ");
        textFieldLabel.setLabelFor(outputLocation);
        outputLocationSelectButton = new JButton("Browse");
        outputLocationSelectButton.addActionListener(this);
        outputPanel.add(textFieldLabel, BorderLayout.WEST);
        outputPanel.add(outputLocation, BorderLayout.CENTER);
        outputPanel.add(outputLocationSelectButton, BorderLayout.EAST);
        generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        actionPanel.add(outputPanel, BorderLayout.NORTH);
        actionPanel.add(generateButton, BorderLayout.EAST);
        mainEditPane.add(actionPanel, BorderLayout.SOUTH);
        return mainEditPane;
    }

    private JTable getParamTable(){
        // Create a model of the data.
        TableModel dataModel = new AbstractTableModel() {
            private String [] cnames ={"Param Name", "Type", "Path Param"};
            private int CSIZE = 3;
            public int getColumnCount() { return CSIZE; }
            public int getRowCount() { return getMethodParams().size();}
            public Object getValueAt(int row, int col) {
                Param p = getMethodParams().get(row);
                if(p == null) return "";
                
                return (col==0)?p.getParamName():(col==1)?p.getParamType():p.isPathParam() ;
            }
            public String getColumnName(int column) {return cnames[column];}
            public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
	        public boolean isCellEditable(int row, int col) {return col == 2;}
            public void setValueAt(Object aValue, int row, int column) {
                if(column == 2) {
                    Param p = getMethodParams().get(row);
                    if(p == null) return;
                    p.setPathParam(!p.isPathParam());
                }
            }
         };


        // Create the table
        return new JTable(dataModel);
    }


    private List<Param> getMethodParams(){
        if(currentTreeNode != null && currentTreeNode instanceof MethodInfo){
            MethodInfo method = (MethodInfo)currentTreeNode;
            return method.getParams();
        }
        return new ArrayList<Param>();
    }

    public void generateServiceTree() {
       if(fileName.getText() == null && fileName.getText().length() == 0) return;

       wsdl2rest = new Wsdl2Rest();
       wsdl2rest.process(fileName.getText(), "", "");
       svcClasses = wsdl2rest.getSvcClasses();
       topServiceTree.removeAllChildren();

       for(ClassDefinition classDef : svcClasses){
            this.clazzDef = classDef;

            if(clazzDef.getClassName() != null){

                DefaultMutableTreeNode svcClass = new DefaultMutableTreeNode(clazzDef);
                topServiceTree.add(svcClass);

                writeMethods(clazzDef.getMethods(), svcClass);
            }
        }
    }

    private void updateResourcePane(MetaInfo inf){
        //remove listners temporary 
        resources.removeActionListener(this);
        httpMethod.removeActionListener(this);
        mimeType.removeActionListener(this);
        resources.removeAllItems();
        httpMethod.removeAllItems();
        mimeType.removeAllItems();
        if(inf.getPreferredResource() != null){
            resources.addItem(inf.getPreferredResource());
            resources.setSelectedItem(inf.getPreferredResource());
        }
        for(String r: inf.getResources()){
            if(inf.getPreferredResource() != null && r.equals(inf.getPreferredResource())) continue;
            resources.addItem(r);
        }

        if(inf.getPreferredHttpMethod() != null){
            httpMethod.removeItem(inf.getPreferredHttpMethod());//if already exists
            httpMethod.addItem(inf.getPreferredHttpMethod());
            httpMethod.setSelectedItem(inf.getPreferredHttpMethod());
            if(!inf.getHttpMethod().equals(inf.getPreferredHttpMethod()))httpMethod.addItem(inf.getHttpMethod() == null?"":inf.getHttpMethod());

        }else httpMethod.addItem(inf.getHttpMethod() == null?"":inf.getHttpMethod());

        if(inf.getPreferredMimeType() != null){
            mimeType.removeItem(inf.getPreferredMimeType());//if already exists
            mimeType.addItem(inf.getPreferredMimeType());
            mimeType.setSelectedItem(inf.getPreferredMimeType());
            if(!inf.getMimeType().equals(inf.getPreferredMimeType()))mimeType.addItem(inf.getMimeType());
        }else mimeType.addItem(inf.getMimeType());

        //restore listners
        resources.addActionListener(this);
        httpMethod.addActionListener(this);
        mimeType.addActionListener(this);
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           serviceMethods.getLastSelectedPathComponent();

        if (node == null) return;

        currentTreeNode = node.getUserObject();
        if (currentTreeNode instanceof MetaInfo) {
            updateResourcePane((MetaInfo)currentTreeNode);
            methodDetails.updateUI();
        }
    }


    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        final Object src = e.getSource();
        
        if (src == openButton) {

            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if(fileName.getText().length()  > 0) fc.setCurrentDirectory(new File(fileName.getText()));
                //This is where a real application would open the file.
                fileName.setText(file.getAbsolutePath());
                generateServiceTree();
                serviceMethods.updateUI();
            } else{
                JOptionPane.showMessageDialog(this, "Please select a WSDL file..."); // ,"warning",  JOptionPane.WARNING_MESSAGE);
            }
        }else if(src == generateButton){
            if(outputLocation.getText().length() > 0)
                wsdl2rest.generateClasses(outputLocation.getText());    
        }else if(src == outputLocationSelectButton){
            JFileChooser outChooser = new JFileChooser();
            if(outputLocation.getText().length()  > 0) outChooser.setCurrentDirectory(new File(outputLocation.getText()));
            outChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            outChooser.setDialogTitle("Choose output path");
            int returnVal = outChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = outChooser.getSelectedFile();
                //This is where a real application would open the file.
                outputLocation.setText(file.getAbsolutePath());
            } else{
                JOptionPane.showMessageDialog(this, "Please select a output location ..."); 
            }
        }else if (src == resources && resources.getSelectedItem() != null) {
            ((MetaInfo)currentTreeNode).setPreferredResource((String) resources.getSelectedItem());
        }else if (src == httpMethod && httpMethod.getSelectedItem() != null) {
            ((MetaInfo)currentTreeNode).setPreferredHttpMethod((String) httpMethod.getSelectedItem());
        }else if (src == mimeType && mimeType.getSelectedItem() != null) {
            ((MetaInfo)currentTreeNode).setPreferredMimeType((String) mimeType.getSelectedItem());
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods, DefaultMutableTreeNode svcClass){
        if(methods == null) return;
        boolean visible = false;
        for(MethodInfo mInf:methods){
            DefaultMutableTreeNode method = new DefaultMutableTreeNode(mInf);
            svcClass.add(method);
        }
    }

    class WSDLFileFilter  extends FileFilter {
        public WSDLFileFilter() {
        }

        public boolean accept(File pathname) {
            return pathname.isDirectory() || pathname.isFile() && pathname.getName().endsWith(".wsdl");
        }

        public String getDescription() {
            return "WSDL Files";
        }
    }

    public static void createAndShowGUI() {
//        try {
//            UIManager.setLookAndFeel(
//                UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.err.println("Couldn't use system look and feel.");
//        }

        //Create and set up the window.
        JFrame frame = new JFrame("Wsdl2Rest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Wsdl2RestForm newContentPane = new Wsdl2RestForm();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
