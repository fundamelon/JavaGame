package main;
import java.util.*;
import java.io.*;
import java.io.*;
/*public class Map {
	
static String path = "path to file";	
	
	
	public static int rows() throws IOException{
		FileReader mFile = new FileReader(path);
		BufferedReader mread = new BufferedReader(mFile);
		String Line;
		int lineNumber = 0;  
		//checks how many lines in the text file
		while((Line = mread.readLine()) != null){
			lineNumber++;
		}
		mread.close();
		return lineNumber;  //returns how many lines
	}
	
	
	
	
	
	public static String[][] fileReader() throws IOException{
		
	}
	
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
	
public static String mapFile = "lib/demora.txt";
	private static String path;
	private char[] toCharArray;
	//gets the file path from another class in the future, right now its in this one
	public Map(String file_path){
		path = file_path;
	}
	
	//err... not really rows but collums.. i always get mixed up. fixed later
	public static int rows() throws IOException{
		FileReader mFile = new FileReader(path);
		BufferedReader mread = new BufferedReader(mFile);
		String Line;
		int lineNumber = 0;  
		//checks how many lines in the text file
		while((Line = mread.readLine()) != null){
			lineNumber++;
		}
		mread.close();
		return lineNumber;  //returns how many lines
	}
	
	//checks length of a row
	public static int rLength() throws IOException{
		FileReader mFile = new FileReader(path);
		BufferedReader mread = new BufferedReader(mFile);
		String row1 = mread.readLine();
		int rLength = row1.length();
		return rLength;
	}
	
	public static String[] read() throws IOException{
		FileReader mfile = new FileReader(path);
		BufferedReader mread = new BufferedReader(mfile);
		int lineCount = rows();  //how many lines in map, will become rows gets from the row method
		String[] map = new String[lineCount];  //makes a string to hold the map
		
		//loop reads the text file
		int i;
		for(i=0; i<lineCount; i++){
			map[i] = mread.readLine();
		}
		mread.close();
		return map;
	}
	
	
	public static char[][] itemMaker() throws IOException{
		int collumns = rows();
		int rowLength = rLength();
		int c = 0,r = 0;
		String[] sMap = read();
		char [][] map = new char[collumns] [rowLength];
		for(r=0; r<rowLength; r++){
			map[0][r] = sMap [c].charAt(r);
		}
		return map;
	}
	
	public static void printmap() throws IOException{
		Map map = new Map(mapFile);
		System.out.println(map.toCharArray);
	}

*/

}








//use if nextLine.equals(block type)





