package jp.jaxa.iss.kibo.rpc.teamAtlas;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.ml.SVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
            S.findItems(image, 1, this);
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
        point = new Point(10.743d, -8.7607d, 4.6654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image2 = api.getMatNavCam();
        try {
            S.findItems(image2, 2, this);
        } catch (IOException e) {
        }
        api.saveMatImage(image2, "area2");


        point = new Point(10.73d, -7.7607d, 4.6654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image3 = api.getMatNavCam();
        try {
            S.findItems(image3, 3, this);
        } catch (IOException e) {
        }
        api.saveMatImage(image3, "area3");


        point = new Point(11.16d, -6.6607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 1f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image4 = api.getMatNavCam();
        try {
            S.findItems(image4, 4, this);
        } catch (IOException e) {
        }
        api.saveMatImage(image4, "area4");

        /* ********************************************************** */
        /* Write your code to recognize which target item the astronaut has. */
        /* ********************************************************** */

        api.reportRoundingCompletion();


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
    public void areaSet(int areaNum, float itemType){
        Log.i(TAG, "Found item of type " + itemType + "in area " + areaNum);
        switch ((int) itemType){
            case 1:
                api.setAreaInfo(areaNum, "Treasure_box");
            case 2:
                api.setAreaInfo(areaNum, "coin");
            case 3:
                api.setAreaInfo(areaNum, "compass");
            case 4:
                api.setAreaInfo(areaNum, "crystal");
            case 5:
                api.setAreaInfo(areaNum, "diamond");
            case 6:
                api.setAreaInfo(areaNum, "emerald");
            case 7:
                api.setAreaInfo(areaNum, "key");
            case 8:
                api.setAreaInfo(areaNum, "letter");
            case 9:
                api.setAreaInfo(areaNum, "coral");
            case 11:
                api.setAreaInfo(areaNum, "shell");
            case 12:
                api.setAreaInfo(areaNum, "fossil");
            default:
                break;
        }
        api.flashlightControlFront(0.5f);
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
}
