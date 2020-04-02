package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import processing.CustomStatusListener;
import processing.Status;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Controller {

    @FXML
    public TextField catalougeFieldTextArea;
    @FXML
    public Button initializeListButton;
    @FXML
    public Label statusListenerLabel;
    @FXML
    public TextArea processedTextArea;
    @FXML
    private ListView<String> classListView;

    @FXML
    private TextArea enterTextArea;

    private OwnClassLoader loader;
    private Object object;
    private Class<?> c;
    private List<String> classes;
    private String dirPath;
    private CustomStatusListener listener = new CustomStatusListener("Brak zadania", 0);
    String dirPathname;
    File directory;
    String text = "";

    public void initialize() throws IOException {

        System.gc();
        classes = new ArrayList<String>();

        dirPathname = catalougeFieldTextArea.getText();
        directory = new File(dirPathname);

        if(directory.exists())
            loadFilesNames(directory);

        //todoListView.getItems().setAll(todoItems);
        classListView.getItems().setAll(classes);
        classListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        statusListenerLabel.setText(listener.getInfo());
    }

    @FXML
    public void handleClickListView() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException, InterruptedException {
        String className = classListView.getSelectionModel().getSelectedItem();
        listener.statusChanged(new Status("Proba zaladowania algorytmu", 0));
        System.out.println(listener.getInfo());

        text = enterTextArea.getText();

        loader = new OwnClassLoader();
        loader.setLoadFromDirPath(dirPath);
        //Class<?> c = loader.loadClass("processing.NumberOfWords");
        c = loader.loadClass(className);


        try {
            object = c.getConstructor().newInstance();
            Method method1 = c.getMethod("getInfo");
            listener.statusChanged(new Status((String) method1.invoke(object), 50));
            System.out.println(listener.getInfo());
            Method method2 = c.getMethod("submitTask", String.class, processing.StatusListener.class);
            method2.invoke(object, text, listener);
            Method method3 = c.getMethod("getResult");
            processedTextArea.setText((String) method3.invoke(object));
            System.out.println(listener.getInfo());
        } catch (NoSuchMethodException e) {
            processedTextArea.setText("Ta klasa nie posiada metody przetwarzajacej tekst!");
            listener.statusChanged(new Status("Brak wybranego algorytmu", 0));
            System.out.println(listener.getInfo());
        }

        //TimeUnit.SECONDS.sleep(5);

        statusListenerLabel.setText(listener.getInfo());
        System.out.println("\n");


        // wyladowanie klasy
        object = null;
        c = null;
        loader = null;
        System.gc();
    }

    private void loadFilesNames(File directory) throws IOException {

        File[] files = directory.listFiles();
        File direct = new File(directory.getAbsolutePath());
        dirPath = direct.getParent();


        for (File file : files) {

            if(file.isFile() && file.getPath().contains(".class")){

                File parent = new File(file.getParent());
                parent.getName();
                String name = stripExtension(parent.getName() + "." + file.getName());
                classes.add(name);

            }
        }
    }

    static String stripExtension (String str) {

        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;

        return str.substring(0, pos);
    }

}
