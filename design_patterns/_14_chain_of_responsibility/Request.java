package design_patterns._14_chain_of_responsibility;

import java.util.Arrays;
import java.util.HashSet;

public 
class Request{
    private HashSet<String>attributes;
    public Request(String ...attrs){
        attributes=new HashSet<>(Arrays.asList(attrs));
    }
    public boolean has(String attribute){
        return attributes.contains(attribute);
    }
}
