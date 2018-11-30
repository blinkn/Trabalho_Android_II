package hencke.com.br.mathgame;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Jogador.class, Score.class}, version = 1)
public abstract class DatabaseStore extends RoomDatabase {

    public abstract JogadorDao getJogadorDao();
    public abstract ScoreDao getScoreDao();

    private static DatabaseStore instance = null;

    public static DatabaseStore getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseStore.class, "mathgame.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
