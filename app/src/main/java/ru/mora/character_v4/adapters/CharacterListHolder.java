package ru.mora.character_v4.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mora.character_v4.R;

public class CharacterListHolder  extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView race;
    public ImageView imageView;
    public TextView list_tv_option;

    public CharacterListHolder(@NonNull View itemView) {
        super(itemView);
        // получение всех элементов
        name = (TextView) itemView.findViewById(R.id.list_tv_name);
        race = (TextView) itemView.findViewById(R.id.list_tv_race);
        imageView = (ImageView) itemView.findViewById(R.id.list_img_icon);
        list_tv_option = (TextView) itemView.findViewById(R.id.list_tv_option);

    }
}
