# ros2-android-demos

将jniLibs下的库文件push到设备vendor/lib64下

用AndroidStudio编译出apk push 到 /system/priv-app/下

如果用mk编译，需要指定platform签名

启动命令：
> adb shell am start -n "org.ros2.demos/org.ros2.demos.ROS2ListenerActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
