package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

/*
* Simulacion de Calles cruzadas simulando el problema de mutua exclusion con semaforos.
* La simulacion consiste en dos calles cruzadas donde circulan dos autos por cada una.
* Unos en sentido vertical y otros en sentido horizontal, donde tiene un punto que puede ser el punto
* que genera mutua exclusion que es la intersección de ambas calles. Por ende se usan semaforos para cancelar eso
* con una estructura como se ve abajo:
*
*                 ↓
*
*   semaforo1    |a |
	             |u |
	             |t |
	             |o |
 ----------------   ---------------
            auto             auto           Semaforo2
-----------------   ---------------
                 |a |
                 |u |
                 |t |
                 |o |

*
* Tanto el semaforo 1 y 2 evitan que los autos se choquen en el cruze de las calles
*Regulando que primero circulen los de la calle horizontal y
* */

class Calles_Cruzadas extends JPanel {
    private Semaphore semaforo1, semaforo2;
    private boolean semaforo1Verde = true;
    private boolean semaforo2Verde = false;
    private int auto1Pos = 50;

    private int auto2Pos = 700;

    private int auto3pos = 1450;

    private int auto4pos = 50;

    public Calles_Cruzadas() {
        semaforo1 = new Semaphore(1);
        semaforo2 = new Semaphore(0);

        new Thread(new Car(1)).start();
        new Thread(new Car(2)).start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(14000);
                    Semaforo();
                    repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void Semaforo() {
        semaforo1Verde = !semaforo1Verde;
        semaforo2Verde = !semaforo2Verde;
        if (semaforo1Verde) {
            semaforo1.release();
            try {
                semaforo2.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            semaforo2.release();
            try {
                semaforo1.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY); // Fondo gris
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(0,200,getWidth(),50);
        g.setColor(Color.BLACK);
        g.fillRect(0,220,getWidth(),50);
        g.setColor(Color.BLACK);
        g.fillRect(720,10,50,1000);
        g.setColor(Color.BLACK);
        g.fillRect(700,10,50,1000);
        g.setColor(semaforo1Verde ? Color.GREEN : Color.RED);
        g.fillRect(1500, 100, 30, 100);
        g.setColor(semaforo2Verde ? Color.GREEN : Color.RED);
        g.fillRect(600, 10, 100, 30);
        g.setColor(Color.BLUE);
        g.fillRect(auto1Pos, 200, 50, 30);
        g.setColor(Color.ORANGE);
        g.fillRect(700, auto2Pos, 30, 50);
        g.setColor(Color.red);
        g.fillRect(auto3pos, 240, 50, 30);
        g.setColor(Color.magenta);
        g.fillRect(740, auto4pos, 30, 50);
    }

    private class Car implements Runnable {
        private int id;

        public Car(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (id == 1) {
                        // Intento tomar el semaforo para que los autos circulen
                        semaforo1.acquire();
                        while (auto1Pos < 1450 && auto3pos>50) {
                            auto1Pos += 10;
                            auto3pos -=10;
                            repaint();
                            Thread.sleep(80);
                        }
                    } else {
                        // Intento tomar el semaforo para que los autos circulen
                        semaforo2.acquire();
                        while (auto2Pos > 50 && auto4pos<700) {
                            auto2Pos -= 10;
                            auto4pos+=10;
                            repaint();
                            Thread.sleep(80);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class App {
    public static void main(String[] args) {
        // Creo la ventana para visualizar la simulación
        JFrame frame = new JFrame("Mutua exclusión calles intersectadas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        Calles_Cruzadas interseccion = new Calles_Cruzadas();
        frame.add(interseccion);
        frame.setVisible(true);
    }
}

