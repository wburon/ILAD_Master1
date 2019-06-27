package model;

public abstract class Heuristique {
	
	public abstract void intitialisation(String path);
	public abstract String getSolutionPathName();
	public abstract void solve();

}
