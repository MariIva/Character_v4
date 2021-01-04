package ru.mora.character_v4.files;

import java.io.IOException;
import java.util.ArrayList;

// интерфейс фабрика
public interface  FactoryFile {
    // сохранение списка персонажей
    void saveCharacter(ArrayList<Character> characters) throws IOException;
    // загрузка персонажей
    ArrayList<Character> loadCharacter() throws IOException, ClassNotFoundException ;
    //удаление персонажа
    void deleteCharacter(ArrayList<Character> characters, int position) throws IOException;
}
