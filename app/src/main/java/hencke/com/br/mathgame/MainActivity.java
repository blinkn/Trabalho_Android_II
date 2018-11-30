package hencke.com.br.mathgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Jogador jogador;
    Button editarPerfilButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jogador = DatabaseStore.getInstance(this).getJogadorDao().fetchJogador();

        Button iniciarJogoButton = findViewById(R.id.button_iniciar_jogo);
        iniciarJogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jogador != null) {
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), PerfilJogadorActivity.class);
                    startActivity(intent);
                }
            }
        });

        editarPerfilButton = findViewById(R.id.button_editar_perfil);
        editarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilJogadorActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        jogador = DatabaseStore.getInstance(this).getJogadorDao().fetchJogador();
        if (jogador != null) {
            editarPerfilButton.setVisibility(View.VISIBLE);
        }
    }
}
