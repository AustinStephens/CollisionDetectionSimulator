import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CollisionDetectionSim extends PApplet {

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

public void setup() {
  
  scene.setup();
}

public void draw() {
  scene.draw(); 
}

public void keyPressed() {
  scene.keyPressed(); 
}

public void mousePressed() {
  scene.mousePressed();
}

public void mouseReleased() {
  scene.mouseReleased();
}
/*
 * This class provides a simple AABB that can be updated, drawn, and used for
 * collision detection against other AABB objects. This class is used within
 * the Polygon class.
 */
class AABB {
  /*
   * This flag is set to false each update, and true during collision detection if it collides.
   * When true, this AABB is drawn in red.
   */
  private boolean colliding = false;
  /*
   * The four edges of this AABB. Each are measured from the world origin.
   */
  public float xmin, xmax, ymin, ymax;
  /*
   * This method should be called each update before collision detection to reset the colliding flag.
   */
  public void resetColliding(){
    colliding = false;
  }
  /*
   * This method recalculates the AABB's four edges by stepping through a list of transformed
   * points in order to find the minimum and maximum values in the x and y dimensions.
   *
   * @param PVector[] points  The list of points to step through.
   */
  public void recalc(PVector[] points) {
    for (int i = 0; i < points.length; i++) {
      PVector p = points[i];
      if (i == 0 || p.x < xmin) xmin = p.x;
      if (i == 0 || p.x > xmax) xmax = p.x;
      if (i == 0 || p.y < ymin) ymin = p.y;
      if (i == 0 || p.y > ymax) ymax = p.y;
    }
  }
  /*
   * This method draws the AABB. If the colliding flag is set to true, this is drawn in red. 
   */
  public void draw() {
    noFill();
    stroke(255);
    strokeWeight(.5f);
    if(colliding) stroke(255, 0, 0);
    rectMode(CORNERS);
    //drawAABB = true;
    if(drawAABB)
      rect(xmin, ymin, xmax, ymax);
  }
  /*
   * Check for collision between this and another AABB.
   *
   * @param AABB aabb  The other AABB to check against this object.
   * @return boolean  Whether or not the objects are colliding.
   */
  public boolean checkCollision(AABB aabb){
    if (xmax < aabb.xmin) return false;
    if (xmin > aabb.xmax) return false;
    if (ymax < aabb.ymin) return false;
    if (ymin > aabb.ymax) return false;
    colliding = true; // flag as colliding this frame so that we draw it in red
    aabb.colliding = true; // set flag on other object
    return true;
  }
}
public class AABB_Prototype extends Scene {

  ArrayList<Box> boxes = new ArrayList();
  ArrayList<Button> buttons = new ArrayList();
  
  ArrayList<String> letters = new ArrayList();
  
  PFont f;
  
  String message01 ="** What is AABB Collision? ** : Also known as 'Axis-Aligned Bounding Box' Collision, AABB Collision forms a rectangular hitbox, or bounding box, around the objects of a 2D game or interactive experience. It then checks to see if the bottom, top, right, or left sides of the bounding box are overlapping with another bounding box or other object. If overlap does occur, the function will return true for that specific side.";
  String message02 ="** Step 1: Variables ** : Your specific method of setting up AABB Collision can vary, but you're going to need at least four variables for each object: its x coordinate, its y coordinate, the object's width, and the object's height. For the latter two, what you put will depend on where the object's center is. For the purpose of this tutorial, we are going to assume that the object's center will be directly in the middle of the object.";
  String message03 ="** Step 2: Setting Up the Sides ** : Up next, you should have every object calculate the edges of its bounding box. This is done by taking the object's x and y coordinates, and then either adding or subtracting its width and height from those values. To find the right and left sides, you would add or subtract half the width to its x value, respectively. To find the bottom and top sides, you would add or subtract half the height to its y value, respectively.";
  String message04 ="** Step 3: Detecting Collision ** : Finally, we have to write the function that checks to see if there is any overlap between the bounding boxes that we have created! To do this, the function will need to have two object inputs: one bounding box, and the 'other' bounding box. For the sake of the tutorial, let's call them A and B.";
  String message05 ="** Step 3: Detecting Collision (cont.d) ** : Next, include multiple 'if' statements that check to see if the value of one of A's sides is either greater than or less than one of B's sides. Specifically, you will want to compare A's right and B's left, A's left and B's right, A's bottom and B's top, and A's top and B's bottom. If any of these 'if' statements are true, you can either have the function return a general 'collision detected' response or, if necessary, a response specific to those two sides.";
  String message06 ="** Step 4: AABB In Action ** : As you may have noticed, there are several boxes strewn about the tutorial's screen. These boxes can be dragged around with the mouse, and if any of their sides overlap with another box's sides, all of the overlapping sides will glow a different color. You may have also noticed the big 'Gravity' button in the upper right-hand corner.";
  String message07 ="** Step 4: AABB In Action (cont.d) ** : Clicking this 'Gravity' button will replace this blander scene with that of a warehouse, and the boxes will turn into crates affected by gravity! They will mainly check for bottom and top collisions at this point, using them to determine when to stop falling downwards.";
  String message08 ="** Step 5: When to Not Use AABB ** : AABB certainly sounds like a handy way to detect collision, yes? Well, there are a number of situations in which using radial collision or point collision would be better. In general, AABB works best with square or rectangular objects. After that, collision detection using bounding boxes becomes more problematic as the objects grow more complex.";
  String message09 ="** Step 5: When to Not Use AABB (cont.d) ** : This would be easily noticable with a circle, which would check for collision even in the otherwise empty corners of the object; in this case, radial collision would be best. If the object were even more complex, such as a player character, these extra collision zones would be even more apparent; point collision would be best for an object that is more complex than a square or a circle.";
  
  
  public void setup() {
    noStroke();
    
    letters.add("A");
    letters.add("B");
    letters.add("C");
    letters.add("D");
    
    float halfBoxSize = boxSize / 2;
  
    Box box1 = new Box();
    box1.x = (width * 0.25f) - halfBoxSize;
    box1.y = (height * 0.55f) - halfBoxSize;
    boxes.add(box1);
    
    println(box1.x);
    println(box1.y);
  
    Box box2 = new Box();
    box2.x = (width * 0.25f) - halfBoxSize;
    box2.y = (height * 0.85f) - halfBoxSize;
    boxes.add(box2);
  
    Box box3 = new Box();
    box3.x = (width * 0.75f) - halfBoxSize;
    box3.y = (height * 0.55f) - halfBoxSize;
    boxes.add(box3);
  
    Box box4 = new Box();
    box4.x = (width * 0.75f) - halfBoxSize;
    box4.y = (height * 0.85f) - halfBoxSize;
    boxes.add(box4);
    
    Button gravity = new Button();
    gravity.x = (width * 0.9f) - 50;
    gravity.y = (height * 0.1f);
    buttons.add(gravity);
    
    Button forward = new Button();
    forward.x = (width * 0.15f) - 50;
    forward.y = (height * 0.315f);
    buttons.add(forward);
    
    Button back = new Button();
    back.x = (width * 0.3f) - 50;
    back.y = (height * 0.315f);
    buttons.add(back);
    
    f = createFont("Arial Bold",11.5f,true);
    boxLabel = createFont("Arial Bold", 24, true);
    
    wall = loadImage("brick_wall" + ".png");
    crate = loadImage("crate" + ".png");
    button_forward = loadImage("button_forward" + ".png");
    button_back = loadImage("button_back" + ".png");
    button_gravity = loadImage("button_gravity" + ".png");
    button_gravity_undo = loadImage("button_gravity_undo" + ".png");
    button_stop = loadImage("button_stop" + ".png");
  }
  
  public void draw() {
    if(gravityMode == true){
      image(wall, 0, 0, 800, 600);
    } else {
      background(128);
    }
    fill(0);
    rect(2,2,328,183);
    
    fill(255);
    rect(5, 5, 325, 180);
  
    for (int i = 0; i < boxes.size(); i++) {
      boxes.get(i).update();
    }
    for (int i = 0; i < boxes.size(); i++) {
      boxes.get(i).draw(letters.get(i));
    }
    for (int i = 0; i < boxes.size(); i++) {
      for (int j = 0; j < boxes.size(); j++) {
        if(j != i && boxes.get(j).checkCollision(boxes.get(i)))
        boxes.get(i).drawAABBLines(boxes.get(j));
      }
    }
    
    for (int k = 0; k < buttons.size(); k++) {
      buttons.get(k).update();
    }
    for (int k = 0; k < buttons.size(); k++) {
      buttons.get(k).draw();
    }
    
    fill(0);
    textFont(f);
    if(currentMessage == 1) {
      text(message01, 10, 10, 320, 170);
    }
    if(currentMessage == 2) {
      text(message02, 10, 10, 320, 170);
    }
    if(currentMessage == 3) {
      text(message03, 10, 10, 320, 170);
    }
    if(currentMessage == 4) {
      text(message04, 10, 10, 320, 170);
    }
    if(currentMessage == 5) {
      text(message05, 10, 10, 320, 170);
    }
    if(currentMessage == 6) {
      text(message06, 10, 10, 320, 170);
    }
    if(currentMessage == 7) {
      text(message07, 10, 10, 320, 170);
    }
    if(currentMessage == 8) {
      text(message08, 10, 10, 320, 170);
    }
    if(currentMessage == 9) {
      text(message09, 10, 10, 320, 170);
    }
    
    //println("X: " + mou/println("Y: " + mouseY);
  }
  
  //void drawBox(Box b) {
  //  if (mousePressed && (mouseButton == LEFT) && mouseX > b.sideL && mouseX < b.sideR && mouseY > b.sideU && mouseY < b.sideD) {
  //    fill(255, 100, 100);
  //    rect(mouseX, mouseY, boxSize, boxSize);
  //
  //    fill(0);
  //    ellipse(mouseX, mouseY, 10, 10);
  //  } else {
  //    fill(255);
  //    rect(b.x, b.y, boxSize, boxSize);
  //
  //    fill(0);
  //    ellipse(b.x, b.y, 10, 10);
  //  }
  //}
  
  //void checkAABB(Box d, Box e) {
  //  if (d.sideR > e.sideL) {
  //    strokeWeight(5);
  //    stroke(100, 255, 100);
  //    line(d.sideR, d.sideU, d.sideR, d.sideD);
  
  //    strokeWeight(5);
  //    stroke(100, 255, 100);
  //    line(e.sideL, e.sideU, e.sideL, e.sideD);
  //  }
  //  if (d.sideL < e.sideR) {
  //    strokeWeight(5);
  //    stroke(100, 255, 100);
  //    line(d.sideL, d.sideU, d.sideL, d.sideD);
  
  //    strokeWeight(5);
  //    stroke(100, 255, 100);
  //    line(e.sideR, e.sideU, e.sideR, e.sideD);
  //  }
  //  if (d.sideD > e.sideU) {
  //    strokeWeight(5);
  //    stroke(100, 100, 255);
  //    line(d.sideL, d.sideD, d.sideR, d.sideD);
  
  //    strokeWeight(5);
  //    stroke(100, 100, 255);
  //    line(e.sideL, e.sideU, e.sideR, e.sideU);
  //  }
  //  if (d.sideU < e.sideD) {
  //    strokeWeight(5);
  //    stroke(100, 100, 255);
  //    line(d.sideL, d.sideU, d.sideR, d.sideU);
  
  //    strokeWeight(5);
  //    stroke(100, 100, 255);
  //    line(e.sideL, e.sideD, e.sideR, e.sideD);
  //  }
  //}
  
  
  
  public void mousePressed() {
    println("mouse: " + mouseX + " " + mouseY);
    for (int i = 0; i < boxes.size(); i++) {
      if (prevMousePressed != mousePressed) {
        boxes.get(i).clicked();
      }
    }
  }
  
  public void mouseReleased() {
    for (int i = 0; i < boxes.size(); i++) {
      boxes.get(i).lockOn = false;
    }
    prevMousePressed = false;
  }
  
  public void keyPressed() {
    if(keyCode == BACKSPACE) {
      gravityMode = false;
      currentMessage = 1;
      scene = new MainMenu();
      scene.setup();
    }
  }
  
  //void mouseDragged() {
  //  if (locked) {
  //    container.movable.x = mouseX - container.x/2;
  //    container.movable.y = mouseY - container.y/2;
  //  }
  //}
}
class Box {
  
  float x;
  float y;
  
  float sideL;
  float sideR;
  float sideU;
  float sideD;
  
  float LIMIT_Y = 480;
  float velY = 0;
  
  boolean hasLandedOnBox = false;
  
  boolean lockOn = false;
  boolean colliding = false;
  
  boolean dirtyOverlap = false;
  
  Box() {

  }
  
  public void update() {
    float halfBoxSize = boxSize / 2;
    
    sideL = (x - halfBoxSize) + halfBoxSize;
    sideR = (x + halfBoxSize) + halfBoxSize;
    sideU = (y - halfBoxSize) + halfBoxSize;
    sideD = (y + halfBoxSize) + halfBoxSize;
    
    if(gravityMode && !lockOn) {
      updatePhysics();
    } else if (!gravityMode) {
      velY = 0;
    }
    
    if(lockOn) {
      x = mouseX - halfBoxSize;
      y = mouseY - halfBoxSize;
      if(gravityMode) {
        velY = 0;
      }
    }
  }
  
  public void draw(String letter) {
    //rect(x, y, boxSize/2, boxSize/2);
    //if (mousePressed && (mouseButton == LEFT) && mouseX > sideL && mouseX < sideR && mouseY > sideU && mouseY < sideD) {
    if (lockOn == true) {
      fill(255, 100, 100);
      if(gravityMode){
        image(crate, mouseX - boxSize/2, mouseY - boxSize/2, boxSize, boxSize);
      } else {
        rect(mouseX - boxSize/2, mouseY - boxSize/2, mouseX + boxSize/2, mouseY + boxSize/2);
      }
      
      fill(0);
      textFont(boxLabel);
      textAlign(CENTER, CENTER);
      text(letter, mouseX, mouseY);
      textAlign(LEFT, TOP);
      //ellipse(mouseX, mouseY, 10, 10);
    } else {
      fill(222);
      if(gravityMode){
        image(crate, x, y, boxSize, boxSize);
      } else {
        rect(x, y, x + boxSize, y + boxSize);
      }
  
      fill(0);
      textFont(boxLabel);
      textAlign(CENTER, CENTER);
      text(letter, x + boxSize/2, y + boxSize/2);
      textAlign(LEFT, TOP);
      //ellipse(x + boxSize/2, y + boxSize/2, 10, 10);
    }
    
    
  }
  
  public void clicked() {
    float halfBoxSize = boxSize / 2;
    float d = dist(mouseX, mouseY, x + halfBoxSize, y + halfBoxSize);
    if (d < halfBoxSize) {
      prevMousePressed = mousePressed;
      if(prevMousePressed) {
        lockOn = true;
      }
    } else {
      
      lockOn = false;
    }
  }
  
  public boolean checkCollision(Box b){
    if (sideR < b.sideL || sideL > b.sideR || sideD < b.sideU || sideU > b.sideD) {
      colliding = false;
      b.colliding = false;
      return false;  
    }
    colliding = true; // flag as colliding this frame so that we draw it in red
    b.colliding = true; // set flag on other object
    return true;
  }
  
  //*** THIS SORTA WORKS, BUT STILL NEED ARRAY HELP FROM VARUN ***
  //*** WITHOUT dirtyOverlap AND b-CENTER CHECKS, IT DOESN'T WORK ***
  public void drawAABBLines(Box b) {
  if (sideU < b.sideD && !dirtyOverlap && sideU > (b.y + boxSize/2)) {
    if(!gravityMode) {
      strokeWeight(5);
      stroke(255, 100, 255);
      line(sideL, sideU, sideR, sideU);

      strokeWeight(5);
      stroke(255, 100, 255);
      line(b.sideL, b.sideD, b.sideR, b.sideD);
      //println("O.Up overlapping B.Down! Pink!");
      dirtyOverlap = true;
    }
  }
  if (sideD > b.sideU && !dirtyOverlap && sideD < (b.y + boxSize/2)) {
    if(gravityMode && !lockOn) {
      hasLandedOnBox = true;
      y = b.y - (boxSize * 0.99f);
      velY = 0;
    }
    if(!gravityMode) {
      strokeWeight(5);
      stroke(100, 100, 255);
      line(sideL, sideD, sideR, sideD);

      strokeWeight(5);
      stroke(100, 100, 255);
      line(b.sideL, b.sideU, b.sideR, b.sideU);
      //println("O.Down overlapping B.Up! Blue!");
      dirtyOverlap = true;
    }
  }
  if (sideR > b.sideL && !dirtyOverlap && sideR < (b.x + boxSize/2)) {
    if(!gravityMode) {
      strokeWeight(5);
      stroke(100, 255, 100);
      line(sideR, sideU, sideR, sideD);

      strokeWeight(5);
      stroke(100, 255, 100);
      line(b.sideL, b.sideU, b.sideL, b.sideD);
      //println("O.Right overlapping B.Left! Green!");
      dirtyOverlap = true;
    }
  }
  if (sideL < b.sideR && !dirtyOverlap && sideL > (b.x + boxSize/2)) {
    if(!gravityMode) {
      strokeWeight(5);
      stroke(200, 100, 100);
      line(sideL, sideU, sideL, sideD);

      strokeWeight(5);
      stroke(200, 100, 100);
      line(b.sideR, b.sideU, b.sideR, b.sideD);
      //println("O.Left overlapping B.Right! Dark red!");
      dirtyOverlap = true;
    }
  }
  noStroke();
  dirtyOverlap = false;
}

public void updatePhysics() {
  float accY = 0.75f;
  
  if(!hasLandedOnBox) {
    velY += accY;
    y += velY;
  } else {
    velY = 0;
    hasLandedOnBox = false;
  }
  
  if(y > LIMIT_Y) {
    y = LIMIT_Y;
    velY = 0;
  }
}
  
}
class Button {
  float x;
  float y;
  
  boolean prevMousePressed = false;
  
  Button() {
    
  }
  
  public void update() {
    if(mousePressed && mouseOverlap() && !prevMousePressed) {
      prevMousePressed = true;
      buttonClick();
    }
    prevMousePressed = mousePressed;
  }
  
  public boolean mouseOverlap() {
    if(mouseX > x && mouseX < (x + buttonSizeW) && mouseY > y && mouseY < (y + buttonSizeH)) {
      return true;
    } else {
      return false;
    }
  }
  
  public void draw() {
    if(mouseOverlap()) {
      fill(100);
    } else {
      fill(50);
    }
    
    if(x > 68 && x < 72) {
      if(currentMessage != 1) {
        image(button_back, x, y, buttonSizeW, buttonSizeH);
      } else {
        image(button_stop, x, y, buttonSizeW, buttonSizeH);
      }
    }
    if(x > 188 && x < 192) {
      if(currentMessage != 9) {
        image(button_forward, x, y, buttonSizeW, buttonSizeH);
      } else {
        image(button_stop, x, y, buttonSizeW, buttonSizeH);
      }
    }
    if(x == 670) {
      if(gravityMode == false) {
        image(button_gravity, x, y, buttonSizeW, buttonSizeH);
      } else {
        image(button_gravity_undo, x, y, buttonSizeW, buttonSizeH);
      }
    }
    //rect(x, y, buttonSizeW, buttonSizeH);
  }
  
  //*** DIRTY BUTTON DIFFERENTIATION ***
  public void buttonClick() {
    if(mouseX > 70 && mouseX < 145) {
      println("Back clicked!");
      if(currentMessage == 1) {
        currentMessage = 1; //no messages past message01
      }
      if(currentMessage == 2) {
        currentMessage = 1;
      }
      if(currentMessage == 3) {
        currentMessage = 2;
      }
      if(currentMessage == 4) {
        currentMessage = 3;
      }
      if(currentMessage == 5) {
        currentMessage = 4;
      }
      if(currentMessage == 6) {
        currentMessage = 5;
      }
      if(currentMessage == 7) {
        currentMessage = 6;
      }
      if(currentMessage == 8) {
        currentMessage = 7;
      }
      if(currentMessage == 9) {
        currentMessage = 8;
      }
    }
    if(mouseX > 190 && mouseX < 265) {
      println("Forward clicked!");
      if(currentMessage == 9) {
        currentMessage = 9; //no messages past message09
      }
      if(currentMessage == 8) {
        currentMessage = 9;
      }
      if(currentMessage == 7) {
        currentMessage = 8;
      }
      if(currentMessage == 6) {
        currentMessage = 7;
      }
      if(currentMessage == 5) {
        currentMessage = 6;
      }
      if(currentMessage == 4) {
        currentMessage = 5;
      }
      if(currentMessage == 3) {
        currentMessage = 4;
      }
      if(currentMessage == 2) {
        currentMessage = 3;
      }
      if(currentMessage == 1) {
        currentMessage = 2;
      }
    }
    if(mouseX > 670 && mouseX < 745) {
      println("Gravity clicked!");
      gravityMode = !gravityMode;
    }
  }
}
class CollisionState extends State {
  
  private float timer;
  private final float MAX_TIMER = 0.25f;
  
  private PVector[] drawnNormals1;
  private PVector[] drawnNormals2;
  private boolean movingPoly1 = false;
  private boolean movingPoly2 = false;
  
  private float timeSinceLastClick = 0;
  private boolean advancedMode = false;
  private int normalIndex = 0;
  private int pointIndex = 0;
  
  private boolean poly2Points = false;
  private boolean poly2Normals = false;
  
  private PVector previousMouse;
  
  private Polygon poly1;
  private Polygon poly2;

  
   public State update() {
     fill(0);
     textAlign(CENTER);
     textSize(14);
     text("Double click to " + (advancedMode ? "return to regular mode" : "go to advanced mode"), 400, 20);
     drawAABB = true;
     if(poly1.checkCollision(poly2))
     {
       poly1.colliding = true;
       poly2.colliding = true;
     }
    
    if(movingPoly1) {
      PVector dif = new PVector(mouseX - previousMouse.x, mouseY - previousMouse.y);
      PVector p = poly1.getPosition().get();
      poly1.setPosition(p.add(dif));
    } else if(movingPoly2) {
      PVector dif = new PVector(mouseX - previousMouse.x, mouseY - previousMouse.y);
      PVector p = poly2.getPosition().get();
      poly2.setPosition(p.add(dif));
    }
   
     stroke(0, 255, 255);
     strokeWeight(.75f);
     for(int i = 0; i < drawnNormals1.length; i++) {
       PVector p = poly1.centerOfEdge(i);
       line(p.x, p.y, p.x + drawnNormals1[i].x, p.y + drawnNormals1[i].y);
     }
    
     for(int i = 0; i < drawnNormals2.length; i++) {
       PVector p = poly2.centerOfEdge(i);
       line(p.x, p.y, p.x + drawnNormals2[i].x, p.y + drawnNormals2[i].y);
     }
     
     if(advancedMode) {
       timer += Time.deltaTime;
       
       Polygon polyPoints = (poly2Points ? poly2 : poly1);
       Polygon polyNormals = (poly2Normals ? poly2 : poly1);
       
       PVector coe = polyNormals.centerOfEdge(normalIndex);
       PVector axis = PVector.mult(polyNormals.normals[normalIndex], 200);
       PVector pt = polyPoints.pointsTransformed[pointIndex];
       float value = PVector.dot(polyNormals.normals[normalIndex], pt) / pt.mag();
       
       PVector proj = PVector.add(coe, PVector.mult(polyNormals.normals[normalIndex], value * 50));
       
       //line(coe.x - axis.x, coe.y - axis.y, coe.x + axis.x, coe.y + axis.y);

       line(coe.x, coe.y, proj.x, proj.y);
       stroke(20);
       line(pt.x, pt.y, proj.x, proj.y);
       
       
       if(timer >= MAX_TIMER) {
         
         if(pointIndex < polyPoints.pointsTransformed.length - 1) pointIndex++;
         else {
           if(polyPoints == poly2) normalIndex++;
           pointIndex = 0;
           poly2Points = !poly2Points;
         }
         
         if(!(normalIndex < polyNormals.normals.length - 1)) {
           normalIndex = 0;
           poly2Normals = !poly2Normals;
         }
         
         timer = 0;
       } 
     }
      
     timeSinceLastClick += Time.deltaTime;
     previousMouse = new PVector(mouseX, mouseY);
     return null;
   }
   
   public CollisionState(Polygon poly1, Polygon poly2) {
      print("hello");
      this.poly1 = poly1;
      this.poly2 = poly2;
      drawnNormals1 = new PVector[poly1.normals.length];
      drawnNormals2 = new PVector[poly2.normals.length];
      
      for(int i = 0; i < drawnNormals1.length; i++) {
         drawnNormals1[i] = new PVector(-poly1.normals[i].x * Polygon.normalLength, -poly1.normals[i].y * Polygon.normalLength);
      }
      
      for(int i = 0; i < drawnNormals2.length; i++) {
        drawnNormals2[i] = new PVector(-poly2.normals[i].x * Polygon.normalLength, -poly2.normals[i].y * Polygon.normalLength);
      }
      
      previousMouse = new PVector(mouseX, mouseY);
   }
   
   public State onMousePressed() {
     //print(poly1.isColliding(new PVector(mouseX, mouseY)));
     if(poly1.isColliding(new PVector(mouseX, mouseY))) 
     {
       movingPoly1 = true;
     } else if (poly2.isColliding(new PVector(mouseX, mouseY))) {
       movingPoly2 = true;
     } 
     
     if(timeSinceLastClick < .2f) {advancedMode = !advancedMode; println("hey");}
     //print("not hit");
     return null;
   }
   
   public State onMouseReleased(Polygon poly1, Polygon poly2) {
     movingPoly1 = false;
     movingPoly2 = false;
     timeSinceLastClick = 0;
     return null;
   }
}
class DrawState extends State{
  
  private float timer = 0;
  private final float MAX_TIMER = .5f;
  private ArrayList<PVector> drawnNormals1 = new ArrayList<PVector>();
  private ArrayList<PVector> drawnNormals2 = new ArrayList<PVector>();
  private int currentNormalIndex = 0;
  
  private boolean drawPoly2Normals = false;

  private Polygon poly1;
  private Polygon poly2;
  
  public DrawState(Polygon poly1, Polygon poly2)
  {
    this.poly1 = poly1;
    this.poly2 = poly2;
    poly1.onDrawState();
    poly2.onDrawState();
    drawnNormals1.add(new PVector(0,0));
    drawnNormals2.add(new PVector(0,0));
    
  }
 
  public State update() {
    timer += Time.deltaTime;
    //print(timer);
    
    if(!drawPoly2Normals && poly1.normals.length > 0)
        drawnNormals1.set(currentNormalIndex, new PVector(-poly1.normals[currentNormalIndex].x * timer / MAX_TIMER * Polygon.normalLength, -poly1.normals[currentNormalIndex].y * timer / MAX_TIMER * Polygon.normalLength));
    else if(poly2.normals.length > 0)
        drawnNormals2.set(currentNormalIndex, new PVector(-poly2.normals[currentNormalIndex].x * timer / MAX_TIMER * Polygon.normalLength, -poly2.normals[currentNormalIndex].y * timer / MAX_TIMER * Polygon.normalLength));
    
    
    if(timer >= MAX_TIMER) {
      timer = MAX_TIMER;
      if(!drawPoly2Normals) {
        if(currentNormalIndex < poly1.normals.length - 1) {
            currentNormalIndex++;
            drawnNormals1.add(new PVector(0,0));
        }
        else {
          currentNormalIndex = 0;
          drawPoly2Normals = true;
        }
      } else {
        if(currentNormalIndex < poly2.normals.length - 1) {
          currentNormalIndex++;
          drawnNormals2.add(new PVector(0,0));
        }
        else 
          return new CollisionState(poly1, poly2);
      }
      
      
      
      timer = 0;
    }
    //print(poly1.normals.length);
    
    stroke(0, 255, 255);
    strokeWeight(.75f);
    for(int i = 0; i < drawnNormals1.size(); i++) {
       if(poly1.normals.length > i) {
         PVector p = poly1.centerOfEdge(i);
         //print(drawnNormals1.get(i));
         line(p.x, p.y, p.x + drawnNormals1.get(i).x, p.y + drawnNormals1.get(i).y);
       }
    }
    
    for(int i = 0; i < drawnNormals2.size(); i++) {
      if(poly2.normals.length > i) {
         PVector p = poly2.centerOfEdge(i);
         line(p.x, p.y, p.x + drawnNormals2.get(i).x, p.y + drawnNormals2.get(i).y);
      }
    }
    
    return null;
    
  }
  
}
class MainMenu extends Scene {
  
  Polygon poly = new Polygon();
  
  public void setup() {
    
    background(50);
    stroke(0);
    strokeWeight(2);
    textSize(40);
    textAlign(CENTER);
    fill(0, 0, 255);
    text("Collision Simulator", 400, 60);
    textSize(13);
    fill(255);
    text("Press backspace to return to main menu", 400, 90);
    
    fill(0, 255, 255, 100);
    ellipse(150, 200, 200, 200);
    textSize(15);
    fill(0);
    text("Radial Collision", 150, 165);
    
    stroke(0);
   
    ellipse(150, 200, 5, 5);
    line(150, 200, 250, 200);
    
    fill(255, 0, 0, 100);
    quad(550, 120, 550, 320, 750, 320, 750, 120);
    fill(0);
    text("AABB Collision", 650, 190);
    
    for(int i = 0; i < 3; i++)
      poly.changeFill();
    
    poly.addPoint(350, 330);
    poly.addPoint(235, 410);
    poly.addPoint(285, 540);
    poly.addPoint(415, 540);
    poly.addPoint(465, 410);
    
    
    
    poly.draw();
    stroke(0, 255, 255);
    strokeWeight(.75f);
    for(int i = 0; i < poly.normals.length; i++) {
      PVector p = poly.centerOfEdge(i);
      line(p.x, p.y, p.x + (poly.normals[i].x * -25), p.y + (poly.normals[i].y * -25));
    }
    fill(0);
    text("Polygonal Collision", 350, 420);
    
  }
  
  public void update() {
    poly.update();
  }
  
  public void mouseReleased() {
    PVector pt = new PVector(mouseX, mouseY);
    if(PVector.sub(new PVector(150, 200), pt).mag() <= 100) {
      println("radial");
      scene = new Radial_Collision();
      scene.setup();
    } else if (pt.x >= 550 && pt.x <= 750 && pt.y >= 120 && pt.y <= 320) {
      scene = new AABB_Prototype();
      scene.setup();
    } else if (poly.isCollidingColor(pt)) {
      scene = new PolygonCollision();
      scene.setup();
    }
  }
  
}
class NoAnimation extends State {
  
  boolean isDoneWithPoly1 = false;
  boolean isDoneWithPoly2 = false;
  int timeStartHeld = 0;
  
  public State update() { 
    fill(0);
    textAlign(CENTER);
    textSize(14);
    text("Create your polygons, press inside the polygon to change the color", 400, 20);
    text("Press and hold to move on to " + (!isDoneWithPoly1 ? "the next Polygon" : "the collision mode"), 400, 40);
    return null; 
  }
  
  public State onMousePressed() {
    timeStartHeld = millis();
    return null;
  }
  
  public State onMouseReleased(Polygon poly1, Polygon poly2) {
    if(Time.toSeconds(millis() - timeStartHeld) < .3f)
    {
     if(!isDoneWithPoly1) {
       //println(poly1.isColliding(new PVector(mouseX, mouseY)));
       if(poly1.isCollidingColor(new PVector(mouseX, mouseY)))
         poly1.changeFill();
       else {
         poly1.addPoint(new PVector(mouseX, mouseY)); print("x"); }
     } else if(!isDoneWithPoly2) {
       if(poly2.isCollidingColor(new PVector(mouseX, mouseY)))
         poly2.changeFill();
       else
         poly2.addPoint(new PVector(mouseX, mouseY));
     }
    }
    else
    {
     if(!isDoneWithPoly1)
       isDoneWithPoly1 = true;
     else {
       isDoneWithPoly2 = true;
       return new DrawState(poly1, poly2);
     }
    }
    return null;
  }
}
class Polygon { 
  
  public static final int normalLength = 20;
  
  public boolean colliding = false; // change to onCollision()
  private boolean doneChecking = false;
  private boolean dirty = true;

  private ArrayList<PVector> points = new ArrayList<PVector>();
  public PVector[] pointsTransformed;
  public PVector[] normals;
  private PVector[] edges;
  
  private PVector position;
  private boolean posIsSet = false;
  
  private int[] colors = {color(255, 255, 255), color(255, 0 ,0), color(0, 0, 255), color(71, 178, 44), color(237, 291, 26), color(255, 141, 25), color(168, 38, 255), color(255, 38, 251), color(25, 235, 255)};
  private float alpha = 75f;
  
  private int colorIndex = 0;
  
  public PVector getPosition() {
    return position;
  }
  
  public void setPosition(PVector p) {
     position = p; 
     dirty = true;
  }

  AABB aabb = new AABB();

  public void update() {
    
    doneChecking = false;
    
    colliding = false; 
    aabb.resetColliding();

    if (dirty) recalc();
  }
 
  public void recalc() {

    dirty = false; // reset the dirty flag

    // build a transformation matrix:
    PMatrix2D matrix = new PMatrix2D();
    if(posIsSet) matrix.translate(position.x, position.y);

    pointsTransformed = new PVector[points.size()];
    for (int i = 0; i < points.size(); i++) {
      PVector p = new PVector();
      matrix.mult(points.get(i), p); // get the transformed point, store it in (p)
      pointsTransformed[i] = p; // store (p) in the pointsTransformed array
    }
    
    //if(pointsTransformed.length != 0) print(pointsTransformed[0]);

    edges = new PVector[points.size()];
    normals = new PVector[points.size()];
    for(int i = 0; i < points.size(); i++) {
        int j = i + 1;
        if(i >= pointsTransformed.length - 1) j = 0;
        
        PVector p1 = pointsTransformed[i];
        PVector p2 = pointsTransformed[j];
        
        edges[i] = new PVector(p2.x - p1.x, p2.y - p1.y);
        normals[i] = new PVector(edges[i].y, -edges[i].x);
        normals[i].normalize();
    }

    // update this object's AABB:
    aabb.recalc(pointsTransformed);
  }

  public void draw() {

    if(points.size() <= 1) return;
    aabb.draw();
    
    stroke(0);
    if(colliding) stroke(150, 150, 150);
    strokeWeight(1.5f);
    
    //alpha = (colorIndex == 0 ? 1 : 0.5f);
    fill(colors[colorIndex], alpha);
    
    //fill(255, 0, 0);
   
    if(position == null) {
      beginShape();
      for (int i = 0; i < points.size(); i++) {
        vertex(points.get(i).x, points.get(i).y);
      }
      vertex(points.get(0).x, points.get(0).y);
      endShape();
    } else { 
      beginShape();
      for (int i = 0; i < points.size(); i++) {
        vertex(pointsTransformed[i].x, pointsTransformed[i].y);
      }
      endShape();
    }
    
    /*if(position == null) {
      for(int i = 0; i < points.size() - 1; i++) {
           line(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);
      }
      line(points.get(points.size()-1).x, points.get(points.size()-1).y, points.get(0).x, points.get(0).y);
    } else {
      for(int i = 0; i < pointsTransformed.length - 1; i++) {
         line(pointsTransformed[i].x, pointsTransformed[i].y, pointsTransformed[i+1].x, pointsTransformed[i+1].y);
      }
      line(pointsTransformed[pointsTransformed.length-1].x, pointsTransformed[pointsTransformed.length-1].y, pointsTransformed[0].x, pointsTransformed[0].y);
    }*/

    
    
  }

  public void addPoint(PVector p) {
    points.add(p);
    recalc();
    aabb.recalc(points.toArray(new PVector[points.size()]));
  }
  
  public void addPoint(float x, float y) {
    addPoint(new PVector(x,y));
  }

  public boolean checkCollision(Polygon poly) {

    if (aabb.checkCollision(poly.aabb)) 
    {  
      for(PVector n : normals) {
        PVector mm1 = projectAlongAxis(n);
        PVector mm2 = poly.projectAlongAxis(n);
        if(mm2.x > mm1.y || mm1.x > mm2.y) return false;
      }
      
      for(PVector n : poly.normals) {
        PVector mm1 = projectAlongAxis(n);
        PVector mm2 = poly.projectAlongAxis(n);
        if(mm2.x > mm1.y || mm1.x > mm2.y) return false;
      }

      return true;
    }
    return false;
  }
  
  public PVector projectAlongAxis(PVector axis) {
    PVector mm = new PVector();
    for(int i = 0; i < pointsTransformed.length; i++) {
      float VALUE = pointsTransformed[i].dot(axis);
      if(i == 0 || VALUE < mm.x) mm.x = VALUE;
      if(i == 0 || VALUE > mm.y) mm.y = VALUE;
    }
    
    return mm;
  }
  
  public void onDrawState() {
    posIsSet = true;
    setPosition(points.get(0));
    points.set(0, new PVector(0,0));
    for(int i = 1; i < points.size(); i++) {
      points.set(i, points.get(i).sub(position));
      //points.get(i).sub(position);
    }
    points.add(new PVector(0,0));
  }
  
  public PVector centerOfEdge(int index) {
    int j = (index == pointsTransformed.length - 1 ? 0 : index + 1);
    PVector p = new PVector(pointsTransformed[index].x , pointsTransformed[index].y);
    return p.add(pointsTransformed[j]).div(2);
  }
  
  public boolean isColliding(PVector point) {
    
    if (point.x > aabb.xmax || point.x < aabb.xmin || point.y > aabb.ymax || point.y < aabb.ymin) return false;
    // in bounding box, now check in polygon:
    for (int i = 0; i < pointsTransformed.length; i++) {
      PVector t = pointsTransformed[i];
      PVector p = point.get();
      p.sub(t);
      float result = normals[i].dot(p);
      //print(result + " ");
      if (result < 0) return false;
    }
    return true;
  }
  
  public boolean isCollidingColor(PVector point) {
    
    if (point.x > aabb.xmax || point.x < aabb.xmin || point.y > aabb.ymax || point.y < aabb.ymin) return false;
    // in bounding box, now check in polygon:
    for (int i = 0; i < points.size(); i++) {
      PVector t = points.get(i);
      PVector p = point.get();
      p.sub(t);
      float result = normals[i].dot(p);
      //print(result + " ");
      if (result < 0) return false;
    }
    return true;
  }
  
  public void changeFill() {
    colorIndex = (colorIndex + 1) % colors.length;
  }
}
class PolygonCollision extends Scene {
  public State state = new NoAnimation();
  Polygon polygon1 = new Polygon();
  Polygon polygon2 = new Polygon();
  
  boolean isDoneWithPoly1 = false;
  boolean isDoneWithPoly2 = false;
  boolean isHeld = false;
  
  float timeStartHeld = 0;
  
  public void setup() {
  }
  
  public void draw() {
    background(60);
    noFill();
    
    stroke(0);
    
    line(0, 300, 800, 300); // x axis
    line(400, 0, 400, 800); // y axis
    
    update();
    
    polygon1.draw();
    polygon2.draw();
  }
  
  public void update() {
    Time.update(millis());
    
    polygon1.update();
    polygon2.update();
    
    State newState = state.update();
    if(newState != null)
      state = newState;
    
  }
  
  public void keyPressed() {
    if(keyCode == BACKSPACE) {
      drawAABB = false;
      scene = new MainMenu();
      scene.setup();
    }
  }
  
  public void mousePressed() {
    state.onMousePressed();
    timeStartHeld = millis();
  }
  public void mouseReleased() {
    State newState = state.onMouseReleased(polygon1, polygon2);
    if(newState != null) state = newState;
  }
}
class Radial_Collision extends Scene {
  PVector circle1 = new PVector();
  PVector circle2 = new PVector();
  ArrayList<PVector> circleArray = new ArrayList();
  boolean mathMode;
  float radius;
  float distance;
  
  public void setup() {
    radius = 150;
    circle2.x = random(-width/2 + 50, width/2 - 50);
    circle2.y = random(-height/2 + 190, width/2 - 70);
    
    circleArray.add(circle1);
    circleArray.add(circle2);
  
    mathMode = false;
  }
  
  public void draw() {
    background(120);
    translate(width/2, height/2);
    textAlign(LEFT);
    fill(0);
    textSize(20);
    if (!mathMode) {
      text("Press ", -380, -280);
      fill(255);
      text("[Space] ", -323, -280);
      fill(0);
      text("to turn on Math Mode!", -247, -280);
      text("Press the ", -380, -250);
      fill(255);
      text("[Left] ", -285, -250);
      fill(0);
      text("or ", -230, -250);
      fill(255);
      text("[Right] ", -203, -250);
      fill(0);
      text("arrow keys to add or remove circles!", -130,-250);
      text("Press the ", -380, -220);
      fill(255);
      text("[Down] ", -285, -220);
      fill(0);
      text("arrow key to randomize the radius!", -210, -220);
    } else {
          text("Press ", -380, -280);
      fill(255);
      text("[Space] ", -323, -280);
      fill(0);
      text("to turn off Math Mode!", -247, -280);
    }
    circle1.x = mouseX - width/2;
    circle1.y = mouseY - height/2;
  
    for (int i = 0; i < circleArray.size(); i++) {
      fill(230, 0, 0, 100);
      noStroke();
      ellipse(circleArray.get(i).x, circleArray.get(i).y, radius, radius);
      stroke(0);
  
      //   for (int k = 0; k < circleArray.size(); k++) {
      if (circleArray.get(0) != circleArray.get(i)) {
        if (Distance(circleArray.get(0), circleArray.get(i)) < radius) {
          fill(0);
          if (!mathMode) {
            textSize(40);
            text("Collision", -100, -160);
            textSize(20);
          }
        }
      }
      //  }
    }
  
    if (mathMode) {
  
  
      if (circleArray.size() > 2) {
        for (int j = circleArray.size(); j > 2; j--) {
          circleArray.remove(j - 1);
        }
      }
  
      if (circleArray.get(1).x != 200) {
        circleArray.get(1).x = 200;
      }
      if (circleArray.get(1).y != 200) {
        circleArray.get(1).y = 200;
      }
  
      //Distance Equation
      fill(0);
      strokeWeight(2);
      text("Distance = ", -380, -240);
      line(-255, -250, -246, -240);
      line(-246, -240, -244, -260);
      line(-244, -260, -40, -260);
      strokeWeight(1);
      text("(x² - x¹)² + (y² - y¹)²", -240, -240);
  
  
      //First Distance equation
      fill(0);
      strokeWeight(2);
      text("Distance = ", -380, -190);
      line(-255, -200, -246, -190);
      line(-246, -190, -244, -210);
      line(-244, -210, 70, -210);
      strokeWeight(1);
      text("(" + PApplet.parseInt(circleArray.get(1).x) + " - " + PApplet.parseInt(circleArray.get(0).x) + ") ² + (" + PApplet.parseInt(circleArray.get(1).y) + " - " + PApplet.parseInt(circleArray.get(0).y) + ") ²", -240, -190);
  
      //Second Distance equation
      strokeWeight(2);
      text("Distance = ", -380, -140);
      line(-255, -150, -246, -140);
      line(-246, -140, -244, -160);
      line(-244, -160, -35, -160);
      strokeWeight(1);
      text("(" + PApplet.parseInt(pow(circleArray.get(1).x - circleArray.get(0).x, 2)) + ") + (" + PApplet.parseInt(pow(circleArray.get(1).y - circleArray.get(0).y, 2)) + ")", -240, -140);
  
      //Third Distance equation
      strokeWeight(2);
      text("Distance = ", -380, -90);
      line(-255, -100, -246, -90);
      line(-246, -90, -244, -110);
      line(-244, -110, -160, -110);
      strokeWeight(1);
      text( PApplet.parseInt(pow(circleArray.get(1).x - circleArray.get(0).x, 2)) + PApplet.parseInt(pow(circleArray.get(1).y - circleArray.get(0).y, 2)), -240, -90);
  
      //Final Distance equation
      text("Distance =   " + PApplet.parseInt(Distance(circleArray.get(0), circleArray.get(1))), -380, -40);
      
      text("Distance - Radii = ", -380, 10);
      fill(255);
      text(PApplet.parseInt(PApplet.parseInt((Distance(circleArray.get(0), circleArray.get(1)))) - radius), -190, 10);
      fill(0);
      text("Circle 1 Radius = ", 150, -260);
      text("Circle 2 Radius = ", 150, -210);
      fill(0,255,0);
      text(PApplet.parseInt(radius/2), 330, -260);
      text(PApplet.parseInt(radius/2), 330, -210);
      fill(0);
  
      text("If the Distance is less than both of the circles radius, the circles are colliding!", -380, 60);
      text("Sum of radii = " + PApplet.parseInt(radius), 150, -160);
  
      textSize(30);
      if (Distance(circleArray.get(0), circleArray.get(1)) < radius) {
        fill(200, 0, 0);
        text("Collision!", 50, 0);
      } else {
        fill(0, 255, 0);
        text("No Collision!", 50, 0);
      }
      textSize(20);
  
  
  
      for (int i = circleArray.size(); i > 0; i--) {
  
  
        if (i > 1) {
          fill(0);
          //distance line between circles
          line(circleArray.get(0).x, circleArray.get(0).y, circleArray.get(i-1).x, circleArray.get(i-1).y);
          textAlign(UP);
          fill(255);
          text(PApplet.parseInt(PApplet.parseInt((Distance(circleArray.get(0), circleArray.get(1)))) - radius), (circleArray.get(0).x + circleArray.get(1).x) / 2, (circleArray.get(0).y + circleArray.get(1).y) / 2); ////////////////////////
          stroke(0, 255, 0);
          fill(0, 255, 0);
          
          float dx1 = circleArray.get(0).x - circleArray.get(i-1).x;
          float dy1 = circleArray.get(0).y - circleArray.get(i-1).y;
          float ang1 = atan2(dy1, dx1);
          
          
          float xDir = circleArray.get(0).x - cos(ang1) * radius/2;
          float yDir = circleArray.get(0).y - sin(ang1) * radius/2;
          float xDir2 = circleArray.get(0).x - cos(ang1 - .4f) * radius/4;
          float yDir2 = circleArray.get(0).y - sin(ang1 - .4f) * radius/4;
          float xDir3 = circleArray.get(1).x - cos(ang1 + 3.3f) * radius/4;
          float yDir3 = circleArray.get(1).y - sin(ang1 + 3.3f) * radius/4;
          
          
          //circle 1 radius
          
          strokeWeight(1.5f);
          line(circleArray.get(0).x, circleArray.get(0).y, xDir, yDir);
          
          strokeWeight(1);
          textAlign(CENTER, CENTER);
          text(PApplet.parseInt(radius/2),  xDir2,  yDir2);//////////////////////////////////////////////////////////////////////
          //circle 2 radius
          
          line(circleArray.get(1).x, circleArray.get(1).y, circleArray.get(1).x + cos(ang1) * radius/2, circleArray.get(1).y + sin(ang1) * radius/2);
          textAlign(LEFT);
          text(PApplet.parseInt(radius/2), xDir3, yDir3);
          stroke(0);
          fill(0);
        }
      }
    }
  }
  
  //calculate distance between 2 points
  public float Distance(PVector circ1, PVector circ2) {
    float distance = sqrt(pow(circ2.x - circ1.x, 2) + pow(circ2.y - circ1.y, 2));
  
    return distance;
  }
  
  public void keyPressed() {
  
    if (key == 32) {
      mathMode = !mathMode;
    }
  
    if (keyCode==RIGHT) {
      if (!mathMode) {
        PVector circle = new PVector();
        circle.x = random(-width/2 + 50, width/2 - 50);
        circle.y = random(-height/2 + 190, height/2 - 80);
  
  
        circleArray.add(circle);
      }
    }
  
  
    if (keyCode== LEFT) {
      if (!mathMode) {
        if (circleArray.size() > 2) {
          circleArray.remove(circleArray.size() - 1);
        }
      }
    }
    
    if(keyCode == BACKSPACE) {
      translate(-width/2, -height/2);
      scene = new MainMenu();
      scene.setup();
    }
    
    if(keyCode==DOWN){
      
      radius = random(50,200);
    }
  }
}
static class Scene {
  
  public void setup() {}
  
  public void draw() {}
  
  public void keyPressed() {}
  
  public void mousePressed() {}
  
  public void mouseReleased() {}
}
class State {
  public State update() { return null; }
  public State onMousePressed() { return null; }
  public State onMouseReleased(Polygon poly1, Polygon poly2) { return null; }
}
static class Time {
 
  public static float deltaTime = 0;
  public static float timeScale = 1;
  private static float prevTime = 0;
  
  public static void update(float milis) {
    float currentTime = milis;
    deltaTime = (currentTime - prevTime) / 1000;
    prevTime = currentTime;
  }
  
  public static float toSeconds(float milis) {
    return milis / 1000 * timeScale;
  }
  
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CollisionDetectionSim" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
