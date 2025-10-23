package org.firstinspires.ftc.team31584;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;

public class VisionManager {

    public final VisionPortal visionPortal1;
    public final VisionPortal visionPortal2;
    private final AprilTagProcessor aprilTag1;
    private final AprilTagProcessor aprilTag2;

    public VisionManager(HardwareMap hardwareMap) {
        // Build AprilTag processors
        aprilTag1 = new AprilTagProcessor.Builder().build();
        aprilTag2 = new AprilTagProcessor.Builder().build();

        // Split preview into 2 panes
        int[] viewIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.HORIZONTAL);

        // Portal for Webcam 1
        visionPortal1 = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag1)
                .setLiveViewContainerId(viewIds[0])
                .setCameraResolution(new Size(640, 480)) // Width x Height
                .build();

        // Portal for Webcam 2
        visionPortal2 = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 2"))
                .addProcessor(aprilTag2)
                .setLiveViewContainerId(viewIds[1])
                .setCameraResolution(new Size(640, 480)) // Width x Height
                .build();
    }

    /** Get AprilTag detections for camera 1 */
    public List<AprilTagDetection> getCam1Detections() {
        return aprilTag1.getDetections();
    }

    /** Get AprilTag detections for camera 2 */
    public List<AprilTagDetection> getCam2Detections() {
        return aprilTag2.getDetections();
    }

    /**
     * Get all AprilTag detections from both cameras,
     * excluding tag IDs 21, 22, and 23 (Obelisk)
     */
    public List<AprilTagDetection> getAllDetections() {
        List<AprilTagDetection> allDetections = new java.util.ArrayList<>();

        if (aprilTag1.getDetections() != null) {
            allDetections.addAll(aprilTag1.getDetections());
        }

        if (aprilTag2.getDetections() != null) {
            allDetections.addAll(aprilTag2.getDetections());
        }

        // Remove obelisk
        allDetections.removeIf(detection ->
                detection.id == 21 || detection.id == 22 || detection.id == 23
        );

        return allDetections;
    }

    private int obelID = -1;

    public int getObeliskID() {
        List<AprilTagDetection> allDetections = new ArrayList<>();

        if (aprilTag1.getDetections() != null) {
            allDetections.addAll(aprilTag1.getDetections());
        }

        if (aprilTag2.getDetections() != null) {
            allDetections.addAll(aprilTag2.getDetections());
        }

        for (AprilTagDetection detection : allDetections) {
            if (detection.id == 21 || detection.id == 22 || detection.id == 23) {
                obelID = detection.id;

                return obelID;
            }
        }

        // No obelisk tag found, send the old one.
        return obelID;
    }


    /** Stop both vision portals */
    public void close() {
        visionPortal1.close();
        visionPortal2.close();
    }
}
