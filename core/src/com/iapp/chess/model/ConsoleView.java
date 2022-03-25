package com.iapp.chess.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleView {

    private BufferedReader reader;

    public ConsoleView() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void print(Figure[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] instanceof Pawn) {
                    System.out.print("П");
                } else if (matrix[i][j] instanceof Rook) {
                    System.out.print("Л");
                } else if (matrix[i][j] instanceof Knight) {
                    System.out.print("К");
                } else if (matrix[i][j] instanceof Bishop) {
                    System.out.print("С");
                } else if (matrix[i][j] instanceof Queen) {
                    System.out.print("Ф");
                } else if (matrix[i][j] instanceof King) {
                    System.out.print("Г");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("---------------------");
        System.out.println();
        System.out.println();
    }

    public Transition readUserMove(Game game) {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (line.equals(".")) return null;

        String[] tokens = line.split(" ");
        int figureX = Integer.parseInt(tokens[0]),
                figureY = Integer.parseInt(tokens[1]),
                moveX = Integer.parseInt(tokens[2]),
                moveY = Integer.parseInt(tokens[3]);


        Transition transition = new Transition(game, game.getFigure(figureX, figureY), new Move(moveX, moveY));
        return transition;
    }
}
