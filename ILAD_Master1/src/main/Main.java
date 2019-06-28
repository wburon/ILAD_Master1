package main;

import java.util.Scanner;

import fichier.WriteSolution;
import heuristiques.Heuristique1;
import heuristiques.Heuristique2;
import heuristiques.Metaheuristique1;
import heuristiques.Metaheuristique2;

public class Main {

	public static void main(String[] args) {
		Scanner clavier = new Scanner(System.in);
		System.out.println("WELCOME");
		int choix = 0;
		String instanceName;
		while(choix != 5){
			System.out.println("1- Heuristique 1");
			System.out.println("2- Heuristique 2");
			System.out.println("3- Métaheuristique 1");
			System.out.println("4- Métaheuristique 2");
			System.out.println("5- QUITTER");
			choix =  clavier.nextInt();
			switch(choix){
			case 1 :
				System.out.print("Entrer le nom d'une instance : ");
				instanceName = clavier.next();
				Heuristique1 h = new Heuristique1();
				h.intitialisation(instanceName);
				h.solve();
				WriteSolution.writeSolution(h);
				break;
			case 2 :
				System.out.print("Entrer le nom d'une instance : ");
				instanceName = clavier.next();
				Heuristique2 h2 = new Heuristique2();
				h2.intitialisation(instanceName);
				h2.solve();
				WriteSolution.writeSolution(h2);
				break;
			case 3 :
				System.out.print("Entrer le nom d'une instance : ");
				instanceName = clavier.next();
				Metaheuristique1 m = new Metaheuristique1();
				m.solve(instanceName);
				break;
			case 4 :
				System.out.print("Entrer le nom d'une instance : ");
				instanceName = clavier.next();
				System.out.print("Entrer un pourcentage : (sans le caractère %)");
				int pourcent = clavier.nextInt();
				System.out.print("Entrer le nombre d'itération : ");
				int n = clavier.nextInt();
				Metaheuristique2 m2 = new Metaheuristique2();
				m2.solve(pourcent, n, instanceName);
				break;
			case 5 :
				System.out.println("BYE");
				break;
			default:
				System.out.println("Ce numéro n'est pas dans la liste !");
			}
		}
		
	}

}
