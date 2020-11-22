package io.github.ddebree.connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Connect4Application {

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        final Solver solver = new Solver();
        System.out.println("input,score,Number of nodes,time(ns)");
        long totalNodes = 0;
        long totalNs = 0;
        long puzzlesSolved = 0;
        while ((line = bufferedReader.readLine()) != null) {
            final String[] splits = line.split(" ");
            final String input = splits[0];
            final String expectedResult = splits[1];
            //System.out.println("Input: \"" + input + "\" Expected result: " + expectedResult);
            try {
                final Position p = Position.fromInputSeq(input);

                final long startTime = System.nanoTime();
                final int score = solver.solve(p);
                final long time = System.nanoTime() - startTime;

                if ( ! expectedResult.equals("" + score)) {
                    System.out.println("Expected scores didnt match. " + input + " " + score);
                }

                puzzlesSolved++;
                totalNs += time;
                totalNodes += solver.getNodeCount();
                System.out.println(input + "," + score + "," + solver.getNodeCount() + "," + time);
            } catch (InvalidMoveException e) {
                System.err.println("Invalid move: \"" + line + "\"");
            }
        }
        System.out.println("Puzzles solved = " + puzzlesSolved);
        System.out.println("Total time (ms) = " + (totalNs / 1_000_000L));
        System.out.println("Total nodes = " + totalNodes);

        System.out.println("Average time to solve = " + (totalNs / puzzlesSolved / 1000L) + "us");
        System.out.println("Average number of positions = " + (totalNodes / puzzlesSolved));
        System.out.println("K-Nodes per second = " + (totalNodes * 1_000_000L / totalNs));
    }

}
