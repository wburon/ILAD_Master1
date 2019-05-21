package fichier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import model.Cellule;

public class ReadFile {
	
	public static Cellule[][] getInstance(String path) throws IOException{

		  File file = new File(path); 
		  
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String st;
		  // 
		  int nbRow = 0, nbColumn = 0, rangeRouter = 0;
		  // compteur pour les 3 premières lignes de traitement
		  int i = 1;
		  while ((st = br.readLine()) != null) {
			  switch(i){
			  	case 1 :
			  		String[] splitSt = st.split(" ");
			  		nbRow = Integer.parseInt(splitSt[0]); nbColumn = Integer.parseInt(splitSt[1]); rangeRouter = Integer.parseInt(splitSt[2]); 
			  		break;
			  	case 2 :
			  		
			  		break;
			  	case 3:
			  		break;
			  	default :
				  System.out.println(st); 
			  }
			  i++;
		  }
		  System.out.println("Nombre de ligne : " + nbRow);
		  System.out.println("Nombre de colonne : " + nbColumn);
		  System.out.println("Range of routeur : " + rangeRouter);
		//Cellule[][] instance = new Cellule[][];
		  return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			getInstance("instances/charleston_road");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
