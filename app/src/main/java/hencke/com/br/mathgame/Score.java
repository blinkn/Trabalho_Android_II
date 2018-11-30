package hencke.com.br.mathgame;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "score")
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int sequencia;

    private String jogador;
    private int score;

    public Score() {
    }

    public Score(String jogador, int score) {
        this.jogador = jogador;
        this.score = score;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
