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
public class UniformField implements  FieldSource {
    private Vector3D direction;

    public UniformField(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public Vector3D get(Vector3D position) {
        return direction;
    }
    
    
}
