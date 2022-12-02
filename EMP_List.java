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
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.sql.DriverManager;

public class EMP_List extends JPanel{
    private TableRowSorter sorter;
    private JScrollPane scrollPane;
    public TableColumn col;
    public JComboBox combo;
    private JTable table;
    private DefaultTableModel model;
    private JPanel pnlCommand;
    private JPanel pnlEntry;
    private JTextField txtSearch;
    private JButton cmdSave;
    private JButton cmdRef;
    public int tablePos = -1;
    public int listPos = -1;
    public static int SavedAlready = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    private ArrayList<Item> MList = new ArrayList<>();
    public EMP_List() {
        
        pnlCommand = new JPanel();
        pnlEntry = new JPanel();
        String[] columnNames = { "ID", "Date", "Type", "Title", "Description", "Priority", "Status"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        MList = LoadRecords();
        addToTable(MList);

        cmdSave = new JButton("Save Changes");
        cmdSave.addActionListener(new SaveButtonListener());
        pnlCommand.add(cmdSave);
        cmdSave.setVisible(false);

        add(table);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFillsViewportHeight(true);

        String[] completionStatus = {"Not Started","Ongoing","Completed"};
        combo = new JComboBox<String>(completionStatus);
        combo.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ae){
                //JOptionPane.showMessageDialog(null, combo.getSelectedItem());
                cmdSave.setVisible(true);
                
            }
        });

        col = table.getColumnModel().getColumn(6);
        col.setCellEditor(new DefaultCellEditor(combo) );
        
        scrollPane = new JScrollPane(table);

        cmdRef = new JButton("Refresh");
        cmdRef.addActionListener(new RefreshButtonListener());
        pnlCommand.add(cmdRef);

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
        add(pnlCommand,BorderLayout.NORTH);

    }



    private void addToTable(ArrayList<Item> aList){
        if (aList.size() > 0) {
            for (Item a : aList){
                    String[] item = {a.getid(), a.getDate(), a.getType(), a.getTitle(), a.getDescription(),a.getPriority(),a.getStatus()};
                    model.addRow(item);
            }
        }
    }

    public void UpdateStatuses(){
        Connection conn= null;
        PreparedStatement stmt = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database");
            conn = DriverManager 
            .getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282","sql9581282","BABN2FhxnM");
            //System.out.println("You are now connected to the database");
            //System.out.println("Inserting data or recrods");

            int rowCount = model.getRowCount();
            ArrayList<Item> List_Copy = new ArrayList<>();
            for (int i=0; i<rowCount; i++){

                String id = model.getValueAt(i, 0).toString();
                String date = model.getValueAt(i, 1).toString();
                String type = model.getValueAt(i, 2).toString();
                String title = model.getValueAt(i, 3).toString();
                String description = model.getValueAt(i, 4).toString();
                String employee = "";
                String priority = model.getValueAt(i, 5).toString();
                String stat = model.getValueAt(i, 6).toString();
                Item item = new Item(id, date, type, title, description, employee, priority, stat);
                List_Copy.add(item);
            }

            for(Item x:List_Copy){
                stmt = conn.prepareStatement("UPDATE TA_MasterList SET Status=? WHERE ID=?");
                stmt.setString(1, x.getStatus());
                stmt.setString(2, x.getid());
                stmt.executeUpdate();
            }
 
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            try{
                if (stmt!=null);
                    conn.close();

            }catch(SQLException se){

            }
            try{
                if (conn!=null);
                    conn.close();
                 
            }catch(SQLException se){
            }
        }
    }

    public ArrayList<Item> LoadRecords(){
        Connection conn= null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Item> DB_List = new ArrayList<>(); 
        

        try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database");
            conn = DriverManager 
            .getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282","sql9581282","BABN2FhxnM");
            //System.out.println("You are now connected to the database");
            //System.out.println("Inserting data or recrods");
            stmt = conn.createStatement();
            

            String sql = "SELECT * FROM TA_MasterList";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String ID = rs.getString("ID");
                String Date = rs.getString("Date");
                String Title = rs.getString("Name");
                String Type = rs.getString("Type");
                String Description = rs.getString("Description");
                String Employees = rs.getString("Employees");
                String Priority = rs.getString("Priority");
                String Status = rs.getString("Status");
                
                Item item = new Item(ID, Date, Type, Title, Description, Employees, Priority, Status);
                DB_List.add(item); 
                
                    
            }
            
            //System.out.println("Everything has been read");
        }
        catch(SQLException se){
            JOptionPane.showMessageDialog(null, "An Error has occured connecting to Database server");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "An Error has occured connecting to Database server");
        }
        finally{
            try{
                if (stmt!=null);
                    conn.close();

            }catch(SQLException se){

            }
            try{
                if (conn!=null);
                    conn.close();

            }catch(SQLException se){

            }
        }
        return DB_List;
    }

    private class SaveButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            UpdateStatuses();
            SavedAlready=1;
            JOptionPane.showMessageDialog(null, "Saved");
            cmdSave.setVisible(false);
        }
    }

    private class RefreshButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //Code for sending back to MasterList here?
            pnlCommand.remove(cmdSave);
        }
    }

    private static void ShowEmpGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Worksheet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EMP_List newContentPane = new EMP_List();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent we)
            { 
                String ObjButtons[] = {"Yes","No"};
                if (SavedAlready==1)
                    newContentPane.UpdateStatuses();
                else{
                    int PromptResult = JOptionPane.showOptionDialog(null,"Would you like to Save Before Quitting?","Online Examination System",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                    if(PromptResult==JOptionPane.YES_OPTION)
                    {
                        newContentPane.UpdateStatuses();
                        System.exit(0);
                    }
                    if(PromptResult==JOptionPane.NO_OPTION)
                    {
                        System.exit(0);
                    }
                }
            }   
        });

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);


        frame.pack();
        frame.setVisible(true);

    }

    //Only needed to test
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ShowEmpGUI();
                }
            });
    }
}
