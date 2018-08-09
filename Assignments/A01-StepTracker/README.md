# Overview
In this assignment, you will build your own step tracker using your Android smartphone's built-in accelerometer (and gyro, optional) along with basic signal processing algorithms and heuristics. You should not use machine learning for this assignment (e.g., supervised learning) as we will experiment with this approach in future assignments.

There will be a midpoint check-in on April 5 [link](https://canvas.uw.edu/courses/1199409/assignments/4187237). You will demonstrate a working version of your prototype to the TA, Liang He, during classtime on April 12.

**Clarification (04/05):** Someone asked on the discussion board: does the Step Tracker have to store tracking data across executions of the app (i.e., data persistence). No, don't worry about that.

# Learning Goals
- Introduce and learn the basics of Android development (e.g., Android Studio, using the emulator, deploying to a device, debugging, the Android architecture).
- Introduce and learn basic methods for accessing and processing built-in sensors on Android.
- Introduce and learn how to apply basic signal processing algorithms in real-time.
- Reflect on and apply basic theories/design principles of persuasive technology (e.g., see [Consolvo et al., CHI'06](https://dl.acm.org/citation.cfm?id=1124840), [Campbell et al., CSCW'08](https://dl.acm.org/citation.cfm?id=1460603)).

# Parts
- [6 pts] Design an algorithm to track steps using the Android smartphone's built-in accelerometer (and, possibly gyroscope). At a minimum, your signal processing approach should include a smoothing filter and a peak detection algorithm. The algorithm should recognize a new step with <= 2 sec latency. You can assume a fixed placement of the phone--e.g., that the user is carrying the device or has it in their pocket. Please make this assumption known in the code and your report.

- [3 pts] Design a debug visualization interfaces that shows a line graph of the real-time accelerometer signal, the smoothed signal, and debug information about your tracker algorithm. This is part of your midpoint check-in due next week, see: https://canvas.uw.edu/courses/1199409/assignments/4187237. You can roll-your-own simple line graph or use an existing library (like [GraphView](http://www.android-graphview.org/)).

- [2 pts] The debug interface should also show: (i) the number of steps your algorithm has tracked; (ii) the number of steps tracked by [Android's Step Detector Sensor](https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-stepdetector);  (iii) and the number of steps tracked by [Android's Step Counter Sensor](https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-stepcounter). **Update:** it appears that the Huawei Honor7X phones do not support the Android Step Detector Sensor (see Discussion). So, you can compare with just the Step Counter Sensor. For those using your own devices, let us know if neither the Step Detector nor Step Counter sensor works on your device.

- [3 pts] Design a creative feedback interface that highlights both when a new step is sensed as well as provides a cumulative count. For example, perhaps you make a fractal tree that dynamically grows based on the number of steps (see [Shiffman's tutorial on implementing object-oriented fractal trees](https://goo.gl/amuFTx) and [Consolvo et al.'s early work on ambient, abstract visualizations to support fitness goals](https://dl.acm.org/citation.cfm?id=1357335)). **Clarification (04/06):** Ideally, the creative interface would be the primary interface of your app and the debug interface would be hidden away separately (think about how a commercial version of this app might do it). However, this is not absolutely necessary for this assignment. For those that do separate them cleanly and elegantly, we will award up to 0.5 pts bonus. See the [Discussion](https://canvas.uw.edu/courses/1199409/discussion_topics/4276867).

- [+2 pts] You can earn up to two points bonus for adding in more sophisticated features (e.g., robustness against shaking the phone or other noise) or performing a more complete analysis of how your algorithm compares to Android's built-in step detection APIs.

# Deliverables
- [2 pts] Your code (either a github link or zipped and uploaded to Canvas). Your code should follow software engineering best practices and be formatted for readability. Any code that is informed by or copied from the Internet (or other sources) must be properly commented with an attribution link back to the source. If you hosted your code on github (or some other online repo), please include a link to it in your report.

- [2 pts] A brief, 1-page report that: (i) provides an overview of your step tracker; (ii) describes your signal processing approach; (iii) describes and provides design rationale for the feedback interface; (iv) enumerates key struggles and challenges; (v) reflects on what you learned. You should include as many images as you want (at least one) that helps explain your step tracker. Images are free. You need not write in prose (you can bullet point the entire report).

- [2 pts] A brief (2 mins or less) video overview of your step tracker posted to YouTube, which walks us through each of the required parts of the assignment. This can and should be super casual, no editing is necessary. You can use your laptop camera for recording. The YouTube video can be unlisted or public (your choice). Please include a link to this video in your report.
