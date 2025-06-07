package jp.jaxa.iss.kibo.rpc.teamAtlas;

import android.util.Log;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.SVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

    double[] area1 = new double[2];
    double[] area2 = new double[2];
    double[] area3 = new double[2];
    double[] area4 = new double[2];

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
            image = processImagePos(image, 1);
        } catch (IOException e) {}
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
        point = new Point(10.743d, -8.7607d, 4.8654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);


        Mat image2 = api.getMatNavCam();

        try {
            image2 = processImagePos(image2, 2);
        } catch (IOException e) {}
        try {
            S.findItems(image2, 2, this, false);
        } catch (IOException e) {
        }
        api.saveMatImage(image2, "area2");


        point = new Point(10.73d, -7.7607d, 4.8654d);
        quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
        api.moveTo(point, quaternion, false);

        Mat image3 = api.getMatNavCam();
        try {
            image3 = processImagePos(image3, 3);
        } catch (IOException e) {}
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
            image4 = processImagePos(image4, 4);
        } catch (IOException e) {}
        try {
            S.findItems(image4, 4, this, false);
        } catch (IOException e) {
        }
        api.saveMatImage(image4, "area4");

        /* ********************************************************** */
        /* Write your code to recognize which target item the astronaut has. */
        /* ********************************************************** */

        point = new Point(11.16d, -6.8607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);

        api.moveTo(point, quaternion, false);

        api.reportRoundingCompletion();

        point = new Point(11.16d, -7.2607d, 4.9654d);
        quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);

        api.moveTo(point, quaternion, false);


        api.notifyRecognitionItem();

        Mat image5 = api.getMatNavCam();
        try {
            S.findItems(image5, 5, this, true);
        } catch (IOException e) {
        }
        api.saveMatImage(image5, "astronautItem");



        // Let's notify the astronaut when you recognize it.


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
                point = new Point(10.743d, -8.7607d, 4.5654d);
                quaternion = new Quaternion(-0.707f, 0f, 0.707f, 0f);
                api.moveTo(point, quaternion, false);
                break;
            case 3:
                point = new Point(10.73d, -7.7607d, 4.5654d);
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
        Mat ma = api.getMatNavCam();
        api.saveMatImage(ma, "FinalShot.png");
    }

    Mat processImagePos(Mat image, int area) throws IOException {
        image = undistort(image);
        if(image.empty() == true) {
            System.out.println("Error: no image found!");
        } else{
            System.out.println("Image found!");
        }

        api.saveMatImage(image , "area" + area + "pre");

        //System.out.println(image.size());
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        //Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

        Mat ogImage = image.clone();
        Mat image32S = new Mat();
        image.convertTo(image32S, CvType.CV_8UC1);

        Imgproc.Canny(image32S, image32S, 100, 100 * 2);

        Mat h = new Mat();
        Imgproc.findContours(image32S, contours, h, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);


        System.out.println(contours.size());

        Mat drawing = Mat.zeros(image32S.size(), CvType.CV_8UC3);


        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        Point[] centers = new Point[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            //System.out.println(boundRect[i].height);
            if (boundRect[i].height<100){
                boundRect[i].height=0;
                boundRect[i].width=0;
            } else{

            }
            centers[i] = new Point();
        }


        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);

        for (MatOfPoint2f poly : contoursPoly) {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }

        Mat contourImg = new Mat(image32S.size(), CvType.CV_32SC1);
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(contourImg, contours, i, new Scalar(255, 255, 255), -1);
            if (boundRect[i].height!=0){
                //Imgproc.rectangle(contourImg, boundRect[i].tl(), boundRect[i].br(), new Scalar(255, 255, 255), 4);
            }
        }

        List<Mat> shapes = new ArrayList<Mat>();
        List<org.opencv.core.Point> locales = new ArrayList<org.opencv.core.Point>();
        List<Rect> bRect = new ArrayList<Rect>();
        for (int i = 0; i < contours.size(); i++) {
            if (boundRect[i].height!=0){
                if (i>0 && boundRect[i].x!=boundRect[i-1].x && boundRect[i].y!=boundRect[i-1].y) {
                    Mat e = new Mat(contourImg, boundRect[i]);
                    e.convertTo(e, CvType.CV_8UC1);
                    //Imgproc.findContours(e, ep, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
                    shapes.add(e);
                    locales.add(new org.opencv.core.Point(boundRect[i].x + (boundRect[i].width/2.0), boundRect[i].y+ (boundRect[i].height/2.0)));
                    bRect.add(boundRect[i]);
                    //Imgproc.matchShapes(e,ex,1,0.0);
                }

            }
        }
        System.out.println(shapes.size());
        Mat p = new Mat();
        InputStream f = getAssets().open("img/paper.png");
        File tempFile = File.createTempFile("paper", ".png");
        OutputStream fw = new FileOutputStream(tempFile, false);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = f.read(bytes)) != -1){
            fw.write(bytes, 0, read);
        }
        fw.close();
        try {
            p = Imgcodecs.imread(tempFile.getAbsolutePath());
        } catch (Exception e){
            Log.i(TAG, "Boowomp");
            Log.i(TAG, e.toString());
            return null;
        }
        p.convertTo(p, CvType.CV_8UC1);
        Mat paper = new Mat();
        Imgproc.Canny(p, paper, 500, 500 * 2);
        Size sz = new Size(300,300);

        List<MatOfPoint> contoursC = new ArrayList<MatOfPoint>();

        Imgproc.findContours(paper, contoursC, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat pImg = new Mat(paper.size(), CvType.CV_32SC1);
        for (int i = 0; i < contoursC.size(); i++) {
            Imgproc.drawContours(pImg, contoursC, i, new Scalar(255, 255, 255), -1);
        }

        pImg.convertTo(pImg, CvType.CV_8UC1);


        int papNum=-1;
        double lowestSCore=1000;
        for (int i = 0; i < shapes.size(); i++) {
            Mat s = shapes.get(i);
            //Imgcodecs.imwrite("Images/img" + i +".jpg", s);
            double pAcc = Imgproc.matchShapes(s, pImg,3,0);
            if (pAcc<lowestSCore){
                lowestSCore=pAcc;
                papNum=i;
            }
            //System.out.println("Object " + i + ": " + pAcc);
        }

        Log.i(TAG ,"Object " + papNum + " is the paper, with a score of " + lowestSCore);

        if (papNum!=-1) {
            if (area == 1) {
                area1[0] = locales.get(papNum).x;
                area1[1] = locales.get(papNum).y;
            }
            if (area == 2) {
                area2[0] = locales.get(papNum).x;
                area2[1] = locales.get(papNum).y;
            }
            if (area == 3) {
                area3[0] = locales.get(papNum).x;
                area3[1] = locales.get(papNum).y;
            }
            if (area == 4) {
                area4[0] = locales.get(papNum).x;
                area4[1] = locales.get(papNum).y;
            }
            Log.i(TAG, "Paper is at point (" + locales.get(papNum).x + ", " + locales.get(papNum).y + ")");

            org.opencv.core.Point po = locales.get(papNum);
            Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB);

            Imgproc.circle(image, locales.get(papNum), 10, new Scalar(0, 0, 255), -1);
            api.saveMatImage(image, "area" + area + "loc.png");

            int pa = 20;

            Rect r = new Rect(bRect.get(papNum).x-pa, bRect.get(papNum).y-pa, bRect.get(papNum).width+2*pa, bRect.get(papNum).height+2*pa);

            return new Mat(ogImage, r);
        }

        return image;

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
