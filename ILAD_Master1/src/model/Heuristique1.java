package model;

import java.util.List;

public class Heuristique1 extends Heuristique{
	
	@Override
	public void intitialisation() {
		
	}

	@Override
	public String getSolutionPathName() {
		return null;
	}

	@Override
	public void solve() {
	}
	
	public static List<Cellule> C(int a,int b) {
		//supprimer intitialisation
		int r = 0; 
		int c = 0;
		boolean isWall = new Boolean(true); 
		String s[][] = new String[r][c];
		for(int i=Math.min(0, a-r); i<Math.min(a+r, b); i++){
			for(int j=0; j<b-r; j++){
				if(s[i][j] == "t"){
					isWall = false; 
					
					for(int k=Math.min(a, r) ; k=Math.max(a, r); k++){
						for (int l=Math.min(b, b); )
					}
				}
					
				
			}
		}
		return null; 
	}
}
