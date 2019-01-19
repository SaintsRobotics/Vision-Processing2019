/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.saintsrobotics.vision;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Robot extends IterativeRobot {
    
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    
    private VisionThread visionThread;
    private MatOfPoint[] contours;
    private Rect r;
    
    private final Object imgLock = new Object();
    
    @Override
    public void robotInit() {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
        
        visionThread = new VisionThread(camera, new GripContours(), pipeline -> {
          synchronized (imgLock) {
            contours = pipeline.findContoursOutput().toArray(new MatOfPoint[] {});
            r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
          }
        });
        visionThread.start();
        
    }
    
    public void teleopPeriodic() {
      synchronized (imgLock) {
        if (contours == null || r == null) {
          DriverStation.reportError("contours is null", false);
          return;
        }
        Double area = r.area();
        int x = r.x;
        int y = r.y;
        int i = 0;
        int centerX = 
        SmartDashboard.putNumber("frame", i);
        SmartDashboard.putNumber("area", area);
        SmartDashboard.putNumber("x", x);
        SmartDashboard.putNumber("y", y);
        i++;
        //for (MatOfPoint mat : contours) {
          //double[] vals = mat.get(0, 0);
          //SmartDashboard.putNumber("Mat" + i + "[0]", vals[0]);
          //SmartDashboard.putNumber("Mat" + i + "[1]", vals[1]);
          
          
        //}
      }
    }
}
    