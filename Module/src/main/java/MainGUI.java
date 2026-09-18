import javax.swing.*;
import java.awt.*;
public class MainGUI extends JFrame {
    private JTextField idField;
    private JTextField titleField;
    private JTextField mentorField;
    private JTextField dateField;
    private JTextField locationField;
    private JTextField maxField;

    private JTextArea outputArea;

    // there should be a private member variable named `sessions` :

    private Session sessions;
    private SessionsList mySessionsList = null;

    // the constructor for the class. This will initialize
    // the class's member variables:
    public MainGUI() {
        // set sessions to a new empty list:
        // sessions = ...
        setTitle("Employee Mentorship and Inclusion Manager");
        setSize(600, 600);
        // when this frame/window closes, halt the whole program:
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createGUI();
        setVisible(true);
    }

    // Create all of the display elements in the frame:
    private void createGUI() {
        // first, the input panel contains all of the field entry elements:
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8,2,5,5));
        // these are all of the input fields that will be in the frame:
        idField = new JTextField();
        titleField = new JTextField();
        mentorField = new JTextField();
        dateField = new JTextField();
        locationField = new JTextField();
        maxField = new JTextField();
        inputPanel.add(new JLabel("Session ID"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Title"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Mentor"));
        inputPanel.add(mentorField);
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Location"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Max Participants"));
        inputPanel.add(maxField);
        add(inputPanel, BorderLayout.NORTH);

        // next, the lower half of the window contains an output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        add(scroll, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Session");
        JButton displayButton = new JButton("Display");
        JButton searchButton = new JButton("Search");
        JButton removeButton = new JButton("Remove");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addSession());
        displayButton.addActionListener(e -> displaySessions());
        searchButton.addActionListener(e -> searchSession());
        removeButton.addActionListener(e -> removeSession());
        registerButton.addActionListener(e -> registerParticipant());
        exitButton.addActionListener(e -> System.exit(0));
    }

    // set all input fields to empty strings, give focus to the first
    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        mentorField.setText("");
        dateField.setText("");
        locationField.setText("");
        maxField.setText("");
        // Put the cursor back in the first field
        idField.requestFocus();
    }

    // the action of the Add Session button
    private void addSession() {
        try {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String mentor = mentorField.getText();
            String date = dateField.getText();
            String location = locationField.getText();
            int maxParticipants = Integer.parseInt(maxField.getText());

            // TO DO: construct a session object, insert it into
            // the list of sessions
            Session mySesh= new Session(id, title, mentor, date, location, maxParticipants, 0);
            SessionsList current = mySessionsList;
            while (current != null){
                if(current.mySes().id() == id){
                    outputArea.setText("SESSION ID ALREADY EXISTS");
                    return;
                }
                current = current.rest();
            }
            //SessionsList mySessions = new SessionsList(mySesh, null);
            mySessionsList = insertByDate(mySessionsList, mySesh);
            IO.println(this.mySessionsList);
            // Print not needed, just in place to test to make sure it wokrs
            //IO.println(mySessions);
            outputArea.setText("Session Added Successfully\n");
            // Clear the input fields
            clearFields();
        }
        catch(Exception e) {
            outputArea.setText("Invalid input");
        }
    }

    private SessionsList insertByDate(SessionsList list, Session newSession){
        if (list == null){
            return new SessionsList(newSession, null);
        }
        if (newSession.date().compareTo(list.mySes().date()) < 0){
            return new SessionsList(newSession, list);
        }
        return new SessionsList(list.mySes(), insertByDate(list.rest(), newSession));
    }

    // display all sessions in the output area
    private void displaySessions() {
        // display all sessions in the output area private void displaySessions(){
        outputArea.setText("");
        // iterate over sessions; display each one
        // to the output window, using the 'append'
        // method of the outputArea

        // between each one, print a separator line,
        // as e.g.
        SessionsList current = mySessionsList;
        while (current != null) {
            Session s = current.mySes();
            outputArea.append("SESSION ID: " + s.id() + "\n");
            outputArea.append("TITLE: " + s.title() + "\n");
            outputArea.append("MENTOR " + s.mentor() + "\n");
            outputArea.append("DATE: " + s.date() + "\n");
            outputArea.append("LOCATION: " + s.location() + "\n");
            outputArea.append("MAX PARTICIPANTS: " + s.maxPar() + "\n");
            outputArea.append("CURRENT PARTICIPANTS: " + s.curPar() + "\n");
            outputArea.append("\n--------------------\n");
            current = current.rest();
        }
    }

    // search by ID if presesnt, mentor otherwise, display results
    private void searchSession() {
        // Search by ID if the ID field is not empty
        if (!idField.getText().trim().isEmpty()) {
            int id = Integer.parseInt(idField.getText().trim());
            // find session by ID, using a `searchByID` method
            // ... code here ...
            SessionsList current = mySessionsList;
            Session result = null;

            while (current != null){
                if (current.mySes().id() == id){
                    result = current.mySes();
                    break;
                }
                current = current.rest();
            }
            /* if (result != null) */
            if (result != null){
                // display session to the output area...
                outputArea.setText("SESSION ID: " + result.id() + "\n");
                outputArea.append("TITLE: " + result.title() + "\n");
                outputArea.append("MENTOR: " + result.mentor() + "\n");
                outputArea.append("DATE: " + result.date() + "\n");
                outputArea.append("LOCATION: " + result.location() + "\n");
                outputArea.append("MAX PARTICIPANTS: " + result.maxPar() + "\n");
            // else
                //outputArea.setText("Session not found.");
            } else {
                outputArea.setText("SESSION NOT FOUND.");
            }
        }
        // Otherwise, search by mentor if the Mentor field is not empty
        else if (!mentorField.getText().trim().isEmpty()) {
            String mentor = mentorField.getText().trim();
            // find session by mentor. In this case, the result
            // may be a list of sessions...
            // ... code here ...
            SessionsList current = mySessionsList;
            boolean found = false;
            outputArea.setText("");

            while (current != null) {
                Session s = current.mySes();
                if (s.mentor().equals(mentor)) {
                    outputArea.append("SESSION ID: " + s.id() + "\n");
                    outputArea.append("TITLE: " + s.title() + "\n");
                    outputArea.append("MENTOR: " + s.mentor() + "\n");
                    outputArea.append("DATE: " + s.date() + "\n");
                    outputArea.append("LOCATION: " + s.location() + "\n");
                    outputArea.append("MAX PARTICIPANTS: " + s.maxPar() + "\n");
                    outputArea.append("\n--------------------\n");
                    found = true;
                }
                current = current.rest();
            }
            if (!found) {
                outputArea.setText("NO SESSION FOUND FOR MENTOR: " + mentor);
            }
        }
        // Nothing entered
        else {
            outputArea.setText("Please enter a Session ID or Mentor Name.");
        }
    }

    // given an id, remove that session from the list   
    private void removeSession() {
       int id = Integer.parseInt(idField.getText());
       SessionsList currentList=mySessionsList;
       boolean isItFound=false;

       while(currentList!=null){
           if(currentList.mySes().id()==id){
               isItFound=true;
               break;
           }
           currentList= currentList.rest();

       }

       if(!isItFound){
           outputArea.setText("SESSION NOT FOUND");
           clearFields();

       }

       mySessionsList=removeSetID(mySessionsList, id);
       if(isItFound){
        outputArea.setText("SESSION HAS BEEN REMOVED");}
       clearFields();

    }

    // Helper function to make remove Session easier
    private SessionsList removeSetID(SessionsList myList, int id){
        if(myList==null){
            return null;
        }

        if(myList.mySes().id()==id){
            return myList.rest();
        }
        return new SessionsList(myList.mySes(), removeSetID(myList.rest(), id));
    }


    // add one to the count of the specified session.
    // MUTATES participant count of session.
    private void registerParticipant() {
        int id = Integer.parseInt(idField.getText());
        // increment participants field of session,
        // print success or failure message.
        SessionsList currList = mySessionsList;
        boolean isItFound=false;
        while(currList!=null){
            Session s = currList.mySes();
            if(s.id()==id){
                isItFound=true;
                if(s.curPar()<s.maxPar()){
                    Session updated = new Session(s.id(), s.title(), s.mentor(), s.date(), s.location(), s.maxPar(), s.curPar()+1);
                    mySessionsList= registerNewID(mySessionsList, id);
                    outputArea.setText("Participant Added Succesfully!");
                    IO.println(updated.curPar());

                }
                else if(s.curPar()==s.maxPar()){
                    outputArea.setText("Session Is Full. ");}
            }

        currList=currList.rest();
        }
        if(!isItFound){
            outputArea.setText("Session Is Not Found");
        }
    }

    // Private Helper Function to update
    private  SessionsList registerNewID(SessionsList list, int id){
        if(list == null){
            return null;
        }
        Session s = list.mySes();
        if(s.id()== id){
            Session updatedSes= new Session(s.id(), s.title(), s.mentor(), s.date(), s.location(), s.maxPar(), s.curPar() +1);
            return new SessionsList(updatedSes, list.rest());
        }
        return new SessionsList( s, registerNewID(list.rest(), id));

    }

    public static void main(String[] args) {
        new MainGUI();
    }
}
