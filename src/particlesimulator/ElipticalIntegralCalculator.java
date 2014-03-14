/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesimulator;

/**
 *
 * @author Henry
 */
public class ElipticalIntegralCalculator {
    public static double completeEllipticIntegralFirstKind(double k)
    {
        double sum = 0;
        double term = 0;
        double above = 0;
        double below = 0;
        sum = 1;
        term = 1;

        above = 1;
        below = 2;

        for (int i = 1; i <= 100; i++)
        {
            term *= above / below;
            sum += Math.pow(k, i) * Math.pow(term, 2);
            above += 2;
            below += 2;
        }
        sum *= 0.5 * Math.PI;
        return sum;
    }

    /// <summary>
    /// Return the Complete Elliptic integral of the 2nd kind
    /// </summary>
    /// <param name="k">E(k^2) absolute value has to be below 1</param>
    /// <returns></returns>
    /// <remarks>Abramowitz and Stegun p.591, formula 17.3.12</remarks>
    public static double completeEllipticIntegralSecondKind(double k)
    {
        double sum = 0;
        double term = 0;
        double above = 0;
        double below = 0;
        sum = 1;
        term = 1;

        above = 1;
        below = 2;

        for (int i = 1; i <= 100; i++)
        {
            term *= above / below;
            sum -= Math.pow(k, i) * Math.pow(term, 2) / above;
            above += 2;
            below += 2;
        }
        sum *= 0.5 * Math.PI;
        return sum;
    }

    /// <summary>
    /// Returns the imcomplete elliptic integral of the first kind 
    /// </summary>
    /// <param name="angle">In degrees, valid value range is from 0 to 90</param>
    /// <param name="k">This function thakes k^2 as the parameter</param>
    /// <returns></returns>
    /// <remarks></remarks>
    public static double incompleteEllipticIntegralFirstKind(double angle, double k)
    {
        double result = 0;
        double ang = angle;
        result = Math.sin(ang) * RF(Math.pow(Math.cos(ang), 2), 1 - k * Math.pow(Math.sin(ang), 2), 1);
        return result;
    }

    /// <summary>
    /// Returns the imcomplete elliptic integral of the second kind 
    /// </summary>
    /// <param name="angle">In degrees, valid value range is from 0 to 90</param>
    /// <param name="k">This function thakes k^2 as the parameter</param>
    /// <returns></returns>
    /// <remarks></remarks>
    public static double incompleteEllipticIntegralSecondKind(double angle, double k)
    {
        double ang = angle;
        return Math.sin(ang) * RF(Math.pow(Math.cos(ang), 2), 1 - k * Math.pow(Math.sin(ang), 2), 1) + (-1D) / (3D) * k *Math.pow(Math.sin(ang), ((3D)))*RD(Math.pow(Math.cos(ang), 2), 1 - k * Math.pow(Math.sin(ang), 2), 1);
    }


    /// <summary>
    /// Computes the R_F from Carlson symmetric form
    /// </summary>
    /// <param name="X"></param>
    /// <param name="y"></param>
    /// <param name="z"></param>
    /// <returns></returns>
    /// <remarks>http://en.wikipedia.org/wiki/Carlson_symmetric_form#Series_Expansion</remarks>
    private static double RF(double X, double Y, double Z)
    {
        double result = 0;
        double A = 0;
        double lamda = 0;
        double dx = 0;
        double dy = 0;
        double dz = 0;
        double MinError = 1E-07;

        do
        {
            lamda = Math.sqrt(X * Y) + Math.sqrt(Y * Z) + Math.sqrt(Z * X);

            X = (X + lamda) / 4;
            Y = (Y + lamda) / 4;
            Z = (Z + lamda) / 4;

            A = (X + Y + Z) / 3;

            dx = (1 - X / A);
            dy = (1 - Y / A);
            dz = (1 - Z / A);

        } while (Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz)) > MinError);

        double E2 = 0;
        double E3 = 0;
        E2 = dx * dy + dy * dz + dz * dx;
        E3 = dy * dx * dz;

        //http://dlmf.nist.gov/19.36#E1
        result = 1 - (1 / 10) * E2 + (1 / 14) * E3 + (1 / 24) * Math.pow(E2, 2) - (3 / 44) * E2 * E3 - (5 / 208) * Math.pow(E2, 3) + (3 / 104) * Math.pow(E3, 2) + (1 / 16) * Math.pow(E2, 2) * E3;

        result *= (1 / Math.sqrt(A));
        return result;

    }

    /// <summary>
    /// Computes the R_D from Carlson symmetric form
    /// </summary>
    /// <param name="X"></param>
    /// <param name="Y"></param>
    /// <param name="Z"></param>
    /// <returns>Construced from R_J(x,y,z,z) which is equal to R_D(x,y,z)</returns>
    /// <remarks>http://en.wikipedia.org/wiki/Carlson_symmetric_form#Series_Expansion</remarks>
    public static double RD(double X, double Y, double Z)
    {
        double sum = 0;
        double fac = 0;
        double lamda = 0;
        double dx = 0;
        double dy = 0;
        double dz = 0;
        double A = 0;
        double MinError = 0;
        MinError = 1E-07;

        sum = 0;
        fac = 1;

        do
        {
            lamda = Math.sqrt(X * Y) + Math.sqrt(Y * Z) + Math.sqrt(Z * X);
            sum += fac / (Math.sqrt(Z) * (Z + lamda));

            fac /= 4;

            X = (X + lamda) / 4;
            Y = (Y + lamda) / 4;
            Z = (Z + lamda) / 4;

            A = (X + Y + 3 * Z) / 5;

            dx = (1 - X / A);
            dy = (1 - Y / A);
            dz = (1 - Z / A);

        } while (Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz)) > MinError);

        double E2 = 0;
        double E3 = 0;
        double E4 = 0;
        double E5 = 0;
        double result = 0;
        E2 = dx * dy + dy * dz + 3 * Math.pow(dz, 2) + 2 * dz * dx + dx * dz + 2 * dy * dz;
        E3 = Math.pow(dz, 3) + dx * Math.pow(dz, 2) + 3 * dx * dy * dz + 2 * dy * Math.pow(dz, 2) + dy * Math.pow(dz, 2) + 2 * dx * Math.pow(dz, 2);
        E4 = dy * Math.pow(dz, 3) + dx * Math.pow(dz, 3) + dx * dy * Math.pow(dz, 2) + 2 * dx * dy * Math.pow(dz, 2);
        E5 = dx * dy * Math.pow(dz, 3);

        //http://dlmf.nist.gov/19.36#E2
        result = (1 - (3 / 14) * E2 + (1 / 6) * E3 + (9 / 88) * Math.pow(E2, 2) - (3 / 22) * E4 - (9 / 52) * E2 * E3 + (3 / 26) * E5 - (1 / 16) * Math.pow(E2, 3) + (3 / 40) * Math.pow(E3, 2) + (3 / 20) * E2 * E4 + (45 / 272) * Math.pow(E2, 2) * E3 - (9 / 68) * (E3 * E4 + E2 * E5));

        result = 3.0 * sum + fac * result / (A * Math.sqrt(A));
        return result;

    }
}
