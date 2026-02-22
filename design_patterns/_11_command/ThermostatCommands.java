package design_patterns._11_command;

public 
abstract class ThermostatCommands implements Command{
    protected Thermostat thermostat;
    public ThermostatCommands(Thermostat thermostat){
        this.thermostat=thermostat;
    }
}
