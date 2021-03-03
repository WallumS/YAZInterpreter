/*
Willem Swierstra

Yazwhatever interpreter

my shoddy attempt to make a interpreter for the interpreted programming language "YazLang"
I added to the language a whole lot, check the README.txt

*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//the console program
public class main {

    static final String evironmentInit = "YAZ 2.0.1 language; interpreter version 2.2\nRead README.TXT for more info";
    static final String yazInit = "Willem Swierstra\nYAZ 2.0.0 language\nRead README.TXT for more info\n"
    + "\n"
    + "run - runs a program that outputs to a file\n" 
    + "ex: \"run test.yzy text.txt\", \"run factorial.yzy here\"\n"
    + "\n"
    + "yaz - opens up the yaz environment\n"
    + "\n"
    + "read - reads a file\n"
    + "ex: \"read test.txt\"\n"
    + "\n"
    + "exit - exits program\n";
    public static void main(String[] args) {

        System.out.println(yazInit);

        InputBuffer in = new InputBuffer();

        // this is the lazy way of doing it, it buffers the last input so i can write
        // one while loop code
        while (!in.nextInp().contains("exit")) {
            // switches on the "command", or the first input given
            switch (in.input[0].toLowerCase()) {
                case "run": // runs a file to output to another file
                    if (in.input.length != 3) {
                        System.out.println("ERROR! incorrect number of arguments!");
                    }
                    System.out.println("Interpreting " + in.input[1] + "...");
                    interpreter.Interpret(in.input[1], in.input[2]);
                    System.out.println("done! check " + in.input[2] + " for the executed program");
                    break;
                case "yaz": // opens the yaz command environment
                    System.out.println(evironmentInit);
                    interpreter.OpenEnvironment(in);
                    break;
                case "read":
                    ReadFileToConsole(in.input[1]);
                    break;
                default:
                    System.out.println("Not a command");
            }
        }
        in.close();
    }
    
    //prints file specified by "filepath" to the default out printstream
    static void ReadFileToConsole(String filepath){
        try {
            File f = new File(filepath);
            Scanner stream = new Scanner(f);
            while (stream.hasNextLine()) 
                System.out.println(stream.nextLine());
            stream.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR! file \"" + filepath + "\" not found!");
        }
    }
}