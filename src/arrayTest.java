import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class arrayTest {

	@Test
	void test() {
		App sortingTest = new App();// here I will be checking to make sure that we are sorting correctly
		Words test[] = new Words[10]; //creating a test array.
		
		for(int k = 0; k < 10; k++) { //creating objects
			test[k] = new Words();
		}
		int min  = 0; //setting the parameters
		int max = 100;
		
		for(int i  = 0; i < 10; i++) { //assigning random numbers
			test[i].count = (int)Math.floor(Math.random() * (max - min + 1) + min); //should assign random numbers to each object
			test[i].key = "Case: " +  i;
		}
		sortingTest.bubbleSort(test, 10);// runs code bubble sort that was made
		
		for(int j = 0; j < 10; j++) {// prints out values
			System.out.println(test[j]);
		}
		
		Words[] verify = new Words[10]; //creating copy of the array
		
		for(int q = 0; q < 10; q++) {
			verify[q] = test[q]; //this copies the data into a second array.
		}
		
		//running sorting algorithm to check against the one I set up 
        for (int i = 0; i < verify.length; i++) {
        	 
            for (int j = i + 1; j < verify.length; j++) {
 
                // Checking elements
                int temp = 0;
                if (verify[j].count < verify[i].count) {
 
                    // Swapping
                    temp = verify[i].count;
                    verify[i].count = verify[j].count;
                    verify[j].count = temp;
                }
            }
        }
		
        //running through the arrays to verify they are good
		for(int z = 0; z < 10; z++) {
		assertEquals( verify[z].count, test[z].count);
		}
		
		
	}

}
