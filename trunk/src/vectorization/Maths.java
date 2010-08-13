package vectorization;

import java.util.LinkedList;
import java.util.List;

public class Maths {

    public static double CalculateEuclideanNorm(List<Point> vector){
        
        double acum = 0;
        for(Point point : vector){
            acum += Math.pow(point.getX(), 2);
            acum += Math.pow(point.getY(), 2);
        }

        return Math.sqrt(acum);
    }

    public static List<Point> SubstractVectors(List<Point> v1, List<Point> v2){
        
        if( v1.size() != v2.size() )
            throw new ArithmeticException("Los vectores tienen q tener el mismo tamanyo");

        List<Point> result = new LinkedList<Point>();

        for ( int index = 0; index < v1.size(); index++ ){
            result.add(v1.get(index).substract(v2.get(index)));
        }
        return result;

    }

    public static List<Point> NormalizeVector(List<Point> vector ){
        double norm = CalculateEuclideanNorm(vector);
        List<Point> result = new LinkedList<Point>();

        for(Point point : vector)
            result.add(point.divideByScalar(norm));
        return result;

    }

    public static double CalculateEuclideanDistance(List<Point> v1, List<Point> v2){

        List<Point> v1Normalized = NormalizeVector(v1);
        List<Point> v2Normalized = NormalizeVector(v2);
        List<Point> substratedVectors = SubstractVectors(v1Normalized, v2Normalized);
        return CalculateEuclideanNorm(substratedVectors);
    }

}