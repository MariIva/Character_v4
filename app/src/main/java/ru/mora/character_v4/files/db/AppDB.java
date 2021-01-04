package ru.mora.character_v4.files.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mora.character_v4.files.Character;

// метка класса базы данных
// entities = {Character.class} - список таблиц
// version = 1 - ыерсия базы данных
@Database(entities = {Character.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    // список действи базы данных
    public abstract CharacterDAO characterDAO();

}
