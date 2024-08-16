package de.femrene.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    public static String getString(String path) {
        Properties props = new Properties();
        try (InputStream in  = new FileInputStream("config.properties")) {
            props.load(in);
            return props.getProperty(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean ifString(String path) {
        Properties props = new Properties();
        if (new File("config.properties").exists()) {
            try (InputStream in  = new FileInputStream("config.properties")) {
                props.load(in);
                return props.containsKey(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

}
