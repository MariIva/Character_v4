package ru.mora.character_v4.files;


import android.content.Context;

import androidx.room.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.mora.character_v4.files.db.AppDB;
import ru.mora.character_v4.files.db.CharacterDAO;
import ru.mora.character_v4.MainActivity;

public class DataBaseFile implements FactoryFile {
    AppDB db;
    CharacterDAO characterDAO;
    MainActivity activity;

    public DataBaseFile(Context context, MainActivity activity) {
        // получение объекта базы данных
        db =  Room.databaseBuilder(context, AppDB.class, "Character_database").build();
        // получение дейсвий с базой данных
        characterDAO = db.characterDAO();
        this.activity = activity;
    }

    @Override
    public void saveCharacter(ArrayList<Character> characters) throws IOException {
        // запуск второго потока для добавления нового персонажа
        Thread thread = new Thread(new Runnable() {
            public void run() {
                characterDAO.insert(characters.get(characters.size()-1));
            }
        });
        thread.start();
    }

    @Override
    public ArrayList<Character> loadCharacter() throws IOException, ClassNotFoundException {
        // запуск второго потока для загружки всего списка персонажей
       Thread thread = new Thread(new Runnable() {
           public void run() {
               List<Character> list = characterDAO.getAll();
               // добавление полученного списка
               activity.characters.addAll(list);
           }
       });
       thread.start();
       // отправно пустого списка
       return  new ArrayList<Character>();
    }

    @Override
    public void deleteCharacter(ArrayList<Character> characters, int position) throws IOException {
        // запуск второго потока для удаления персонажа
        Thread thread = new Thread(new Runnable() {
            public void run() {
                characterDAO.delete(characters.get(position));
                characters.remove(position);
            }
        });
        thread.start();
    }


}
