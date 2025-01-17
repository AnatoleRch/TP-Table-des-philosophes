package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " mange...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
    public void run() {
        while (running) {
            boolean bG = false;
            boolean bD = false;
            try {
                think();
                // Aléatoirement prendre la baguette de gauche puis de droite ou l'inverse
                switch(new Random().nextInt(2)) {
                    case 0:
                        bG=myLeftStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        bD=myRightStick.take();
                        break;
                    case 1:
                        bD=myRightStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        bG=myLeftStick.take();
                }
                // Si on arrive ici, on a pu "take" les 2 baguettes
                if(bG && bD){ 
                    eat();
                }
                // On libère les baguettes :
                if(bG){ 
                    myLeftStick.release();
                }
                if(bD){ 
                    myRightStick.release();
                }
                // try again
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}
