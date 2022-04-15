import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        String savingDirectory = "C://Users//lisit//IdeaProjects//Games//savegames//";

        GameProgress gameProgress1 = new GameProgress(100, 5, 1, 1);
        GameProgress gameProgress2 = new GameProgress(80, 4, 2, 10);
        GameProgress gameProgress3 = new GameProgress(50, 2, 4, 100);

        //Реализуйте метод saveGame(),
        // принимающий в качестве аргументов полный путь к файлу типа String
        // и объект класса GameProgress
        saveGame(savingDirectory + "save1.dat", gameProgress1);
        saveGame(savingDirectory + "save2.dat", gameProgress2);
        saveGame(savingDirectory + "save3.dat", gameProgress3);

        //реализуйте метод zipFiles(), принимающий в качестве аргументов String полный путь к файлу архива и
        // список запаковываемых объектов в виде списка строчек String полного пути к файлу
        String zipPath = savingDirectory + "zip.zip";
        String unzipPath = savingDirectory;

        List<String> listForZip = new ArrayList<>();
        listForZip.add(savingDirectory + "save1.dat");
        listForZip.add(savingDirectory + "save2.dat");
        listForZip.add(savingDirectory + "save3.dat");

        zipFiles(zipPath, listForZip);


        //Реализуйте метод openZip(), который принимал бы два аргумента: путь к файлу типа String
        //и путь к папке, куда стоит разархивировать файлы типа String
        openZip(zipPath, unzipPath);

        //реализуйте метод openProgress(),
        // который бы в качестве аргумента принимал путь к файлу с сохраненной игрой типа String
        // и возвращал объект типа GameProgress
        System.out.println(openProgress(savingDirectory + "unzip_save2.dat"));

    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath, true);
             ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream)) {
            objOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> filesForZip) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipPath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            Iterator<String> iterator = filesForZip.iterator();
            while (iterator.hasNext()) {
                File file = new File(iterator.next());
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
                fis.close();
                file.delete();
            }
            zipOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipPath, String unzipPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(unzipPath + "unzip_" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }
}