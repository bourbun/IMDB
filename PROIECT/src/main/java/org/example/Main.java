package org.example;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//        SwingUtilities.invokeLater(LoginFrame::new);
        IMDB imdb = new IMDB();
        System.out.println("Welcome! Select an option:");
        System.out.println("1. Terminal");
        System.out.println("2. GUI");
        Scanner S = new Scanner(System.in);
        String option = S.nextLine();
        while (!option.equals("1") && !option.equals("2")) {
            System.out.println("Invalid option. Please try again.");
            option = S.nextLine();
        }
        if (option.equals("1")) {
            IMDB.getInstance().runTerminal();
        } else {
            IMDB.getInstance().runGUI();
        }
    }
}
