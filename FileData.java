
import java.io.IOException;

public class FileData {	
	
	public static void main(String[] args) throws IOException {
        //System.out.println("Hello, World");
        //System.out.println(args.length);
        
        String input_name = "./test.txt";
        String output_name = "./out.txt";
        
        try{
        	ReadFile file = new ReadFile(input_name);
        	String[] aryLines = file.OpenFile();
        	
        	int i;
        	for(i=0; i < aryLines.length; i++){
        		System.out.println(aryLines[i]);
        	}
        }
        catch (IOException e){
        	System.out.println(e.getMessage());
        }
        
        try{
        	WriteFile data = new WriteFile(input_name, true);
        	data.writeToFile("Hello nemo1234");
        	data.writeToFile("6789");
        	System.out.println("Text File Written To");
        }
        catch (IOException e){
        	System.out.println(e.getMessage());
        }
        
    }

	
}
