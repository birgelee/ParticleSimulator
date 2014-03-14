package particlesimulator;

import com.jme3.math.Vector3f;
import java.awt.geom.Point2D;


public class Vector3D {
    
    public enum CoordinateSystem {
        CYLINDRICAL,
        SPHERICAL,
        RECTANGULAR
    }
    public static Vector3D fromVector3f(Vector3f o) {
        return new Vector3D(o.x,o.y
                ,o.z);
    }
    public static double dot(Vector3D vector1, Vector3D vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }
    
    public static Vector3D cross(Vector3D u, Vector3D v) {
        return new Vector3D(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x);
    }
    public static Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    
    public static Vector3D subtract(Vector3D a, Vector3D b) {
        return new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    
    public static Vector3D multiply(Vector3D vector, double scalar) {
        return new Vector3D(vector.x * scalar,vector.y * scalar,vector.z * scalar);
    }
    
    public static Vector3D divide(Vector3D vector, double scalar) {
        return new Vector3D(vector.x / scalar, vector.y / scalar, vector.z / scalar);
    }
    
    public static double lengthSquared(Vector3D vector) {
        return Math.pow(vector.x, 2) + Math.pow(vector.y, 2) + Math.pow(vector.z, 2);
    }
    
    public static double length(Vector3D vector) {
        return Math.sqrt(lengthSquared(vector));
    }
    
    public static Vector3D normalize(Vector3D vector) {
        Vector3D value = new Vector3D(vector);
        value.divide(length(value));
        return value;
    }
    
    public static double getAngle(double x , double y) {
        if (x > 0) {
            return Math.atan(y / x);
        }
        else if (x < 0) {
            return Math.atan(y / x) + Math.PI;
        } else if (y > 0) {
            return -Math.PI / 2;
        } else {
            return Math.PI / 2;
        }
    }
    
    /*
     * The vector with theta = 0 is [0,0,1]
     */
    public static Point2D.Double getAngle(Vector3D vector) {
        double theta = Math.asin(vector.y / vector.getLength());
        double phi = Math.asin(vector.x / vector.getR());
        
        return new Point2D.Double(theta, phi);
    }
    
    public static Vector3D scale(Vector3D vector, double length) {
        return new Vector3D(vector).normalize().multiply(length);
    }
    
    public static Vector3D clamp(Vector3D vector, double length) {
        if (length(vector) > length) {
            return scale(vector, length);
        }
        return new Vector3D(vector);
    }
    
    public double x;
    public double y;
    public double z;
    
    public Vector3D() {
        this(0, 0, 0);
    }
    
    public Vector3D(Vector3D o) {
        this(o.x, o.y, o.z);
    }
    /*
     In the three systems, the vars are:
     * Speical coordinates: r, theta, phi
     * Clyindrical coordinates: roe, theta, z
     * Rectangular coordinates: x, y, z
     */
    public Vector3D(double a, double b, double c, CoordinateSystem coordinateSystem) {
        if (coordinateSystem == CoordinateSystem.SPHERICAL) {
            double r = a, theta = b, phi = c;
            double rOnGround = Math.sin(theta) * r;
            y = Math.cos(theta) * r;
            x = Math.sin(phi) * rOnGround;
            z = Math.cos(phi) * rOnGround;
        } else if (coordinateSystem == CoordinateSystem.RECTANGULAR) {
            x = a;
            y = b;
            z = c;
        } else if (coordinateSystem == CoordinateSystem.CYLINDRICAL) {
            z = c;
            y = a*Math.sin(b);
            x = a*Math.cos(b);
        }
    }
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    /*
     * The length of the progection of this 3D vector on the plane defined by the x and z axises
     */
    public double getR() {
        return Math.sqrt(x *x + z *z);
    }
    /*
     * Retunr value is of the format rho, phi, z
     */
    public double[] getCylindricalCordinates() {
        double[] result = {0,0,z};
        result[0] = Math.sqrt(x*x+y+y);
        if (x == 0 && y == 0) {
            result[1] = 0;
        } else if (x >= 0) {
            result[1] = Math.atan(y/Math.sqrt(x*x+y+y));
        } else {
            result[1] = -Math.atan(y/Math.sqrt(x*x+y+y)) + Math.PI;
        }
        return result;
    }
    
    public Vector3D setAngle(double theta, double phi) {
        double magnitude = length(this);
        this.copy(new Vector3D(magnitude, theta, phi, CoordinateSystem.CYLINDRICAL));
        return this;
    }
    public Vector3D add(Vector3D o) {
        x += o.x;
        y += o.y;
        z += o.z;
        return this;
    }
    
    public Point2D.Double getAngle() {
        return getAngle(this);
    }
    public Vector3D addAngle(double longitude, double lattitude) {
        Point2D.Double originalAngle = getAngle(this);
        Point2D.Double newAngle = new Point2D.Double(originalAngle.x + longitude,originalAngle.y + lattitude);
        this.setAngle(newAngle.x, newAngle.y);
        return this;
    }
    public Vector3D addAngle(Vector3D o) {
        Point2D.Double ang = getAngle(o);
        return addAngle(ang.x, ang.y);
    }
    public Vector3D subtract(Vector3D o) {
        x -= o.x;
        y -= o.y;
        z -= o.z;
        return this;
    }
    public Vector3D copy(Vector3D o) {
        x = o.x;
        y = o.y;
        z = o.z;
        return this;
    }
    public Vector3D multiply(double scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }
    
    public Vector3D divide(double scalar) {
        x /= scalar;
        y /= scalar;
        z /= scalar;
        return this;
    }
    
    public Vector3D subtractAngle(double theta, double phi) {
        Point2D.Double currentAngle = getAngle(this);
        currentAngle.x -= theta;
        currentAngle.y -= phi;
        setAngle(theta, phi);
        return this;
    }
    
    public Vector3D subtractAngle(Vector3D o) {
        Point2D.Double ang = getAngle(o);
        return subtractAngle(ang.x, ang.y);
    }
    
    
    public Vector3D normalize() {
        divide(length(this));
        return this;
    }
    
    public Vector3D scale(double length) {
        normalize();
        multiply(length);
        return this;
    }
    
    public Vector3D clamp(double length) {
        if (length(this) > length) {
            normalize();
            multiply(length);
        }
        return this;
    }
    public double getLength() {
        return length(this);
    }
    
    public Vector3f toVector3f() {
        return new Vector3f((float)x, (float)y, (float)z);
    }
    
    
    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
    
    public String toCylString() {
       double[] cylcord = getCylindricalCordinates();
       return "["+cylcord[0] +", " + cylcord[1] + ", " + cylcord[2] +"]";
    }
    
}
