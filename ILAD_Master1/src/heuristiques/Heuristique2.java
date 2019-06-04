package heuristiques;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fichier.ReadFile;
import model.Cellule;
import model.Heuristique;

public class Heuristique2 extends Heuristique{
	
	private ReadFile rf;
	private Cellule[][] instance;
	private List<Cellule> cellsEligibleRouteur;
	private List<Cellule> cellsConnectToBackbone;
	
	@Override
	public void intitialisation() {
		try {
			rf = new ReadFile();
			instance = rf.getInstance("instances/charleston_road");
			cellsEligibleRouteur = formListCelluleEligibleRouteur();
			cellsConnectToBackbone = initListBackbone();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<Cellule> initListBackbone() {
		int ix = rf.getxInitBackbone();
		int iy = rf.getyInitBackbone();
		List<Cellule> list = new ArrayList<>();
		instance[ix][iy].setConnectToBackbone(true);
		list.add(instance[ix][iy]);
		return list;
	}

	private List<Cellule> formListCelluleEligibleRouteur() {
		List<Cellule> list = new ArrayList<>();
		for(int i=1; i<instance.length; i++){
			for(int j = 0; j <instance[0].length; j++){
				if(instance[i][j].getStatut().equals("."))
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
		while(!(this.cellsEligibleRouteur).isEmpty()){
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
				int distance = Math.max(coordMax.getX()-r-1, coordMax.getY()-c-1);
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
				}
				cost += prixInstall;
			}
			this.cellsEligibleRouteur.remove(coordMax);
		}
		
		
		
	}

	private List<Cellule> PCC(Cellule coordMax, Cellule celluleReliee) {
		
	}

	private List<Cellule> CBIS(int a, int b) {
		List<Cellule> cbisab = new ArrayList<>();
		for (int r = Math.max(0, a - rf.getRangeRouter()); r <= Math.min(a + rf.getRangeRouter(), rf.getNbRow()); r++) {
			for (int c = Math.max(0, b - rf.getRangeRouter()); c <= Math.min(b + rf.getRangeRouter(),rf.getNbColumn()); c++) {
				if(!instance[r][c].isCouvert()){
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
	
	

}
