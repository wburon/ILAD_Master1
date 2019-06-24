package heuristiques;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fichier.ReadFile;
import model.Cellule;
import model.Heuristique;

public class Heuristique1 extends Heuristique {

	private ReadFile rf;
	private Cellule[][] instance;
	private ArrayList<Cellule> cellsEligibleRouteur;
	private ArrayList<Cellule> cellsConnectToBackbone;
	private int number_of_cells_to_cover = 0, number_of_cells_cover;

	
	
	public ArrayList<Cellule> getCellsEligibleRouteur() {
		return cellsEligibleRouteur;
	}

	public void setCellsEligibleRouteur(ArrayList<Cellule> cellsEligibleRouteur) {
		this.cellsEligibleRouteur = cellsEligibleRouteur;
	}

	public ArrayList<Cellule> getCellsConnectToBackbone() {
		return cellsConnectToBackbone;
	}

	public void setCellsConnectToBackbone(ArrayList<Cellule> cellsConnectToBackbone) {
		this.cellsConnectToBackbone = cellsConnectToBackbone;
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
	public String getSolutionPathName() {
		return null;
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
		for (int i = 1; i < instance.length; i++) {
			for (int j = 0; j < instance[0].length; j++) {
				if (instance[i][j].getStatut().equals(".")) {
					list.add(instance[i][j]);
					this.number_of_cells_to_cover++;
				} else if (instance[i][j].getStatut().equals("-"))
					list.add(instance[i][j]);
			}
		}
		return list;
	}

	private List<Cellule> removeCellOfList(ArrayList<Cellule> list_of_cells, Cellule cell) {
		List<Cellule> listToReturn = list_of_cells;
		for (Cellule c : listToReturn) {
			if (c.getX() == cell.getX() && c.getY() == cell.getY()) {
				listToReturn.remove(c);
				return listToReturn;
			}
		}
		return listToReturn;
	}

	public void solve() {
		/*
		 * On choisit l'efficacit� minimale que doit avoir un routeur pour qu'il
		 * soit eligible � �tre plac�
		 */
		int eff = 300;
		int cost = 0;
		int born1 = 0;
		while (born1 != 0) {
			for (int j = 0; j < rf.getNbColumn(); j++) {
				for (int i = 0; i < rf.getNbRow(); i++) {
					if (instance[i][j].getStatut().equals(".")){
						born1 = j;
					}
					}
				}
		}
		int born2 = -1;
		while (born2 == -1) {
			for (int j = 0; j < rf.getNbColumn(); j++) {
				for (int i = 0; i < rf.getNbRow(); i++) {
					if(instance[rf.getNbRow()-i][rf.getNbColumn()-j].getStatut().equals("."));
						born2  = j;
					}
				}
			}

		int prixinstall = (rf.getNbColumn() - 1) * rf.getBackboneCost();
		if (cost + prixinstall <= rf.getBudget()) {
			for (int i = 0; i < rf.getNbColumn(); i++) {
				instance[rf.getxInitBackbone()][i].setConnectToBackbone(true);
				this.cellsConnectToBackbone.add(instance[rf.getxInitBackbone()][i]);
			}
			cost += prixinstall;
		}

		Cellule coordMax = new Cellule();
		List<Cellule> listCoordMax = new ArrayList<>();
		for (int j = 0; j < rf.getNbColumn(); j++) {
			for (int i = 0; i < rf.getNbRow(); i++) {
				if (!instance[i][j].getStatut().equals("#")) {
					List<Cellule> listCoord = CBIS(i, j);
					// si efficacit� > eff
					if (listCoord.size() > eff) {
						coordMax.setX(i);
						coordMax.setY(j);
						listCoordMax = listCoord;
						int maxlength = listCoord.size();
						int distanceMin = Integer.MAX_VALUE;
						// On recup�re la cellule connect�e au backbone la plus
						// proche de la colonne
						Cellule celluleReliee = new Cellule();
						for (Cellule backbone_cell : this.cellsConnectToBackbone) {
							if (backbone_cell.getY() == j) {
								int r = backbone_cell.getX();
								int c = backbone_cell.getY();
								int distance = Math.max(Math.abs(coordMax.getX() - r - 1),
										Math.abs(coordMax.getY() - c - 1));
								if (distance < distanceMin) {
									distanceMin = distance;
									celluleReliee.setX(r);
									celluleReliee.setY(c);
								}
							}
						}

						int prixInstall = rf.getRouteurCost() + distanceMin * rf.getBackboneCost();
						if (cost + prixInstall <= rf.getBudget() && prixInstall < 1000 * maxlength) {
							instance[coordMax.getX()][coordMax.getY()].setRouterOn(true);
							// On relie le routeur � cellule Reli�e en ajoutant
							// des jetons au backbone
							List<Cellule> listBackbone = PCC(coordMax, celluleReliee);
							for (Cellule cell_to_connect : listBackbone) {
								instance[cell_to_connect.getX()][cell_to_connect.getY()].setConnectToBackbone(true);
							}
							// couvre les cellules couvertes par le nouveau
							// routeur plac�
							for (Cellule cell_now_couverte : listCoordMax) {
								instance[cell_now_couverte.getX()][cell_now_couverte.getY()].setCouvert(true);
								this.number_of_cells_cover++;
							}
							cost += prixInstall;
						}
						System.out.println("new cost = " + cost + " taille de liste restante : "
								+ this.cellsEligibleRouteur.size());
						removeCellOfList(this.cellsEligibleRouteur, coordMax);
					}
					
				}
			}
		}
		System.out.println("FIN");
	}

	// Affiche le r�sultat final
	public static void main(String[] args) {
		int nbRouteur = 0;
		Heuristique1 h = new Heuristique1();
		h.intitialisation();
		h.solve();
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		for (int row = 0; row < h.getRf().getNbRow(); row++) {
			for (int col = 0; col < h.getRf().getNbColumn(); col++) {
				if (h.getInstance()[row][col].isRouterOn()){
					System.out.print("R");
					nbRouteur++;
				}
				else if (h.getInstance()[row][col].isConnectToBackbone())
					System.out.print("B");
				else
					System.out.print(h.getInstance()[row][col].getStatut());
			}
			System.out.println();
		}
		
		
		
		System.out.println("Score : " + (1000*h.getNumber_of_cells_cover() + (h.getRf().getBudget() - (h.getCellsConnectToBackbone().size()*h.getRf().getBackboneCost() + nbRouteur*h.getRf().getRouteurCost()) )) );
	}

	private List<Cellule> PCC(Cellule coordMax, Cellule celluleReliee) {
		ArrayList<Cellule> listToConnect = new ArrayList<>();
		// sameColumn
		if (celluleReliee.getY() == coordMax.getY()) {
			for (int i = Math.min(coordMax.getX(), celluleReliee.getX()); i < Math.max(coordMax.getX(),
					celluleReliee.getX()); i++) {
				instance[i][celluleReliee.getY()].setConnectToBackbone(true);
				listToConnect.add(instance[i][celluleReliee.getY()]);
			}
		}
		removeCellOfList(listToConnect, instance[celluleReliee.getX()][celluleReliee.getY()]);
		this.cellsConnectToBackbone.addAll(listToConnect);
		return listToConnect;
	}

	private List<Cellule> CBIS(int a, int b) {
		List<Cellule> cbisab = new ArrayList<>();
		for (int r = Math.max(0, a - rf.getRangeRouter()); r < Math.min(a + rf.getRangeRouter(), rf.getNbRow()); r++) {
			for (int c = Math.max(0, b - rf.getRangeRouter()); c < Math.min(b + rf.getRangeRouter(),
					rf.getNbColumn()); c++) {
				if (!instance[r][c].isCouvert() && instance[r][c].getStatut().equals(".")) {
					boolean isWall = false;
					for (int w = Math.min(a, r); w <= Math.max(a, r); w++) {
						for (int v = Math.min(b, c); v <= Math.max(b, c); v++) {
							if (instance[w][v].getStatut().equals("#"))
								isWall = true;
						}
					}
					if (!isWall)
						cbisab.add(instance[r][c]);
				}
			}
		}
		return cbisab;
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

}
