package hencke.com.br.mathgame;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder>{


    private List<Score> scoreList = new ArrayList<Score>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(
                android.R.layout.simple_list_item_2,
                viewGroup,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Score score = scoreList.get(i);
        viewHolder.jogadorNome.setText(score.getJogador());
        viewHolder.pontos.setText(score.getScore()+"");
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }


    public void setup(List<Score> scores) {
        this.scoreList.clear();
        this.scoreList.addAll(scores);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView jogadorNome;
        TextView pontos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jogadorNome = itemView.findViewById(android.R.id.text1);
            pontos = itemView.findViewById(android.R.id.text2);
        }
    }
}
