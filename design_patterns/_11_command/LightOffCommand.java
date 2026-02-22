package design_patterns._11_command;

public 
class LightOffCommand extends LightCommands{
    public LightOffCommand(Light light) {
        super(light);
    }
    @Override
    public void execute() {
        light.off();
    }
    @Override
    public void undo() {
       light.on();
    }
}
