package design_patterns._11_command;

public
class SetTemperatureCommand extends ThermostatCommands{
    private int prevTemperature=-1;
    private int newTemperature;
    public SetTemperatureCommand(Thermostat thermostat,int newTemperature){
        super(thermostat);
        this.newTemperature=newTemperature;
    }
    @Override
    public void execute() {
        prevTemperature=thermostat.getTemperature();
        thermostat.setTemperature(newTemperature);
        System.out.println("Prev temp = "+prevTemperature+" New temp="+newTemperature);
    }
    @Override
    public void undo() {
        thermostat.setTemperature(prevTemperature);
    }
}