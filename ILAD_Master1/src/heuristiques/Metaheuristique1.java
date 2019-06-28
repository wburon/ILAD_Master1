package heuristiques;

import java.util.ArrayList;

import fichier.WriteSolution;

public class Metaheuristique1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void solve(String path){
		ArrayList<Integer> listetabou = new ArrayList<>();
		listetabou.add(300);
		Heuristique1 h = new Heuristique1();
		h.intitialisation(path);
		Heuristique1 hstock = h;
		h.solve();
		int score = h.getScore();
		int besteff = 300;
		for(int i =0; i < 10; i++){
			Heuristique1 h3 = hstock;
			int neweff = (int) Math.round((Math.random() * ( Math.pow(h.getRf().getRangeRouter()*2+1,2) - 0 )));
			while(listetabou.contains(neweff)){
				neweff = (int) Math.round((Math.random() * ( Math.pow(h.getRf().getRangeRouter()*2+1,2) - 0 )));
			}
			listetabou.add(neweff);
			h3.setEff(neweff);
			System.out.println("Solve with eff = " + neweff);
			h3.solve();
			if(h3.getScore() > score){
				System.out.println("Ancien Score : " + score + " Nouveau Score : " + h3.getScore());
				besteff = neweff;
				score = h3.getScore();
				h = h3;
			}else
				System.out.println("PAS MIEUX !");
		}
		System.out.println("BEST EFF : " + besteff);
		WriteSolution.writeSolution(h);
	}

}
