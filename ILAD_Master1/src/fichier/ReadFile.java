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
		  int backboneCost = 0, routeurCost = 0, budget = 0;
		  int xInitBackbone, yInitBackbone;
		  // compteur pour les 3 premi�res lignes de traitement
		  int i = 1;
		  // num�ro de la ligne de l'instance
		  int j = 0;
		  // tableau correspondant � l'instance
		  Cellule[][] instance = new Cellule[nbRow][nbColumn];
		  while ((st = br.readLine()) != null) {
			  String[] splitSt = st.split(" ");
			  switch(i){
			  	case 1 :
			  		nbRow = Integer.parseInt(splitSt[0]); nbColumn = Integer.parseInt(splitSt[1]); rangeRouter = Integer.parseInt(splitSt[2]); 
			  		break;
			  	case 2 :
			  		backboneCost = Integer.parseInt(splitSt[0]); routeurCost = Integer.parseInt(splitSt[1]); budget = Integer.parseInt(splitSt[2]);
			  		break;
			  	case 3:
			  		xInitBackbone = Integer.parseInt(splitSt[0]); yInitBackbone = Integer.parseInt(splitSt[1]);
			  		break;
			  	default :
			  		for(int k = 0; k < nbColumn; k++){
			  			instance[j][k] = new Cellule(j,k, String.valueOf(st.charAt(k)));
			  		}
			  		j++;
			  }
			  i++;
		  }
		  System.out.println("Nombre de ligne : " + nbRow);
		  System.out.println("Nombre de colonne : " + nbColumn);
		  System.out.println("Range of routeur : " + rangeRouter);
		  System.out.println("Backbone cost : " + backboneCost);
		  System.out.println("Router cost : " + routeurCost);
		  System.out.println("Budget : " + budget);
		  for(int row=0; i<nbRow; i++){
			  for(int col = 0; j < nbColumn; j++){
				  System.out.print(instance[row][col].getStatut());
			  }
			  System.out.println();
		  }
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
