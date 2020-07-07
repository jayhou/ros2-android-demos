package org.ros2.demos;

import android.util.Log;
import android.widget.TextView;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.contexts.Context;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.subscription.Subscription;
import org.ros2.rcljava.client.Client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import soa_messages.srv.FrtACControl;
import soa_messages.srv.FrtACControl_Response;

public class ListenerNode  {
    private static final String TAG = "ListenerNode";
    private String topic;

    private Node node;

    private TextView listenerView;

    private Client<example_interfaces.srv.AddTwoInts> mAddTwoIntsClient;
    private Client<soa_messages.srv.FrtACControl> mFrtACControlClient;


    private Subscription<std_msgs.msg.String> subscriber;

    public ListenerNode(final TextView listenerView, Context rclContext) {
        node = RCLJava.createNode("Xcu_Node","hu_vehicle_hal", rclContext);
        this.topic = "chatter";
        this.listenerView = listenerView;
        this.subscriber = this.node.<std_msgs.msg.String>createSubscription(
                std_msgs.msg.String.class, this.topic, msg
                        -> Log.d("ListenerNode","Hello ROS2 from Android: " + msg.getData()));
        try {
            this.mAddTwoIntsClient = this.node.<example_interfaces.srv.AddTwoInts>createClient(example_interfaces.srv.AddTwoInts.class,"add_two_ints");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        try {
            mFrtACControlClient = node.createClient(soa_messages.srv.FrtACControl.class, "frt_ac_control");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void addTwoInts() {
        if(mAddTwoIntsClient != null) {
            Log.d(TAG, "addTwoInts ----------- ");
            example_interfaces.srv.AddTwoInts_Request request =
                    new example_interfaces.srv.AddTwoInts_Request();
            request.setA(2);
            request.setB(3);

            Future<example_interfaces.srv.AddTwoInts_Response> future =
                    mAddTwoIntsClient.asyncSendRequest(request);
            try {
                Log.d(TAG, "result of " + request.getA() + " + " + request.getB() + " = " + future.get().getSum());
            } catch (ExecutionException e) {
                Log.e(TAG, "ExecutionException:" + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void frtACControl() {
        if(mFrtACControlClient != null) {
            Log.d(TAG, "frtACControl ---------- ");
            soa_messages.srv.FrtACControl_Request request =
                    new soa_messages.srv.FrtACControl_Request();
            request.setControlSource((byte)0x0);
            request.setFrtAcswReq(true);
            request.setFrtModeReq((byte)0x1);
            request.setRequestId((short)88);
            Future<FrtACControl_Response> future =
                    mFrtACControlClient.asyncSendRequest(request);
            try {
                Log.d(TAG, "result of request id : " + request.getRequestId() + "  response code:" + future.get().getResponseCode());
            } catch (ExecutionException e) {
                Log.e(TAG, "ExecutionException:" + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException:" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "frtACControlClient not ready!");
        }
    }
}
