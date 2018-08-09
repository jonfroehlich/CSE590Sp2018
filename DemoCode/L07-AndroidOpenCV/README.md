# Setting Up OpenCV in Android Studio
Notes on starting an Android OpenCV project in Android Studio.

1. Download the [OpenCV for Android library from SourceForge](https://sourceforge.net/projects/opencvlibrary/files/opencv-android/). Similar to Python, the OpenCV team maintains both a version 2 (2.4.13) and a version 3 of the library (currently 3.4.1). We are using [3.4.1](https://sourceforge.net/projects/opencvlibrary/files/opencv-android/3.4.1/) in our course. 

2. Unzip the OpenCV library in a directory of your choice. I put mine in \CSE590\libs\OpenCV-3.4.1-android-sdk. Within this folder, there should be three sub-folders: apk, samples, and sdk.

3. In Android Studio, create a new project. I made mine API 24: Android 7 (Nougat).

4. In this new project, import the OpenCV Module via File -> New -> Import Module. Select the sdk/java folder. So, on my machine the *Source Directory* says: "/Users/jonf/CSE590/OpenCVSandbox/OpenCV-3.4.1-android-sdk/sdk/java" and the *Module Name* says "openCVLibrary341". Hit Next. The ADT Importer will have three checkboxes selected--keep the defaults (all selected) and select Finish.

5. Gradle should auto-build and complete successfully (though Android Studio may need to download some additional Android SDKs). Now select Build -> Make Project. This should fail (it fails with 50 errors on my machine). What to do? See next step.

6. On the left sidebar (Project), select Grade Scripts -> build.gradle (Module: openCVLibrary341). Note: do not select the build.gradle for Module: app. Switch the minSdkVersion to 21 and the compileSdkVersion to 21. Android Studio should show the yellow pop-up at the top of the build.gradle file that says "Gradle files have change since last sync. A project sync may be necessary for the IDE to work properly. Sync Now." Click on "Sync Now."

7. Now, Gradle should auto-build again and, as before, complete successfully. Once again, select Build -> Make Project. This time, the build should work. Woohoo! But we're not done yet...

8. We have to add openCVLibrary341 as a dependency to our app. On the left sidebar (Project), right-click on "app" and select "Open Module Settings." Make sure "app" is selected under modules and click on Dependencies. In the Dependencies view, click on the "+" sign and you'll be presented with three options: Library Dependency, Jar Dependency, and Module Dependency. Select "Module Dependency" and a pop-up Dialog should appear with the title "Choose Modules." Select openCVLibrary341. Click OK. Gradle will sync again and hopefully complete successfully.

9. Now you can start using and building apps with the OpenCV Android library! For a simple example based on the [Hello OpenCV Sample](https://docs.opencv.org/2.4/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html#hello-opencv-sample), see OpenCVTest.

10. When opening OpenCV-based Android apps, you may be greeted by an "Android Gradle Plugin Update Recommended" pop-up dialog with three options: "Update", "Remind me tomorrow", "Don't remind me again for this project." Select the latter. Do not update!

# Manually Installing the OpenCV Manager
The OpenCV Manager on the Google Play store is outdated. Thus, you must manually install the OpenCV Manager in order to use the example source code (e.g., both the code I've posted to github as well as the code in /OpenCV-3.4.1-android-sdk/samples). Follow the directions below.

1. Go to "OpenCV-3.4.1-android-sdk\apk" and find the OpenCV manager apk that corresponds to your smartphone or tablet's microprocessor.
2. The Huawei Honor7X uses a 64-bit Kirin 659 ARM-based microprocessor. Thus, we will use "OpenCV_3.4.1_Manager_3.41_arm64-v8a.apk" 
3. From the terminal, type `> adb install OpenCV_3.4.1_Manager_3.41_arm64-v8a.apk` (If the adb command is not found, you might have to add platform-tools to your path. See https://stackoverflow.com/a/7609388).

# Building a Basic App
Some things you must do:
* In AndroidManifest.xml, add the following just after `</application>`:
```
<uses-permission android:name="android.permission.CAMERA"/>

<uses-feature android:name="android.hardware.camera" android:required="false"/>
<uses-feature android:name="android.hardware.camera.autofocus" android:required="false"/>
<uses-feature android:name="android.hardware.camera.front" android:required="false"/>
<uses-feature android:name="android.hardware.camera.front.autofocus" android:required="false"/>
```
* For API 23+, you must also get the user's permission at runtime in addition to the manifest permission settings. Add this code and call it from onCreate in your MainActivity class. When the permission pop-up appears, please grant the app permission to the camera. Note: you may have to run the app twice--the first time to grant the app permission and the second time it should run. Alternatively, you can go into Settings -> Apps -> `<name of your app>` -> Permissions -> Camera and grant the app permissions that way.
```
  public static void verifyAndAskForExternalStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            // For API 23+ you need to request camera permissions even if they are already in your manifest.
            // See: http://developer.android.com/training/permissions/requesting.html
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    1);

        }
    }
 ```

# Problems

## Canny Edge Detector Throws Exception
Can't get Canny edge detector to work. Received the following error:
```
E/art: No implementation found for void org.opencv.imgproc.Imgproc.Canny_3(long, long, double, double) (tried Java_org_opencv_imgproc_Imgproc_Canny_13 and Java_org_opencv_imgproc_Imgproc_Canny_13__JJDD)
E/AndroidRuntime: FATAL EXCEPTION: Thread-7
                  Process: io.makeabilitylab.opencvimageprocessing, PID: 14758
                  java.lang.UnsatisfiedLinkError: No implementation found for void org.opencv.imgproc.Imgproc.Canny_3(long, long, double, double) (tried Java_org_opencv_imgproc_Imgproc_Canny_13 and Java_org_opencv_imgproc_Imgproc_Canny_13__JJDD)
                      at org.opencv.imgproc.Imgproc.Canny_3(Native Method)
                      at org.opencv.imgproc.Imgproc.Canny(Imgproc.java:1128)
                      at io.makeabilitylab.opencvimageprocessing.MainActivity.onCameraFrame(MainActivity.java:151)
                      at org.opencv.android.CameraBridgeViewBase.deliverAndDrawFrame(CameraBridgeViewBase.java:392)
                      at org.opencv.android.JavaCameraView$CameraWorker.run(JavaCameraView.java:373)
                      at java.lang.Thread.run(Thread.java:776)
```
From Googling, I found that perhaps the OpenCV Manager on the Play Store is outdated (see [link](https://github.com/opencv/opencv/issues/9497#issuecomment-340000573)). See the subsection entitled "Manually Installing the OpenCV Manager." Once I did this, the Canny Edge detector worked great!

# Resources
A few resources I found and used when attempting to setup Android Studio for OpenCV development.
* [Building first Android OpenCV application](https://docs.opencv.org/2.4/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html#hello-opencv-sample)
* [Setting up Android Studio for OpenCV development](https://medium.com/@sukritipaul005/a-beginners-guide-to-installing-opencv-android-in-android-studio-ea46a7b4f2d3)
* [OpenCV for Tegra](https://docs.nvidia.com/gameworks/content/technologies/mobile/opencv_main.htm?tocpath=Technologies%7CMobile%20Technologies%7COpenCV%20for%20Tegra%7C_____0)

