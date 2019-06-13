We have five assignments--one due every two weeks.

## [A1-StepTracker](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A01-StepTracker)
In this assignment, you will build your own step tracker using your Android smartphone's built-in accelerometer (and gyro, optional) along with basic signal processing algorithms and heuristics. You should not use machine learning for this assignment (e.g., supervised learning) as we will experiment with this approach in future assignments.

## [A2-GestureRecognizer](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A02-GestureRecognizer)
Imagine working for a company that would like to use the phone as an input device to an interactive video game system like the XBox or Playstation--for example, using the phone as a paddle in tennis or as a "ball" in bowling. In this assignment, you will build your own 3D gesture recognizer to automatically recognize these gestures. 

While in the "real world" you would ultimately need to create a real-time gesture recognizer, for this assignment you will make an offline version in Jupyter Notebook. Specifically, you will make two recognizers: (i) a shape-matching recognizer such as via a Euclidean distance metric or Dynamic Time Warping and (ii) a feature-based (or model-based) recognizer using a support-vector machine (SVM) (recommended), kNN, neural net, or another classifier of your choosing.

## [A3-InteractiveNightLight](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A03-InteractiveNightLight)
Imagine working for a new "Internet of Things" (IoT) company that wants to make smart, interactive ambient lights including a new series of children's night lights, artistic home lighting, and even installations for public spaces (restaurants, atriums, etc.). In this assignment, you will build a new prototype IoT night light.

The night light must be controlled both through your Android phone (via Bluetooth) and through custom physical controls that you design. Your night light should also be encased in a custom lo-fi enclosure that you design and create (e.g., using cardboard, paper, fabric, cotton balls, etc.). For your check-in next week, you must complete an initial version of the circuit and Arduino code to change the color of the light (using a mechanism of your choosing) and brightness (using a photosensitive resistor).

## [A4-SmartSpaces](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A04-SmartSpaces)
Having rolled out a very successful line of IoT night lights :), your company has tasked you with prototyping a new line of products related to smart spaces. In particular, they would like to enhance kitchen spaces with a smart tracking system that, for example, can help occupants find ingredients, track product usage for auto-reordering, and even warn parents when children get too close to a hot stove or oven. As a two-week course assignment, we are going to simplify things and only focus on the last scenario.

Using some of the computer vision (CV) concepts learned in class along with posted sample code, you are going to create a CV-based motion tracking system that: (i) Detects the face of and tracks the movement of at least one user in the Android camera view; (ii) Measures the distance to that user using the ultrasonic sensor, which is mounted on a servo motor. Using the motion tracking information from Android, the servo motor automatically rotates the ultrasonic sensor so that it is always facing the detected user.; (iii) and more, see the full [write-up here](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A04-SmartSpaces)

## [A5-Fabrication](https://github.com/jonfroehlich/CSE590Sp2018/tree/master/Assignments/A05-Fabrication)
In this assignment, you will extend your A4 project by designing and fabricating (1) a 3D-printed stand for the servo motor and (2) a 3D-printed motor attachment for the ultrasonic sensor. The 3D-printed stand should provide sufficient support such that when the servo is activated, the gearbox does not move. You may make a free-standing structure (e.g., a stand for a table) or an attachment (e.g., an attachment for the Honor7X). The 3D-printed attachment for the ultrasonic sensor should either plug directly into the servo's gear peg or onto a propeller and should robustly hold the sensor.
