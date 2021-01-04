package ru.mora.character_v4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.mora.character_v4.adapters.CharacterCardAdapter;
import ru.mora.character_v4.adapters.CharacterListAdapter;
import ru.mora.character_v4.files.Character;

public class ListCharacterFragment extends Fragment
        implements View.OnClickListener{

    private static final String ARG_CHAR = "characters";
    RecyclerView recyclerView;
    ArrayList<Character> characters;


    // интервейс для реализации в активности
    public interface OnClickAddButtonListener {
        void onButtonClickAdd(int index);       // добавление персонажа
        void onButtonClickSee(int position);    // просмотр персонажа
        void onButtonClickDelete(int position); // удаление персонажа
    }

    // создание фрагмента
    public ListCharacterFragment() {
        // Required empty public constructor
    }

    // создание фрагмента с входными данными - список персонажей
    public static ListCharacterFragment newInstance(ArrayList<Character> characters) {
        ListCharacterFragment fragment = new ListCharacterFragment();
        // сохрание входных данных
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CHAR, characters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // получение входных данных
        characters = getArguments().getParcelableArrayList(ARG_CHAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        // получаем предпочтения всего приложения
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // получение способа хранения
        String key = prefs.getString("list", "list_ver");

        View rootView = inflater.inflate(R.layout.fragment_list_character, container, false);

        // установка адаптера в список
        recyclerView = rootView.findViewById(R.id.list_char);
        if (key.equals("list_ver")){
            setCharacterList(0);
        }
        else if (key.equals("DB")){
            setCharacterList(1);
        }

        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab);
        button.setOnClickListener(this);

        return rootView;
    }

    // клик на кнопку
    @Override
    public void onClick(View view) {
        // получение методов интерфейса реализованных в активности
        OnClickAddButtonListener listener = (OnClickAddButtonListener) getActivity();
        // запуск метода активности
        listener.onButtonClickAdd(R.id.fab);
    }

    // установка адаптера
    public void setCharacterList(int v) {
        if (v == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            // создаем адаптер заполняющий список
            CharacterListAdapter characterAdapter = new CharacterListAdapter(getContext(), this.characters);
            // устанавливаем адаптер
            recyclerView.setAdapter(characterAdapter);
        }
        if (v == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                    GridLayoutManager.VERTICAL, false));
            // создаем адаптер заполняющий список
            CharacterCardAdapter characterAdapter = new CharacterCardAdapter(getContext(), characters);
            // устанавливаем адаптер
            recyclerView.setAdapter(characterAdapter);
        }
    }

    // установка адапрета и обновления списка персонажей
    public void setCharacterList(int v, ArrayList<Character> characters) {
        this.characters = characters;
        if (v == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            // создаем адаптер заполняющий список
            CharacterListAdapter characterAdapter = new CharacterListAdapter(getContext(), this.characters);
            // устанавливаем адаптер
            recyclerView.setAdapter(characterAdapter);
        }
        if (v == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                    GridLayoutManager.VERTICAL, false));
            // создаем адаптер заполняющий список
            CharacterCardAdapter characterAdapter = new CharacterCardAdapter(getContext(), this.characters);
            // устанавливаем адаптер
            recyclerView.setAdapter(characterAdapter);
        }
    }

}