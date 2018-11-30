package hencke.com.br.mathgame;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class GameActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private TextView conta, pontuacaoTela, tempo;
    private Button r1, r2, r3, r4;
    private int respostaCorreta = 0;
    private int pontos = 0;
    Jogador jogador;

    private AsyncTask<Void, Void, Void> preTimer, timer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        conta = findViewById(R.id.text_view_conta);
        r1 = findViewById(R.id.buttonResposta1);
        r2 = findViewById(R.id.buttonResposta2);
        r3 = findViewById(R.id.buttonResposta3);
        r4 = findViewById(R.id.buttonResposta4);
        pontuacaoTela = findViewById(R.id.textview_pontos);
        progressBar = findViewById(R.id.progress_bar);
        tempo = findViewById(R.id.textview_tempo);
        getJogador();
        addClickListeners();
        initPreTimer();
    }

    private void getJogador() {
        AsyncTask task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                jogador = DatabaseStore.getInstance(GameActivity.this).getJogadorDao().fetchJogador();
                return null;
            }
        }.execute();
    }

    private void initPreTimer(){
        final int[] preTimerSecs = {3};

        preTimer = new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                conta.setText(preTimerSecs[0]+"");
                r1.setEnabled(false);
                r2.setEnabled(false);
                r3.setEnabled(false);
                r4.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                while(preTimerSecs[0] > 0){
                    try {
                        Thread.sleep(1000);
                        preTimerSecs[0]--;
                        publishProgress();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... voids) {
                conta.setText(preTimerSecs[0]+"");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startGame();
            }
        };
        preTimer.execute();
    }

    private void startGame() {
        generateRandomExpression();

        final int[] time = {30};
        tempo.setText("30s");
        timer = new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                r1.setEnabled(true);
                r2.setEnabled(true);
                r3.setEnabled(true);
                r4.setEnabled(true);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    while(time[0] > 0) {
                        Thread.sleep(1000);
                        time[0]--;
                        publishProgress();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                progressBar.setProgress(Math.round(time[0] * 100) / 30);
                tempo.setText(String.format("%ss", time[0]));
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                conta.setText("Acabou o Tempo!");
                gravaScore();
                r1.setEnabled(false);
                r2.setEnabled(false);
                r3.setEnabled(false);
                r4.setEnabled(false);
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.execute();
    }

    private void gravaScore() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseStore.getInstance(GameActivity.this).getScoreDao().insert(new Score(jogador.getNome(), pontos));
                return null;
            }
        }.execute();
    }

    private void generateRandomExpression() {

        int numero1, numero2;
        Random rand = new Random();
        numero1 = rand.nextInt(20-1)+1;
        numero2 = rand.nextInt(20-1)+1;
        respostaCorreta = numero1 + numero2;
        conta.setText(String.format("%d+%d", numero1, numero2));
        r1.setText("");
        r2.setText("");
        r3.setText("");
        r4.setText("");

        int bRespostaCorreta = rand.nextInt(5-1)+1;
        switch (bRespostaCorreta) {
            case 1:
                r1.setText(respostaCorreta+"");
                r2.setText((respostaCorreta+2)+"");
                r3.setText((respostaCorreta+1)+"");
                r4.setText((respostaCorreta-1)+"");
                break;
            case 2:
                r1.setText((respostaCorreta+2)+"");
                r2.setText(respostaCorreta+"");
                r3.setText((respostaCorreta-1)+"");
                r4.setText((respostaCorreta+1)+"");
                break;
            case 3:
                r1.setText((respostaCorreta-1)+"");
                r2.setText((respostaCorreta+1)+"");
                r3.setText(respostaCorreta+"");
                r4.setText((respostaCorreta+2)+"");
                break;
            case 4:
                r1.setText((respostaCorreta+2)+"");
                r2.setText((respostaCorreta-1)+"");
                r3.setText((respostaCorreta+1)+"");
                r4.setText(respostaCorreta+"");
                break;

        }

    }

    private void addClickListeners() {
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = r1.getText().toString();
                if(Integer.valueOf(resposta) == respostaCorreta){
                    pontos += 1;
                }else{
                    pontos = pontos -1 < 0 ? 0 : pontos-1;
                }
                pontuacaoTela.setText(pontos+"");
                generateRandomExpression();
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = r2.getText().toString();
                if(Integer.valueOf(resposta) == respostaCorreta){
                    pontos += 1;
                }else{
                    pontos = pontos -1 < 0 ? 0 : pontos-1;
                }
                pontuacaoTela.setText(pontos+"");
                generateRandomExpression();
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = r3.getText().toString();
                if(Integer.valueOf(resposta) == respostaCorreta){
                    pontos += 1;
                }else{
                    pontos = pontos -1 < 0 ? 0 : pontos-1;
                }
                pontuacaoTela.setText(pontos+"");
                generateRandomExpression();
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = r4.getText().toString();
                if(Integer.valueOf(resposta) == respostaCorreta){
                    pontos += 1;
                }else{
                    pontos = pontos -1 < 0 ? 0 : pontos-1;
                }
                pontuacaoTela.setText(pontos+"");
                generateRandomExpression();
            }
        });

    }


    @Override
    protected void onDestroy() {
        if(preTimer != null) {
            preTimer.cancel(true);
        }
        if(timer != null) {
            timer.cancel(true);
        }
        super.onDestroy();
    }
}
