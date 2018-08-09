# Overview
Having rolled out a very successful line of IoT night lights :), your company has tasked you with prototyping a new line of products related to smart spaces. In particular, they would like to enhance kitchen spaces with a smart tracking system that, for example, can help occupants find ingredients, track product usage for auto-reordering, and even warn parents when children get too close to a hot stove or oven. As a two-week course assignment, we are going to simplify things and only focus on the last scenario.

Using some of the computer vision (CV) concepts learned in class along with posted sample code, you are going to create a CV-based motion tracking system that:

- Detects the face of and tracks the movement of at least one user in the Android camera view
- Measures the distance to that user using the ultrasonic sensor, which is mounted on a servo motor. Using the motion tracking information from Android, the servo motor automatically rotates the ultrasonic sensor so that it is always facing the detected user. We have posted example code for the [servo here](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/DemoCode/L06-Arduino/RedBearDuoServoSweep) and the [ultrasonic sensor here](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/DemoCode/L06-Arduino/RedBearDuoUltrasonicRangeFinder). Note that the servo motor only rotates from 0-180 degrees. 
- Flashes an LED and plays a tone using the piezoelectric buzzer when the detected user is within 0.5 meters. Note that it appears the tone function only works on pins are: D0, D1, A0, A1, A4, A5, A6, A7, RX, TX. See this [Discussion](https://canvas.uw.edu/courses/1199409/discussion_topics/4335655) and this [forum post](https://community.particle.io/t/tone-doesnt-work-solved/5806/7).
- Has a functional lo-fi stand for the servo that allows the servo to sit robustly on a table or other surface even when the motor is sweeping back and forth and a lo-fi attachment mechanism that affixes the ultrasonic sensor to the servo motor. For A5, you will design and fabricate 3D-printed versions.
- Includes one creative feature related to the "smart spaces" theme (e.g., face recognition, speech interaction, hand tracking + gesture recognition)

The *midpoint checkin* includes properly tracking a face and automatically adjusting the servo motor accordingly.

For your deliverables, you will turn in a video demo, all of your source code for both Arduino and Android + pictures of your circuit, and a short 1-page report (not including images) reporting on your design and key challenges/learnings.

# Learning Goals
- Reinforce and extend learning related to electronic circuits, sensor and actuators, and hardware prototyping
- Gain experience using computer vision libraries and a basic understanding of CV principles
- Begin thinking about hardware enclosures and electro-mechanical design (i.e., designing for actuation).

As with previous assignments, we expect that you will seek out external sources to help you learn and complete this assignment. Please properly attribute your sources in your report (and in your code).

# Parts
We have provided significant amounts of skeleton code for this assignment on github (for both Arduino and Android). Please see the [A04-SmartSpaces/Code](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A04-SmartSpaces/Code) dir in github.

- [4.5 pts] Arduino, circuit, sensors+actuators: The Arduino+hardware portion of this assignment includes: (i) a servo motor that responds, in real-time, to the location of the detected user in the Android camera; (ii) an ultrasonic sensor attached to the servo that measures the distance to the detected user and transmits this data back to Android; (iii) an alarm + flashing LEDs when the detected user is within 0.5 meters

- [4.5 pts] Android: The Android app should: (i) detect one or more user faces in real-time; (ii) track their location and transmit this data to Arduino for processing; (iii) receive the distance calculations from Arduino and visualize them on the screen. Your distance calculation output here can be anything you want from simple text output to something more creative (in a future prototype, you could imagine using this distance estimation to trigger taking a video/picture that is then sent to the user much like home security systems).

- [1 pt] Smoothing. The face tracking data and the ultrasonic measurements should be appropriately smoothed to improve performance (a window-based moving average filter is fine). You should decide whether to perform the smoothing on Android or Arduino or both!

- [1 pt] Lo-fi stand and servo attachment: Design a functional stand for the servo motor (card board works great for this) and an attachment for the ultrasonic sensor (which should sit robustly on top of the servo). In the next assignment (A5), you will design and fabricate 3D-printed versions. 

- [3 pts] Creative feature: Add in a creative feature of your choice thematically related to "smart spaces." For example, you could include face recognition in addition to detection so that the system could differentiate between adults and children and only sound the alarm for the latter category. You could also try and process the video stream (e.g., using a technique learned in class) to track general motion (not just of faces) or track hands and perform basic gesture recognition.

# Deliverables
- [2 pts] Your code (either a github link or zipped and uploaded to Canvas). Your code should follow software engineering best practices and be formatted for readability. Any code that is informed by or copied from the Internet (or other sources) must be properly commented with an attribution link back to the source. If you hosted your code on github (or some other online repo), please include a link to it in your report.

- [2 pts] A brief, 1-page report that: (i) provides an overview of your design; (ii) describes your creative feature; (iii) enumerates key struggles and challenges; (v) reflects on what you learned. You should include as many images as you want (at least one) that helps explain your night light. Images are free. You need not write in prose (you can bullet point the entire report). Please use headers to clearly separate the four sections of your report.

- [2 pts] A brief (2 mins or less) video overview of your design posted to YouTube, Vimeo, or some other cloud service (we will not accept uploaded mp4s directly to Canvas). The video should walk us through each of the required parts of the assignment. This can and should be super casual, no editing is necessary. You can use your laptop camera or an additional smartphone for recording. The video can be unlisted or public (your choice). Please include a link to this video in your report.
