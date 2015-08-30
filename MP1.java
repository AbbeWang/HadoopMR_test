

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;


public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
        
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        String[] textLines = new String[10000];
        Integer[] index = getIndexes();
        ArrayList<String> wordList = new ArrayList<String>();
       
        //read the inputfile
        try{
        	String[] aryLines = OpenFile(inputFileName);
        	
        	//process only the titles with certain indexes
        	int i,j;
        	for(i=0, j=0; i<10000; i++, j++){
        		textLines[i] = aryLines[index[j]];
        	}
        	
        }
        catch (IOException e){
        	System.out.println(e.getMessage());
        }
        
        
        //Step 1: Divide each sentence into a list of words using delimiters
        int i;
        for(i=0; i<10000; i++){
        	StringTokenizer st = new StringTokenizer(textLines[i], delimiters, false);
        	
        	while (st.hasMoreTokens()) {
         		String sepWord = st.nextToken().toString();
         		
         		//Step 2: Make all the tokens lowercase and remove any tailing and leading spaces
         		sepWord = sepWord.toLowerCase();
         		sepWord = sepWord.replace(" ", "");
        		
        		wordList.add(sepWord);
    	    }
        	
        }
                
        
        //Step 3: Ignore all common words provided in the “stopWordsArray” variable
        ArrayList<String> dropWords = new ArrayList<String>();
        
        int j;
        for(j=0; j<stopWordsArray.length; j++){
        	dropWords.add(stopWordsArray[j]);
        }
        
        wordList.removeAll(dropWords);
        
        
        //Step 4: Keep track of word frequencies
        Map<String, Integer> wordFreq = new HashMap<String, Integer>();
        
        Iterator<String> it = wordList.iterator();
        while(it.hasNext()){
        	String wordAppear = it.next();
        	if(wordFreq.containsKey(wordAppear)){
        		wordFreq.replace(wordAppear, wordFreq.get(wordAppear), wordFreq.get(wordAppear)+1);
        	}
        	else{
        		wordFreq.put(wordAppear, 1);
        	}
        }
                
        
        //Step 5: Sort the list by frequency in a descending order
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(wordFreq.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
        	public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){        		
        		if(o2.getValue() == o1.getValue()){
        			return (o1.getKey().compareTo(o2.getKey()));
        		}
        		else{
        			return (o2.getValue() - o1.getValue());
        		}
        	}
        });
                
        
        //Step 6: Return the top 20 items from the sorted list as a String Array
        int m;
        for(m=0; m<20; m++){
        	ret[m] = list.get(m).getKey();
        }
        
        
        //Write to the outputFile
        String output_path = "./output.txt";
        
        FileWriter write = new FileWriter(output_path, true);
        PrintWriter print_line = new PrintWriter(write);
        
        for(m=0; m<20; m++){
        	print_line.printf("%s"+"%n", ret[m]);
        }
        
        print_line.close();
        
        return ret;
    }
    
	//create a method that returns all the lines of code from the text file
	public String[] OpenFile(String path) throws IOException{
		
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines(path);
		String[] textData = new String[numberOfLines];
		
		int i;
		for(i=0; i<numberOfLines; i++){
			textData[i] = textReader.readLine();
		}
		
		textReader.close();
		return textData;
	}
	
	
	//count how many lines the text file has
	int readLines(String path) throws IOException{
		
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		String aLine;
		int numberOfLines = 0;
		
		while((aLine = bf.readLine()) != null){
			numberOfLines++;
		}
		bf.close();
		
		return numberOfLines;
	}



    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];  //11944827
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
