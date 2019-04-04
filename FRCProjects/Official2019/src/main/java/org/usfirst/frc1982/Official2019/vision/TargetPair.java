package org.usfirst.frc1982.Official2019.vision;

public class TargetPair{
    private VisionTarget left,right;
    private double x,y, xDistance,yDistance;

    public TargetPair(VisionTarget left, VisionTarget right){
        this.left = left;
        this.right = right;

        x = (left.getX()+right.getX())/2;
        y = (left.getY()+right.getY())/2;

        xDistance = Math.abs(left.getX()-right.getX());
        yDistance = left.getY()-right.getY();
    }

    public double getX(){ return x;}
    public double getY(){ return y;}

    public double getXDistance(){ return xDistance;}
    public double getYDistance(){ return yDistance;}

    public VisionTarget getLeft(){ return left;}
    public VisionTarget getRight(){ return right;}

    public String toString(){
        return "target pair with x at "+x;
    }
}