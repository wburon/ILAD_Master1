package heuristiques;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fichier.ReadFile;
import model.Cellule;
import model.Heuristique;

public class Heuristique2 extends Heuristique{
	
	private ReadFile rf;
	private Cellule[][] instance;
	private ArrayList<Cellule> cellsEligibleRouteur;
	private ArrayList<Cellule> cellsConnectToBackbone, cellsWithRouter;
	private int number_of_cells_to_cover = 0, number_of_cells_cover;
	
	
	public ArrayList<Cellule> getCellsEligibleRouteur() {
		return cellsEligibleRouteur;
	}

	public void setCellsEligibleRouteur(ArrayList<Cellule> cellsEligibleRouteur) {
		this.cellsEligibleRouteur = cellsEligibleRouteur;
	}

	public int getNumber_of_cells_to_cover() {
		return number_of_cells_to_cover;
	}

	public void setNumber_of_cells_to_cover(int number_of_cells_to_cover) {
		this.number_of_cells_to_cover = number_of_cells_to_cover;
	}

	public int getNumber_of_cells_cover() {
		return number_of_cells_cover;
	}

	public void setNumber_of_cells_cover(int number_of_cells_cover) {
		this.number_of_cells_cover = number_of_cells_cover;
	}

	public ArrayList<Cellule> getCellsWithRouter() {
		return cellsWithRouter;
	}

	public void setCellsWithRouter(ArrayList<Cellule> cellsWithRouter) {
		this.cellsWithRouter = cellsWithRouter;
	}

	public ArrayList<Cellule> getCellsConnectToBackbone() {
		return cellsConnectToBackbone;
	}

	public void setCellsConnectToBackbone(ArrayList<Cellule> cellsConnectToBackbone) {
		this.cellsConnectToBackbone = cellsConnectToBackbone;
	}

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
	public void intitialisation(String path) {
		try {
			rf = new ReadFile();
			instance = rf.getInstance("instances/"+path);
			cellsEligibleRouteur = formListCelluleEligibleRouteur();
			cellsConnectToBackbone = initListBackbone();
			cellsWithRouter = new ArrayList<>();
			number_of_cells_cover = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialisation de la liste de cellule formant le backbone (cellule initial) et mise a jour de l'instance
	 * @return
	 */
	private ArrayList<Cellule> initListBackbone() {
		int ix = rf.getxInitBackbone();
		int iy = rf.getyInitBackbone();
		ArrayList<Cellule> list = new ArrayList<>();
		instance[ix][iy].setConnectToBackbone(true);
		list.add(instance[ix][iy]);
		return list;
	}

	/**
	 * Initialisation de la liste contennat les cellules éligibles à la pose d'un routeur
	 * @return
	 */
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
		return "heuristique2.txt";
	}

	@Override
	public void solve() {
		int numberOfAbandon = 0;
		// Coût
		int cost = 0;
		// Analyse de chaque cellule eligible : arret si toutes testées ou si toutes les zones sont couvertes
		while(!(this.cellsEligibleRouteur).isEmpty() && this.number_of_cells_cover != this.number_of_cells_to_cover && numberOfAbandon < 100){
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
				numberOfAbandon = 0;
				// Routeur
				this.cellsWithRouter.add(coordMax);
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
				// maj du coût total
				cost += prixInstall;
			}else{
				numberOfAbandon++;
			}
			// Suppression de la case Eligible testée
			removeCellOfList(this.cellsEligibleRouteur,coordMax);
		}	
		System.out.println("FIN");
	}
	
	/**
	 * Supprime une Cellule cell d'une liste de Cellule list_of_cells
	 */
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

	/**
	 * Recherche le plus court chemin entre la Cellule coordMax et la Cellule celluleReliee
	 * @param coordMax : Cellule testé
	 * @param celluleReliee : Cellule du backbone identifié comme la plus proche
	 * @return
	 */
	private List<Cellule> PCC(Cellule coordMax, Cellule celluleReliee) {
		System.out.println("FROM (" + celluleReliee.getX() + "," + celluleReliee.getY() + ") TO (" + coordMax.getX() + "," + coordMax.getY() + ")");
		ArrayList<Cellule> listToConnect = new ArrayList<>();
		// Les deux cellules sont sur la même ligne
		if (celluleReliee.getX() == coordMax.getX()) {
			for (int i = Math.min(coordMax.getY(), celluleReliee.getY()); i <= Math.max(coordMax.getY(),
					celluleReliee.getY()); i++) {
				instance[celluleReliee.getX()][i].setConnectToBackbone(true);
				listToConnect.add(instance[celluleReliee.getX()][i]);
			}
			if (Math.min(coordMax.getY(), celluleReliee.getY()) == coordMax.getY())
				listToConnect = renverse(listToConnect);
		}
		// Les deux cellules sont sur la même colonne
		else if (celluleReliee.getY() == coordMax.getY()) {
			for (int i = Math.min(coordMax.getX(), celluleReliee.getX()); i <= Math.max(coordMax.getX(),
					celluleReliee.getX()); i++) {
				instance[i][celluleReliee.getY()].setConnectToBackbone(true);
				listToConnect.add(instance[i][celluleReliee.getY()]);
			}
			if (Math.min(coordMax.getX(), celluleReliee.getX()) == coordMax.getX())
				listToConnect = renverse(listToConnect);
		}
		// Les deux cellules ne sont pas sur la même ligne ou colonne
		else if (celluleReliee.getX() != coordMax.getX() && celluleReliee.getY() != coordMax.getY()) {
			int q1 = 0, q2 = 0;
			// si routeur en haut à droite de la cellule du backbone
			if (coordMax.getX() < celluleReliee.getX() && coordMax.getY() > celluleReliee.getY()) {
				q1 = 1;
				q2 = -1;
			}
			// si routeur en haut à gauche de la cellule du backbone
			if (coordMax.getX() < celluleReliee.getX() && coordMax.getY() < celluleReliee.getY()) {
				q1 = 1;
				q2 = 1;
			}
			// si routeur en bas à gauche de la cellule du backbone
			if (coordMax.getX() > celluleReliee.getX() && coordMax.getY() < celluleReliee.getY()) {
				q1 = -1;
				q2 = 1;
			}
			// si routeur en bas à droite de la cellule du backbone
			if (coordMax.getX() > celluleReliee.getX() && coordMax.getY() > celluleReliee.getY()) {
				q1 = -1;
				q2 = -1;
			}
			int k = 0;
			instance[coordMax.getX() + k * q1][coordMax.getY() + k * q2].setConnectToBackbone(true);
			listToConnect.add(instance[coordMax.getX() + k * q1][coordMax.getY() + k * q2]);
			// Tant qu'on ne se retrouve pas sur la même ligne ou colonne on
			// continue en diagonal
			do {
				k++;
				instance[coordMax.getX() + k * q1][coordMax.getY() + k * q2].setConnectToBackbone(true);
				listToConnect.add(instance[coordMax.getX() + k * q1][coordMax.getY() + k * q2]);
			} while (celluleReliee.getX() != coordMax.getX() + k * q1
					&& celluleReliee.getY() != coordMax.getY() + k * q2);
			if (!(celluleReliee.getX() == coordMax.getX() + k * q1
					&& celluleReliee.getY() == coordMax.getY() + k * q2)) {
				ArrayList<Cellule> listProvisoire = new ArrayList<>();
				// même ligne
				if (celluleReliee.getX() == coordMax.getX() + k * q1) {
					for (int i = Math.min(coordMax.getY() + 1 + k * q2, celluleReliee.getY()); i < Math
							.max(coordMax.getY() + 1 + k * q2, celluleReliee.getY()); i++) {
						instance[celluleReliee.getX()][i].setConnectToBackbone(true);
						listProvisoire.add(instance[celluleReliee.getX()][i]);
					}
				}
				// même colonne
				if (celluleReliee.getY() == coordMax.getY() + k * q2) {
					for (int i = Math.min(coordMax.getX() + 1 + k * q1, celluleReliee.getX()); i < Math
							.max(coordMax.getX() + 1 + k * q1, celluleReliee.getX()); i++) {
						instance[i][celluleReliee.getY()].setConnectToBackbone(true);
						listProvisoire.add(instance[i][celluleReliee.getY()]);
					}
				}
				listToConnect.addAll(listProvisoire);
			}
			// la liste ainsi obtenu à ajouté des cellules du routeur au
			// backbone, la solution demande le contraire donc on retourne la
			// liste
			listToConnect = renverse(listToConnect);
		}
		removeCellOfList(listToConnect, instance[celluleReliee.getX()][celluleReliee.getY()]);
		this.cellsConnectToBackbone.addAll(listToConnect);
//		 for(Cellule c : listToConnect){
//		 System.out.println(c.getX()+","+c.getY());
//		 }
		return listToConnect;
	}

	/**
	 * Renverse une liste, le dernier élément devient le premier, etc
	 * @param listToConnect
	 * @return
	 */
	private ArrayList<Cellule> renverse(ArrayList<Cellule> listToConnect) {
		ArrayList<Cellule> returnList = new ArrayList<>();
		for(int i = listToConnect.size()-1; i >=0; i--){
			returnList.add(listToConnect.get(i));
		}
		return returnList;
	}

	/**
	 * Une dérivation de l'algorithme C(a,b), qui ne compte que les cellules non couvertes
	 * @param a
	 * @param b
	 * @return
	 */
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
		h.intitialisation("opera");
		h.solve();
		System.out.println();
		for(int row=0; row<h.getRf().getNbRow(); row++){
			  for(int col = 0; col < h.getRf().getNbColumn(); col++){
				  if(h.getInstance()[row][col].isRouterOn())
					  System.out.print("R");
				  else if(h.getRf().getxInitBackbone() == row && h.getRf().getyInitBackbone() == col)
					  System.out.print("I");
				  else if(h.getInstance()[row][col].isConnectToBackbone())
					  System.out.print("B");
//				  else if(h.getInstance()[row][col].isCouvert())
//					  System.out.print("C");
				  else
					  System.out.print(h.getInstance()[row][col].getStatut());
				
			  }
			  System.out.println();
		  }
//		for(Cellule c : h.getCellsConnectToBackbone())
//			System.out.println(c.getX() + "," + c.getY());
		System.out.println(h.verifyListBackbone(h.getCellsConnectToBackbone(), h.getRf().getxInitBackbone(), h.getRf().getyInitBackbone()));
//		System.out.println(h.verifiListBackbone());
		h.writeSolution(h);
	}
	
//	private boolean verifiListBackbone() {
//		for(Cellule c : this.cellsConnectToBackbone){
//			if(!this.getInstance()[c.getX()][c.getY()].isConnectToBackbone())
//				return false;
//		}
//		return true;
//	}

	/**
	 * Méthode de vérification du résultat
	 * @param list
	 * @param ixBackbone
	 * @param iyBackbone
	 * @return
	 */
	public boolean verifyListBackbone(ArrayList<Cellule> list, int ixBackbone, int iyBackbone){
		for(int i = 0; i < list.size(); i++){
			if(!(Math.abs(ixBackbone - list.get(i).getX()) <= 1 || Math.abs(iyBackbone - list.get(i).getY()) <= 1)){
				if(!isConnect(i, list))
					return false;
			}
		}
		return true;
	}

	private boolean isConnect(int i, ArrayList<Cellule> list) {
		for(int j =  i - 1; j >= 0; j--){
			if(Math.abs(list.get(j).getX() - list.get(i).getX()) <= 1 || Math.abs(list.get(j).getY() - list.get(i).getY()) <= 1)
				return true;
		}
		return false;
	}
	
	public void writeSolution(Heuristique2 h){
		ArrayList<String> listePath = new ArrayList<>();
		listePath.add("charleston_road");
		listePath.add("rue_de_londres");
		listePath.add("opera");
		listePath.add("lets_go_higher");
		ArrayList<Cellule> backbone = h.getCellsConnectToBackbone();
		ArrayList<Cellule> routeur = h.getCellsWithRouter();
		 try (FileWriter writer = new FileWriter("solution/SubmissionFile/opera.txt");
	             BufferedWriter bw = new BufferedWriter(writer)) {

	            bw.write(backbone.size()+"\n");
	            for(Cellule c : backbone)
	            	bw.write("("+c.getX()+","+c.getY()+")\n");
	            bw.write(routeur.size()+"\n");
	            for(Cellule c : routeur)
	            	bw.write("("+c.getX()+","+c.getY()+")\n");

	        } catch (IOException e) {
	            System.err.format("IOException: %s%n", e);
	        }
		try {
			FileWriter writer = new FileWriter("solution/MapFile/heuristique2/opera.txt");
			BufferedWriter bw = new BufferedWriter(writer);
			for(int row=0; row<h.getRf().getNbRow(); row++){
				  for(int col = 0; col < h.getRf().getNbColumn(); col++){
					  if(h.getInstance()[row][col].isRouterOn())
						  bw.write("R");
					  else if(h.getRf().getxInitBackbone() == row && h.getRf().getyInitBackbone() == col)
						  bw.write("I");
					  else if(h.getInstance()[row][col].isConnectToBackbone())
						  bw.write("B");
//					  else if(h.getInstance()[row][col].isCouvert())
//						  bw.write("C");
					  else
						  bw.write(h.getInstance()[row][col].getStatut());
					
				  }
				  bw.write("\n");
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		 System.out.println("SCORE : "+(1000*h.getNumber_of_cells_cover()+(h.getRf().getBudget()-(backbone.size()*h.getRf().getBackboneCost()+routeur.size()*h.getRf().getRouteurCost()))));
	}
	
	public int getScore(){
		return (1000*this.number_of_cells_cover+(this.rf.getBudget()-(this.cellsConnectToBackbone.size()*this.rf.getBackboneCost()+this.cellsWithRouter.size()*this.rf.getRouteurCost())));

	}
	

}
