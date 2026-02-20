package design_patterns._06_adapter;


public class Client {
    StringDataProcessor processor;

    public Client(StringDataProcessor processor) {
        this.processor = processor;
    }

    String process(int nums[]) {
        return processor.process(nums);
    }
}