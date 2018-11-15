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
      text("(" + int(circleArray.get(1).x) + " - " + int(circleArray.get(0).x) + ") ² + (" + int(circleArray.get(1).y) + " - " + int(circleArray.get(0).y) + ") ²", -240, -190);
  
      //Second Distance equation
      strokeWeight(2);
      text("Distance = ", -380, -140);
      line(-255, -150, -246, -140);
      line(-246, -140, -244, -160);
      line(-244, -160, -35, -160);
      strokeWeight(1);
      text("(" + int(pow(circleArray.get(1).x - circleArray.get(0).x, 2)) + ") + (" + int(pow(circleArray.get(1).y - circleArray.get(0).y, 2)) + ")", -240, -140);
  
      //Third Distance equation
      strokeWeight(2);
      text("Distance = ", -380, -90);
      line(-255, -100, -246, -90);
      line(-246, -90, -244, -110);
      line(-244, -110, -160, -110);
      strokeWeight(1);
      text( int(pow(circleArray.get(1).x - circleArray.get(0).x, 2)) + int(pow(circleArray.get(1).y - circleArray.get(0).y, 2)), -240, -90);
  
      //Final Distance equation
      text("Distance =   " + int(Distance(circleArray.get(0), circleArray.get(1))), -380, -40);
      
      text("Distance - Radii = ", -380, 10);
      fill(255);
      text(int(int((Distance(circleArray.get(0), circleArray.get(1)))) - radius), -190, 10);
      fill(0);
      text("Circle 1 Radius = ", 150, -260);
      text("Circle 2 Radius = ", 150, -210);
      fill(0,255,0);
      text(int(radius/2), 330, -260);
      text(int(radius/2), 330, -210);
      fill(0);
  
      text("If the Distance is less than both of the circles radius, the circles are colliding!", -380, 60);
      text("Sum of radii = " + int(radius), 150, -160);
  
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
          text(int(int((Distance(circleArray.get(0), circleArray.get(1)))) - radius), (circleArray.get(0).x + circleArray.get(1).x) / 2, (circleArray.get(0).y + circleArray.get(1).y) / 2); ////////////////////////
          stroke(0, 255, 0);
          fill(0, 255, 0);
          
          float dx1 = circleArray.get(0).x - circleArray.get(i-1).x;
          float dy1 = circleArray.get(0).y - circleArray.get(i-1).y;
          float ang1 = atan2(dy1, dx1);
          
          
          float xDir = circleArray.get(0).x - cos(ang1) * radius/2;
          float yDir = circleArray.get(0).y - sin(ang1) * radius/2;
          float xDir2 = circleArray.get(0).x - cos(ang1 - .4) * radius/4;
          float yDir2 = circleArray.get(0).y - sin(ang1 - .4) * radius/4;
          float xDir3 = circleArray.get(1).x - cos(ang1 + 3.3) * radius/4;
          float yDir3 = circleArray.get(1).y - sin(ang1 + 3.3) * radius/4;
          
          
          //circle 1 radius
          
          strokeWeight(1.5);
          line(circleArray.get(0).x, circleArray.get(0).y, xDir, yDir);
          
          strokeWeight(1);
          textAlign(CENTER, CENTER);
          text(int(radius/2),  xDir2,  yDir2);//////////////////////////////////////////////////////////////////////
          //circle 2 radius
          
          line(circleArray.get(1).x, circleArray.get(1).y, circleArray.get(1).x + cos(ang1) * radius/2, circleArray.get(1).y + sin(ang1) * radius/2);
          textAlign(LEFT);
          text(int(radius/2), xDir3, yDir3);
          stroke(0);
          fill(0);
        }
      }
    }
  }
  
  //calculate distance between 2 points
  float Distance(PVector circ1, PVector circ2) {
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
