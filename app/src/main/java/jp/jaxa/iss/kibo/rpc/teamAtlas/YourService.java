package jp.jaxa.iss.kibo.rpc.teamAtlas;

import android.util.Log;

import org.opencv.core.Mat;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import org.opencv.core.Mat;

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

        Log.i(TAG, "Oasis Zone Score Analysis Begin");

        // astrobee starts at (9.815, -9.806, 4.293) w/ rotation (1, 0, 0, 0)

        /*//// move thru oasis 2; score test
        // move down first to not collide with oasis 1
        Point point = new Point(9.815d, -9.5d, 4.946d);
        Quaternion quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move into position for oasis 2
        point = new Point(10.926d, -9.5d, 4.946d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move thru zone
        point = new Point(10.926d, -8.449d, 4.946d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);*/

        /*//// move thru oasis 3; score test
        // move down first to not collide with oasis 1/2
        Point point = new Point(9.815d, -8.45d, 4.946d);
        Quaternion quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move into position for oasis 3
        point = new Point(10.426d, -8.45d, 4.946d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move thru zone
        point = new Point(10.426d, -7.399d, 4.946d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);*/

        //// move thru oasis 4; score test
        // move down first to not collide with oasis 1/2/3
        Point point = new Point(9.815d, -7.4d, 4.426d);
        Quaternion quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move into position for oasis 4
        point = new Point(10.926d, -7.4d, 4.425d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);
        // move thru zone
        point = new Point(10.926d, -6.349d, 4.425d);
        //quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);


        api.reportRoundingCompletion();
        api.notifyRecognitionItem();
        api.takeTargetItemSnapshot();
    }

    @Override
    protected void runPlan2(){
       // write your plan 2 here.

        /*// move to "middle" of zone
        Point point = new Point(10.785d, -9.806d, 4.450d);
        Quaternion quaternion = new Quaternion(1f, 0f, 0f, 0f);
        api.moveTo(point, quaternion, false);


        // move to camera spot
        point = new Point(11.425d, -9.806d, 5.195d);
        quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point, quaternion, false);*/

        /* // move diagonal thru oasis 1
        point = new Point(11.426d, -9.806d, 4.975d);
        //quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point, quaternion, false);

        // repeat back and forth to see oasis vs time tradeoff
        point = new Point(10.424d, -9.806d, 4.450d);
        api.moveTo(point, quaternion, false);

        point = new Point(11.426d, -9.806d, 4.975d);
        quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        api.moveTo(point, quaternion, false);*/

        /* /// move straight thru
        point = new Point(11.425d, -9.806d, 4.450d);
        quaternion = new Quaternion(1f, 0f, 0f, 0f);

        Log.i(TAG, "Moving through zone, timer start");
        long startTime = System.nanoTime();
        api.moveTo(point, quaternion, false);
        long endTime = System.nanoTime();

        // output time taken
        long elapsed = (endTime - startTime)/1_000_000;
        Log.i(TAG, "Finished, approx. time taken (ms): " + elapsed);*/
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here.

        // Move to a point.
        //Point point = new Point(10.9d, -9.92284d, 5.195d);
        //Quaternion quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        //api.moveTo(point, quaternion, false);

        // Get a camera image.
        //Mat image = api.getMatNavCam();

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
        //point = new Point(11.143d, -6.7607d, 4.9654d);
        //quaternion = new Quaternion(0f, 0f, 0.707f, 0.707f);
        //api.moveTo(point, quaternion, false);
        //api.reportRoundingCompletion();

        /* ********************************************************** */
        /* Write your code to recognize which target item the astronaut has. */
        /* ********************************************************** */

        // Let's notify the astronaut when you recognize it.
        //api.notifyRecognitionItem();

        /* ******************************************************************************************************* */
        /* Write your code to move Astrobee to the location of the target item (what the astronaut is looking for) */
        /* ******************************************************************************************************* */

        // Take a snapshot of the target item.
        //api.takeTargetItemSnapshot();
    }

    // You can add your method.
    private String yourMethod(){
        return "your method";
    }
}
