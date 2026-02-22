package design_patterns._11_command;

public 
class LightOnCommand extends LightCommands{
    public LightOnCommand(Light light){
        super(light);
    }
    @Override
    public void execute() {
        light.on();
    }
    @Override
    public void undo() {
        light.off();
    }
}
