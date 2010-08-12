package vectorization;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.LinkedList;
import java.util.List;

public class FourierTransformer {

    public static int MAX_COEFFICIENTS = 150;

    private static List<Point> reorderInputPoints(List<Point> input) {
        List<Point> copy = new LinkedList<Point>();
        for(Point p:input)
            copy.add(p);

        List<Point> out = new LinkedList<Point>();
        Point point = null;
        Point point2 = null;
        double min_distance = Double.MAX_VALUE;
        Point min_point = null;
        for (int i=0; i<copy.size(); i++) {
            point = copy.get(i);
            if (i == 0) {
                out.add(point);
            } else {
                min_distance = Double.MAX_VALUE;
                min_point = null;
                for (int j = (i+1); j < copy.size(); j++) {
                    point2 = copy.get(j);
                    if (point.distanceTo(point2) < min_distance) {
                        min_distance = point.distanceTo(point2);
                        min_point = point2;
                    }
                }
                if (min_point != null) {
                   out.add(min_point);
                   copy.remove(min_point);
                }
            }
        }
        return out;
    }

    /**
     * Esta es la transformada segun lo que dijo Ale
     */
    public static List<Point> transform(List<Point> input){
        input = reorderInputPoints(input);
        List<Point> out = new LinkedList<Point>();
        Point transformedPoint = null;
        int N = MAX_COEFFICIENTS;
        for (int k = 0; k < N; k++) {
            double u = 0;
            double v = 0;
            int n = 0;
            for(Point point:input) {
                u += point.getX() * cos((2*PI*k*n)/N) + point.getY() * sin((2*PI*k*n)/N);
                v += point.getY() * cos((2*PI*k*n)/N) - point.getX() * sin((2*PI*k*n)/N);
                n++;
            }
            transformedPoint = new Point((int)Math.round(u), (int)Math.round(v));
            out.add(transformedPoint);
        }
        return out;
    }
   
}