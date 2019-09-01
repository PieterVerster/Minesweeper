import java.io.*;
import java.util.*;
import javax.swing.Timer;

public class ReaderWriter
{	
	//Save file to file name
	public void saveFile(String fileName, ArrayList<String> timeList)
	{
		try
		{
			File file = new File(fileName);		//create new file from fileName
			FileWriter fileWriter = new FileWriter(file);	//create fileWriter to write to file
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);		//create bufferedWriter to write multiple lines in file
			
			for (int i = 0; i < timeList.size(); i++)		//for every item in timeList
			{
				bufferedWriter.write(timeList.get(i));		//write item to file
				bufferedWriter.newLine();					//go to new line
			}
			
			bufferedWriter.flush();		//write anything still in buffer to file
			bufferedWriter.close();		//close buffer
		}
		catch (IOException e)
		{
			System.out.println("Could not save the file");
		}
		catch (Exception e)
		{
			System.out.println("Something else went wrong with saving file");
		}
	
	} //end of saveFile()
	
//===========================================================================================================================================================
	
	//Load file from file name
	public ArrayList<String> loadFile(String fileName)
	{
		ArrayList<String> bestTimes = new ArrayList<String>();		//create ArrayList for best times
		
		try
		{
			File file = new File(fileName);		//create new file from fileName
			FileReader fileReader = new FileReader(file);	//create fileReader to read from file
			BufferedReader bufferedReader = new BufferedReader(fileReader);		//create bufferedReader to read multiple lines in file
			
			String line = null;		//create null String (in case line is empty, then will still continue)
			
			while ((line = bufferedReader.readLine()) != null)		//while text file line is not empty
			{
				bestTimes.add(line);		//add line to bestTimes ArrayList
			}
			
			fileReader.close();		//close buffer
		}
		catch (IOException e)
		{
			System.out.println("Could not load the file");
		}
		catch (Exception e)
		{
			System.out.println("Something else went wrong with loading file");
		}
		
		return bestTimes;
	
	} //end of loadFile()
	
//===========================================================================================================================================================
	
	//delete file
	public void deleteFile(String fileName)
	{
		File file = new File(fileName);		//create new File object
		
		if(file.delete()) 	//delete file
		{ 
			System.out.println("File deleted successfully"); 
		} 
		else
		{ 
			System.out.println("Failed to delete the file"); 
		} 
	
	} //end of deleteFile()
	
//===========================================================================================================================================================

} //end of class ReaderWriter