package de.lessvoid.nifty.elements.events;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;

public class NiftyMouseEvent implements NiftyEvent {
  private final Element element;
  private final int mouseX;
  private final int mouseY;
  private final int mouseWheel;
  private final boolean button0Down;
  private final boolean button1Down;
  private final boolean button2Down;
  private final boolean button0InitialDown;
  private final boolean button1InitialDown;
  private final boolean button2InitialDown;
  private final boolean button0Release;
  private final boolean button1Release;
  private final boolean button2Release;

  public NiftyMouseEvent(final Element element, @Nonnull final NiftyMouseInputEvent source) {
    this.element = element;
    this.mouseX = source.getMouseX();
    this.mouseY = source.getMouseY();
    this.mouseWheel = source.getMouseWheel();
    this.button0Down = source.isButton0Down();
    this.button1Down = source.isButton1Down();
    this.button2Down = source.isButton2Down();
    this.button0InitialDown = source.isButton0InitialDown();
    this.button1InitialDown = source.isButton1InitialDown();
    this.button2InitialDown = source.isButton2InitialDown();
    this.button0Release = source.isButton0Release();
    this.button1Release = source.isButton1Release();
    this.button2Release = source.isButton2Release();
  }

  public Element getElement() {
    return element;
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

  public int getMouseWheel() {
    return mouseWheel;
  }

  public boolean isButton0Down() {
    return button0Down;
  }

  public boolean isButton1Down() {
    return button1Down;
  }

  public boolean isButton2Down() {
    return button2Down;
  }

  public boolean isAnyButtonDown() {
    return button0Down || button1Down || button2Down;
  }

  public boolean isButton0InitialDown() {
    return button0InitialDown;
  }

  public boolean isButton1InitialDown() {
    return button1InitialDown;
  }

  public boolean isButton2InitialDown() {
    return button2InitialDown;
  }

  public boolean isButton0Release() {
    return button0Release;
  }

  public boolean isButton1Release() {
    return button1Release;
  }

  public boolean isButton2Release() {
    return button2Release;
  }

  @Override
  @Nonnull
  public String toString() {
    return
        "mouseX = " + mouseX + ", " +
            "mouseY = " + mouseY + ", " +
            "button0Down = " + button0Down + ", " +
            "button1Down = " + button1Down + ", " +
            "button2Down = " + button2Down + ", " +
            "button0InitialDown = " + button0InitialDown + ", " +
            "button1InitialDown = " + button1InitialDown + ", " +
            "button2InitialDown = " + button2InitialDown + ", " +
            "button0Release = " + button0Release + ", " +
            "button1Release = " + button1Release + ", " +
            "button2Release = " + button2Release;
  }
}
