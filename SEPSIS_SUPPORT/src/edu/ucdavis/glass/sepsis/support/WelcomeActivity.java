/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ucdavis.glass.sepsis.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class WelcomeActivity extends Activity 
{
	private GestureDetector mGestureDetector;
	
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        mGestureDetector = createGestureDetector(this);	
        setContentView(R.layout.welcome_screen);
        
        // load options
        FileInputStream fis;
        try 
        {
            System.out.println("loading options...");
            fis = openFileInput(Global.OPTIONS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Global.options = (Global.Options) ois.readObject();
            ois.close();
            System.out.println("loading options complete");
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("loading options failed");
            Global.alertUser(WelcomeActivity.this, "Exception", "Loading options failed.");
            e.printStackTrace();
			// make new options
            //System.out.println("new options");
			//Global.options = new Global.Options();
        }
        
        // load recent patients
        try 
        {
            System.out.println("loading patients...");
            fis = openFileInput(Global.PATIENTS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Global.recentPatients = (ArrayDeque<Patient>) ois.readObject();
            ois.close();
            System.out.println("loading patients complete");
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("loading patients failed");
            Global.alertUser(WelcomeActivity.this, "Exception", "Loading patients failed.");
            e.printStackTrace();
			// make new patient deque
            //System.out.println("new patients");
			//Global.recentPatients = new ArrayDeque<Patient>();
        }
    }
	
	@Override
    protected void onDestroy() 
	{
        super.onDestroy();
        
        // write out recent patients
        try 
        {
            System.out.println("saving patients...");
        	FileOutputStream fos = openFileOutput(Global.PATIENTS_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Global.recentPatients);
            oos.close();
            System.out.println("saving patients completed");
        } 
        catch (Exception e) 
        {
        	// error        	
            System.out.println("saving patients failed");
            Global.alertUser(WelcomeActivity.this, "Exception", "Saving recent patients failed.");
            e.printStackTrace();
        }
        
        // write out options
        try 
        {
            System.out.println("saving options...");
        	FileOutputStream fos = openFileOutput(Global.OPTIONS_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Global.options);
            oos.close();
            System.out.println("saving options completed");
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("saving options failed");
            Global.alertUser(WelcomeActivity.this, "Exception", "Saving options failed.");
            e.printStackTrace();
        }
    }
	
	private GestureDetector createGestureDetector(Context context) 
	{
    GestureDetector gestureDetector = new GestureDetector(context);
        // create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() 
        {
            @Override
            public boolean onGesture(Gesture gesture) 
            {
                if (gesture == Gesture.TAP) 
                {
                	startActivity( new Intent(getApplicationContext(), QRScannerActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                	// SHORT TERM SOLUTION, SKIP QR, MOSTLY FOR TESTING AND DEVELOPMENT EASE
                	// pass FAKE QR message to the overview activity
    				Intent overviewIntent = new Intent(getApplicationContext(), OverviewActivity.class);
    				overviewIntent.putExtra("Patient info", 2 ); 
    				startActivity( overviewIntent );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go to recent patient view
                	startActivity( new Intent(getApplicationContext(), RecentPatientActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to options view
                    startActivity( new Intent(getApplicationContext(), OptionsActivity.class));
                	return true;
                }
                
                return false;
            }
        });
        
        return gestureDetector;
    }

    // send generic motion events to the gesture detector
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) 
    {
        if (mGestureDetector != null) 
            return mGestureDetector.onMotionEvent(event);
        
        return false;
    }
}
