package ru.mora.character_v4.files.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.mora.character_v4.files.Character;

// список действи базы данных
@Dao
public interface  CharacterDAO {
    // запрос на получение списка всех персонажей
    @Query("SELECT * FROM character")
    List<Character> getAll();

    // запрос на получение персонажа по уникальному номеру
    @Query("SELECT * FROM character WHERE id = :id")
    Character getById(long id);

    // добавление нового персонажа в базу данных
    @Insert
    void insert(Character character);

    // обновление персонажа
    @Update
    void update(Character character);

    // удаление персонажа
    @Delete
    void delete(Character character);
}
