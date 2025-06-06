package jp.jaxa.iss.kibo.rpc.teamAtlas;

import android.util.Log;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.ml.SVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee.
 */

public class YourService extends KiboRpcService {
    final String
        TAG = "ATLAS",
        SIM = "Simulator",
        IRL = "Orbit";

    @Override
    protected void runPlan1(){
        // The mission starts.
        api.startMission();

        svm S = new svm();

        // Move to a point.
        Point point = new Point(10.9d, -9.72284d, 5.25d);
        Quaternion quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point, quaternion, false);

        // Get a camera image.
        Mat image = api.getMatNavCam();
        Log.i(TAG, "TookPhoto");
        Log.i(TAG, "Base Folder: " +  getFilesDir().getName());
        Log.i(TAG, "Base Folder: " +  getFilesDir().getPath());
        try {
            S.findItems(image, 1, this, false);
        } catch (IOException e) {

        }
        Log.i(TAG, "FinishedPhoto");
        api.saveMatImage(image, "area1");


        /* ******************************************************************************** */
        /* Write your code to recognize the type and number of landmark items in each area! */
        /* If there is a treasure item, remember it.                                        */
        /* ******************************************************************************** */

        // When you recognize landmark items, let’s set the type and number.
        //api.setAreaInfo(1, "item_name", 1);

        /* **************************************************** */
        /* Let's move to each area and recognize the items. */
        /* **************************************************** */



        // When you move to the front of the astronaut, report the rounding completion.
        point = new Point(10.743d, -8.7607d, 4.5654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);


        Mat image2 = api.getMatNavCam();

        image2 = new Mat(image2, new Rect(0,0, 960, 960));
        try {
            S.findItems(image2, 2, this, false);
        } catch (IOException e) {
        }
        api.saveMatImage(image2, "area2");


        point = new Point(10.73d, -7.7607d, 4.5654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image3 = api.getMatNavCam();
        image3 = new Mat(image3, new Rect(0,0, 960, 960));
        try {
            S.findItems(image3, 3, this, false);
        } catch (IOException e) {
        }
        api.saveMatImage(image3, "area3");


        point = new Point(11.16d, -6.6607d, 5.2654d);
        quaternion = new Quaternion(0f, 0f, 1f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image4 = api.getMatNavCam();
        try {
            S.findItems(image4, 4, this, false);
        } catch (IOException e) {
        }
        api.saveMatImage(image4, "area4");

        /* ********************************************************** */
        /* Write your code to recognize which target item the astronaut has. */
        /* ********************************************************** */
        api.reportRoundingCompletion();

        point = new Point(11.16d, -6.8607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);

        api.moveTo(point, quaternion, false);



        Mat image5 = api.getMatNavCam();
        try {
            S.findItems(image5, 5, this, true);
        } catch (IOException e) {
        }
        api.saveMatImage(image5, "astronautItem");



        // Let's notify the astronaut when you recognize it.
        api.notifyRecognitionItem();

        /* ******************************************************************************************************* */
        /* Write your code to move Astrobee to the location of the target item (what the astronaut is looking for) */
        /* ******************************************************************************************************* */

        // Take a snapshot of the target item.
        api.takeTargetItemSnapshot();

        Log.i(TAG, "Hello World from the log");
    }

    @Override
    protected void runPlan2(){
       // write your plan 2 here.
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here.
    }

    // You can add your method.
    public void areaSet(int areaNum, float itemType, int itemNum){
        Log.i(TAG, "Found item of type " + ((int) itemType) + " in area " + areaNum);
        switch ((int) itemType){
            case 1:
                api.setAreaInfo(areaNum, "treasure_box", itemNum);
                break;
            case 2:
                api.setAreaInfo(areaNum, "coin", itemNum);
                break;
            case 3:
                api.setAreaInfo(areaNum, "compass", itemNum);
                break;
            case 4:
                api.setAreaInfo(areaNum, "crystal", itemNum);
                break;
            case 5:
                api.setAreaInfo(areaNum, "diamond", itemNum);
                break;
            case 6:
                api.setAreaInfo(areaNum, "emerald", itemNum);
                break;
            case 7:
                api.setAreaInfo(areaNum, "key", itemNum);
                break;
            case 8:
                api.setAreaInfo(areaNum, "letter", itemNum);
                break;
            case 9:
                api.setAreaInfo(areaNum, "coral", itemNum);
                break;
            case 11:
                api.setAreaInfo(areaNum, "shell", itemNum);
                break;
            case 12:
                api.setAreaInfo(areaNum, "fossil", itemNum);
                break;
            default:
                break;
        }
    }

    public SVM getSVMFIle() throws IOException {
        InputStream f = getAssets().open("svm/svm_model.xml");
        File tempFile = File.createTempFile("svm", ".xml");
        OutputStream fw = new FileOutputStream(tempFile, false);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = f.read(bytes)) != -1){
            fw.write(bytes, 0, read);
        }
        fw.close();
        try {
            SVM s = SVM.load(tempFile.getAbsolutePath());
            return s;
        } catch (Exception e){
            Log.i(TAG, "Boowomp");
            Log.i(TAG, e.toString());
            return null;
        }
    }

    public void GoToTreasure(int area) {
        switch (area){
            case 1:
                Point point = new Point(10.9d, -9.72284d, 5.25d);
                Quaternion quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
                api.moveTo(point, quaternion, false);
                break;
            case 2:
                point = new Point(10.743d, -8.7607d, 4.7654d);
                quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
                api.moveTo(point, quaternion, false);
                break;
            case 3:
                point = new Point(10.73d, -7.7607d, 4.7654d);
                quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
                api.moveTo(point, quaternion, false);
                break;
            case 4:
                point = new Point(11.16d, -6.6607d, 5.2654d);
                quaternion = new Quaternion(0f, 0f, 1f, 0f);
                api.moveTo(point, quaternion, false);
                break;
            default:
                break;
        }
        Mat ma = api.getMatDockCam();
        api.saveMatImage(ma, "FinalShot.png");
    }

    public void saveImage(Mat i, int area){
        api.saveMatImage(i, "area" + area + "processed.png");
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

        }
        catch(Exception e) {
            return i;
        }
    }

}
