package hencke.com.br.mathgame;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JogadorDao {

    @Query("SELECT * FROM jogador WHERE id = 1")
    Jogador fetchJogador();

    @Query("SELECT * FROM jogador")
    List<Jogador> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Jogador jogador);

}
