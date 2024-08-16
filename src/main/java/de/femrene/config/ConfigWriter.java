package de.femrene.config;

import java.io.*;
import java.util.Properties;

public class ConfigWriter {

    public ConfigWriter(String path, String value) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException ex) {
            System.out.println("File not found or could not be loaded. A new file will be created.");
        }
        props.setProperty(path, value);
        try (OutputStream out = new FileOutputStream("config.properties")) {
            props.store(out, "Configuration File");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
