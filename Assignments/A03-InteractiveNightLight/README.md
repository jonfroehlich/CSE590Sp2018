![Picture of an IoT night light](https://github.com/jonfroehlich/CSE590Sp2018/blob/master/Assignments/A03-InteractiveNightLight/IotNiteLightPicture.png)

# Overview
Imagine working for a new "Internet of Things" (IoT) company that wants to make smart, interactive ambient lights including a new series of children's night lights, artistic home lighting, and even installations for public spaces (restaurants, atriums, etc.). In this assignment, you will build a new prototype IoT night light.

The night light must be controlled both through your Android phone (via Bluetooth) and through custom physical controls that you design. Your night light should also be encased in a custom lo-fi enclosure that you design and create (e.g., using cardboard, paper, fabric, cotton balls, etc.). For your check-in next week, you must complete an initial version of the circuit and  Arduino code to change the color of the light (using a mechanism of your choosing) and brightness (using a photosensitive resistor).

For your deliverables, you will turn in a video demo (like A1), all of your source code for both Arduino and Android + pictures of your circuit, and a short 1-page report (not including images) reporting on your design and key challenges/learnings.

# Learning Goals
- Introduce and learn basics of electronic circuits, including voltage, current, and resistance
- Introduce and learn basic circuit design concepts, including voltage dividers, pull-up and pull-down resistors
- Introduce and learn the popular embedded prototyping platform Arduino (specifically, the RedBear Duo) and programming concepts therein
- Introduce and learn basic IoT concepts (e.g., connecting to a device, I/O).

As with all other assignments, we expect that you will seek out external sources to help you learn and complete this assignment. Please properly attribute your sources in your report (and in your code).

# Parts
- [5 pts] Circuit + physical controls: Design your night light using an RGB LED. The individual color hues should be selectable via custom physical controls that you design (e.g., using trim POTs, using the force-sensitive resistor). The brightness of the LED should change automatically based on ambient light (inversely proportional to light level)

- [5 pts] Smartphone app: Design a simple Android-based smartphone app that allows you to select a color via touch (a manual color picker) and via the accelerometer/gyroscope. 

- [2 pts] Lo-fi enclosure: Design a creative lo-fi enclosure that diffuses the LED (or LEDs) and exposes the controls and power. 

- [2 pts] Creative feature: Add in a creative feature of your choice--this could be a new physical control, actuation (e.g., LED affixed to servo), incorporating your step counter code (make the night light glow with each step), present ambient information (e.g., number of unread emails on smartphone, number of visits to your webpage, new commit to github), etc. Up to you. Be creative!

# Bonus
- [+1 pt] Fully synchronize states between Arduino and Android. So, when the Android app first connects with the RedBear Duo, it should synchronize its UI with the current nite light state (i.e., the color showing on Android should reflect actual color on night lite). Then, whenever the physical controls of the night lite are used, the Android UI should be properly updated. 

# Deliverables
- [2 pts] Your code (either a github link or zipped and uploaded to Canvas). Your code should follow software engineering best practices and be formatted for readability. Any code that is informed by or copied from the Internet (or other sources) must be properly commented with an attribution link back to the source. If you hosted your code on github (or some other online repo), please include a link to it in your report.

- [2 pts] A brief, 1-page report that: (i) provides an overview of your night light; (ii) describes your creative feature; (iii) enumerates key struggles and challenges; (v) reflects on what you learned. You should include as many images as you want (at least one) that helps explain your night light. Images are free. You need not write in prose (you can bullet point the entire report).

- [2 pts] A brief (2 mins or less) video overview of your night light posted to YouTube, which walks us through each of the required parts of the assignment. This can and should be super casual, no editing is necessary. You can use your laptop camera or an additional smartphone for recording. The YouTube video can be unlisted or public (your choice). Please include a link to this video in your report.
