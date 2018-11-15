boolean gravityMode = false;

float boxSize = 75f;
PImage crate;

float currentMessage = 1; //1 = message01, 2 = message02, etc.

PImage wall;
PImage button_forward;
PImage button_back;
PImage button_gravity;
PImage button_gravity_undo;
PImage button_stop;

boolean prevMousePressed = false;

float buttonSizeH = 40;
float buttonSizeW = 75;

PFont boxLabel;

boolean drawAABB = false;

int width = 800;
int height = 600;

Scene scene = new MainMenu();

void setup() {
  size(800, 600);
  scene.setup();
}

void draw() {
  scene.draw(); 
}

void keyPressed() {
  scene.keyPressed(); 
}

void mousePressed() {
  scene.mousePressed();
}

void mouseReleased() {
  scene.mouseReleased();
}
