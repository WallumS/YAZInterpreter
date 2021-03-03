//This class only exists because im lazy and i also want to pass a scanner output around
import java.util.Scanner;

public class InputBuffer {
    public String[] input;
    public Scanner scanner = new Scanner(System.in);
    public String nextInp(){
        input = scanner.nextLine().split(" ");
        return input[0];
    }
    public String nextInpFull(){
        String h = scanner.nextLine();
        input = h.split(" ");
        return h;
    }
    public void close(){
        scanner.close();
    }
}   
