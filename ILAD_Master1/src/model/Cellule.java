package model;

public class Cellule {
	
	private boolean couvert;
	private int x;
	private int y;
	private boolean connectToBackbone;
	private boolean routerOn;
	
	public Cellule(){
		
	}
	
	public Cellule(boolean couvert, int x, int y, boolean connectToBackbone, boolean routerOn) {
		super();
		this.couvert = couvert;
		this.x = x;
		this.y = y;
		this.connectToBackbone = connectToBackbone;
		this.routerOn = routerOn;
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
