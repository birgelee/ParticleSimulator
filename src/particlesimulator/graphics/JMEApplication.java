/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesimulator.graphics;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.*;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.util.ArrayList;
import java.util.List;
import particlesimulator.Particle;
import particlesimulator.Vector3D;
import particlesimulator.VectorField;

/**
 *
 * @author Henry
 */
public class JMEApplication extends SimpleApplication {
    
    public boolean hasStarted = false;
    private List<Particle> particles = new ArrayList<Particle>();
    private List<Geometry> geometries = new ArrayList<Geometry>();
    private TerrainQuad terrain;
    Material mat_terrain;
   private Mesh line;
    private Geometry geomLine;
    VectorField eField, bField;

    private Arrow bFieldPointer;
    private Arrow eFieldPointer;
    private Geometry bFieldPointerGeom;
    public JMEApplication(List<Particle> particles, VectorField eField, VectorField bField) {
        super();
        this.particles = particles;
        this.eField = eField;
        this.bField = bField;
    }

    @Override
    public void update() {
        hasStarted = true;
        //line.setBuffer(VertexBuffer.Type.Position, 3, new float[]{0, 0, 0, cam.getLocation().x + 10, cam.getLocation().y, cam.getLocation().z});
        //bFieldPointerGeom.setLocalTranslation(cam.getLocation().add(cam.getDirection().normalize()));
        //bFieldPointer.setArrowExtent(bField.get(Vector3D.fromVector3f(cam.getLocation())).normalize().divide(2).toVector3f());
   
        super.update(); //To change body of generated methods, choose Tools | Templates.
        for (int i = 0; i < geometries.size(); i ++) {
            Particle particle = particles.get(i);
            geometries.get(i).setLocalTranslation(new Vector3f((float) particle.getPosition().x, (float) particle.getPosition().y,(float) particle.getPosition().z));
        }
    }
    
    public boolean hasStopped = false;
    @Override
    public void stop() {
        hasStopped = true;
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop(boolean waitFor) {
        hasStopped = true;
        super.stop(waitFor); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 7, 0));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(75);
        //bFieldPointer = new Arrow(bField.get(Vector3D.fromVector3f(cam.getLocation())).normalize().divide(2).toVector3f());
        //bFieldPointer.setLineWidth(4);
        //bFieldPointerGeom = putShape(bFieldPointer, ColorRGBA.Red);
        //bFieldPointerGeom.setLocalTranslation(cam.getLocation().add(cam.getDirection().normalize()));
        attachGrid(10000, ColorRGBA.Red);
        //Draw feild lines
        for (int x = -10 ; x <= 10; x++) {
                for (int z = -10; z <= 10; z++) {
                    float y = -.1f;
                    Vector3D bDirection = bField.get(new Vector3D(x, y, z));
                    bDirection.normalize().divide(2);
                    //bDirection.addAngle(Math.PI,0 );
                    Arrow arrow = new Arrow(new Vector3f((float)bDirection.x, (float)bDirection.y, (float)bDirection.z));
                    arrow.setLineWidth(4); // make arrow thicker
                    putShape(arrow, ColorRGBA.Red).setLocalTranslation(new Vector3f(x, y, z));
                }
        }
        
        for (Particle particle : particles) {
            Sphere s = new Sphere(50, 50, .3F); // create cube shape
            Geometry geom = new Geometry("Sphere", s);  // create cube geometry from the shape
            Material mat = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
            mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
            geom.setMaterial(mat);                   // set the cube's material
            geom.setLocalTranslation(new Vector3f((float) particle.getPosition().x, (float) particle.getPosition().y, (float) particle.getPosition().z));
            rootNode.attachChild(geom);              // make the cube appear in the scene
            geometries.add(geom);
        }
    }
    
    private Geometry putShape(Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
    
    private Geometry attachGrid(int size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe grid", new Grid(size, size, 10f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.center().move(new Vector3f(0,-12,0));
        rootNode.attachChild(g);
        return g;
    }
    
}
