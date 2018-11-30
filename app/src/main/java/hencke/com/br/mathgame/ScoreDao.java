package hencke.com.br.mathgame;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {

    @Query("Select * from score order by score desc")
    List<Score> fetchScores();

    @Insert
    void insert(Score score);

}
