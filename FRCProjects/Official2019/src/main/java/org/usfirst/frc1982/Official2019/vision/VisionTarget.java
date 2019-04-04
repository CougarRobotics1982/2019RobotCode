package org.usfirst.frc1982.Official2019.vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;


public class VisionTarget{
    private Point highest, leftMost, rightMost, lowest;
    private double middleX, middleY /*,  width, height, boundingWidth, boundingHeight*/;
    
    //width and height is based on distance between points
    //boundingWidth and height is based on the difference bewtween the rightmost and leftmost x, similar for height.

    private boolean slopedUp; //slope of the rectangle, // is up, \\ is down

    public VisionTarget(MatOfPoint contour){
        Point[] points = contour.toArray();
        highest=points[0];
        leftMost=points[0];
        rightMost=points[0];
        lowest=points[0];

        for(Point p: points){
            if(p.y<highest.y)
                highest=p;
            if(p.y>lowest.y)
                lowest=p;
            if(p.x>rightMost.x)
                rightMost=p;
            if(p.x<leftMost.x)
                leftMost=p;
        }

        slopedUp = rightMost.y<leftMost.y;

        middleX = (leftMost.x+rightMost.x)/2;
        middleY = (highest.y+lowest.y)/2;
    }
    

    public String toString(){
        String r = "rect at ("+(int)middleX+","+(int)middleY+")";
        if(slopedUp)
            r = "upward sloping "+r;
        else
            r = "downward sloping "+r;
        return r;
    }

    public double getX(){ return middleX;}
    public double getY(){ return middleY;}
    
    public boolean angledRight(){ return slopedUp;}
    public boolean angledLeft(){ return !slopedUp;}
}