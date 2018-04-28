

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
  
  int potVal = analogRead(POT_INPUT_PIN);
  int ledVal = map(potVal, 0, 4092, 0, 255);
  analogWrite(LED_OUTPUT_PIN, ledVal);

  delay(100);
}




