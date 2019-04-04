package org.usfirst.frc1982.Official2019;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
// import org.usfirst.frc1982.Official2019.commands.*;
import org.usfirst.frc1982.Official2019.subsystems.*;
import org.usfirst.frc1982.Official2019.vision.*;

import org.opencv.core.MatOfPoint;
import java.util.ArrayList;

import java.util.Comparator;


public class Robot extends TimedRobot {
    // private final boolean bothCameras = false;

    public static final int imgWidth = 320;
    public static final int imgHeight = 240;

    public static OI oi;
    public static DriveBase driveBase;
    public static Elevator elevator;
    public static Intake intake;

    private VisionThread vThread;
    private static GripPipeline gripPipeline;
    private static TargetPair bestTarget;

    public void robotInit() {
        driveBase = new DriveBase();
        elevator = new Elevator();
        intake = new Intake();
        visionInit();
        oi = new OI();
    }

    private void visionInit(){
        int cameraFPS = 10;
        int cameraExposure = 40;
        int cameraBrightness = 20;

        UsbCamera robotVisionCamera = CameraServer.getInstance().startAutomaticCapture(1);
        if(Constants.twoCameras){
            robotVisionCamera.setResolution(imgWidth,imgHeight);
            robotVisionCamera.setFPS(cameraFPS);
            robotVisionCamera.setExposureManual(cameraExposure);
            robotVisionCamera.setBrightness(cameraBrightness);
            gripPipeline = new GripPipeline();
            vThread = new VisionThread(robotVisionCamera, gripPipeline, pipeline -> 
            {});
            if(Constants.useVisionProcessing)
                vThread.start();
            CameraServer.getInstance().startAutomaticCapture(0).setExposureAuto();;
        }else{
            robotVisionCamera.setExposureAuto();
        }
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    }

    public void teleopPeriodic() {
        if(Constants.useVisionProcessing)
            findTargetPairs();
        Scheduler.getInstance().run();
        // System.out.println(elevator.getEncoder());
    }


    //start of vision methods

    public static void findTargetPairs(){
        ArrayList<VisionTarget> targets = new ArrayList<>();

        ArrayList<MatOfPoint> contours = gripPipeline.improvedContoursOutput();
        for(MatOfPoint contour:contours)
            targets.add(new VisionTarget(contour));
        
        // ArrayList<MatOfPoint> contours = gripPipeline.filterContoursOutput();
        // // ArrayList<MatOfPoint> contours = (ArrayList<MatOfPoint>) gripPipeline.findContoursOutput().clone();
        // try{
        //     for(MatOfPoint contour: contours)
        //         targets.add(new VisionTarget(contour));
        // } catch (Exception e){
        //    try{
        //        targets.clear();
        //         for(MatOfPoint contour: contours)
        //             targets.add(new VisionTarget(contour));
        //    }catch(Exception ignored){}
        // }

        class sortByX implements Comparator<VisionTarget>{
            public int compare(VisionTarget a, VisionTarget b){ return (int)(a.getX()-b.getX()); }
        }
        
        targets.sort(new sortByX());
        
        ArrayList<TargetPair> pairs = new ArrayList<>();

        for(int i=0; i<targets.size()-1;i++){
            if(targets.get(i).angledRight() && targets.get(i+1).angledLeft())
                pairs.add(new TargetPair(targets.get(i),targets.get(i+1)));
        }
        if(pairs.size()>0){
            TargetPair closestToCenter = pairs.get(0);
            for(TargetPair pair: pairs)
                if(distFromCenter(pair.getX())<distFromCenter(closestToCenter.getX()))
                    closestToCenter = pair;
            bestTarget = closestToCenter;
        }else{
            bestTarget = null;
        }
    }

    public  static TargetPair getTarget(){              return bestTarget; }
    public  static boolean    hasTarget(){              return bestTarget!=null; }
    private static double     distFromCenter(double x){ return Math.abs(x-(imgWidth/2)); }
}