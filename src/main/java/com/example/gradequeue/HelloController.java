package com.example.gradequeue;

import com.google.gson.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

public class HelloController {
    @FXML
    private TextField queueTypeTextField;
    @FXML
    private TextField lastActionTextField;
    @FXML
    private TableView <Grades> gradeTableView;
    @FXML
    private TableColumn <Grades, String> nameTableColumn;
    @FXML
    private TableColumn <Grades, String> categoryTableColumn;
    @FXML
    private TableColumn <Grades, Integer> scoreTableColumn;
    @FXML
    private Tab addTab;
    @FXML
    private Tab removeTab;
    @FXML
    private Tab clearTab;
    @FXML
    private MenuItem openNormalMenuItem;
    @FXML
    private MenuItem openPriorityMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private TextField addNameTextField;
    @FXML
    private TextField addCategoryTextField;
    @FXML
    private TextField addScoreTextField;
    @FXML
    private Button addGradeButton;
    @FXML
    private Button removeGradeButton;
    @FXML
    private Button clearGradeButton;
    private Queue<Grades> normalQueue;
    private Queue<Grades> priorityQueue;

    // this method will initialize the normal and priority queue,
    // as well as set the column names in the table view
    public void initialize() {
        System.out.println ("Initialize called");
        // will set the column to hold only the name property from Grades
        // and populate it with only name values
        nameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Grades,String>("Name"));
        // will set the column to hold only the category property from Grades
        // and populate it with only category values
        categoryTableColumn.setCellValueFactory(
                new PropertyValueFactory<Grades,String>("Category"));
        // will set the column to hold only the score property from Grades
        // and populate it with only score values
        scoreTableColumn.setCellValueFactory(
                new PropertyValueFactory<Grades,Integer>("Score"));
        // initialize normalQueue as a new instance of LinkedList
        // will store Grades objects in the order they are added
        normalQueue = new LinkedList<>();
        // initializes priorityQueue as a new instance of PriorityQueue
        // will store Grades objects in priority order
        priorityQueue = new PriorityQueue<>();
    }
    // this method will read from the json file and populate the table view
    // uses File as our parameter
    private void readFromFile (File file) {
        // try method will open a new FileReader to read content in the file
        // use try catch method to ensure FileReader closes automatically after try method
        try (FileReader reader = new FileReader(file)) {
            // parse will represent JsonParser, used to parse json data read from file
            JsonParser parser = new JsonParser();
            // reads json data from reader, parses it into a json array and assigns variable
            // jsonArray to it. jsonArray holds the components in the json file as an array
            JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
            // gets the observable list reference
            ObservableList<Grades> gradesList = gradeTableView.getItems();
            // enhanced for loop will iterate through each element e within jsonArray
            for (JsonElement e : jsonArray) {
                // json object will retrieve each element in the json array
                JsonObject jObject = e.getAsJsonObject();
                // extracts name value from json object and stores it in name
                String name = jObject.get("name").getAsString();
                // extracts category value from json object and stores it in category
                String category = jObject.get("category").getAsString();
                // extracts score value from json object and stores it in score
                int score = jObject.get("score").getAsInt();
                // new Grades object created "g" which will hold the extracted data
                Grades g = new Grades (name, category, score);
                // adds extracted values into the gradesList
                gradesList.add(g);
                // adds extracted values into the normal queue
                normalQueue.add(g);
            }
            // once all data has been extracted, we will update the table view with
            // the normal queue data
            updateNQTableView();
            // prints in the output box the elements that were added into the tableview
            System.out.println("Elements in Normal Queue:");
            // enhanced for loop will iterate through the Grades object g in the normal queue
            // and print each name, category and score
            for (Grades g : normalQueue) {
                System.out.println(g.getName() + " - " + g.getCategory() + " - " + g.getScore());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // method will handle when we press the open normal queue menu item
    public void handleOpenNormalQueueMenuItem() {
        // lets us know that menu item was pressed
        System.out.println ("handleOpenNormalQueueMenuItem called");
        // set fileChooser variable as the new FileChooser method
        FileChooser fileChooser = new FileChooser();
        // sets title of file chooser dialog to Open
        fileChooser.setTitle("Open");
        // filter will display only json files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // opens the dialog to the user allowing them to choose the json file and will store
        // the selected json file into selectedFile
        File selectedFile = fileChooser.showOpenDialog(null);
        // if statement will check to see if no file was selected by the user
        // if not, then the try method will proceed
        if (selectedFile != null) {
            try {
                // if file was selected, we will call the readFromFile method on the selected file
                // which will extract the data within that json file and display it in the table view
                readFromFile(selectedFile);
                // set the queue type to normal queue to let the user know which queue type is being
                // shown in the table view
                queueTypeTextField.setText("Normal Queue");
                // set the last action to be the last action the user did which was pressing the load
                // from normal queue menu item
                lastActionTextField.setText("Load Normal Queue from JSON");
                // lets us know that the json file was successfully extracted and displayed in the table view
                System.out.println ("JSON file successfully displayed in table view as normal queue");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    // comparator method will be used to compare the grades and sort them based on score
    // will be used to sort grades in priority order for priority queue
    public class GradesComparator implements Comparator<Grades> {
        // overrides the compare method defined in comparator interface
        // will compare two grades g1, g2 and return an integer value indicating order
        @Override
        public int compare(Grades g1, Grades g2) {
            // checks if score of g1 is less than score of g2
            if (g1.getScore() < g2.getScore()) {
                // if g1 is less than g2, return positive integer 1 indicating that g1 should
                // come after g2
                return 1;
            }
            // checks if score of g1 is greater than score of g2
            else if (g1.getScore() > g2.getScore()) {
                // if g1 is greater than g2, return negative integer indicating that
                // g1 should come before g2
                return -1;
            }
            // if both scores are equal, return 0 indicating that there is no need to
            // sort either of the grades
            return 0;
        }
    }
    // this method is that same as the other read from file method, except it will be for the
    // priority queue only
    private void readFromFilePQ (File file) {
        // try method will open a new FileReader to read content in the file
        // use try catch method to ensure FileReader closes automatically after try method
        try (FileReader reader = new FileReader(file)) {
            // parse will represent JsonParser, used to parse json data read from file
            JsonParser parser = new JsonParser();
            // reads json data from reader, parses it into a json array and assigns variable
            // jsonArray to it. jsonArray holds the components in the json file as an array
            JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
            // gets the observable list reference
            ObservableList<Grades> gradesList = gradeTableView.getItems();
            // enhanced for loop will iterate through each element e within jsonArray
            for (JsonElement e : jsonArray) {
                // json object will retrieve each element in the json array
                JsonObject jObject = e.getAsJsonObject();
                // extracts name value from json object and stores it in name
                String name = jObject.get("name").getAsString();
                // extracts category value from json object and stores it in category
                String category = jObject.get("category").getAsString();
                // extracts score value from json object and stores it in score
                int score = jObject.get("score").getAsInt();
                // new Grades object created "g" which will hold the extracted data
                Grades g = new Grades (name, category, score);
                // adds extracted values into the gradesList
                gradesList.add(g);
                // adds extracted values into priority queue
                priorityQueue.add(g);
            }
            // once all the data has been extracted, we will update the table view with the
            // priority queue sorted data
            updatePQTableView();
            // prints in the output box the elements that were added into the tableview
            System.out.println("Elements in Priority Queue:");
            // enhanced for loop will iterate through the Grades object g in the normal queue
            // and print each name, category and score
            for (Grades g : priorityQueue) {
                System.out.println(g.getName() + " - " + g.getCategory() + " - " + g.getScore());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // this method will handle when we press the open priority queue menu item
    public void handleOpenPriorityQueueMenuItem() {
        // lets us know the priority queue menu item was pressed
        System.out.println ("handleOpenPriorityQueueMenuItem called");
        // set fileChooser variable as the new FileChooser method
        FileChooser fileChooser = new FileChooser();
        // sets title of file chooser dialog to Open
        fileChooser.setTitle("Open");
        // filter will display only json files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // opens the dialog to the user allowing them to choose the json file and will store
        // the selected json file into selectedFile
        File selectedFile = fileChooser.showOpenDialog(null);
        // if statement will check to see if no file was selected by the user
        // if not, then the try method will proceed
        if (selectedFile != null) {
            try {
                // creates instance of GradesComparator using variable gc
                GradesComparator gc = new GradesComparator();
                // initializes new priority queue with specified comparator gc, which calls
                // the GradesComparator method to order the elements
                priorityQueue = new PriorityQueue<>(gc);
                // we will call the readFromFilePQ method on the selected file
                // which will extract the data within that json file and display it in the table view
                readFromFilePQ(selectedFile);
                // set the queue type to priority queue to let the user know which queue type is being
                // shown in the table view
                queueTypeTextField.setText("Priority Queue");
                // set the last action to be the last action the user did which was pressing the load
                // from priority queue menu item
                lastActionTextField.setText("Load Priority Queue from JSON");
                // lets us know that the json file was successfully extracted and displayed in the table view
                System.out.println ("JSON file successfully displayed in table view as priority queue");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    // this method will update the table view with the normal queue data
    private void updateNQTableView() {
        // first we clear any data that was originally in the table view
        gradeTableView.getItems().clear();
        // then we populate it with the data from the normal queue
        gradeTableView.getItems().addAll(normalQueue);
    }
    // this method will update the table view with the priority queue data
    private void updatePQTableView() {
        // first we clear any data that was originally in the table view
        gradeTableView.getItems().clear();
        // then we populate it with the data from the priority queue
        gradeTableView.getItems().addAll(priorityQueue);
    }
    // this method will handle when the save menu item is pressed
    public void handleSaveMenuItem() {
        // lets us know that the save menu item was pressed
        System.out.println ("handleSaveMenuItem called");
        // set fileChooser variable as the new FileChooser method
        FileChooser fileChooser = new FileChooser();
        // sets title of file chooser dialog to Save
        fileChooser.setTitle("Save");
        // filter will display only json files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        // opens the dialog to the user allowing them to choose the json file to save to and will store
        // the selected json file into selectedFile
        File selectedFile = fileChooser.showOpenDialog(null);
        // if statement will check to see if no file was selected by the user
        if (selectedFile != null) {
            // if a file was selected, we will call the saveToFile method and save the data
            // into the selected file
            saveToFile(selectedFile);
        }
    }
    // this method will be saving the data into the json file
    // takes File object as a parameter
    private void saveToFile(File file) {
        // try catch will create a FileWriter object writer and initialize it with file,
        // use try catch to make sure FileWriter closes automatically after try
        try (FileWriter writer = new FileWriter(file)) {
            // create StringBuilder object jsonBuilder which will serve as a way to build
            // the json string
            StringBuilder jsonBuilder = new StringBuilder();
            // adds an open bracket followed by a new line to start us off
            // not included in for loop otherwise it would repeat
            jsonBuilder.append("[\n");
            // retrieves grades currently being displayed in the table view
            ObservableList<Grades> gradesList = gradeTableView.getItems();
            // enhanced for loop will iterate of each Grades object in gradesList
            for (Grades grade : gradesList) {
                // format of how the json text will appear in the json file
                // tab and squiggly bracket gets appended
                jsonBuilder.append("\t{")
                        // gets name and appends it to the jsonBuilder, followed by a new line
                        // need to put \ before quotes for those quotes to appear in the json file
                        .append("\"name\": \"").append(grade.getName()).append("\", \n")
                        // tab to align with name
                        // gets category and appends it to the jsonBuilder, followed by a new line
                        // need to put \ before quotes for those quotes to appear in the json file
                        .append("\t\"category\": \"").append(grade.getCategory()).append("\", \n")
                        // tab to align with name
                        // gets score and appends it to the jsonBuilder, followed by a new line
                        // need to put \ before quotes for those quotes to appear in the json file
                        .append("\t\"score\": ").append(grade.getScore())
                        // end with appending closed squiggly bracket followed by a new line
                        .append("},\n");
            }

            // checks if gradeList is not empty
            if (!gradesList.isEmpty()) {
                // removes the trailing comma and newline characters for the last item
                // - 2 to take out the comma and the newline, -1 for just taking out the newline
                jsonBuilder.setLength(jsonBuilder.length() - 2);
            }
            // once all data gets added, append the closed square bracket to finish the json format
            jsonBuilder.append("\n]");
            // writes the json string built by the jsonBuilder to the file using Filewriter
            writer.write(jsonBuilder.toString());
            // lets us know the path where the grade data was saved to
            System.out.println("Grade data saved to: " + file.getAbsolutePath());
            // sets the last action text field to be the last action the user did which was
            // saving queue to json file
            lastActionTextField.setText("Save Queue to JSON File.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // this method will handle the close menu item
    public void handleCloseMenuItem() {
        // lets us know the close menu item was pressed
        System.out.println ("handleCloseMenuItem called");
        // closes the application
        Platform.exit();
    }
    // this method will handle when the add grade button is pressed
    public void handleAddGradeButton() {
        // lets us know the add grade button was pressed
        System.out.println ("handleAddGradeButton called");
        // sets name equal to the text the user wrote in the name textfield
        String name = addNameTextField.getText();
        // sets category equal to the text the user wrote in the category textfield
        String category = addCategoryTextField.getText();
        // sets score equal to the text the user wrote in the score textfield
        int score = Integer.parseInt(addScoreTextField.getText());
        // creates new Grades object with the variables from the text fields and stores them in
        // name, category and score
        Grades grades = new Grades (name, category, score);
        // retrieves the list of items curently displayed in the table view and assigns it
        // to variable g
        ObservableList<Grades> g = gradeTableView.getItems();
        // adds the new Grades object to the list of items in the table view
        g.add(grades);
        // sets the last action text field to be the last action the user did which was
        // adding an item to the queue
        lastActionTextField.setText("Adding an item to the queue.");
        // after adding the name, category and score to the table view, clear the text fields
        addNameTextField.clear();
        addCategoryTextField.clear();
        addScoreTextField.clear();
    }
    // this method will handle when the remove grade button is pressed
    public void handleRemoveGradeButton() {
        // lets us know that the remove grade button was pressed
        System.out.println ("handleRemoveGradeButton called");
        // checks is the queue type in the text field equals priority queue
        if (queueTypeTextField.getText().equals("Priority Queue")) {
            // assigns variable removeGradePQ to the highest priority item that was removed
            Grades removeGradePQ = priorityQueue.poll();
            // calls the display alert method which will contain the info of the grade that
            // is going to be removed
            displayAlert(removeGradePQ);
            // update the table view to display the priority queue after removing a grade
            updatePQTableView();
            // sets the last action text field to be the last action the user did which was
            // removing an item from the queue
            lastActionTextField.setText("Removing an item from the queue.");
        }
        // checks if the queue type in the text field equals normal queue
        else if (queueTypeTextField.getText().equals("Normal Queue")) {
            // assigns variable removeGradeNQ to the first item in that was removed
            Grades removeGradeNQ = normalQueue.poll();
            // calls the display alert method which will contain the info of the grade that
            // is going to be removed
            displayAlert(removeGradeNQ);
            // update the table view to display the normal queue after removing a grade
            updateNQTableView();
            // sets the last action text field to be the last action the user did which was
            // removing an item from the queue
            lastActionTextField.setText("Removing an item from the queue.");
        }
        // if neither normal queue or priority queue is in the text field, return queue empty
        else {
            System.out.println ("Queue empty");
        }
    }
    // this method will contain the alert dialog box that will appear whenever a grade
    // is being removed from the queue and tells us which grade is being removed
    // takes grade as a parameter
    private void displayAlert(Grades grade) {
        // create new instance of alert and declaring the alert type to display an informational msg
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // set title of the alert window to queue update
        alert.setTitle("Queue Update");
        // set the header text to remove grade from queue
        alert.setHeaderText("Remove Grade from Queue");
        // lets use know the name, category and score that is being removed
        alert.setContentText(grade.getName() + ", " + grade.getCategory() + ", " + grade.getScore());
        // sets ok button for user to press
        alert.getButtonTypes().setAll(ButtonType.OK);
        // alert stays displayed until user either closes the dialog box or presses ok
        alert.showAndWait();
    }
    // this method will handle when the clear rade button is pressed
    public void handleClearGradeButton() {
        // lets us know the clear grade button was pressed
        System.out.println ("handleClearGradeButton called");
        // clears the table view
        gradeTableView.getItems().clear();
        // sets the last action to "Clearing the queue" to let the user know the last
        // thing they did was clear the queue
        lastActionTextField.setText("Clearing the queue.");
    }
}