package model;

public class Cellule {
	
	private boolean couvert;
	private int x;
	private int y;
	private boolean connectToBackbone;
	private boolean routerOn;
	private String statut;
	
	public Cellule(){
		
	}
	
	public Cellule(int x, int y, String statut){
		this.x = x;
		this.y = y;
		this.statut = statut;
	}
	
	public Cellule(boolean couvert, int x, int y, boolean connectToBackbone, boolean routerOn, String statut) {
		super();
		this.couvert = couvert;
		this.x = x;
		this.y = y;
		this.connectToBackbone = connectToBackbone;
		this.routerOn = routerOn;
		this.statut = statut;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public boolean isCouvert() {
		return couvert;
	}

	public void setCouvert(boolean couvert) {
		this.couvert = couvert;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isConnectToBackbone() {
		return connectToBackbone;
	}

	public void setConnectToBackbone(boolean connectToBackbone) {
		this.connectToBackbone = connectToBackbone;
	}

	public boolean isRouterOn() {
		return routerOn;
	}

	public void setRouterOn(boolean routerOn) {
		this.routerOn = routerOn;
	}

}
