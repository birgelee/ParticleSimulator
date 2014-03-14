/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulator;

/**
 *
 * @author Henry
 */
public class Particle {
    private Vector3D position;
    private Vector3D velocity;
    private double charge;
    
    public Particle(Vector3D position, Vector3D velocity, double charge) {
        this.position = position;
        this.velocity = velocity;
        this.charge = charge;
    }
    
    private long lastUpdate = System.currentTimeMillis();
    public void updatePosition(long currentTime, Vector3D bField, Vector3D eField) {
        long millisDelta = currentTime - lastUpdate;
        double delta = ((double) millisDelta) / (double) 1000;
        lastUpdate = System.currentTimeMillis();
        updatePosition(delta, bField, eField);
    }
    public void updatePosition(double delta, Vector3D bField, Vector3D eField) {
        getVelocity().add(getAcceleration(bField, eField).multiply(delta));
        getPosition().add(Vector3D.multiply(getVelocity(), delta));
    }
    
    private Vector3D getAcceleration(Vector3D eField, Vector3D bField) { 
        Vector3D result = new Vector3D();
        result.add(Vector3D.multiply(eField, getCharge()));
        result.add(Vector3D.cross(Vector3D.multiply(getVelocity(), getCharge()), bField));
        return result;
    }
    public void fakeLstUpdate(long time) {
        lastUpdate = time;
    }

    /**
     * @return the position
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * @return the velocity
     */
    public Vector3D getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    /**
     * @return the charge
     */
    public double getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(double charge) {
        this.charge = charge;
    }
}
