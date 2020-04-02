package sample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OwnClassLoader extends ClassLoader {
    String loadFromDirPath = "";

    public void setLoadFromDirPath(String loadFromDirPath) {
        this.loadFromDirPath = loadFromDirPath;
    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        Path classPath = Paths.get(loadFromDirPath + "\\" + name.replace('.', File.separatorChar)+".class");
        //System.out.println(classPath);
        if (Files.exists(classPath)){
            try {
                byte[] byteCode = Files.readAllBytes(classPath);
                return this.defineClass(name, byteCode, 0, byteCode.length);
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        } else {
            throw new ClassNotFoundException(name);
        }
    }

    private byte[] loadClassFromFile(String fileName)  {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}