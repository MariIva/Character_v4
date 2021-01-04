package ru.mora.character_v4.files;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LocalFile implements FactoryFile {

    String path;
    Context context;

    public LocalFile(Context context) {
        // путь к файлу
        path = context.getFilesDir().toString()+"/Character_v3.dat";
        this.context = context;
    }

    // сохрание списка персонажей, как сериаливанные объекты
    public void saveCharacter(ArrayList<Character> characters)  throws IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(characters);
            oos.close();
    }

    public ArrayList<Character> loadCharacter()  throws IOException, ClassNotFoundException {
        // создаем описание файла
        ArrayList<Character> characters = new ArrayList<>();
        File f = new File(path);
        // если файла не существует, то создаем его
        if (!f.exists()) {
            f.createNewFile();
        }
        FileInputStream stream = new FileInputStream(path);
        if (stream.available() > 0) {
            ObjectInputStream ois = new ObjectInputStream(stream);
            characters = (ArrayList<Character>) ois.readObject();
            ois.close();
        }
        return characters;
    }

    @Override
    public void deleteCharacter(ArrayList<Character> characters, int position) throws IOException {
        // удаление персонажа из списка
        characters.remove(position);
        // сохранение обновленного списка
        saveCharacter(characters);
    }
}
