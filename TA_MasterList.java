import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class TA_MasterList extends JPanel{
    private JScrollPane scrollPane;
    private TableRowSorter sorter;
    private JTable table;
    private DefaultTableModel model;
    private JPanel pnlCommand;
    private JPanel pnlEntry;
    private JTextField txtID;
    private JTextField txtSearch;
    private JTextField txtDate;
    //private JTextField txtType;
    private JCheckBox[] Employees;
    private JComboBox<String> Type;
    private JComboBox<String> Priority;
    private JTextField txtTitle;
    private JTextArea txtDes;
    //private JTextField txtPriority;
    private String[] names = {"DL","MSJ","DC","JS","NF"};
    private String[] types = {"","Task", "Appointment"};
    private String[] priority = {"","High", "Medium", "Low"};
    public int tablePos = -1;
    public int listPos = -1;
    public int AddOrUpdate = 0;
    private JButton cmdAdd;
    private JButton cmdDel;
    private JButton cmdUp;
    private JButton cmdSend;
    private JButton cmdCancel;
    public static int IDcount = 1;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    private ArrayList<Item> MList = new ArrayList<>();
    public TA_MasterList() {
        
        pnlCommand = new JPanel();
        pnlEntry = new JPanel();
        String[] columnNames = { "ID", "Date", "Type", "Title", "Description", "Employee","Priority",};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        addToTable(MList);

        add(table);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFillsViewportHeight(true);
        

        scrollPane = new JScrollPane(table);

        //String[] completionStatus = {"Not Started","Ongoing","Completed"};


        pnlEntry.add(new JLabel("ADD WORKSHEET ITEM"));

        pnlEntry.add(new JLabel("ID [Auto Generated]:"));
        txtID = new JTextField(5);
        txtID.setText(String.valueOf(IDcount));
        txtID.setEditable(false);
        pnlEntry.add(txtID);

        pnlEntry.add(new JLabel("Date:"));
        txtDate = new JTextField(20);
        pnlEntry.add(txtDate);

        pnlEntry.add(new JLabel("Type:"));
        Type = new JComboBox<>(types);
        pnlEntry.add(Type);

        pnlEntry.add(new JLabel("Title:"));
        txtTitle = new JTextField(20);
        pnlEntry.add(txtTitle);

        pnlEntry.add(new JLabel("Description:"));
        txtDes = new JTextArea();
        pnlEntry.add(txtDes);

        pnlEntry.add(new JLabel("Employee:"));
        Employees = new JCheckBox[names.length];
        for (int i = 0; i < names.length; i++) {
            Employees[i] = new JCheckBox(names[i]);
            pnlEntry.add(Employees[i]);
        }

        pnlEntry.add(new JLabel("Priority:"));
        Priority = new JComboBox<>(priority);
        pnlEntry.add(Priority);
        

        cmdAdd = new JButton("Add Item");
        cmdAdd.addActionListener(new AddButtonListener());
        cmdAdd.setSize(100,100);
        pnlEntry.add(cmdAdd);

        cmdDel = new JButton("Delete Item");
        cmdDel.addActionListener(new DelButtonListener());
        pnlCommand.add(cmdDel);

        cmdUp = new JButton("Update Item");
        cmdUp.addActionListener(new UpdateButtonListener());
        pnlCommand.add(cmdUp);

        cmdSend = new JButton("Send to Employees");
        cmdSend.addActionListener(new SendButtonListener());
        pnlCommand.add(cmdSend);

        pnlCommand.add(new JLabel("Search:"));
        txtSearch = new JTextField(20);
        pnlCommand.add(txtSearch);


        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               search(txtSearch.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
               search(txtSearch.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               search(txtSearch.getText());
            }
            public void search(String str) {
               if (str.length() == 0) {
                  sorter.setRowFilter(null);
               } else {
                  sorter.setRowFilter(RowFilter.regexFilter(str));
               }
            }
         });



        pnlEntry.setLayout(new GridLayout(0,1));
        
        setBorder( new EmptyBorder(5, 10, 10, 10) );
        setLayout(new BorderLayout(20,5));
        add(scrollPane, BorderLayout.CENTER);
        add(pnlEntry, BorderLayout.EAST);
        add(pnlCommand,BorderLayout.NORTH);

    }

    private void addToTable(ArrayList<Item> aList){
        if (aList.size() > 0) {
            for (Item a : aList){
                    String[] item = {a.getid(), a.getDate(), a.getType(), a.getTitle(), a.getDescription(), a.getEmployees(), a.getPriority()};
                    model.addRow(item);
            }
        }
    }

    /*private ArrayList<Item> loadAppointments() {
    
    }*/

    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            String id;
            String date;
            String type;
            String title;
            String employee="";
            String description;
            String priority;
            String error="";

            try{    
                id = txtID.getText();
                date = txtDate.getText();
                type = Type.getItemAt(Type.getSelectedIndex());
                title = txtTitle.getText();
                description = txtDes.getText();
                priority = Priority.getItemAt(Priority.getSelectedIndex());
                int first = 0;
                for (int i = 0; i < names.length; i++) {
                    if (Employees[i].isSelected())
                    {
                        if (first==0)
                        {
                            employee+=names[i];
                            first++;
                        }
                        else{
                            employee+="," + names[i];
                        }
                    }       
                }

                if (date.equals("") && type.equals("") && title.equals("") && description.equals("") && employee.equals("") && priority.equals("")){
                    error = "empty";
                    throw new Exception();
                }
                    
            
                //if (id.equals(""))
                //    id = "-";
                if (date.equals(""))
                    date = "-";
                if (type.equals("")){
                    error = "emptyType";
                    throw new Exception();
                }
                if (title.equals(""))
                    title = "-";
                if (description.equals(""))
                    description = "-";
                if (employee.equals(""))
                    employee = "-";
                if (priority.equals("")){
                    error = "emptyPriority";
                    throw new Exception();
                }
                    

            //if (date.equals("-") && project.equals("-") && attendees.equals("-") && location.equals("-") && purpose.equals("-") && comments.equals("-")){
                
                Item app = new Item(id, date, type, title, description, employee, priority);

            
                if (AddOrUpdate==0)
                    MList.add(app);
                else if (AddOrUpdate==1){
                    if (listPos!=-1){
                        MList.remove(listPos); 
                        MList.add(listPos, app);
                    }  
                    model.removeRow(tablePos);
                }
                    
                String[] item = {id, date, type, title, description, employee, priority};

                if (AddOrUpdate==0)
                    model.addRow(item);
                else if (AddOrUpdate==1)
                    model.insertRow(tablePos, item);

                if (AddOrUpdate==0)
                    IDcount+=1;
                txtID.setText(String.valueOf(IDcount));
                txtDate.setText("");
                Type.setSelectedIndex(0);
                txtTitle.setText("");
                txtDes.setText("");
                Priority.setSelectedIndex(0);
                for (int i = 0; i < names.length; i++) {
                    Employees[i].setSelected(false);
                }
                if (AddOrUpdate==1){
                    pnlEntry.remove(cmdCancel);
                    cmdAdd.setText("Add Appointment");
                    pnlCommand.add(cmdDel);
                    pnlCommand.add(cmdUp);
                }
                AddOrUpdate = 0;
            }
            catch (Exception exc){
                if (error.equals("empty"))
                    JOptionPane.showMessageDialog(null, "No Data Was Entered");
                if (error.equals("emptyType"))
                    JOptionPane.showMessageDialog(null, "No Type was Selected");
                if (error.equals("emptyPriority"))
                    JOptionPane.showMessageDialog(null, "No Priority was Selected");
            } 
        }
    }

    private class DelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                int pos = table.getSelectedRow();
                String id = model.getValueAt(pos, 0).toString();
                String date = model.getValueAt(pos, 1).toString();
                String type = model.getValueAt(pos, 2).toString();
                String title = model.getValueAt(pos, 3).toString();
                String description = model.getValueAt(pos, 4).toString();
                String employee = model.getValueAt(pos, 5).toString();
                String priority = model.getValueAt(pos, 6).toString();
                model.removeRow(pos);
                Item delData = new Item(id, date, type, title, description, employee, priority);
                int delPos=-1;
                for (Item a:MList){
                    if (a.getid()==delData.getid() /*&& a.getDate().equals(delData.getDate()) && a.getType().equals(delData.getType()) 
                        && a.getTitle().equals(delData.getTitle()) && a.getDescription().equals(delData.getDescription()) && a.getEmployees().equals(delData.getEmployees()) && a.getPriority().equals(delData.getPriority())*/){
                            delPos = MList.indexOf(a);
                    }
                }
                if (delPos >=0)
                    MList.remove(delPos);
            }
        
        }
    }

    private class UpdateButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                AddOrUpdate=1;
                cmdAdd.setText("Update");
                pnlCommand.remove(cmdDel);
                pnlCommand.remove(cmdUp);
                cmdCancel = new JButton("Cancel");
                cmdCancel.addActionListener(new CancelButtonListener());
                pnlEntry.add(cmdCancel);

                int pos = table.getSelectedRow();
                String id = model.getValueAt(pos, 0).toString();
                String date = model.getValueAt(pos, 1).toString();
                String type = model.getValueAt(pos, 2).toString();
                String title = model.getValueAt(pos, 3).toString();
                String description = model.getValueAt(pos, 4).toString();
                String employee = model.getValueAt(pos, 5).toString();
                String priority = model.getValueAt(pos, 6).toString();
                //model.removeRow(pos);
                Item delData = new Item(id, date, type, title, description, employee, priority);
                //cacheApp = upData;
                int upPos=-1;
                for (Item a:MList){
                    if (a.getid()==delData.getid() && a.getDate().equals(delData.getDate()) && a.getType().equals(delData.getType()) 
                        && a.getTitle().equals(delData.getTitle()) && a.getDescription().equals(delData.getDescription()) && a.getEmployees().equals(delData.getEmployees()) && a.getPriority().equals(delData.getPriority())){
                            upPos = MList.indexOf(a);
                    }
                }
                tablePos=pos;
                listPos=upPos;
                txtID.setText(id);
                txtDate.setText(date);
                Type.setSelectedIndex(Arrays.asList(types).indexOf(type));
                txtTitle.setText(title);
                
                if (description.equals("-"))
                    txtDes.setText("");
                else
                    txtDes.setText(description);

                String[] selEmployees = employee.split(",");
                for (String i:selEmployees) {
                    for (JCheckBox a:Employees){
                        if (i.equals(a.getText()))
                            a.setSelected(true);
                    }
                }
                Priority.setSelectedIndex(Arrays.asList(TA_MasterList.this.priority).indexOf(priority));
            }
        }
    }    

    private class CancelButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //txtID.setText("");
            txtDate.setText("");
            Type.setSelectedIndex(0);
            txtTitle.setText("");
            txtDes.setText("");
            Priority.setSelectedIndex(0);
            for (int i = 0; i < names.length; i++) {
                Employees[i].setSelected(false);
            }
            pnlEntry.remove(cmdCancel);
            cmdAdd.setText("Add Appointment");
            pnlCommand.add(cmdDel);
            pnlCommand.add(cmdUp);
        }
    }

    private class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            ArrayList<Item> DL = new ArrayList<>();
            ArrayList<Item> MSJ = new ArrayList<>();
            ArrayList<Item> DC = new ArrayList<>();
            ArrayList<Item> JS = new ArrayList<>();
            ArrayList<Item> NF = new ArrayList<>();
            for (Item i: MList){
                if (i.getEmployees().contains("DL"))
                    DL.add(i);

                if (i.getEmployees().contains("MSJ"))
                    MSJ.add(i);

                if (i.getEmployees().contains("DC"))
                    DC.add(i);

                if (i.getEmployees().contains("JS"))
                    JS.add(i);

                if (i.getEmployees().contains("NF"))
                    NF.add(i);
            }

            //Code for sedning to server here?
        }
    }

    private static void ShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Master List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TA_MasterList newContentPane = new TA_MasterList();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ShowGUI();
                }
            });
    }
}
