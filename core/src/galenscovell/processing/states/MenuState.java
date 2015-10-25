package galenscovell.processing.states;

public class MenuState implements State {

    public MenuState() {

    }

    public void enter() {
        System.out.println("\tEntering MENU state.");
    }

    public void exit() {
        System.out.println("\tLeaving MENU state.");
    }

    public void update(float delta) {

    }

    public void handleInput(float x, float y) {

    }

    public void handleInterfaceEvent(String event) {

    }
}
