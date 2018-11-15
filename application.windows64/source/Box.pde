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
  
  void update() {
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
  
  void draw(String letter) {
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
  
  void clicked() {
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
  
  boolean checkCollision(Box b){
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
  void drawAABBLines(Box b) {
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
      y = b.y - (boxSize * 0.99);
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

void updatePhysics() {
  float accY = 0.75;
  
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
