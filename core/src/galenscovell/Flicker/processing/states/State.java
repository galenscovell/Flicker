package galenscovell.flicker.processing.states;

public interface State {
    void enter();
    void exit();
    void update(float delta);
    void handleInput(float x, float y);
    void handleInterfaceEvent(int moveType);
    StateType getStateType();
}