/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulator;

import java.util.ArrayList;
import java.util.List;
import particlesimulator.graphics.JMEApplication;

/**
 *
 * @author Henry
 */
public class ParticleSimulator {

    /**
     * @param args the command line arguments
     */
    
    private static SourceField eField = new SourceField();
    private static SourceField bField = new SourceField();
    private static List<Particle> particles = new ArrayList<Particle>();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("starting app");
        System.out.println("vector: " + new Vector3D(1, 0, 0, Vector3D.CoordinateSystem.SPHERICAL));
        //System.out.println((new Vector3D(Math.PI / 4, Math.PI / 4, 30, true)).getLength());
        //particles.add(new Particle(new Vector3D(.1,0,-1), new Vector3D(.5,0,-.1), 30));
        particles.add(new Particle(new Vector3D(.1,0,-1.3), new Vector3D(.5,0,.3), 30));
        //particles.add(new Particle(new Vector3D(0,0,-1), new Vector3D(0,0,0), 1));
        //eField.getSources().add(new PointSource(new Vector3D(), 1));
        //eField.getSources().add(new PointSource(new Vector3D(0,0,1), 1));
        bField.getSources().add(new CurrentLoop(new Vector3D(), new Vector3D(0, 0, 2), Math.pow(10, 7), 1));
        //bField.getSources().add(new Solenoid(3, 1, 15, 100, new Vector3D(0,0, -7)));
        final JMEApplication app = new JMEApplication(particles, eField, bField);
        app.setShowSettings(false);
        Thread appStarter = new Thread(new Runnable() {

            @Override
            public void run() {
               app.start();
            }
        });
        appStarter.start();
        while (!app.hasStarted) {
            System.out.print("");
        }
        System.out.println("beginning particle engine");
        for (Particle p : particles) {
            //System.out.println(p.getPosition());
            p.fakeLstUpdate(System.currentTimeMillis());

        }
        while (!app.hasStopped) {
             for (Particle p : particles) {
                 //System.out.println(p.getPosition());
                  p.updatePosition(System.currentTimeMillis(), eField.get(p.getPosition()), bField.get(p.getPosition()));
                  Thread.sleep(1);
             }
        }
    }
    
}
