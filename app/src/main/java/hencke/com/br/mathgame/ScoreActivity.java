package hencke.com.br.mathgame;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class ScoreActivity extends AppCompatActivity {

    Button jogarNovamenteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        setTitle("Ranking Geral");

        final ScoreAdapter adapter = new ScoreAdapter();

        RecyclerView taskList = findViewById(R.id.score_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(adapter);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                adapter.setup(DatabaseStore.getInstance(ScoreActivity.this).getScoreDao().fetchScores());
                return null;
            }
        }.execute();


        jogarNovamenteButton = findViewById(R.id.button_jogar_novamente);
        jogarNovamenteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
