package fichier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import heuristiques.Heuristique1;
import heuristiques.Heuristique2;
import model.Cellule;

public class WriteSolution {

	public static void writeSolution(Heuristique2 h) {
		ArrayList<Cellule> backbone = h.getCellsConnectToBackbone();
		ArrayList<Cellule> routeur = h.getCellsWithRouter();
		try {
			FileWriter writer = new FileWriter("solution/SubmissionFile/heuristique2/"+h.getInstanceName()+".txt");
			BufferedWriter bw = new BufferedWriter(writer);

			bw.write(backbone.size() + "\n");
			for (Cellule c : backbone){
				if(!(c.getX() == h.getRf().getxInitBackbone() && c.getY() == h.getRf().getyInitBackbone()))
					bw.write("(" + c.getX() + "," + c.getY() + ")\n");
			}
			bw.write(routeur.size() + "\n");
			for (Cellule c : routeur)
				bw.write("(" + c.getX() + "," + c.getY() + ")\n");
			
			bw.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		try {
			FileWriter writer = new FileWriter("solution/MapFile/heuristique2/"+h.getInstanceName()+".txt");
			BufferedWriter bw2 = new BufferedWriter(writer);
			for (int row = 0; row < h.getRf().getNbRow(); row++) {
				for (int col = 0; col < h.getRf().getNbColumn(); col++) {
					if (h.getInstance()[row][col].isRouterOn())
						bw2.write("R");
					else if (h.getRf().getxInitBackbone() == row && h.getRf().getyInitBackbone() == col)
						bw2.write("I");
					else if (h.getInstance()[row][col].isConnectToBackbone())
						bw2.write("B");
					// else if(h.getInstance()[row][col].isCouvert())
					// bw.write("C");
					else
						bw2.write(h.getInstance()[row][col].getStatut());

				}
				bw2.write("\n");
			}
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SCORE : " + h.getScore());
	}
	
	public static void writeSolution(Heuristique1 h) {
		ArrayList<Cellule> backbone = h.getCellsConnectToBackbone();
		ArrayList<Cellule> routeur = h.getCellsWithRouteur();
		try {
			FileWriter writer = new FileWriter("solution/SubmissionFile/heuristique1/"+h.getInstanceName()+".txt");
			BufferedWriter bw = new BufferedWriter(writer);

			bw.write(backbone.size() + "\n");
			for (Cellule c : backbone){
				if(!(c.getX() == h.getRf().getxInitBackbone() && c.getY() == h.getRf().getyInitBackbone()))
					bw.write("(" + c.getX() + "," + c.getY() + ")\n");
			}
			bw.write(routeur.size() + "\n");
			for (Cellule c : routeur)
				bw.write("(" + c.getX() + "," + c.getY() + ")\n");
			bw.close();
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		try {
			FileWriter writer = new FileWriter("solution/MapFile/heuristique1/"+h.getInstanceName()+".txt");
			BufferedWriter bw2 = new BufferedWriter(writer);
			for (int row = 0; row < h.getRf().getNbRow(); row++) {
				for (int col = 0; col < h.getRf().getNbColumn(); col++) {
					if (h.getInstance()[row][col].isRouterOn())
						bw2.write("R");
					else if (h.getRf().getxInitBackbone() == row && h.getRf().getyInitBackbone() == col)
						bw2.write("I");
					else if (h.getInstance()[row][col].isConnectToBackbone())
						bw2.write("B");
					// else if(h.getInstance()[row][col].isCouvert())
					// bw.write("C");
					else
						bw2.write(h.getInstance()[row][col].getStatut());

				}
				bw2.write("\n");
			}
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("SCORE : " + h.getScore());
	}

}
