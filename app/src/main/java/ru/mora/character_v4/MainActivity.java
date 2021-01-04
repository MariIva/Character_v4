package ru.mora.character_v4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ru.mora.character_v4.files.Character;
import ru.mora.character_v4.files.DataBaseFile;
import ru.mora.character_v4.files.FactoryFile;
import ru.mora.character_v4.files.LocalFile;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
                   ListCharacterFragment.OnClickAddButtonListener,
                   CharFragment.OnCharFragmentListener{

    SharedPreferences prefs;

    FrameLayout frameLayout = null;

    SettingsFragment settingsFragment;
    ListCharacterFragment listCharacterFragment;
    CharFragment charFragment;

    // список персонажей
    public ArrayList<Character> characters = new ArrayList<>();
    // код запроса разрешения на запись
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    // код запроса выбора картинки
    final static int SELECT_PICTURE = 1;

    FactoryFile factoryFile;

    // теги для фрагментов
    final static String TAG_LIST = "TAG_LIST";
    final static String TAG_SETTING = "TAG_SETTING";
    final static String TAG_CREATE = "TAG_CREATE";

    // ключи для сохранения состояния
    final static String STATE_FRAGMENT = "STATE_FRAGMENT";
    final static String STATE_VALUES = "STATE_VALUES";


    // метод устанавливающий тему активности
    public void setTheme() {
        // берем контекст приложения
        Context context = getApplicationContext();
        // получаем предпочтения всего приложения
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // берем значение цветовой темы
        String theme = prefs.getString("view", "standart");
        // берем значение включения режима темной темы
        boolean dark = prefs.getBoolean("dark_theme", false);
        // установка нужной цветовой схемы
        if (theme.equals("costom_theme")){
            setTheme(R.style.Theme_CUSTOM_THEME);
        }
        else{
            setTheme(R.style.Theme_Character_v4);
        }
        // установка темной темы
        if (dark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void permission()  {
        int permissionStatus_write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus_write == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,     // эта активность
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},     // список размешений
                    PERMISSION_WRITE_EXTERNAL_STORAGE);   // код запроса разрешения
        }
        else{
            try {
                characters = factoryFile.loadCharacter();
            }catch (IOException | ClassNotFoundException e){
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main_layout);
                //создаем Snackbar
                Snackbar snackbar = Snackbar.make(layout, "Ошибка чтения файла", Snackbar.LENGTH_LONG);
                // показываем Snackbar
                snackbar.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // установка темы
        setTheme();
        setContentView(R.layout.activity_main);
        // получение SharedPreferences приложения
        prefs.registerOnSharedPreferenceChangeListener(this);

        // получение способа и объекта хранение персонажей
        getFactory(prefs);

        // получение размешения на чтение и запись
        permission();

        // создание фрагментов
        settingsFragment = new SettingsFragment();
        charFragment = CharFragment.newInstance();
        listCharacterFragment = ListCharacterFragment.newInstance(characters);
        // установка фрагмента списка
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list, listCharacterFragment)
                .commit();

        frameLayout = (FrameLayout) findViewById(R.id.fragment_);

        // если было сохранено состояние
        // например при перевороте эерана
        if (savedInstanceState!=null) {
            // получение основного фрагмента
            String fragment = savedInstanceState.getString(STATE_FRAGMENT);
            // если ориентация вертикальная
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // если фрагмент настройки
                if (fragment.equals(TAG_SETTING)){
                    // замена фрагмента списка на настройки
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list, settingsFragment, TAG_SETTING)
                            .addToBackStack(null)
                            .commit();
                }
                // если фрагмент создания персонажа
                else if (fragment.equals(TAG_CREATE)){
                    // создание фрагмента с начальными значениями (сохраненными введенными данными)
                    charFragment = CharFragment.newInstance(savedInstanceState.getStringArray(STATE_VALUES));

                    // замена фрагмента списка на создания персонажа
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list, charFragment, TAG_CREATE)
                            .addToBackStack(null)
                            .commit();
                }
            }
            // если ориентация горизонтальная
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // если фрагмент настройки
                if (fragment.equals(TAG_SETTING)){
                    // установка  фрагмента в правую часть экрана
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_, settingsFragment, TAG_SETTING)
                            .addToBackStack(null)
                            .commit();
                }
                // если фрагмент создания персонажа
                else if (fragment.equals(TAG_CREATE)){
                    // создание фрагмента с начальными значениями (сохраненными введенными данными)
                    charFragment = CharFragment.newInstance(savedInstanceState.getStringArray(STATE_VALUES));
                    // установка  фрагмента в правую часть экрана
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_, charFragment, TAG_CREATE)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }

    }

    // сохранение состояния активности и фрагментов
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        // получение фрагментов из менеджера
        SettingsFragment sFragment = (SettingsFragment)getSupportFragmentManager().findFragmentByTag(TAG_SETTING);
        CharFragment cFragment = (CharFragment)getSupportFragmentManager().findFragmentByTag(TAG_CREATE);
        // если основной фрагмент - настройки
        if (sFragment != null && sFragment.isVisible()) {
            // сохранение метки фрагмента
            saveInstanceState.putString(STATE_FRAGMENT, TAG_SETTING);
        }
        // если основной фрагмент - создание персонажа
        else if (cFragment != null && cFragment.isVisible()) {
            // получение введенных пользователем значений
            String strings[] = cFragment.getStrings();
            // сохранение метки фрагмента
            saveInstanceState.putString(STATE_FRAGMENT, TAG_CREATE);
            // сохрание значений
            saveInstanceState.putStringArray(STATE_VALUES, strings);
        }
        // если основной фрагмент - список
        else {
            saveInstanceState.putString(STATE_FRAGMENT, TAG_LIST);
        }
        // всегда вызывайте суперкласс для сохранения состояний видов
        super.onSaveInstanceState(saveInstanceState);
    }

    // метод, создающий меню на актиквности
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //  метод обрабатывабщий клики на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // если клик на пунк "setting", то открываем активность настроек
        switch (item.getItemId()) {
            case R.id.action_settings:
                // если ориентация вертикальная
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // замена фрагмента списка на настройки
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list, settingsFragment, TAG_SETTING)
                            .addToBackStack(null)
                            .commit();
                }
                // если ориентация горизонтальная
                else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // установка  фрагмента в правую часть экрана
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_, settingsFragment, TAG_SETTING)
                            .commit();
                }
                return true;
            default:
                // Handle fragment menu items
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onButtonClickAdd(int index) {
        switch (index) {
            case R.id.fab:
                // если ориентация вертикальная
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // замена фрагмента списка на созлание персонажа
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_list, charFragment, TAG_CREATE)
                            .addToBackStack(null)
                            .commit();
                }
                // если ориентация горизонтальная
                else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // установка  фрагмента в правую часть экрана
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_, charFragment, TAG_CREATE)
                            .commit();
                    break;
                }
        }
    }

    @Override
    public void onButtonClickSee(int position) {
        // создание фрагмента с входными данными - выбранный персонаж
        charFragment = CharFragment.newInstance(characters.get(position));
        // если ориентация вертикальная
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // замена фрагмента списка на созлание персонажа
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, charFragment, TAG_CREATE)
                    .addToBackStack(null)
                    .commit();
        }
        // если ориентация горизонтальная
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // установка  фрагмента в правую часть экрана
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_, charFragment, TAG_CREATE)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onButtonClickDelete(int position) {
        try {
            // удаление выбранного персонажа
            factoryFile.deleteCharacter(characters, position);
        } catch (Exception e) {
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main_layout);
            //создаем Snackbar
            Snackbar snackbar = Snackbar.make(layout, "Ошибка удаления", Snackbar.LENGTH_LONG);
            // показываем Snackbar
            snackbar.show();
        }
    }

    @Override
    public void onImageClick() {
        // при клике на imageView открывается стандартный выбор картинки из памяти
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void getCharacter(Character character) {
        if (character!=null) {
            // добавляем персонажа в список
            characters.add(character);
            try {
                // сохраняем обновленный список персонажей
                factoryFile.saveCharacter(characters);
            } catch (IOException e) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main_layout);
                //создаем Snackbar
                Snackbar snackbar = Snackbar.make(layout, "Ошибка записи в файл", Snackbar.LENGTH_LONG);
                // показываем Snackbar
                snackbar.show();
            }

            // если ориентация вертикальная
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // удаление фрагмента с активности
                getFragmentManager().popBackStack();
                FragmentManager fm =getSupportFragmentManager();
                if( fm.getBackStackEntryCount()>0) {
                    fm.popBackStack();
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // если пользователь решил изменить цветовую схему, пересоздаем MainActivity
        if (key.equals("view")) {
            this.recreate();
        }
        // если пользователь переключил темную тему
        if (key.equals("dark_theme")){
            boolean theme = sharedPreferences.getBoolean("dark_theme", false);
            if (theme){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        // изменение вида списка
        if (key.equals("list")){
            String str = sharedPreferences.getString("list", "list_ver");
            // обновление адаптера
            if (str.equals("list_ver")){
                listCharacterFragment.setCharacterList(0);
            }
            else{
                listCharacterFragment.setCharacterList(1);
            }
        }
        // изменение способа хранения
        if (key.equals("save_file")) {
            try {
                // получение новоого объекта хранилища
                getFactory(sharedPreferences);
                // загрузка списка из хранилища
                characters = factoryFile.loadCharacter();
                // обновление адаптера
                String str = sharedPreferences.getString("save_file", "local_file");
                if (str.equals("local_file")){
                    listCharacterFragment.setCharacterList(0, characters);
                } else if (str.equals("DB")){
                    listCharacterFragment.setCharacterList(1, characters);
                }
            }catch (IOException | ClassNotFoundException e){
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main_layout);
                //создаем Snackbar
                Snackbar snackbar = Snackbar.make(layout, "Ошибка чтения файла", Snackbar.LENGTH_LONG);
                // показываем Snackbar
                snackbar.show();
            }

        }
    }

    private void getFactory(SharedPreferences sharedPreferences){
        // получение способа хранения
        String str = sharedPreferences.getString("save_file", "local_file");
        if (str.equals("local_file")){
            // получение локального файла
            factoryFile = new LocalFile(this);
        } else if (str.equals("DB")){
            // получение базы данных
            factoryFile = new DataBaseFile(getApplicationContext(), this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // обработка ответа пользователя на запрос разрешения
        switch (requestCode) { // код запроса
            case PERMISSION_WRITE_EXTERNAL_STORAGE:  //TODO
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_layout);
                    //создаем Snackbar
                    Snackbar snackbar = Snackbar.make(layout, "Ваши данные не будут сохраняться", Snackbar.LENGTH_LONG);
                    // показываем Snackbar
                    snackbar.show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // если пользователь выбирал и выбрал картинку
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // получаем Uri
                 Uri selectedImageURI = data.getData();
                // переводим полученных Uri в реальный Uri
                File imageFile = new File(getRealPathFromURI(this, selectedImageURI));
                selectedImageURI = Uri.fromFile(imageFile);
                // отображаем картинку
                charFragment.setImage(selectedImageURI);

            }

        }
    }

    // метод переводящий полученную Uri в реальный Uri
    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


}