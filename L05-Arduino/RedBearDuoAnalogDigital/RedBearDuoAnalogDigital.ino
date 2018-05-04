/*
 * Reads in an analog value on A0 and D0 and writes to serial. Used to show the
 * difference between analog and digital reads. For example, try to hook up
 * a potentiometer to A0 and then open Serial Monitor or Serial Plotter to view values
 * 
 * By Jon Froehlich for CSE590
 * http://makeabilitylab.io
 * 
 */

/* 
 * IMPORTANT: When working with the RedBear Duo, you must have this line of
 * code at the top of your program. The default state is SYSTEM_MODE(AUTOMATIC);
 * however, this puts the RedBear Duo in a special cloud-based mode that we 
 * are not using. For our purposes, set SYSTEM_MODE(SEMI_AUTOMATIC) or
 * SYSTEM_MODE(MANUAL). See https://docs.particle.io/reference/firmware/photon/#system-modes
 */
SYSTEM_MODE(MANUAL); 

const int ANALOG_INPUT = A0;
const int DIGITAL_INPUT = D0;

void setup() {
  pinMode(DIGITAL_INPUT, INPUT);
  pinMode(ANALOG_INPUT, INPUT);

  Serial.begin(9600);
}

void loop() {
  
  int analogVal = analogRead(ANALOG_INPUT);
  int digitalVal = digitalRead(DIGITAL_INPUT);

  Serial.print((analogVal / 4092.0) * 3.3);
  Serial.print(",");
  Serial.print(digitalVal * 3.3);
  Serial.println();

  delay(50);
}


