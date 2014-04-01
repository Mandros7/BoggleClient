package es.uniovi.alumno;
import es.uniovi.computadores.Network;

public class Prueba {

	public static void main(String[] args) throws InterruptedException {
		Network red = new Network();
		Client c = new Client(red);
		System.out.println("Funcionando");
	}

}