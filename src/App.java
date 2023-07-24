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




/***
 * 
 * @author Matthew Del Real
 * @version 07/16/2023
 */


public class App {
	
	/***
	 * 
	 * @param args for main method
	 * @throws FileNotFoundException for opening file
	 */
    public static void main(String[] args) throws FileNotFoundException { //throwing exception based off of eclipse recommendation

        
        File text = new File("C:\\Users\\matth\\Documents\\words\\src\\Poem.txt"); //setting file object
        Scanner input = new Scanner(text, "UTF-8"); //setting scanner object
		Words[] wordsArr = new Words[100000]; //creating the words array

		for(int z = 0; z < wordsArr.length; z++){ //intializing ever object
			wordsArr[z] = new Words();
		}


		int totalWords = 0;
        
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


		/*
		for(int k = 0; k < totalWords; k++){//prints out result from the test :)
			System.out.println(wordsArr[k]);

		}
		*/

	String driverName = "mysql-connector-j-8.1.0";


/*
	try{
		
    Class.forName(driverName);
	}catch(ClassNotFoundException e){
		e.printStackTrace();
	}
*/

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

	}
        
      

    
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



