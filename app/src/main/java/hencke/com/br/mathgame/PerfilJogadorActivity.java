package hencke.com.br.mathgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class PerfilJogadorActivity extends AppCompatActivity {

    private ImageView imageviewFotoJogador;
    private Button buttonContinuar, buttonVoltar;
    private File file;
    private Uri outputDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_jogador);
        setTitle("Perfil");

        buttonContinuar = findViewById(R.id.button_continuar);
        buttonVoltar = findViewById(R.id.button_voltar);
        imageviewFotoJogador = findViewById(R.id.imageview_foto_jogador);

        Jogador jogador = DatabaseStore.getInstance(PerfilJogadorActivity.this).getJogadorDao().fetchJogador();
        if (jogador != null) {
            buttonContinuar.setVisibility(View.GONE);
            buttonVoltar.setVisibility(View.VISIBLE);
            EditText nome = findViewById(R.id.edit_text_nome_jogador);
            nome.setText(jogador.getNome());
            if (!jogador.getFoto().isEmpty()) {
                file = new File(jogador.getFoto());
                renderFoto();
            }
        }


        imageviewFotoJogador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "photo.jpg");
                outputDir = FileProvider.getUriForFile(PerfilJogadorActivity.this,
                        BuildConfig.APPLICATION_ID, file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputDir);

                startActivityForResult(
                        intent,
                        1_000);
            }
        });


        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nome = findViewById(R.id.edit_text_nome_jogador);
                String nomeJogador = nome.getText().toString();
                if (nomeJogador.length() == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PerfilJogadorActivity.this);
                    alert.setTitle("Atenção")
                            .setMessage("Informe o seu Nome!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else {
                    String foto = file == null ? "" : file.getPath();
                    Intent intent = new Intent(PerfilJogadorActivity.this, GameActivity.class);
                    gravarJogador(nomeJogador, foto, intent);
                }
            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nome = findViewById(R.id.edit_text_nome_jogador);
                String nomeJogador = nome.getText().toString();
                if (nomeJogador.length() == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PerfilJogadorActivity.this);
                    alert.setTitle("Atenção")
                            .setMessage("Informe o seu Nome!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else {
                    String foto = file == null ? "" : file.getPath();
                    gravarJogador(nomeJogador, foto, null);
                }
            }
        });


    }

    private void gravarJogador(final String nomeJogador, final String foto, final Intent intent) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Jogador jogador = new Jogador(1, nomeJogador, foto);
                DatabaseStore.getInstance(PerfilJogadorActivity.this).getJogadorDao().insert(jogador);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(intent != null){
                    startActivity(intent);
                }
                Toast.makeText(PerfilJogadorActivity.this, "aqui", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (file != null) {
            outState.putString("file", file.getPath());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1_000) {
            if (data != null && data.hasExtra("data")) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                imageviewFotoJogador.setImageBitmap(thumbnail);
            } else {
                renderFoto();
            }
        }
    }

    private void renderFoto() {
        int width = 120;
        int height = 120;

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        int scaleFactor = Math.min(imageWidth / width,
                imageHeight / height);

        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;

        Bitmap image = BitmapFactory.decodeFile(file.getPath(),
                factoryOptions);
        imageviewFotoJogador.setImageBitmap(image);
    }
}
