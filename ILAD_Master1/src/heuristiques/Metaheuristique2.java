package heuristiques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

import fichier.WriteSolution;
import model.Cellule;

public class Metaheuristique2 {

	public static void main(String[] args) {
		Metaheuristique2 meta = new Metaheuristique2();
		meta.solve(20, 10, "charleston_road");
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
	
	public void solve(int purcentOfRouterSelect, int n, String path){
		Heuristique2 h = new Heuristique2();
		h.intitialisation(path);
		h.solve();
		int score = h.getScore();
		System.out.println("FIRST SOLUCE : "+ score);
		for(int i = 0; i < n; i++){
			Heuristique2 h2 = new Heuristique2();
			h2.intitialisation(path);
			ArrayList<Cellule> cellEligible = h2.getCellsEligibleRouteur();
			ArrayList<Cellule> cellWithRouter = h2.getCellsWithRouter();
			int numberOfRouterSelect = Math.round(cellWithRouter.size() * purcentOfRouterSelect / 100);
			double numberOfRouter = cellWithRouter.size();
			for(int j = 0; j < numberOfRouterSelect; j++){
				int router = (int) Math.round(Math.random() * numberOfRouter);
				Cellule cell = cellWithRouter.get(router);
				removeCellOfList(cellEligible, cell);
			}
			h2.setCellsEligibleRouteur(cellEligible);
			System.out.println("NEW SOLVE");
			h2.solve();
			if(h2.getScore() > score ){
				System.out.println("Ancien Score : " + score + " Nouveau Score : " + h2.getScore());
				h = h2;
				score = h2.getScore();
			}else
				System.out.println("PAS MIEUX !");
		}
		WriteSolution.writeSolution(h);
		
	}

}
