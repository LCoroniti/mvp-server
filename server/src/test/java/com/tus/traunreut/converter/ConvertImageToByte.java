package com.tus.traunreut.converter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConvertImageToByte {
    @Test
    void testPrintPostgresByteaForUpdate() throws IOException {
        String filePath = "F:/dev/logos/frauen/münchenost.png";

        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        StringBuilder hexString = new StringBuilder();
        for (byte b : fileBytes) {
            hexString.append(String.format("%02X", b));
        }

        System.out.println("UPDATE clubs SET logo = decode('" + hexString + "', 'hex') WHERE club_id = 123;");
    }
}
