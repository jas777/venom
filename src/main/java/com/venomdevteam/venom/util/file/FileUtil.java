package com.venomdevteam.venom.util.file;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class FileUtil {

    /**
     * @param url  - Direct URL of the file
     * @param path - Path to save the file
     * @return File object
     * <p>
     * Downloads file into given path
     */
    public static File download(URL url, String path) {

        File result = null;

        try {

            InputStream stream = url.openStream();

            BufferedInputStream bufferedStream = new BufferedInputStream(stream);
            FileOutputStream outputStream = new FileOutputStream(path);

            byte[] data = new byte[1024];

            int byteContent;

            while ((byteContent = bufferedStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, byteContent);
            }

            result = new File(path);

        } catch (IOException e) {
            LoggerFactory.getLogger("venom").error(e.getMessage());
        }

        LoggerFactory.getLogger("venom").debug("Saved file to: " + path);

        return result;

    }

    public static ByteArrayOutputStream download(URL url) {

        ByteArrayOutputStream result = null;

        try {

            InputStream stream = url.openStream();

            BufferedInputStream bufferedStream = new BufferedInputStream(stream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] data = new byte[1024];

            int byteContent;

            while ((byteContent = bufferedStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, byteContent);
            }

            result = outputStream;

        } catch (IOException e) {
            LoggerFactory.getLogger("venom").error(e.getMessage());
        }

        LoggerFactory.getLogger("venom").debug("Downloaded file as byte array");

        return result;

    }

}
