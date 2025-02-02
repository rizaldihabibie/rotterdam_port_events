package com.example.demo.util;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class CommonFunction {
    public static boolean isOutsideRotterdam(double[] shipCoordinates){

        List<Point2D.Double> rotterdamPortPolygon = Arrays.asList(
                new Point2D.Double(4.09365, 51.98509),
                new Point2D.Double(4.08719, 52.01616),
                new Point2D.Double(3.98969, 52.0345),
                new Point2D.Double(3.94652, 51.99088),
                new Point2D.Double(3.95805, 51.9598),
                new Point2D.Double(3.98431, 51.91666),
                new Point2D.Double(4.46901, 51.82003),
                new Point2D.Double(4.55084, 51.64443),
                new Point2D.Double(4.629, 51.664),
                new Point2D.Double(4.69875, 51.83797),
                new Point2D.Double(4.5382, 51.91703),
                new Point2D.Double(4.09365, 51.98509)
        );

        Point2D.Double shipCoordinatesPoint = new Point2D.Double(shipCoordinates[0], shipCoordinates[1]);

        Path2D.Double path = new Path2D.Double();
        Point2D.Double firstPoint = rotterdamPortPolygon.get(0);
        path.moveTo(firstPoint.x, firstPoint.y);

        for (int i = 1; i < rotterdamPortPolygon.size(); i++) {
            Point2D.Double p = rotterdamPortPolygon.get(i);
            path.lineTo(p.x, p.y);
        }

        path.closePath();

        return !path.contains(shipCoordinatesPoint);

    }
}
