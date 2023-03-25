package com.uvg.gt;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Function;

/**
 * App
 */
public class App {

    public static ArrayList<Object> _title = new ArrayList<>() {
        {
            add("..######..########..##.....##");
            add(".##....##.##.....##.##.....##");
            add(".##.......##.....##.##.....##");
            add(".##.......########..##.....##");
            add(".##.......##........##.....##");
            add(".##....##.##........##.....##");
            add("..######..##.........#######.");
        }
    };

    public static void main(String[] args) throws FileNotFoundException {

        clearConsole();
        showTitle(_title, ANSI_BLUE);

        // /home/elrohirgt/Documents/Development/Java/hoja_de_trabajo_8/data.txt
        var file = formLabel("File path ", ANSI_CYAN, s -> {
            var f = new File(s);
            var exists = f.exists();
            if (!exists) {
                writeErrorMessage("Please enter a valid file path!");
            }
            return exists;
        }, s -> {
            return new File(s);
        });
        writeWarningMessage(String.format("We will be using: %s", file.getPath()));

        var answer = formLabel("Use the Java Collection Framework? (y/n)", ANSI_CYAN);
        IPriorityQueue<Process> queue = new VectorHeap<>();
        if (answer.toLowerCase().equals("y")) {
            queue = new JCFPriorityQueue<>();
        }

        parseFile(file, queue);

        Process current = null;
        Process next = queue.peek();
        while (!queue.isEmpty() || current != null) {
            clearConsole();

            consoleWriteLine("==== CURRENT ====", ANSI_BLUE);
            if (current == null) {
                writeErrorMessage("No current process!");
            } else {
                printProcess(current);
            }

            consoleWriteLine("==== NEXT ====", ANSI_YELLOW);
            if (next == null) {
                writeErrorMessage("No next process!");
            } else {
                printProcess(next);
            }

            printSpaceSeparated("y)", "Advance to next process", ANSI_YELLOW);
            printSpaceSeparated("q)", "Quit", ANSI_YELLOW);

            answer = formLabel("Select an option", ANSI_CYAN);
            if (answer.toLowerCase().equals("y")) {
                try {
                    current = queue.dequeue();
                } catch (Exception e) {
                    current = null;
                }
                try {
                    next = queue.peek();
                } catch (Exception e) {
                    next = null;
                }
            } else if (answer.toLowerCase().equals("q")) {
                break;
            } else {
                writeErrorMessage("Please write a valid option!");
            }
        }

        writeSuccessMessage("Thank you for using ChichicasteLinux!");
    }

    private static void printProcess(Process process) {
        showInTable(process, p -> {
            var map = new LinkedHashMap<>();
            map.put("Name", process.getName());
            map.put("Owner", process.getOwner());
            map.put("Nice Value", process.getNice());
            map.put("Priority", process.getPriority());
            return map;
        });

    }

    private static void parseFile(File file, IPriorityQueue<Process> queue) throws FileNotFoundException {
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            var line = sc.nextLine();
            var parts = line.split(",");
            var niceValue = Integer.parseInt(parts[2]);
            var process = new Process(parts[0], parts[1], niceValue);

            queue.insert(process, process.getPriority());
        }

        sc.close();
    }

    public static void writeWarningMessage(Object message) {
        consoleWriteLine(message, ANSI_YELLOW);
    }

    public static <T> void printList(Object title, Iterable<T> list) {
        consoleWriteLine(title, ANSI_CYAN);
        for (T itemT : list) {
            System.out.println(itemT.toString() + " ");
        }
    }

    public static <T> void printTable(Object title, Iterable<T> list, int columnWidth, String separator) {
        consoleWriteLine(title, ANSI_CYAN);
        int i = 0;
        for (T itemT : list) {
            if (i % columnWidth == 0) {
                System.out.println();
            }
            System.out.print(itemT.toString() + separator);
            i++;
        }
        System.out.println();
    }

    public static void writeErrorMessage(Object message) {
        consoleWriteLine(message, ANSI_WHITE, ANSI_RED_BACKGROUND);
    }

    public static void writeSuccessMessage(Object message) {
        consoleWriteLine(message, ANSI_WHITE, ANSI_GREEN_BACKGROUND);
    }

    public static void showTitle(ArrayList<Object> titulo, String foreground) {
        for (Object line : titulo) {
            consoleWriteLine(line, foreground);
        }
    }

    public static <T> void progressBar(Object prefix, Iterable<Object> frames) {
        progressBar(prefix, frames, 3000, ANSI_YELLOW);
    }

    public static <T> void progressBar(Object prefix, Iterable<Object> frames, long durationInMs, Object color) {
        final long START_MS = Clock.systemUTC().instant().toEpochMilli();
        boolean startAgain = true;

        System.out.print(String.format("%s%s", color, prefix));
        while (startAgain) {
            for (Object frame : frames) {
                final String stringFrame = frame.toString();
                System.out.print(stringFrame);

                final long CURRENT_MS = Clock.systemUTC().instant().toEpochMilli();
                if (CURRENT_MS - START_MS > durationInMs) {
                    System.out.println(ANSI_RESET);
                    startAgain = false;
                    break;
                }

                try {
                    Thread.sleep(200, 0);
                } catch (Exception e) {
                }

                for (int i = 0; i < stringFrame.length(); i++) {
                    System.out.print("\b \b"); // \b is a not destructive backspace, that's why we need the space
                }
            }
        }

    }

    public static <T> void showInTable(T item, Function<T, LinkedHashMap<Object, Object>> convertToMap) {
        Integer maxLength = null;
        var rows = convertToMap.apply(item);
        if (maxLength == null) {
            maxLength = rows.keySet().stream()
                    .map(o -> o.toString().length())
                    .max(Integer::compare)
                    .orElse(0) + 1;
        }

        for (Object rowName : rows.keySet()) {
            int spacing = maxLength - rowName.toString().length() + rows.get(rowName).toString().length();
            var format = "%" + spacing + "s";
            consoleWrite(rowName + ":", ANSI_CYAN);
            consoleWriteLine(String.format(format, rows.get(rowName)));
        }
    }

    public static <T> void showInTable(Iterable<T> list,
            Function<T, LinkedHashMap<Object, Object>> convertToMap) {
        Integer maxLength = null;
        for (T itemT : list) {
            var rows = convertToMap.apply(itemT);
            if (maxLength == null) {
                maxLength = rows.keySet().stream()
                        .map(o -> o.toString().length())
                        .max(Integer::compare)
                        .orElse(0) + 1;
            }
            for (Object rowName : rows.keySet()) {
                int spacing = maxLength - rowName.toString().length() + rows.get(rowName).toString().length();
                var format = "%" + spacing + "s";
                consoleWrite(rowName + ":", ANSI_CYAN);
                consoleWriteLine(String.format(format, rows.get(rowName)));
            }
            consoleWriteLine(SUB_DIVIDER);
        }
    }

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";

    static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    static final int DIVIDER_LENGTH = 32;
    static final int DIVIDER_HALF_LENGTH = DIVIDER_LENGTH / 2;
    static final String DIVIDER = new String(new char[DIVIDER_LENGTH]).replace("\0", "=");
    static final String SUB_DIVIDER = new String(new char[DIVIDER_LENGTH]).replace("\0", "-");

    public static String formLabel(Object label, String color) {
        consoleWrite(label + ": ");
        return waitForInputColored(color);
    }

    public static String formLabel(Object label, String color, Function<String, Boolean> check) {
        String input = "";
        do {
            input = formLabel(label, color);
        } while (!check.apply(input));

        return input;
    }

    public static <T> T formLabel(Object label, String color, Function<String, Boolean> check,
            Function<String, T> conv) {
        String input = formLabel(label, color, check);
        return conv.apply(input);
    }

    public static String waitForInputColored(String color) {
        System.out.print(color);
        String s = System.console().readLine();
        System.out.print(ANSI_RESET);
        return s;
    }

    public static void printSpaceSeparated(Object identifier, Object option, String color) {
        consoleWrite(identifier + " ", color);
        consoleWriteLine(option.toString());
    }

    public static void printSpaceSeparatedFor(Object identifier, Object option, String color) {
        consoleWrite(identifier + " ");
        consoleWriteLine(option.toString(), color);
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void consoleWriteLine(Object message) {
        consoleWriteLine(message, "", "");
    }

    public static void consoleWriteLine(Object message, String foreground) {
        consoleWriteLine(message, foreground, "");
    }

    public static void consoleWriteLine(Object message, String foreground, String background) {
        System.out.println(background + foreground + message + ANSI_RESET);
    }

    public static void consoleWrite(Object message) {
        consoleWrite(message, "", "");
    }

    public static void consoleWrite(Object message, String foreground) {
        consoleWrite(message, foreground, "");
    }

    public static void consoleWrite(Object message, String foreground, String background) {
        System.out.print(background + foreground + message + ANSI_RESET);
    }
}