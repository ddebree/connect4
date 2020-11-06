package io.github.ddebree.connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Connect4Application {

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println("Got: " + line);
        }
        System.out.println("Done");
    }

}
