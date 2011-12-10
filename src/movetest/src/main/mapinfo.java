/*package main;
import java.io.*;



public class mapinfo {
	public static void main(String [] args) throws IOException{
	
	String mapFile = "lib/demora.txt";
	
	try{
		Map map = new Map(mapFile);
		String[] maplines = map.read(); // gets the map read lines
		
		int i;
		for(i=0; i < maplines.length; i++){
			System.out.println(maplines[i]);
		}
	}
	catch (IOException e){
		System.out.println(e.getMessage());
	}
	}
}

*/