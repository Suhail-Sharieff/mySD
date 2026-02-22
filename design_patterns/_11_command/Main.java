package design_patterns._11_command;


/*
Say im building smart home, i want to design remote from which i can turn on ,turn off light, set thermostat temperator and also undo operations are supported

Brute force is to definea single contrller and add all those methods and logic into it, this makes the class extremely big and complex to understand and extend and also violates signle reponsibility principle, so v make use of Command pattern to handle that efficiently

*/

public class Main {
    public static void main(String[] args) {
        
        //say i have a light and a thermostat at gome
        Light myLight=new Light();
        Thermostat myThermostat=new Thermostat();

        //create cmd btns for light
        LightCommands lightOnBtn=new LightOnCommand(myLight);
        LightCommands lightOffBtn=new LightOffCommand(myLight);

        //create cmd btns for thermostat
        ThermostatCommands setTempTo23=new SetTemperatureCommand(myThermostat, 23);


        //user actions
        Client user=new Client();
        user.executeCommand(lightOnBtn);

        user.executeCommand(lightOffBtn);

        user.undo();

        user.executeCommand(setTempTo23);

    }
}



