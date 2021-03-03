import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

//making this an enum so i don't have to memorize the codes and its readable
enum errorValues {
    sucess, incorrectNumberOfArguments, notFOrC, notACommand, CouldNotParse
}

//this bit is very, very janky, but essentially i wanted it to return a string and also return the value from the operation
class returnValues {
    errorValues ev;
    String returnString;
    public returnValues(errorValues e, String r){
        ev = e;
        returnString = r;
    }
}

// I made this a seperate class because it is seperate functionality than the
// command environment
public class interpreter {

    // the main interpreter class
    static void Interpret(String filepath, String opfp) {
        String[] commands = parseYAGFile(filepath);
        if (commands == null) return;
        File out;
        PrintStream p; //by specifying a printstream here, it is easy to have stuff output to either the console or the 
        if (opfp.contains("here")) {
            p = System.out;
            out = null;
        } else {
            out = new File(opfp);
            try {
                out = new File(opfp);
                out.createNewFile();
                p = new PrintStream(new FileOutputStream(out, false));
            } catch (IOException e) {
                return;
            }
        }
        HashMap<String, String> vars = new HashMap<String, String>();
        for (int i = 0; i < commands.length; i++){
            interpLine(commands[i], i + 1, "file: " + filepath, p, vars);            
        }
    }

    //parses a file
    static String[] parseYAGFile(String filepath) {
        List<String> cmdlist = new ArrayList<String>();
        try {
            File f = new File(filepath);
            Scanner stream = new Scanner(f);
            while (stream.hasNextLine()) 
                cmdlist.add(stream.nextLine());
            stream.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR! file \"" + filepath + "\" not found!");
            return null;
        }
        return cmdlist.toArray(new String[0]);
    }

    // Takes in a string to parse, parses it, and then sets that output stream to p. uses int line and String environment for error printing
    static String interpLine(String l, int line, String environment, PrintStream p, HashMap<String, String> vars){
        String command = l.split(" ")[0]; 
        //this is such a stupid way of doing it lmao but i didn't want excess variables and there isn't a delete operator in java
        String[] args = parseArgs(l, vars);
        returnValues thisCommand;
            switch(command){
                case "CONVERT":
                    thisCommand = CONVERT(args, p);
                    break;
                case "REPEAT":
                    thisCommand = REPEAT(args, p);
                    break;
                case "RANGE":
                    thisCommand = RANGE(args, p);
                    break;
                case "ADD":
                    thisCommand = ASMD(args, p, 1);
                    break;
                case "SUB":
                    thisCommand = ASMD(args, p, 2);
                    break;
                case "MUL":
                    thisCommand = ASMD(args, p, 3);
                    break;
                case "DIV":
                    thisCommand = ASMD(args, p, 4);
                    break;
                case "CONCAT":
                    thisCommand = CONCAT(args, p);
                    break;
                case "SET":
                    thisCommand = SET(args, p, vars);
                    break;
                case "PRINT":
                    thisCommand = PRINT(args, p, vars);
                    break;
                case "LOOP":
                    thisCommand = LOOP(args, p, vars);
                    break;
                default:
                    thisCommand = new returnValues(errorValues.notACommand, "");
            }
            switch(thisCommand.ev){
                case sucess:
                    return thisCommand.returnString;
                case incorrectNumberOfArguments:
                    System.out.println("ERROR! Incorrect number of arguments at line " + line + " in \"" + environment + "\"");
                    break;
                case notFOrC:
                    System.out.println("ERROR! Temperature conversion at line " + line + " in \"" + environment + "\" does not include a unit F or unit C");
                    break;
                case notACommand:
                    System.out.println("ERROR! Not a valid command at line " + line + " in \"" + environment + "\"");
                    break;
                case CouldNotParse:
                    System.out.println("ERROR! Could not parse arguments at line " + line + " in \"" + environment + "\"");
                    break;
            }
        return "";
    }

    // this runs a python like command interface in the console
    public static void OpenEnvironment(InputBuffer s){
        String h = "";
        int i = 1;
        HashMap<String, String> vars = new HashMap<String, String>();
        while(true){
            h = s.nextInpFull();
            if (h.contains("EXIT")) break;
            interpLine(h, i, "command line environment", System.out, vars);
            i++;
        }
    }

    //this one could be useful outside of here
    public static String[] parseArgs(String input, HashMap<String, String> vars){
        //this is quite possibly the dumbest way to do it, but essentially its a regex string that i managed to figure out eventually
        String[] tempSplit = input.replaceAll("\\s*\\([^\\)]*\\)\\s*", "").split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < tempSplit.length; i++) {
            if (tempSplit[i].startsWith("'")){
                tempSplit[i] = vars.get(tempSplit[i].replace("'", ""));
            }   
        }
        return Arrays.copyOfRange(tempSplit, 1, tempSplit.length); //this removes the command from the string array by copying it from the second index to the full length
    }

    //does the convert operation
    static returnValues CONVERT(String[] args, PrintStream p){
        String x = "";
        if(args.length != 2) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        int temp = Integer.parseInt(args[0]);

        //converts the tempurture on the second argument, if its not a capital f or capital c, it returns an exception.
        switch (args[1]){
            case "F":
                temp = (int)(((float)temp - 32.0f) * (5.0f/9.0f));
                x = temp + "c";
                break;
            case "C":
                temp = (int)(((float)temp * (9.0f/5.0f))  + 32.0f);
                x = temp + "f";
                break;
            default:
                return new returnValues(errorValues.notFOrC, "");
        }
        
        //prints whatever the tempurature is
        if(p != null) p.println(x);
        return new returnValues(errorValues.sucess, Integer.toString(temp));
    }

    //does the repeat operation
    static returnValues REPEAT(String[] args, PrintStream p) {
        //checks to make sure it is, in fact, even, before starting to parse it
        if(args.length % 2 != 0) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        
        int nofrep = args.length / 2;
        
        String[] strings = new String[nofrep];
        int[] reps = new int[nofrep];

        //setup
        for(int i = 0; i < nofrep; i++){
            strings[i] = args[(i * 2)].replace("\"", "");
            reps[i] = Integer.parseInt(args[(i * 2) + 1]);
        }

        String rstr = "";
        //printing
        for(int i = 0; i < nofrep; i++){
            for (int j = 0; j < reps[i]; j++){
                rstr += strings[i];
            }
        }

        if(p != null) p.println(rstr);
        return new returnValues(errorValues.sucess, rstr);
    }
    
    //does the "range" Operation
    static returnValues RANGE(String[] args, PrintStream p) {
        if(args.length != 3) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        int i, end, it;
        try {
            i = Integer.parseInt(args[0]);
            end = Integer.parseInt(args[1]);
            it = Integer.parseInt(args[2]);
        } catch(Exception e){
            return new returnValues(errorValues.CouldNotParse, "");
        }

        while(true){
            if(p != null) p.print(i + " ");
            i += it;
            if (i >= end) break;
        }

        if(p != null) p.print("\n");
        return new returnValues(errorValues.sucess, Integer.toString(i));
    }

    //adds, subtracts, multiplies, divides based on the type
    static returnValues ASMD(String[] args, PrintStream p, int type){
        int a, b, result = 0;
        try {
            a = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[1]);
        } catch(Exception e){
            return new returnValues(errorValues.CouldNotParse, "");
        }
        switch(type){
            case 1:
                result = a+b;
                break;
            case 2:
                result = a-b;
                break;
            case 3:
                result = a*b;
                break;
            case 4:
                result = a/b;
                break;
        }
        if(p != null) p.println(result);
        return new returnValues(errorValues.sucess, Integer.toString(result));
    }

    //concats two strings
    static returnValues CONCAT(String[] args, PrintStream p){
        if(args.length < 2) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        String output = "";
        for(int i = 0; i < args.length; i++){
            output += args[i].replace("\"", "");
        }
        if(p != null) p.println(output);
        return new returnValues(errorValues.sucess, output);
    }

    //sets a variable
    static returnValues SET(String[] args, PrintStream p, HashMap<String, String> vars){
        if(args.length != 2) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        String output = args[1];
        if(args[1].startsWith("\"")){
            output = interpLine(args[1].replace("\"", ""), 0, "internal", null, vars);
        }
        vars.put(args[0], output);
        return new returnValues(errorValues.sucess, "");
    }

    //prints a value
    static returnValues PRINT(String[] args, PrintStream p, HashMap<String, String> vars){
        if(args.length != 1) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        String output = args[0];
        if(args[0].startsWith("\"")){
            output = interpLine(args[0].replace("\"", ""), 0, "internal", null, vars);
        }
        p.println(output);
        return new returnValues(errorValues.sucess, output);
    }

    //loops over an amount
    static returnValues LOOP(String[] args, PrintStream p, HashMap<String, String> vars){
        if(args.length < 2) return new returnValues(errorValues.incorrectNumberOfArguments, "");
        String[] cmds = Arrays.copyOfRange(args, 1, args.length);
        int it;
        try {
            it = Integer.parseInt(args[0]);
        } catch(Exception e){
            return new returnValues(errorValues.CouldNotParse, "");
        }
        for (int i = 0; i < it; i++){
            for (int ln = 0; ln < cmds.length; ln++){
                String cmd = cmds[ln].replace("_IT", Integer.toString(i)).replace("\"", "").replace("/", "\"");
                interpLine(cmd, 0, "internal - loop - iteration: " + i, p, vars);
            }
        }
        return new returnValues(errorValues.sucess, "");
    }
}