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

    private VisionPortal visionPortal1;
    private VisionPortal visionPortal2;
    private AprilTagProcessor aprilTag1;
    private AprilTagProcessor aprilTag2;
    private final boolean useWebcam1 = true;
    private final boolean useWebcam2 = false;

    public VisionManager(HardwareMap hardwareMap) {
        int activeCams = (useWebcam1 ? 1 : 0) + (useWebcam2 ? 1 : 0);
        int[] viewIds = VisionPortal.makeMultiPortalView(Math.max(activeCams, 1), VisionPortal.MultiPortalLayout.HORIZONTAL);

        int viewIndex = 0;

        if (useWebcam1) {
            aprilTag1 = new AprilTagProcessor.Builder().build();
            visionPortal1 = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag1)
                    .setLiveViewContainerId(viewIds[viewIndex++])
                    .setCameraResolution(new Size(640, 480))
                    .build();
        }

        if (useWebcam2) {
            aprilTag2 = new AprilTagProcessor.Builder().build();
            visionPortal2 = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 2"))
                    .addProcessor(aprilTag2)
                    .setLiveViewContainerId(viewIds[viewIndex])
                    .setCameraResolution(new Size(640, 480))
                    .build();
        }
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
        List<AprilTagDetection> all = new ArrayList<>();
        all.addAll(getCam1Detections());
        all.addAll(getCam2Detections());
        all.removeIf(tag -> tag.id == 21 || tag.id == 22 || tag.id == 23);
        return all;
    }

    private int obelID = -1;

    public int getObeliskID() {
        for (AprilTagDetection detection : getAllDetections()) {
            if (detection.id == 21 || detection.id == 22 || detection.id == 23) {
                obelID = detection.id;
                return obelID;
            }
        }
        return obelID;
    }


    /** Stop both vision portals */
    public void close() {
        if (visionPortal1 != null) visionPortal1.close();
        if (visionPortal2 != null) visionPortal2.close();
    }
}
