package ru.mora.character_v4.files;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity // метка таблицы базы данных
public class Character implements  Parcelable, Serializable {

    @PrimaryKey(autoGenerate = true) // метка первичного ключа с автогенерацией номеров
    public long id;

    public String img_uri;
    public String name;
    public int age;
    public String sex;
    public String race;
    public int str;
    public int dex;
    public int con;
    public int intl;
    public int wis;
    public int charm;

    public Character(String img_uri, String name, int age, String sex, String race, int str,
                     int dex, int con, int intl, int wis, int charm) {
        this.img_uri = img_uri;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.race = race;
        this.str = str;
        this.dex = dex;
        this.con = con;
        this.intl = intl;
        this.wis = wis;
        this.charm = charm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // правило записи полей класса
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img_uri);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(race);
        dest.writeInt(age);
        dest.writeInt(str);
        dest.writeInt(dex);
        dest.writeInt(con);
        dest.writeInt(intl);
        dest.writeInt(wis);
        dest.writeInt(charm);
    }

    // создатель объектов из потока битов
    @Ignore // метка игнорирования поля при добавлении/изменении записи
    public static final Creator<Character> CREATOR = new Creator<Character>() {
        // правило считывание полей класса
        @Override
        public Character createFromParcel(Parcel in) {
            String img = in.readString();
            String name = in.readString();
            String sex = in.readString();
            String race = in.readString();
            int age = in.readInt();
            int str = in.readInt();
            int dex = in.readInt();
            int con = in.readInt();
            int intl = in.readInt();
            int wis = in.readInt();
            int charm = in.readInt();
            return new Character(img, name, age, sex, race, str, dex, con, intl, wis, charm);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

}
