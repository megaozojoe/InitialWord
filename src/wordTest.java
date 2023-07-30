import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class wordTest {

	@Test
	void test() {
		App test = new App();
		
		String clean2 = App.cleanString("hor!!se");
		String clean = App.cleanString("rav!e'n"); //this test cleaning the word of random characters
		assertEquals("raven", clean); //this tests the cleaning function the code
		assertEquals("horse", clean2);
	}

}
