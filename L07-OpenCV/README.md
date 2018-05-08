# Setting Up OpenCV in Android Studio
Notes on starting an Android OpenCV project in Android Studio.

1. Download the [OpenCV for Android library from SourceForge](https://sourceforge.net/projects/opencvlibrary/files/opencv-android/). Similar to Python, the OpenCV team maintains both a version 2 (2.4.13) and a version 3 of the library (currently 3.4.1). We are using [3.4.1](https://sourceforge.net/projects/opencvlibrary/files/opencv-android/3.4.1/) in our course. 

2. Unzip the OpenCV library in a directory of your choice. I put mine in \CSE590\libs\OpenCV-3.4.1-android-sdk. Within this folder, there should be three sub-folders: apk, samples, and sdk.

3. In Android Studio, create a new project. I made mine API 24: Android 7 (Nougat).

4. In this new project, import the OpenCV Module via File -> New -> Import Module. Select the sdk/java folder. So, on my machine the *Source Directory* says: "/Users/jonf/CSE590/OpenCVSandbox/OpenCV-3.4.1-android-sdk/sdk/java" and the *Module Name* says "openCVLibrary341". Hit Next. The ADT Importer will have three checkboxes selected--keep the defaults (all selected) and select Finish.

5. Gradle should auto-build and complete successfully (though Android Studio may need to download some additional Android SDKs). Now select Build -> Make Project. This should fail (it fails with 50 errors on my machine). What to do? See next step.

6. On the left sidebar (Project), select Grade Scripts -> build.gradle (Module: openCVLibrary341). Note: do not select the build.gradle for Module: app. Switch the minSdkVersion to 21 and the compileSdkVersion to 21. Android Studio should show the yellow pop-up at the top of the build.gradle file that says "Gradle files have change since last sync. A project sync may be necessary for the IDE to work properly. Sync Now." Click on "Sync Now."

7. Now, Gradle should auto-build again and, as before, complete successfully. Once again, select Build -> Make Project. This time, the build should work. Woohoo!

8. Now you can start using and building apps with the OpenCV Android library! For a simple example based on the [Hello OpenCV Sample](https://docs.opencv.org/2.4/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html#hello-opencv-sample), see OpenCVTest.

## Resources
A few resources I found and used when attempting to setup Android Studio for OpenCV development.
* [Building first Android OpenCV application](https://docs.opencv.org/2.4/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html#hello-opencv-sample)
* [Setting up Android Studio for OpenCV development](https://medium.com/@sukritipaul005/a-beginners-guide-to-installing-opencv-android-in-android-studio-ea46a7b4f2d3)
