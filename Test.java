import java.util.List;

public class Test{
    public static void main(String[] args) {
        List<String>li=List.of("1","2","3");
        String ans=String.join(",", li);
        System.out.println(List.of(ans.split(" "))  );

    }
}