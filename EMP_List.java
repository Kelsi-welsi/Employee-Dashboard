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
        addToTable(MList);

        cmdSave = new JButton("Save Changes");
        cmdSave.addActionListener(new SaveButtonListener());

        add(table);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFillsViewportHeight(true);

        String[] completionStatus = {"Not Started","Ongoing","Completed"};
        combo = new JComboBox<String>(completionStatus);
        /*combo.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ae){
                //JOptionPane.showMessageDialog(null, combo.getSelectedItem());
                pnlCommand.add(cmdSave);
            }
        });*/

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
                    String[] item = {a.getid(), a.getDate(), a.getType(), a.getTitle(), a.getDescription(), a.getEmployees(), a.getPriority()};
                    model.addRow(item);
            }
        }
    }


    private class SaveButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //Code for sending back to MasterList here?
            pnlCommand.remove(cmdSave);
        }
    }

    private class RefreshButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //Code for sending back to MasterList here?
            pnlCommand.remove(cmdSave);
        }
    }

    private static void ShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Worksheet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EMP_List newContentPane = new EMP_List();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);

    }

    //Only needed to test
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ShowGUI();
                }
            });
    }
}
