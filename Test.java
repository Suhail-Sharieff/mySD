import java.util.List;

public class Test{
    public static void main(String[] args) {
        int a=3,b=4,c=5;
        double cosA=(b*b+c*c-a*a)/(2d*b*c);
        double angA=Math.acos(cosA);
        double cosB=(a*a+c*c-b*b)/(2d*a*c);
        double angB=Math.acos(cosB);

        double angC=180d-(angA+angB);

        System.out.println(angA+" "+angB+" "+angC);

    }
}