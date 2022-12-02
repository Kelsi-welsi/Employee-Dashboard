import javax.swing.*;
import java.sql.*;
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
import java.net.*;
import java.io.*;
import java.sql.*;
import java.sql.DriverManager;

public class TA_MasterList extends JPanel{
    //Handling the Server    
    //catch(IOException i)
    //    System.out.println(i);

    private JScrollPane scrollPane;
    private TableRowSorter sorter;
    public TableColumn col;
    public JComboBox combo;
    private JTable table;
    private DefaultTableModel model;
    private JPanel pnlCommand;
    public JLabel search = new JLabel("Search:");
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
    public String status= "";
    public static int IDcount = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    private ArrayList<Item> MList = new ArrayList<>();

    public TA_MasterList() {
        
        pnlCommand = new JPanel();
        pnlEntry = new JPanel();
        String[] columnNames = { "ID", "Date", "Type", "Title", "Description", "Employee","Priority","Status"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        MList = LoadRecords();
        addToTable(MList);

        add(table);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFillsViewportHeight(true);
        
        String[] completionStatus = {"Not Started","Ongoing","Completed"};
        combo = new JComboBox<String>(completionStatus);
        /*combo.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ae){
                int a = table.getSelectedRow();
                //JOptionPane.showMessageDialog(null, combo.getSelectedItem());
                JOptionPane.showMessageDialog(null, a);
            }
        });*/

        

        col = table.getColumnModel().getColumn(7);
        col.setCellEditor(new DefaultCellEditor(combo) );

        scrollPane = new JScrollPane(table);

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

        cmdSend = new JButton("Send to Employees");
        cmdSend.addActionListener(new SendButtonListener());
        pnlCommand.add(cmdSend);

        cmdDel = new JButton("Delete Item");
        cmdDel.addActionListener(new DelButtonListener());
        pnlCommand.add(cmdDel);

        cmdUp = new JButton("Update Item");
        cmdUp.addActionListener(new UpdateButtonListener());
        pnlCommand.add(cmdUp);

        pnlCommand.add(search);
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
                if (Integer.parseInt(ID)>IDcount)
                    IDcount = Integer.parseInt(ID);
                    
            }
            IDcount = IDcount + 1; 
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

    public void AddRecord(Item item){
        Connection conn= null;
        PreparedStatement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database");
            conn = DriverManager 
            .getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282","sql9581282","BABN2FhxnM");
            System.out.println("You are now connected to the database");
            System.out.println("Inserting data or recrods");
            stmt = conn.prepareStatement("INSERT INTO TA_MasterList VALUE (?,?,?,?,?,?,?,?)");
            stmt.setString(1, item.getid());
            stmt.setString(2, item.getDate());
            stmt.setString(3, item.getTitle());
            stmt.setString(4, item.getType());
            stmt.setString(5, item.getDescription());
            stmt.setString(6, item.getEmployees());
            stmt.setString(7, item.getPriority());
            stmt.setString(8, item.getStatus());
            
            stmt.executeUpdate();

            System.out.println("Inserted record to table");
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
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
  
    public void DeleteRecord(Item item){
        Connection conn= null;
        PreparedStatement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database");
            conn = DriverManager 
            .getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282","sql9581282","BABN2FhxnM");
            System.out.println("You are now connected to the database");
            System.out.println("Inserting data or recrods");
            stmt = conn.prepareStatement("DELETE FROM TA_MasterList WHERE ID = ?");
            stmt.setString(1, item.getid());
            stmt.executeUpdate();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
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

    public void UpdateRecord(Item item){
        Connection conn= null;
        PreparedStatement stmt = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Connecting to database");
            conn = DriverManager 
            .getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9581282","sql9581282","BABN2FhxnM");
            //System.out.println("You are now connected to the database");
            //System.out.println("Inserting data or recrods");

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Date=? WHERE ID=?");
            stmt.setString(1, item.getDate());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Name=? WHERE ID=?");
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Type=? WHERE ID=?");
            stmt.setString(1, item.getType());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Description=? WHERE ID=?");
            stmt.setString(1, item.getDescription());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Employees=? WHERE ID=?");
            stmt.setString(1, item.getEmployees());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Priority=? WHERE ID=?");
            stmt.setString(1, item.getPriority());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE TA_MasterList SET Status=? WHERE ID=?");
            stmt.setString(1, item.getStatus());
            stmt.setString(2, item.getid());
            stmt.executeUpdate();

            System.out.println("Record Updated");
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
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
                String employee = model.getValueAt(i, 5).toString();
                String priority = model.getValueAt(i, 6).toString();
                String stat = model.getValueAt(i, 7).toString();
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

    private void addToTable(ArrayList<Item> aList){
        if (aList.size() > 0) {
            for (Item a : aList){
                    String[] item = {a.getid(), a.getDate(), a.getType(), a.getTitle(), a.getDescription(), a.getEmployees(), a.getPriority(), a.getStatus()};
                    model.addRow(item);
            }
        }
    }

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
                            employee+=" , " + names[i];
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
                if (AddOrUpdate==0)
                    status = "Not Started";

                Item app = new Item(id, date, type, title, description, employee, priority, status);

            
                if (AddOrUpdate==0)
                    MList.add(app);

                else if (AddOrUpdate==1){
                    if (listPos!=-1){
                        MList.remove(listPos); 
                        MList.add(listPos, app);
                    }  
                    model.removeRow(tablePos);
                }

                //if (AddOrUpdate==0)
                //    status = "Select...";

                String[] item = {id, date, type, title, description, employee, priority, status};

                if (AddOrUpdate==0){
                    model.addRow(item);
                    AddRecord(app);
                }
                    
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
                    cmdAdd.setText("Add Item");
                    pnlCommand.add(cmdSend);
                    pnlCommand.add(cmdDel);
                    pnlCommand.add(cmdUp);
                    pnlCommand.add(search);
                    pnlCommand.add(txtSearch);
                    
                }

                if (AddOrUpdate==0){
                    AddRecord(app);
                }

                if (AddOrUpdate==1){
                    UpdateRecord(app);
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
                String status = model.getValueAt(pos, 7).toString();

                model.removeRow(pos);
                Item delData = new Item(id, date, type, title, description, employee, priority, status);
                int delPos=-1;
                for (Item a:MList){
                    if (a.getid()==delData.getid() /*&& a.getDate().equals(delData.getDate()) && a.getType().equals(delData.getType()) 
                        && a.getTitle().equals(delData.getTitle()) && a.getDescription().equals(delData.getDescription()) && a.getEmployees().equals(delData.getEmployees()) && a.getPriority().equals(delData.getPriority())*/){
                            delPos = MList.indexOf(a);
                    }
                }
                if (delPos >=0)
                    MList.remove(delPos);
                    DeleteRecord(delData);
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
                pnlCommand.remove(cmdSend);
                pnlCommand.remove(txtSearch);
                pnlCommand.remove(search);

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
                status = model.getValueAt(pos, 7).toString();
                //model.removeRow(pos);
                Item delData = new Item(id, date, type, title, description, employee, priority, status);
                //cacheApp = upData;
                int upPos=-1;
                for (Item a:MList){
                    if (a.getid()==delData.getid()){
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

                String[] selEmployees = employee.split(" , ");
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
            pnlCommand.add(cmdSend);
            pnlCommand.add(cmdDel);
            pnlCommand.add(cmdUp);
            pnlCommand.add(search);
            pnlCommand.add(txtSearch);
        }
    }

    private class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //Code for sedning to server here?
            UpdateStatuses();
        }
    }


    private static void ShowMasterGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Master List");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        TA_MasterList newContentPane = new TA_MasterList();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent we)
            { 
                String ObjButtons[] = {"Yes","No"};
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
        });

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        //javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //        public void run() {
        //            ShowGUI();
        //        }
        //   });
        ShowMasterGUI();
    }
}
