package heuristiques;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import fichier.ReadFile;
import model.Cellule;
import model.Heuristique;

public class Heuristique2 extends Heuristique{
	
	private ReadFile rf;
	private Cellule[][] instance;
	private ArrayList<Cellule> cellsEligibleRouteur;
	private ArrayList<Cellule> cellsConnectToBackbone;
	private int number_of_cells_to_cover = 0, number_of_cells_cover;
	
	
	public ReadFile getRf() {
		return rf;
	}

	public void setRf(ReadFile rf) {
		this.rf = rf;
	}

	public Cellule[][] getInstance() {
		return instance;
	}

	public void setInstance(Cellule[][] instance) {
		this.instance = instance;
	}

	@Override
	public void intitialisation() {
		try {
			rf = new ReadFile();
			instance = rf.getInstance("instances/charleston_road");
			cellsEligibleRouteur = formListCelluleEligibleRouteur();
			cellsConnectToBackbone = initListBackbone();
			number_of_cells_cover = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<Cellule> initListBackbone() {
		int ix = rf.getxInitBackbone();
		int iy = rf.getyInitBackbone();
		ArrayList<Cellule> list = new ArrayList<>();
		instance[ix][iy].setConnectToBackbone(true);
		list.add(instance[ix][iy]);
		return list;
	}

	private ArrayList<Cellule> formListCelluleEligibleRouteur() {
		ArrayList<Cellule> list = new ArrayList<>();
		for(int i=1; i<instance.length; i++){
			for(int j = 0; j <instance[0].length; j++){
				if(instance[i][j].getStatut().equals(".")){
					list.add(instance[i][j]);
					this.number_of_cells_to_cover++;
				} else if(instance[i][j].getStatut().equals("-"))
					list.add(instance[i][j]);
			}
		}
		return list;
	}

	@Override
	public String getSolutionPathName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void solve() {
		// Coût
		int cost = 0;
		// Analyse de chaque cellule eligible
		while(!(this.cellsEligibleRouteur).isEmpty() && this.number_of_cells_cover != this.number_of_cells_to_cover){
			int maxlength = 0;
			Cellule coordMax = new Cellule();
			List<Cellule> listCoordMax = new ArrayList<>();
			// Recherche de l'emplacement avec la plus grande efficacité
			for(Cellule cell : this.cellsEligibleRouteur){
				int i = cell.getX();
				int j = cell.getY();
				if(!instance[i][j].getStatut().equals("#")){
					List<Cellule> listCoord = CBIS(i,j);
					if(listCoord.size() > maxlength){
						maxlength = listCoord.size();
						coordMax.setX(i);
						coordMax.setY(j);
						listCoordMax = listCoord;
					}
				}
			}
			int distanceMin = Integer.MAX_VALUE;
			Cellule celluleReliee = new Cellule();
			// Recherche de la distance la plus courte backbone-routeur
			for(Cellule backbone_cell : this.cellsConnectToBackbone){
				int r = backbone_cell.getX();
				int c = backbone_cell.getY();
				int distance = Math.max(Math.abs(coordMax.getX()-r-1), Math.abs(coordMax.getY()-c-1));
				if(distance < distanceMin){
					distanceMin = distance;
					celluleReliee.setX(r);
					celluleReliee.setY(c);
				}
			}
			// Calcul du coût d'installation, vérification de la limite budget, mise en place(ou non) de l'opération 
			int prixInstall = rf.getRouteurCost() + distanceMin * rf.getBackboneCost();
			if(cost + prixInstall <= rf.getBudget() && prixInstall < 1000 * maxlength){
				instance[coordMax.getX()][coordMax.getY()].setRouterOn(true);
				// On relie le routeur à cellule Reliée en ajoutant des jetons au backbone
				List<Cellule> listBackbone = PCC(coordMax,celluleReliee);
				for(Cellule cell_to_connect : listBackbone){
					instance[cell_to_connect.getX()][cell_to_connect.getY()].setConnectToBackbone(true);
				}
				for(Cellule cell_now_couverte : listCoordMax){
					instance[cell_now_couverte.getX()][cell_now_couverte.getY()].setCouvert(true);
					this.number_of_cells_cover++;
				}
				cost += prixInstall;
			}
			System.out.println("new cost = " + cost + " taille de liste restante : " + this.cellsEligibleRouteur.size());
			removeCellOfList(this.cellsEligibleRouteur,coordMax);
		}	
		System.out.println("FIN");
	}
	
	private List<Cellule> removeCellOfList(ArrayList<Cellule> list_of_cells, Cellule cell){
		List<Cellule> listToReturn = list_of_cells;
		for(Cellule c : listToReturn){
			if(c.getX() == cell.getX() && c.getY() == cell.getY()){
				listToReturn.remove(c);
				return listToReturn;
			}
		}
		return listToReturn;
	}

	private List<Cellule> PCC(Cellule coordMax, Cellule celluleReliee) {
		ArrayList<Cellule> listToConnect = new ArrayList<>();
		//sameLine
		if(celluleReliee.getX() == coordMax.getX()){
			for(int i = Math.min(coordMax.getY(), celluleReliee.getY()); i < Math.max(coordMax.getY(),  celluleReliee.getY()); i++){
				instance[celluleReliee.getX()][i].setConnectToBackbone(true);
				listToConnect.add(instance[celluleReliee.getX()][i]);
			}
		}
		//sameColumn
		if(celluleReliee.getY() == coordMax.getY()){
			for(int i = Math.min(coordMax.getX(), celluleReliee.getX()); i < Math.max(coordMax.getX(), celluleReliee.getX()); i++){
				instance[i][celluleReliee.getY()].setConnectToBackbone(true);
				listToConnect.add(instance[i][celluleReliee.getY()]);
			}
		}
		//en diagonale
		if(celluleReliee.getX() != coordMax.getX() && celluleReliee.getY() != coordMax.getY()){
			int q1 = 0, q2 = 0;
			// si routeur en haut à droite de backbone
			if(coordMax.getX() < celluleReliee.getX() && coordMax.getY() > celluleReliee.getY()){
				q1 = 1; q2 = -1;
			}
			// si routeur en haut à gauche du backbone
			if(coordMax.getX() < celluleReliee.getX() && coordMax.getY() < celluleReliee.getY()){
				q1 = 1; q2 = 1;
			}
			// si routeur en bas à gauche du backbone
			if(coordMax.getX() > celluleReliee.getX() && coordMax.getY() < celluleReliee.getY()){
				q1 = -1; q2 = 1;
			}
			// si routeur en bas à droite du backbone
			if(coordMax.getX() > celluleReliee.getX() && coordMax.getY() > celluleReliee.getY()){
				q1 = -1; q2 = -1;
			}
			int k = 1;
			while(celluleReliee.getX() != coordMax.getX()+k*q1 && celluleReliee.getY() != coordMax.getY()+k*q2){
				instance[coordMax.getX()+k*q1][coordMax.getY()+k*q2].setConnectToBackbone(true);
				listToConnect.add(instance[coordMax.getX()+k*q1][coordMax.getY()+k*q2]);
				k++;
			}
			//sameLine
			if(celluleReliee.getX() == coordMax.getX()+k*q1){
				for(int i = Math.min(coordMax.getY()+k*q2, celluleReliee.getY()); i < Math.max(coordMax.getY()+k*q2,  celluleReliee.getY()); i++){
					instance[celluleReliee.getX()][i].setConnectToBackbone(true);
					listToConnect.add(instance[celluleReliee.getX()][i]);
				}
			}
			//sameColumn
			if(celluleReliee.getY() == coordMax.getY()+k*q2){
				for(int i = Math.min(coordMax.getX()+k*q1, celluleReliee.getX()); i < Math.max(coordMax.getX()+k*q1, celluleReliee.getX()); i++){
					instance[i][celluleReliee.getY()].setConnectToBackbone(true);
					listToConnect.add(instance[i][celluleReliee.getY()]);
				}
			}
		}
		removeCellOfList(listToConnect,instance[celluleReliee.getX()][celluleReliee.getY()]);
		this.cellsConnectToBackbone.addAll(listToConnect);
		return listToConnect;
	}

	private List<Cellule> CBIS(int a, int b) {
		List<Cellule> cbisab = new ArrayList<>();
		for (int r = Math.max(0, a - rf.getRangeRouter()); r < Math.min(a + rf.getRangeRouter(), rf.getNbRow()); r++) {
			for (int c = Math.max(0, b - rf.getRangeRouter()); c < Math.min(b + rf.getRangeRouter(),rf.getNbColumn()); c++) {
				if(!instance[r][c].isCouvert() && instance[r][c].getStatut().equals(".")){
					boolean isWall = false;
					for(int w = Math.min(a, r); w <= Math.max(a, r); w++){
						for(int v = Math.min(b, c); v <= Math.max(b, c); v++){
							if(instance[w][v].getStatut().equals("#"))
								isWall = true;
						}
					}
					if(!isWall)
						cbisab.add(instance[r][c]);
				}
			}
		}
		return cbisab;
	}
	
	public static void main(String[] args) {
		Heuristique2 h = new Heuristique2();
		h.intitialisation();
		h.solve();
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		for(int row=0; row<h.getRf().getNbRow(); row++){
			  for(int col = 0; col < h.getRf().getNbColumn(); col++){
				  if(h.getInstance()[row][col].isRouterOn())
					  System.out.print("R");
				  else if(h.getInstance()[row][col].isConnectToBackbone())
					  System.out.print("B");
				  else 
					  System.out.print(h.getInstance()[row][col].getStatut());
			  }
			  System.out.println();
		  }
	}
	

}
