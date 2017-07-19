package com.example.vitor.driverforme_motorista.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.entidades.Motorista;
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_motorista.logica.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class TelaLogin extends AppCompatActivity {

    //FirebaseAuth classe que realiza a autenticação do login
    private FirebaseAuth firebaseAuth;
    private Button btCadastro, btLogin;
    private EditText fdEmail, fdSenha;
    //o builder e dialog são fundamentais para bloquear as ações do usuario enquanto o sistema realiza a autenticação (é a caixinha que aparece com titulo "Login" e corpo "Logando"
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    //referencia do banco de dados do cliente
    private DatabaseReference motoristaReferencia;
    private MotoristaEstatico motoristaEstatico;
    private ValueEventListener valueEventListenerUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        btCadastro = (Button) findViewById(R.id.btCadastro);
        btLogin = (Button) findViewById(R.id.btLogin);

        fdEmail = (EditText) findViewById(R.id.fdEmail);
        fdSenha = (EditText) findViewById(R.id.fdSenha);

        motoristaEstatico = new MotoristaEstatico();

        firebaseAuth = FirebaseEstatico.getFirebaseAutenticacao();
        motoristaReferencia = FirebaseEstatico.getFirebase().child("motoristas");
        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, Cadastro.class);
                intent.putExtra("operacao", "cadastro");
                startActivity(intent);

            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(TelaLogin.this);
                builder.setTitle("Login");
                builder.setMessage("Logando...");
                dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                motoristaReferencia.child(Base64Custom.codificar(fdEmail.getText().toString()));

                login(fdEmail.getText().toString(), fdSenha.getText().toString());
            }
        });

    }

    //classe que realiza a autenticação através de email e senha
    public void login(String email, String senha) {
        try {

            firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(TelaLogin.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Motorista m = new Motorista();
                        m.setEmail(fdEmail.getText().toString());
                        m.setSenha(fdSenha.getText().toString());
                        motoristaEstatico.setMotorista(m);
                        dialog.cancel();
                        Toast.makeText(TelaLogin.this, "Sucesso ao realizar login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TelaLogin.this, TelaInicial.class);
                        startActivity(intent);

                    } else {
                        dialog.cancel();
                        Toast.makeText(TelaLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            dialog.cancel();
            Toast.makeText(TelaLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
