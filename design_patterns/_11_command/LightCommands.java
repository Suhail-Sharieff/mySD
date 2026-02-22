package design_patterns._11_command;

public abstract class LightCommands implements Command{
    protected Light light;
    public LightCommands(Light light){
        this.light=light;
    }
}