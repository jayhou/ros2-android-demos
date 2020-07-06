package org.ros2.demos;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.contexts.Context;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.subscription.Subscription;
import org.ros2.rcljava.client.Client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ListenerNode  {
    private static final String TAG = "ListenerNode";
    private final String topic;

    private Node node;

    private final TextView listenerView;

    private Client<example_interfaces.srv.AddTwoInts> client;

    private Subscription<std_msgs.msg.String> subscriber;

    public ListenerNode(final TextView listenerView, Context rclContext) {
        node = RCLJava.createNode("Xcu_Node","hu_vehicle_hal", rclContext);
        this.topic = "chatter";
        this.listenerView = listenerView;
        this.subscriber = this.node.<std_msgs.msg.String>createSubscription(
                std_msgs.msg.String.class, this.topic, msg
                        -> Log.d("ListenerNode","Hello ROS2 from Android: " + msg.getData()));
        try {
            this.client = this.node.<example_interfaces.srv.AddTwoInts>createClient(example_interfaces.srv.AddTwoInts.class,"add_two_ints");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addTwoInts() {
        if(client != null) {
            Log.d(TAG, "addTwoInts ----------- ");
            example_interfaces.srv.AddTwoInts_Request request =
                    new example_interfaces.srv.AddTwoInts_Request();
            request.setA(2);
            request.setB(3);

            Future<example_interfaces.srv.AddTwoInts_Response> future =
                    client.asyncSendRequest(request);
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
}
