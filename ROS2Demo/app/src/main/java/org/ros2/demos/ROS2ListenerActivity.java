/* Copyright 2016-2017 Esteve Fernandez <esteve@apache.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ros2.demos;;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros2.rcljava.RCLJava;

import org.ros2.android.activity.ROSActivity;

public class ROS2ListenerActivity extends ROSActivity {

    private static final String IS_WORKING = "isWorking";

    private ListenerNode listenerNode;

    private TextView listenerView;

    private static String TAG = ROS2ListenerActivity.class.getName();

    private boolean isWorking;

    /** Called when the activity is first created. */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState != null) {
            isWorking = savedInstanceState.getBoolean(IS_WORKING);
        }

        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(startListener);

        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(stopListener);

        Button addTwoInts = findViewById(R.id.addTwoInts);
        addTwoInts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "request 2+3 to pc .... ");
                        listenerNode.addTwoInts();
                        Log.d(TAG, "request FrtACControl...");
                        listenerNode.frtACControl();
                    }
                }).start();
            }
        });

        listenerView = (TextView)findViewById(R.id.listenerView);
        listenerView.setMovementMethod(new ScrollingMovementMethod());

        RCLJava.rclJavaInit();

        listenerNode =
                new ListenerNode(listenerView, RCLJava.getDefaultContext());

        changeState(isWorking);
    }

    // Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(final View view) {
            Log.d(TAG, "onClick() called - start button");
            Toast.makeText(
                    ROS2ListenerActivity.this, "The Start button was clicked.",
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "onClick() ended - start button");
            changeState(true);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(final View view) {
            Log.d(TAG, "onClick() called - stop button");
            Toast.makeText(
                    ROS2ListenerActivity.this, "The Stop button was clicked.",
                    Toast.LENGTH_LONG).show();
            changeState(false);
            Log.d(TAG, "onClick() ended - stop button");
        }
    };

    private void changeState(boolean isWorking) {
        this.isWorking = isWorking;
        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStart.setEnabled(!isWorking);
        buttonStop.setEnabled(isWorking);
        if (isWorking){
            //getExecutor().addNode(listenerNode);
        } else {
            //getExecutor().removeNode(listenerNode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putBoolean(IS_WORKING, isWorking);
        }
        super.onSaveInstanceState(outState);
    }
}

