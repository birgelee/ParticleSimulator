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
public class PointSource implements FieldSource {
    Vector3D position;
    double magnitude;

    public PointSource(Vector3D position, double magnitude) {
        this.position = position;
        this.magnitude = magnitude;
    }

    @Override
    public Vector3D get(Vector3D position) {
        Vector3D displacement = Vector3D.subtract(this.position, position);
        double strength =  -(magnitude) / (displacement.getLength() * displacement.getLength());
        return Vector3D.normalize(displacement).multiply(strength);
    }
    
}
