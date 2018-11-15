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
    box1.x = (width * 0.25) - halfBoxSize;
    box1.y = (height * 0.55) - halfBoxSize;
    boxes.add(box1);
    
    println(box1.x);
    println(box1.y);
  
    Box box2 = new Box();
    box2.x = (width * 0.25) - halfBoxSize;
    box2.y = (height * 0.85) - halfBoxSize;
    boxes.add(box2);
  
    Box box3 = new Box();
    box3.x = (width * 0.75) - halfBoxSize;
    box3.y = (height * 0.55) - halfBoxSize;
    boxes.add(box3);
  
    Box box4 = new Box();
    box4.x = (width * 0.75) - halfBoxSize;
    box4.y = (height * 0.85) - halfBoxSize;
    boxes.add(box4);
    
    Button gravity = new Button();
    gravity.x = (width * 0.9) - 50;
    gravity.y = (height * 0.1);
    buttons.add(gravity);
    
    Button forward = new Button();
    forward.x = (width * 0.15) - 50;
    forward.y = (height * 0.315);
    buttons.add(forward);
    
    Button back = new Button();
    back.x = (width * 0.3) - 50;
    back.y = (height * 0.315);
    buttons.add(back);
    
    f = createFont("Arial Bold",11.5,true);
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
