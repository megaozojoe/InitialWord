//Program by Matthew del Real


//importing helpful packages
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;




/***
 * 
 * @author Matthew Del Real
 * @version 07/16/2023
 */


public class App extends Application{
	static BorderPane root;
	public static Words[] wordsArr = new Words[100000]; //array of all the words used
	public static int totalWords = 0; //count of the program
	
		@Override
		public void start(Stage primaryStage) { //setup for the basis of GUI
			try {
				root = new BorderPane();
				Scene scene = new Scene(root,700,100);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.setTitle("Word Counter");
				primaryStage.show();
				load();
			}catch(Exception e) {
				e.printStackTrace(); //prints when error
			}
		}
		
		private int buttonIndex = 0; //index for the button scrolling through the text
		public void load(){
			

			
			VBox vbox = new VBox(); //creates vbox
			VBox wBox = new VBox(); //this is the wordBox
			HBox bBox = new HBox();

			Label wordLabel = new Label("\nWord: " + wordsArr[buttonIndex].key + ", Count: " + wordsArr[buttonIndex].count); //This label will show the words that were processed
			Label infoLabel = new Label("Use button to scroll through the most popular words in the Raven by Edgar Allen Poe.\nSorted by most popular word first.\n"); //gives information
			Label arrayLen = new  Label( "This is " +buttonIndex +  " out of " + totalWords + " words." );
			
			infoLabel.setStyle("-fx-font-family: Arial;-fx-font-size: 1.2em; -fx-font-weight: bold;");
			wordLabel.setStyle("-fx-font-family: Arial;-fx-font-size: 1.2em; -fx-font-weight: bold;");
			arrayLen.setStyle("-fx-font-family: Arial;-fx-font-size: 1.2em; -fx-font-weight: bold;");
			
			vbox.getChildren().add(infoLabel);
			vbox.getChildren().add(arrayLen);
			vbox.getChildren().add(wordLabel);
			root.setLeft(vbox);
			
			
			Button nxtBtn = new Button("Next"); //create button
			Button backBtn = new Button("Back");
			
			nxtBtn.setOnAction(new EventHandler <ActionEvent>(){ //creates button handler
				public void handle(ActionEvent arg0) {
					if(buttonIndex != totalWords) { // this verifies that we do not overflow
						buttonIndex++; //increase the count of words
						wordLabel.setText("\nWord: " + wordsArr[buttonIndex].key + ", Count: " + wordsArr[buttonIndex].count); //changes the labels text
						arrayLen.setText("This is " +buttonIndex +  " out of " + totalWords + " words.");
						
					}
				}
			});
			
				backBtn.setOnAction(new EventHandler <ActionEvent>(){ //creates button handler
					public void handle(ActionEvent arg0) {
						if(buttonIndex != 0) { //this prevents underflow
							buttonIndex = buttonIndex - 1; //decrease the count
							wordLabel.setText("\nWord: " + wordsArr[buttonIndex].key + ", Count: " + wordsArr[buttonIndex].count); //change the label
							arrayLen.setText("This is " +buttonIndex +  " out of " + totalWords  + " words.");
						}
					}
				});
			bBox.getChildren().add(nxtBtn);
			bBox.getChildren().add(backBtn);
			root.setRight(bBox);
			
		}
	
	
    static void bubbleSort (Words arr[], int n) { //this will be to sort the array by the values highest value
    	int i, j;
    	Words temp;
    	boolean swapped;
    	
    	for (i = 0; i < n; i++) { //setting up the swap function
    		swapped = false;
    		for (j = 0; j < n - i -1; j++) { //horrible o^2 runtime however, I do not think that I am penalized for runtime
    			
    			if(arr[j].count > arr[j+ 1].count) { //comparing values
    				temp = arr[j]; //setting up temp value before swap
    				arr[j] = arr[j + 1]; //executes the switch in the array.
    				arr[j+1] = temp;
    				swapped = true;
    			}
    			
    		}
    		
    		if(swapped == false) { //if elements are not swapped break
    			break;
    		}
    		
    	}
    }
    
    static Words[] reverseArr(Words arr[], int n) { //passing the array I want to reverse that has been sorted and the length
    	Words[] temp = new Words[n]; //temp array the same length as the normal array
    	int count = 0;
    	
    	for(int i = n; i != 0; i-- ) { //starting from the back of the array lol :)
    		temp[count] = arr[i-1]; //these should be oppisistes that are going towards each other.
    		count++;
    	}
    	
    	
    	return temp;
    }
    
    
	/***
	 * 
	 * @param args for main method
	 * @throws FileNotFoundException for opening file
	 */
    public static void main(String[] args) throws FileNotFoundException { //throwing exception based off of eclipse recommendation

        
        File text = new File("/Poem.txt"); //setting file object which should be in the same folder as the actual code. 
        Scanner input = new Scanner(text, "UTF-8"); //setting scanner object
		//Words[] wordsArr = new Words[100000]; //creating the words array

		for(int z = 0; z < wordsArr.length; z++){ //intializing ever object
			wordsArr[z] = new Words();
		}
		
		
		
        
        boolean start = false;//used for the start and end of the poem
        

        while(input.hasNext()) {//checks that there is more data to process 
        	String s = input.next(); //sets to a string



			if(s.contains("class=\"chapter\"")){//checks for the start of the story
				start = true;
			}

		if(s.equals("</div><!--end")){//checks for the end of the story
				start = false;
		}

		if(start){
				if(s.startsWith("<") && s.endsWith(">")){//makes sure that this is not a html element
					continue;
				}
				else{
					s = cleanString(s);//cleans up any random characters

					boolean match = false;
					int index = 0;
					for(int i =0; i < wordsArr.length; i++){ //running through full array
						if(wordsArr[i].key.equals(s)){ // checking for matching words through whole array
							match = true;
							index = i;
							break;
						}
					}
					if(match == true){ //if a match is found. count it up
						wordsArr[index].count = wordsArr[index].count + 1;
					}
					else{ //if no match is found, then create that new object
						wordsArr[totalWords].key = s; 
						wordsArr[totalWords].count = 1; //increment
						totalWords++; //increments total words that are used
					}

				}
			}
			else{
				continue;
			}
						
        }




	String driverName = "mysql-connector-j-8.1.0";

	
	

	//This current code will print to the server that is connected. However, currently testing without the server
	try{
		
    Class.forName(driverName);
	}catch(ClassNotFoundException e){
		e.printStackTrace();
	}

	


	try{ //creates teh connection to
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/word_occurances", "root", "megahose");
	Statement statement = connection.createStatement();

	for(int s = 0; s < totalWords; s++){

	String sql =  "INSERT INTO WORD(WRD, COUNT) VALUES('" + wordsArr[s].key +"','" + wordsArr[s].count + "');" 	;
	int rowsAffected    = statement.executeUpdate(sql);



	}
	String query = "SELECT * FROM word_occurances.word order by count desc;";
	ResultSet rs = statement.executeQuery(query);
	while(rs.next()){
		String result_word = rs.getString("wrd");
		int result_count = rs.getInt("count");

		System.out.println(result_word + " " + result_count);
		
	}


	
	}catch(SQLException e){
		e.printStackTrace();
	}

	

	bubbleSort(wordsArr, totalWords); //sorts the array
	
	Words[] sortedArr = new Words[totalWords]; //creating sorted array

	
	wordsArr = reverseArr(wordsArr, totalWords); //reverse array
	
	
	for(int q = 0; q < 20; q++) { //prints top 20 results
		System.out.println(wordsArr[q].key + "       " + wordsArr[q].count);
	}
	
	
	
	launch(args);
	
	}
        
      
    /***
     * @param in is A string from the poem is passed so that it can be cleaned up and added to the array
     * @return This returns the modified string that has been clean of random chracters and excludes any html tags I have seen.
     */
    public static  String cleanString(String in) {
    	char[] rem = {'<','>','"',',','!','-',':','%','&',';','=','@', '/', '.', '\'', '?'}; //characters to remove from string
    	StringBuilder sb = new StringBuilder(in);// string builder for deleting characters
    	int counter = 0; //counts deleted characters
    	
    	for(int i  = 0; i < in.length(); i++) { //going through string
    		for(int j = 0; j < rem.length; j++) { //going through array
    			if(in.charAt(i) == rem[j]) { //checking for match
    				sb.deleteCharAt(i- counter); //deleting
    				counter++;
    			}
    		}
    	}
    	    	
    	// Deletes trouble with br tag that was sticking around on words
    	if(sb != null && !sb.isEmpty() && sb.length() > 3 ){ //checks to make sure that the modification is long enough
    		if(sb.substring(sb.length()-2, sb.length()).equals("br")) { // checks that br is there
    			sb.delete(sb.length()-2, sb.length() );
    		}
    	}
    		
 
    	
    	if(sb != null && !sb.isEmpty()) { //this checks the string builder to make sure it is not empty or null
    		if(sb.toString().equals("stylemarginleft") || sb.toString().equals("span")) { //checks if the string builder is stylemarginleft and deletes it 
    			sb.delete(0, sb.length()); //start of word and end of word
    		}
    	}
    	
    	    	
    	return sb.toString(); //returns adjusted string

    }

}



class Words{ //create a words object that has the word and count
	public String key = "";
	public int count;

	Words(){ //words constructor that sets default values
		this.key = "";
		this.count = 0;

	}

	public String toString(){ //creates string for words object
		return "Keyword:" + this.key + ", count: " + count;  
	}

}





