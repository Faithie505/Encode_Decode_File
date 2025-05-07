package ie.atu.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

/** 
 * The class encodes and decodes text files using a word-to-number and number-to-word mapping system.
 * 
 * @author fathia
 * @version 1.0
 */
public class Runner {
	static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		//variables - these  are automatically set/initialised in case the user does not chose option 1, 2 or 3
		String mappingFile = ""; //"./encodings-10000.csv";
		String encodeFile = ""; //"./DivineComedyDante.txt";
		String out = "";//"./out.txt";
		String decode = "decode.txt";

		//this will store all the suffixes
		//List<String> suffix = new ArrayList<>();;
		String suffix = "";
		Map<String, Integer> suffixes = new TreeMap<>();
		
		int choice;
		
		File myFile = new File(mappingFile);//the path to the file that is used to encode
		File encodedFile = new File(encodeFile);//path to the encoding file
		File outputFile = new File(out); //path to the output file of the encoded text
		
		Map<String, Integer> mapE = null;
		
		
		List<String> suffixWords = new ArrayList<String>();

		choice = menu();	        
		
		//allows user to chose the option
		while(choice != 6){
			keyboard.nextLine();
			
			// - - - the running time of this is O(1). It run in constant time because there are no loops, meaning no change in time as input increases - - - //
		if(choice == 1) {
			System.out.println(" - - - Specifying Mapping File - - - ");
			System.out.println("Please specify the Mapping File");
			//takes in the users choice
			mappingFile = keyboard.nextLine();
			//the new encoding file is used
			myFile = new File(mappingFile);
		}
		// - - - the running time of this is O(1). It run in constant time because there are no loops, meaning no change in time as input increases - - - //
		else if(choice == 2) {
			System.out.println(" \n- - - Specifying Text File To Encode - - - ");
			System.out.print("Please type the name of the file you want to encode: ");
			encodeFile = keyboard.nextLine();
			encodedFile = new File(encodeFile);
			
		}
		// - - - the running time of this is O(1). It run in constant time because there are no loops, meaning no change in time as input increases - - - //
		else if(choice == 3) {
			System.out.println(" - - - Specify Output File (default: ./out.txt) - - - ");
			out = keyboard.nextLine(); //allows user to create a new file to store the output
			outputFile = new File(out);
			
		}
		
		// - - - the running time of this is O(n). The two loops will run based in the number of inputs they are given - - - //

		else if(choice == 4) {
			System.out.println(" - - - Encoding Text File - - - ");
			mapE = new TreeMap<>();

			//read from file
			try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)))) {
				//variables
			    String next; 
			    String readWord;
			    int notInMap;

			    
			    
			    // Read the file line by line
			    while ((next = br.readLine()) != null) {
			        String[] pair = next.split(",", 2); // split line into word and number

			        if (pair.length == 2) {
			        	//removes any white soaces
			            String word = pair[0].trim();
			            int number = Integer.parseInt(pair[1].trim());
			            if(word.contains("@@")) {
			            	word = word.substring(2, word.length());
			            	suffix = word;
			            	suffixes.put(suffix, number);

			            }
			            //puts the words and the numbers ito the map
			            mapE.put(word, number);

			        }
			    }
			    
			    //reads word by word in the file
		        try {
		        	Scanner scanner = new Scanner(encodedFile);
		        	//creates a filewriter object that will write into the output file
		             FileWriter fileWriter = new FileWriter(out); 
		             int encodedValue = 0;

		            // Read words one by one
		            while (scanner.hasNext()) {
		                readWord = scanner.next();

		                // Check if map contains the word in the encoded file (in lowercase as that is how it is in the ecoding file)
		                if (mapE.containsKey(readWord.toLowerCase())) {
		                	//the value(number) of the word is stored in an int variable and is written to the file
		                    encodedValue = mapE.get(readWord.toLowerCase());
		                    fileWriter.write(encodedValue + "\n");
		                }
		                
		            }
		            scanner.close();
		            fileWriter.close();

		        System.out.printf("Encoding complete!");

		        } catch (Exception e) {
		        	throw new RuntimeException("Error trying to encode file! " + e.getMessage());
		        }
			}
			catch (Exception e) {
			    throw new RuntimeException("Unable to read the file into the map! " + e.getMessage());
			}


		}
		
		// - - - the running time of this is O(n^2). A nested loop is used, and as the input doubles, time quadruples.  - - - //

		else if(choice == 5) {
			System.out.println(" - - - Decode Text File - - - ");
			//reads from the output file
			try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(out)))) {
				//read from the output file
				String readWord;//converts each number to a word
				String next;
				
				//Scanner scanner = new Scanner("./out.txt");

				
				String encodedValue = "";
				//write the decoded words into the decode.txt file
				FileWriter decodeFile = new FileWriter(decode); 
				
				//loops for each word in the output file
				if(mapE != null && (!mapE.isEmpty() || out.length() > 0)){//only works if the user has pressed 4 and encoded the map, or if the file out.txt isnt empty
					
				    while ((next = br.readLine()) != null) 
				    { //loops till the end of the file

						readWord = next; //reads each number from the out file	
						//System.out.print(readWord +" ");
						for (Entry<String, Integer> entry : mapE.entrySet()) {//loops for each entry in mapE
					        if (entry.getValue() == Integer.parseInt(readWord)) { //is the value (number)is the same as readword
					        	encodedValue = entry.getKey(); //gets the key (word) of the number
					        	if(encodedValue.startsWith("@@")){
									decodeFile.write(encodedValue);
					        	}
					        	else {
									decodeFile.write(encodedValue+" ");
					        	}
					        	//System.out.print(encodedValue +" , ");

					        }
					    } 
					}
					//scanner.close();
				}
				System.out.println("Encoded word is: " + encodedValue);

				decodeFile.close();
			}//end of teh try loop
			catch (Exception e) {
			    throw new RuntimeException("Unable to read the file into the map! " + e.getMessage());
			}

			
		}
		choice = menu();
	}
	    }
	
		public static void printProgress(int index, int total) {
			if (index > total) return;	//Out of range
	        int size = 50; 				//Must be less than console width
		    char done = '█';			//Change to whatever you like.
		    char todo = '░';			//Change to whatever you like.
		    
		    //Compute basic metrics for the meter
	        int complete = (100 * index) / total;
	        int completeLen = size * complete / 100;
	        
	        /*
	         * A StringBuilder should be used for string concatenation inside a 
	         * loop. However, as the number of loop iterations is small, using
	         * the "+" operator may be more efficient as the instructions can
	         * be optimized by the compiler. Either way, the performance overhead
	         * will be marginal.  
	         */
	        StringBuilder sb = new StringBuilder();
	        sb.append("[");
	        for (int i = 0; i < size; i++) {
	        	sb.append((i < completeLen) ? done : todo);
	        }
	        
	        /*
	         * The line feed escape character "\r" returns the cursor to the 
	         * start of the current line. Calling print(...) overwrites the
	         * existing line and creates the illusion of an animation.
	         */
	        System.out.print("\r" + sb + "] " + complete + "%");
	        
	        //Once the meter reaches its max, move to a new line.
	        if (done == total) System.out.println("\n");
	    }
	
	
	
	public static int  menu(){
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*              Encoding Words with Suffixes                *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Mapping File");
		System.out.println("(2) Specify Text File to Encode");
		System.out.println("(3) Specify Output File (default: ./out.txt)");
		System.out.println("(4) Encode Text File");
		System.out.println("(5) Decode Text File");
		System.out.println("(6) Quit");
		System.out.print("Please enter your choice: ");
		int choice = keyboard.nextInt();
		return choice;

	}
	
	
}
