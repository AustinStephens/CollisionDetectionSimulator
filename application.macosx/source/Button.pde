class Button {
  float x;
  float y;
  
  boolean prevMousePressed = false;
  
  Button() {
    
  }
  
  void update() {
    if(mousePressed && mouseOverlap() && !prevMousePressed) {
      prevMousePressed = true;
      buttonClick();
    }
    prevMousePressed = mousePressed;
  }
  
  boolean mouseOverlap() {
    if(mouseX > x && mouseX < (x + buttonSizeW) && mouseY > y && mouseY < (y + buttonSizeH)) {
      return true;
    } else {
      return false;
    }
  }
  
  void draw() {
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
  void buttonClick() {
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
