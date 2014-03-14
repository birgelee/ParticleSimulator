/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Henry
 */
public class SourceField implements VectorField {
    
    private List<FieldSource> sources = new ArrayList<FieldSource>();
    
    
    public List<FieldSource> getSources() {
        return sources;
    }
    
    @Override
    public Vector3D get(Vector3D position) {
        Vector3D result = new Vector3D();
        for (FieldSource fieldSource : sources) {
            result.add(fieldSource.get(position));
        }
        return result;
    }
    
}
