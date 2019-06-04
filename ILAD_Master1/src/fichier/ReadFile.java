package fichier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import model.Cellule;

public class ReadFile {
	
	private int nbRow = 0, nbColumn = 0, rangeRouter = 0, backboneCost = 0, routeurCost = 0, budget = 0, xInitBackbone, yInitBackbone;
	
	
	
	public int getNbRow() {
		return nbRow;
	}

	public void setNbRow(int nbRow) {
		this.nbRow = nbRow;
	}

	public int getNbColumn() {
		return nbColumn;
	}

	public void setNbColumn(int nbColumn) {
		this.nbColumn = nbColumn;
	}

	public int getRangeRouter() {
		return rangeRouter;
	}

	public void setRangeRouter(int rangeRouter) {
		this.rangeRouter = rangeRouter;
	}

	public int getBackboneCost() {
		return backboneCost;
	}

	public void setBackboneCost(int backboneCost) {
		this.backboneCost = backboneCost;
	}

	public int getRouteurCost() {
		return routeurCost;
	}

	public void setRouteurCost(int routeurCost) {
		this.routeurCost = routeurCost;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public int getxInitBackbone() {
		return xInitBackbone;
	}

	public void setxInitBackbone(int xInitBackbone) {
		this.xInitBackbone = xInitBackbone;
	}

	public int getyInitBackbone() {
		return yInitBackbone;
	}

	public void setyInitBackbone(int yInitBackbone) {
		this.yInitBackbone = yInitBackbone;
	}

	public Cellule[][] getInstance(String path) throws IOException{

		  File file = new File(path); 
		  
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String st;
		  // compteur pour les 3 premières lignes de traitement
		  int i = 1;
		  // numéro de la ligne de l'instance
		  int j = 0;
		  // tableau correspondant à l'instance
		  Cellule[][] instance = null;
		  while ((st = br.readLine()) != null) {
			  String[] splitSt = st.split(" ");
			  switch(i){
			  	case 1 :
			  		nbRow = Integer.parseInt(splitSt[0]); nbColumn = Integer.parseInt(splitSt[1]); rangeRouter = Integer.parseInt(splitSt[2]); 
			  		instance = new Cellule[nbRow][nbColumn];
			  		break;
			  	case 2 :
			  		backboneCost = Integer.parseInt(splitSt[0]); routeurCost = Integer.parseInt(splitSt[1]); budget = Integer.parseInt(splitSt[2]);
			  		break;
			  	case 3:
			  		xInitBackbone = Integer.parseInt(splitSt[0]); yInitBackbone = Integer.parseInt(splitSt[1]);
			  		break;
			  	default :
			  		for(int k = 0; k < nbColumn; k++){
			  			String charact = String.valueOf(st.charAt(k));
			  			instance[j][k] = new Cellule(j,k,charact);
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
		  for(int row=0; row<nbRow; row++){
			  for(int col = 0; col < nbColumn; col++){
				  System.out.print(instance[row][col].getStatut());
			  }
			  System.out.println();
		  }
		  return instance;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ReadFile rf = new ReadFile();
			rf.getInstance("instances/charleston_road");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
