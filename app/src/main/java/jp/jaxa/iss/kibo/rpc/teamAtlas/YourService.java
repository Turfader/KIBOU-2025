package jp.jaxa.iss.kibo.rpc.teamAtlas;

import android.util.Log;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.SVM;

import java.io.*;
import java.util.*;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee.
 */

public class YourService extends KiboRpcService {
    final String TAG = "ATLAS";

    double[] area1 = new double[2];
    double[] area2 = new double[2];
    double[] area3 = new double[2];
    double[] area4 = new double[2];

    @Override
    protected void runPlan1(){
        api.startMission();
        svm S = new svm();

        try {
            S.loadSVM(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Point point = new Point(10.9d, -9.72284d, 5.45d);
        api.flashlightControlFront(0f);
        Quaternion quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point, quaternion, false);

        Mat image = api.getMatNavCam();
        Log.i(TAG, "TookPhoto");
        try {
            S.findItems(image, 1, this, false);
        } catch (IOException e) {}
        Log.i(TAG, "FinishedPhoto");
        api.saveMatImage(image, "area1");

        point = new Point(10.743d, -8.7607d, 4.8654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);
        Mat image2 = api.getMatNavCam();
        try { S.findItems(image2, 2, this, false); } catch (IOException e) {}
        api.saveMatImage(image2, "area2");

        point = new Point(10.73d, -7.7607d, 4.8654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);
        Mat image3 = api.getMatNavCam();
        try { S.findItems(image3, 3, this, false); } catch (IOException e) {}
        api.saveMatImage(image3, "area3");

        point = new Point(11.16d, -6.6607d, 5.2654d);
        quaternion = new Quaternion(0f, 0f, 1f, 0f);
        api.moveTo(point, quaternion, false);
        Mat image4 = api.getMatNavCam();
        try { S.findItems(image4, 4, this, false); } catch (IOException e) {}
        api.saveMatImage(image4, "area4");

        point = new Point(11.16d, -6.8607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);
        api.moveTo(point, quaternion, false);

        api.reportRoundingCompletion();

        point = new Point(11.16d, -7.1607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);
        api.moveTo(point, quaternion, false);

        api.notifyRecognitionItem();
        Mat image5 = api.getMatNavCam();
        try { S.findItems(image5, 5, this, true); } catch (IOException e) {}
        api.saveMatImage(image5, "astronautItem");

        api.takeTargetItemSnapshot();

        Log.i(TAG, "Hello World from the log");
    }

    @Override
    protected void runPlan2(){ }

    @Override
    protected void runPlan3(){ }

    public void areaSet(int areaNum, float itemType, int itemNum){
        Log.i(TAG, "Found item of type " + ((int) itemType) + " in area " + areaNum);
        switch ((int) itemType){
            case 1: api.setAreaInfo(areaNum, "treasure_box", itemNum); break;
            case 2: api.setAreaInfo(areaNum, "coin", itemNum); break;
            case 3: api.setAreaInfo(areaNum, "compass", itemNum); break;
            case 4: api.setAreaInfo(areaNum, "crystal", itemNum); break;
            case 5: api.setAreaInfo(areaNum, "diamond", itemNum); break;
            case 6: api.setAreaInfo(areaNum, "emerald", itemNum); break;
            case 7: api.setAreaInfo(areaNum, "key", itemNum); break;
            case 8: api.setAreaInfo(areaNum, "letter", itemNum); break;
            case 9: api.setAreaInfo(areaNum, "coral", itemNum); break;
            case 11: api.setAreaInfo(areaNum, "shell", itemNum); break;
            case 12: api.setAreaInfo(areaNum, "fossil", itemNum); break;
            default: break;
        }
    }

    public SVM getSVMFIle() throws IOException {
        InputStream f = getAssets().open("svm/svm_model.xml");
        File tempFile = File.createTempFile("svm", ".xml");
        OutputStream fw = new FileOutputStream(tempFile, false);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = f.read(bytes)) != -1){ fw.write(bytes, 0, read); }
        fw.close();
        try {
            return SVM.load(tempFile.getAbsolutePath());
        } catch (Exception e){
            Log.i(TAG, "Boowomp");
            Log.i(TAG, e.toString());
            return null;
        }
    }

    public void GoToTreasure(int area) {
        Point point;
        Quaternion quaternion;
        switch (area){
            case 1:
                point = new Point(10.9d, -9.72284d, 5.25d);
                quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
                break;
            case 2:
                point = new Point(10.743d, -8.7607d, 4.5654d);
                quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
                break;
            case 3:
                point = new Point(10.73d, -7.7607d, 4.5654d);
                quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
                break;
            case 4:
                point = new Point(11.16d, -6.6607d, 5.2654d);
                quaternion = new Quaternion(0f, 0f, 1f, 0f);
                break;
            default:
                return;
        }
        api.moveTo(point, quaternion, false);
        Mat ma = api.getMatNavCam();
        api.saveMatImage(ma, "FinalShot.png");
    }

    public void saveImage(Mat i, int area){
        api.saveMatImage(i, "area" + area + "processed.png");
    }

    public void saveImage2(Mat i, int area){
        api.saveMatImage(i, "area" + area + "processed2.png");
    }

    public Mat undistort(Mat i){
        try{
            Mat cMatrix = new Mat(3,3, CvType.CV_64F);
            Mat cCoef = new Mat(1,5, CvType.CV_64F);
            cMatrix.put(0,0, api.getNavCamIntrinsics()[0]);
            cCoef.put(0,0, api.getNavCamIntrinsics()[1]);
            cCoef.convertTo(cCoef, CvType.CV_64F);
            Mat unI = new Mat();
            Calib3d.undistort(i, unI, cMatrix, cCoef);
            return unI;
        } catch(Exception e) {
            return i;
        }
    }
}
