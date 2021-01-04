package ru.mora.character_v4.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mora.character_v4.R;

public class CharacterCardHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView race;
    public TextView list_tv_option;
    public ImageView imageView;
    public Button list_bt;

    public CharacterCardHolder(@NonNull View itemView) {
        super(itemView);
        // получение всех элементов карты
        imageView = (ImageView) itemView.findViewById(R.id.list_img_icon);
        name = (TextView) itemView.findViewById(R.id.list_tv_name);
        race = (TextView) itemView.findViewById(R.id.list_tv_race);
        list_tv_option = (TextView) itemView.findViewById(R.id.list_tv_option);
        list_bt = (Button) itemView.findViewById(R.id.list_bt);

    }
}
