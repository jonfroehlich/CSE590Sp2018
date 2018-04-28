/*
 * This example reads in a potentiometer value (from A0) and sets the brightness 
 * of an LED (hooked up to D0).
 * 
 * The analogWrite() function uses PWM, so if you want to change the pin you're
 * using, be sure to use another PWM capable pin. On the Arduino Uno, the PWM pins
 * are identified with a "~" sign, like ~3, ~5, ~6, ~9, ~10 and ~11. On the RedBear
 * Duo, please consult the pin layout data sheet provided in class or the lecture slides.
 * 
 * By Jon Froehlich for CSE590
 * http://makeabilitylab.io
 */

/* 
 * IMPORTANT: When working with the RedBear Duo, you must have this line of
 * code at the top of your program. The default state is SYSTEM_MODE(AUTOMATIC);
 * however, this puts the RedBear Duo in a special cloud-based mode that we 
 * are not using. For our purposes, set SYSTEM_MODE(SEMI_AUTOMATIC) or
 * SYSTEM_MODE(MANUAL). See https://docs.particle.io/reference/firmware/photon/#system-modes
 */
SYSTEM_MODE(MANUAL); 

const int LED_OUTPUT_PIN = D0;
const int POT_INPUT_PIN = A0;

void setup() {
  pinMode(LED_OUTPUT_PIN, OUTPUT);
  pinMode(POT_INPUT_PIN, INPUT);
}

void loop() {

  // read the potentiometer value
  int potVal = analogRead(POT_INPUT_PIN);

  // the analogRead on the RedBear Duo seems to go from 0 to 4092 (and not 4095
  // as you would expect with a power of two--e.g., 2^12 or 12 bits). On the Arduino
  // Uno, the analogRead ranges from 0 to 1023 (2^10 or 10 bits). Regardless,
  // we need to remap this value linearly from the large range (0-4092) to
  // the smaller range (0-255) since the analogWrite function can only write out
  // 0-255 (a byte--2^8). 
  int ledVal = map(potVal, 0, 4092, 0, 255);

  // write out the LED value. This value is translated to voltage by: 
  // voltageVal = max_voltage * val/255 or voltageVal = 3.3V * val/255 in 
  // the case of the RedBear Duo
  analogWrite(LED_OUTPUT_PIN, ledVal);

  delay(100);
}




