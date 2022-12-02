import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // This  allows you to use Action listener interface
import java.util.ArrayList;
import java.util.Arrays;

public class UserLogin extends JPanel

{

    protected JLabel username, password, Atype, cpanel;;
    protected JTextField userField, passField;
    protected ButtonGroup button;
    protected JButton login,cancel;
    protected JCheckBox aterms;
    //protected JRadioButton Manager , Employee;
    protected int radioselect = 0;
    private UserLogin thisform;
    protected static JFrame frame;
    private LoginInfo MT = new LoginInfo("TA1", "TA1", "Manager","MT");
    private LoginInfo SJT = new LoginInfo("TA2", "TA2", "Manager","SJT");
    private LoginInfo DL = new LoginInfo("TA3", "TA3", "Employee","DL");
    private LoginInfo MSJ = new LoginInfo("TA4", "TA4", "Employee","MSJ");
    private LoginInfo DC = new LoginInfo("TA5", "TA5", "Employee", "DC");
    private LoginInfo JS = new LoginInfo("TA6", "TA6", "Employee", "JS");
    private LoginInfo NF = new LoginInfo("TA7", "TA7", "Employee","NF");
    private ArrayList<LoginInfo> Logins = new ArrayList<LoginInfo>(Arrays.asList(MT, SJT, DL, MSJ, DC, JS, NF));
    
    public UserLogin()
    {
        thisform = this;
        try
        {

            JPanel iput = new JPanel();
            iput.setLayout(new GridLayout(5,3));

            username = new JLabel("Username:");
            userField = new JTextField(30);
            password = new JLabel("password:");  // this block of code is for the input panel
            passField = new JTextField(30);

            iput.add(username);
            iput.add(userField);
            iput.add(password);
            iput.add(passField);
            iput.setPreferredSize(new Dimension(350,100));

            JPanel buttonpanel = new JPanel();
            login = new JButton("Login");
            cancel = new JButton("cancel");
            Blistener blistener = new Blistener(); // creatig an instance of the BListener class
            login.addActionListener(blistener);
            cancel.addActionListener(blistener);

            buttonpanel.add(login);
            buttonpanel.add(cancel);
            buttonpanel.setPreferredSize(new Dimension(270,50));


            JPanel confirm = new JPanel();
            cpanel = new JLabel("Login Status:");
            confirm.add(cpanel);
            confirm.setPreferredSize(new Dimension(270,50));

            add(iput);
            //add(radiopanel);
            add(buttonpanel);
            add(confirm);

            
            setBackground(Color.GRAY);
            setPreferredSize(new Dimension(400,350));


        }
        catch( Exception e)
        {
            e.getMessage();
        }
    }


    private class Blistener implements ActionListener
    {   
    
       
        public void actionPerformed(ActionEvent e)
        {   
            // the getsource is used to differtiate between more than one buttons
            String username = userField.getText();
            String password = passField.getText();
            String Type = "";
            String code = "";
            int found = 0;

            if(e.getSource() == login)  
            {
                if (username.equals("") || password.equals(""))
                    cpanel.setText("Missing Field");
                else{
                    for (LoginInfo info: Logins){
                        if (info.getUsername().equals(username) && info.getPassword().equals(password)){
                            found=1;
                            Type = info.getisManager();
                            code = info.getCoder();
                        }
                    }
                    if (found==1){
                        cpanel.setText("Login Successful");
                        if (Type.equals("Employee")){
                            frame.setVisible(false);
                            EMP_List.ShowEmpGUI(code);
                        }
                        if (Type.equals("Manager")){
                            frame.setVisible(false);
                            TA_MasterList.ShowMasterGUI();
                        }
                            
                        
                    }
                    else{
                        cpanel.setText("Login Unsuccessful");
                        userField.setText("");
                        passField.setText("");
                    }
                }

                
                    
            }
            else
                System.exit(0);
            

        }
    }

    public static void main(String [] args)
    {
        UserLogin logo = new UserLogin();
        frame = new JFrame();
        frame.setTitle("Taylor Architechs login");
        frame.getContentPane().add(logo);;       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    
}
