package com.goldsprite.consolepokemon;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InterceptOutput {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public void startIntercepting() {
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    public void stopIntercepting() {
        System.setOut(originalOut);
    }

    public String getInterceptedOutput() {
        return outputStream.toString();
    }
}

