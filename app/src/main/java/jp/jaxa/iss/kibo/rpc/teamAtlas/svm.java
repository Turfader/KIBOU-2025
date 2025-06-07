package jp.jaxa.iss.kibo.rpc.teamAtlas;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.SVM;
import org.opencv.ml.Ml;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class svm {

    int[] treasure = new int[5];

    private static Mat computeHOGFeatures(Mat img) {
        // HOG parameters (adjust as needed)
        Size winSize = new Size(64, 64);
        Size blockSize = new Size(16, 16);
        Size blockStride = new Size(8, 8);
        Size cellSize = new Size(8, 8);
        int nbins = 9;

        // Initialize HOG descriptor
        org.opencv.objdetect.HOGDescriptor hog = new org.opencv.objdetect.HOGDescriptor(
                winSize, blockSize, blockStride, cellSize, nbins
        );

        // Compute HOG features
        MatOfFloat descriptors = new MatOfFloat();
        hog.compute(img, descriptors);

        return descriptors.reshape(1, 1); // Reshape to a single row
    }

    public void findItems(Mat i,  int area, YourService s, Boolean reportTreasure) throws IOException {
        SVM svm = s.getSVMFIle();
        //i = s.undistort(i);
        if (svm!=null) {

            Mat image = i;
            if (image.empty()) {
                System.err.println("Failed to load test image!");
            }
            Mat gray = image;

            Mat thresh = new Mat();
            Imgproc.Canny(gray, thresh, 200, 200 * 2);
            //thresh = gray;
            //Imgcodecs.imwrite("test.png", thresh);


// Find contours
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            System.out.println(contours.size());
            int[] numItem = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0};
// Predict each contour
            for (MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                Mat patch = new Mat(image, rect); // Crop the object
                if (patch.height() > 15 && patch.width() > 15) {
                    Mat resizedPatch = new Mat();
                    Imgproc.resize(patch, resizedPatch, new Size(64, 64)); // Match training size

                    Mat featureVector = computeHOGFeatures(resizedPatch);
                    featureVector.convertTo(featureVector, CvType.CV_32F);

                    Mat certainty = new Mat();

                    float label = svm.predict(featureVector);

                    //System.out.println(label);


                    // Draw bounding box and label
                    if (label != 0f && label != 10.0f) {
                        if (reportTreasure == false && label != 4.0f && label != 5.0f && label != 6.0f) {
                            numItem[(int) label]++;
                        }
                        if (label == 4.0f || label == 5.0f || label == 6.0f){
                            treasure[area-1] = (int)label-3;
                        }
                        Imgproc.rectangle(image, rect, new Scalar(0, 255, 0), 2);
                        Imgproc.putText(image, Float.toString(label), new Point(rect.x, rect.y + 15),
                                Imgproc.FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0, 255, 0), 2);
                    }


                }
            }
            if (reportTreasure == false){
                float label=0;
                int biggestVal=0;
                for (int j=0; j<numItem.length; j++){
                    if (numItem[j]>biggestVal){
                        label=j;
                        biggestVal=numItem[j];
                    }
                }
                if (biggestVal>0) {
                    s.areaSet(area, label, biggestVal);
                }
            } else{
                for(int j=0; j<treasure.length-1; j++){
                    if (treasure[4] == treasure[j]){
                        s.GoToTreasure(j+1);
                        break;
                    }
                }
            }
            s.saveImage(image, area);
        }

// Save the output
    }
}