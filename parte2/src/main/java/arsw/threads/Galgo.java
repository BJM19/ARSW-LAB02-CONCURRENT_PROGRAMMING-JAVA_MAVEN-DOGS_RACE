package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 * 
 * @author rlopez
 * 
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;
	private boolean corriendo;

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl=reg;
		corriendo=false;
	}

	public void corra() throws InterruptedException {
		corriendo=true;
		while (paso < carril.size()) {			
			Thread.sleep(100);
			synchronized (this){
				while(!corriendo){
					this.wait();
				}
			}
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);

				if (paso == carril.size()) {
					carril.finish();
					int ubicacion;
					synchronized(regl) {
						ubicacion = regl.getUltimaPosicionAlcanzada();
						regl.setUltimaPosicionAlcanzada(ubicacion + 1);
						System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
					}
					if (ubicacion == 1) {
						regl.setGanador(this.getName());
					}

			}
		}
	}

	public synchronized void setCorriendo(boolean estado){
		corriendo=estado;
		if(corriendo){
			this.notifyAll();
		}
	}

	@Override
	public void run() {
		
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
