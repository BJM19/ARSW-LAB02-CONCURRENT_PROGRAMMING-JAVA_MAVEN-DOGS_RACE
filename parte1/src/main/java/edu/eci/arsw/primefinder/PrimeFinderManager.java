package edu.eci.arsw.primefinder;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Scanner;

public class PrimeFinderManager{

    private ArrayList<PrimeFinderThread> primeFinderThreads;
    private int threadNumber;

    public PrimeFinderManager (int threadNumber){
        this.threadNumber = threadNumber;
        primeFinderThreads = new ArrayList<>();
        crearHilos();
        try {
            iniciarHilos();
            Thread.sleep(5*1000);
            detenerHilos();
            reanudarHilos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void crearHilos(){
        int division = 30000000 / threadNumber;
        int start=0, target=division;
        //Crear Hilos
        for (int i=0; i<threadNumber; i++) {
            PrimeFinderThread pft = new PrimeFinderThread(start, target);
            primeFinderThreads.add(pft);
            start = target + 1;
            target += division;
        }
    }

    private void iniciarHilos() throws InterruptedException {
        for(PrimeFinderThread primeFinderThread : primeFinderThreads){
            primeFinderThread.start();
        }
    }

    private void detenerHilos() throws InterruptedException {
        for(PrimeFinderThread primeFinderThread : primeFinderThreads){
            primeFinderThread.setRunning(false);
        }
    }

    private synchronized void reanudarHilos(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter To Continue");
        while (!scanner.nextLine().equals("")){
            System.out.println("Press Enter To Continue");
        }
        for (PrimeFinderThread primeFinderThread : primeFinderThreads){
            primeFinderThread.setRunning(true);
        }
    }

}
