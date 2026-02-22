package design_patterns._11_command;

import java.util.Stack;

public class Client {

    public Client() {
        history=new Stack<>();
    }
    private Stack<Command> history;

    public void executeCommand(Command command) {
        history.push(command);
        command.execute();
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command last = history.pop();
            last.undo();
        } else {
            System.out.println("No commands to undo");
        }
    }

    
    
    
}

