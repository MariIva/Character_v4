package ru.mora.character_v4.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.mora.character_v4.ListCharacterFragment;
import ru.mora.character_v4.files.Character;
import ru.mora.character_v4.R;

// адаптер списка в виде списка
public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<Character> characters;
    private Context mContext;

    public CharacterListAdapter(Context context, ArrayList<Character> characters) {
        this.inflater = LayoutInflater.from(context);
        this.characters = characters;
        this.mContext = context;
    }

    // создание холдера
    @NonNull
    @Override
    public CharacterListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_row, parent, false);
        return new CharacterListHolder(view);
    }

    // заполнение элемента списка значениями
    @Override
    public void onBindViewHolder(@NonNull CharacterListHolder holder, int position) {
        Character character = characters.get(position);

        // заполняем View в пункте списка данными
        holder.name.setText(character.name);
        holder.race.setText(character.race);
        if (character.img_uri.equals("")){
            holder.imageView.setImageResource(R.drawable.char_icon);
        }
        else {
            Uri uri = Uri.parse(character.img_uri);
            holder.imageView.setImageURI(uri);
        }

        // слушатель на клик на строку
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // получение методов интерфейса реализованных в активности
                ListCharacterFragment.OnClickAddButtonListener listener =
                        (ListCharacterFragment.OnClickAddButtonListener) mContext;
                // запуск метода активности
                listener.onButtonClickSee(position);
            }
        });

        // слушатель на клик на ⋮ (три точки)
        holder.list_tv_option.setOnClickListener(new View.OnClickListener() {
            // показ контекстного меню
            @Override
            public void onClick(View v) {
                // создание объекта меню
                PopupMenu popupMenu = new PopupMenu(mContext, holder.list_tv_option);
                // заполнение меню элементами
                popupMenu.inflate(R.menu.option_menu);
                // слушаетль на клик на элемент меню
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                // получение методов интерфейса реализованных в активности
                                ListCharacterFragment.OnClickAddButtonListener listener =
                                        (ListCharacterFragment.OnClickAddButtonListener) mContext;
                                // запуск метода активности
                                listener.onButtonClickDelete(position);
                                // обновление списка
                                notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                // показ меню
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }
}
