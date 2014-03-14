/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesimulator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Henry
 */
public class Solenoid implements FieldSource {
    
    public Solenoid(double radious, double loopsDencity, double length, double current, Vector3D position) {
        this(radious, loopsDencity, length, current, 5, position);
    }
    List<CurrentLoop> loops = new ArrayList<CurrentLoop>();
    public Solenoid(double radious, double loopsDencity, double length, double current, double loopsPerARealLoop, Vector3D position) {
        double loopCount = loopsDencity * length * loopsPerARealLoop;
        double fakeCurrent = current / loopsPerARealLoop;
        int iloopCount = (int) Math.round(loopCount);
        for (int i = 0; i < iloopCount; i++) {
            loops.add(new CurrentLoop(new Vector3D(position.x, position.y, position.z + (((double)i) / ((double)loopCount)) * length), new Vector3D(0, 0, 1), fakeCurrent, radious));
        }
    }

    @Override
    public Vector3D get(Vector3D position) {
        Vector3D result = new Vector3D();
        for (CurrentLoop currentLoop : loops) {
            result.add(currentLoop.get(position));
        }
        return result;
    }
    
}
