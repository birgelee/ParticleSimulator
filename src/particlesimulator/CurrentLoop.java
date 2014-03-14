/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesimulator;

/**
 *
 * @author Henry
 */
public class CurrentLoop implements FieldSource {
    
    private Vector3D position;
    private Vector3D direction;
    double current;

    double radius;

    public CurrentLoop(Vector3D position, Vector3D direction, double current, double radius) {
        this.position = position;
        this.direction = direction;
        this.current = current;
        this.radius = radius;
    }
    
    
    
    private static double u0 = 4 * Math.PI * Math.pow(10, -7);
    public static double Bo(double i, double a) {
        return Bo(i, a, u0);
    }
    public static double Bo(double i, double a,double u) {
        return i*u/2D/a;
    }
    public static double al(double r, double a) {
        return r / a;
    }
    public static double be(double x, double a) {
        return x / a;
    }
    private static double ga(double x, double r) {
        return x / r;
    }
    private static double Q(double r, double x, double a) {
        return Math.pow((1 + al(r,a)), 2) + Math.pow(be(x,a), 2);
    }
    
    private static double k(double r, double x, double a) {
        return Math.sqrt(4 * al(r,a)/Q(r, x, a));
    }
    
    private static double K(double k) {
        return ElipticalIntegralCalculator.completeEllipticIntegralFirstKind(k * k);
    }
    private static double E(double k) {
        return ElipticalIntegralCalculator.completeEllipticIntegralSecondKind(k * k);
    }
    public static double Baxial(double i, double a, double x) {
        return Baxial(i, a, x, u0);
    }
    public static double Baxial(double i, double a, double x, double u) {
        if (a == 0) {
            if (x == 0) {
                return Double.MAX_VALUE;
            } else {
                return 0.0;
            }
        } else {
            return (u * i * a * a) / 2.0 / Math.pow((a * a + x * x), (1.5));
        }
    }
    
    public static double Bx(double i, double a, double x, double r) {
        double result;
        if (r == 0) {
            if (x == 0) {
                result = Bo(i, a);
            } else {
                result = Baxial(i, a, x);
            }
        } else {
            result = Bo(i, a)
                    * (E(k(r, x, a)) * ((1.0 - Math.pow(al(r, a), 2) - Math.pow(be(x, a), 2)) / (Q(r, x, a) - 4 * al(r, a))) + K(k(r, x, a)))
                    / Math.PI / Math.sqrt(Q(r, x, a));
        }
        if (Double.isNaN(result)) {
            System.out.println("nan computation in current loop in bx");
        }
        return result;
    }
    
    public static double Br(double i, double a, double x, double r) {
        double result;
        if (r == 0) {
            result =  0;
        } else {
            result = Bo(i, a) * ga(x, r)
                    * (E(k(r, x, a)) * ((1.0 + Math.pow(al(r, a), 2) + Math.pow(be(x, a), 2)) / (Q(r, x, a) - 4 * al(r, a))) - K(k(r, x, a)))
                    / Math.PI / Math.sqrt(Q(r, x, a));
        }
        if (Double.isNaN(result)) {
            System.out.println("nan computation in current loop br");
        }
        return result;
    }
    
    @Override
    public Vector3D get(Vector3D position) {
        Vector3D displacement = Vector3D.subtract(position, this.position);
        //displacement.subtract(direction);
        boolean shouldInvert = false;
        if (displacement.y < 0) {
            shouldInvert = true;
            displacement.y = -displacement.y;
        }
        double[] cylCord = displacement.getCylindricalCordinates();
        double x = cylCord[2];
        double r = cylCord[0];
        double br = Br(current, radius, x, r);
        double bx = Bx(current, radius, x, r);
        Vector3D result;
        if (r > 0) {
            result = new Vector3D(br, cylCord[1], bx, Vector3D.CoordinateSystem.CYLINDRICAL);
        } else {
            result = new Vector3D(-br, cylCord[1], bx, Vector3D.CoordinateSystem.CYLINDRICAL);
        }
        if (shouldInvert) {
            result.y = -result.y;
        }
        //result.addAngle(direction);
        return result;
    }
    
}
