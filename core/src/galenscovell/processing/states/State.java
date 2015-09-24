package galenscovell.processing.states;

public interface State {
    public void enter();
    public void exit();
    public void update(float delta);
    public void handleInput(float x, float y);
    public void handleInterfaceEvent(String event);
}
