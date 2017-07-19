package com.example.vitor.driverforme_motorista.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.entidades.Motorista;
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_motorista.logica.Base64Custom;
import com.example.vitor.driverforme_motorista.logica.MotoristaLogica;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Cadastro extends AppCompatActivity {

    private EditText fdNome, fdEmail, fdSenha, fdPais, fdEstado, fdCidade, fdBairro, fdRua, fdTelefone, fdCpf, fdCnh;
    private Button btCadastro;
    //classe que realiza a autenticação e cadastro do usuário no firebase
    private FirebaseAuth firebaseAuth;
    //classe que permite a manipulação do banco de dados em tempo real do firebase, é a referência do banco de dados (óbvio pelo nome_
    private DatabaseReference referenciaMotorista = FirebaseEstatico.getFirebase().child("motoristas");
    private Motorista motorista = new Motorista();
    private Base64Custom codificador;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private MotoristaEstatico me;
    //criando query para pesquisar um cliente na hora da edição
    private Query qrMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        fdNome = (EditText) findViewById(R.id.fdNome);
        fdEmail = (EditText) findViewById(R.id.fdEmail);
        fdSenha = (EditText) findViewById(R.id.fdSenha);
        fdPais = (EditText) findViewById(R.id.fdPais);
        fdEstado = (EditText) findViewById(R.id.fdEstado);
        fdCidade = (EditText) findViewById(R.id.fdCidade);
        fdBairro = (EditText) findViewById(R.id.fdBairro);
        fdRua = (EditText) findViewById(R.id.fdRua);
        fdTelefone = (EditText) findViewById(R.id.fdTelefone);
        fdCpf = (EditText) findViewById(R.id.fdCpf);
        fdCnh = (EditText) findViewById(R.id.fdCnh);

        btCadastro = (Button) findViewById(R.id.btCadastro);

        codificador = new Base64Custom();

        me = new MotoristaEstatico();


        //criação das mascaras e dos escutadores
        SimpleMaskFormatter mascaraTelefone = new SimpleMaskFormatter("+NN (NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(fdTelefone, mascaraTelefone);

        SimpleMaskFormatter mascaraCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskCpf = new MaskTextWatcher(fdCpf, mascaraCpf);

        SimpleMaskFormatter mascaraCartao = new SimpleMaskFormatter("NNNNNNNNNNN");
        MaskTextWatcher maskCnh = new MaskTextWatcher(fdCnh, mascaraCartao);

        //adicionando os escutadores criados nas fields

        fdTelefone.addTextChangedListener(maskTelefone);
        fdCpf.addTextChangedListener(maskCpf);
        fdCnh.addTextChangedListener(maskCnh);

        Bundle extras = getIntent().getExtras();


        if(extras!=null) {

            btCadastro.setText("CADASTRO");
            //recupera a instancia estática criando no FirebaseEstatico do tipo FirebaseAuth.getInstance
            firebaseAuth = FirebaseEstatico.getFirebaseAutenticacao();
            //adicionando escutador no botao de cadastro
            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MotoristaLogica cl = new MotoristaLogica();
                    String erro = "";
                    erro += cl.validaNome(fdNome.getText().toString());
                    erro += cl.validaEmail(fdEmail.getText().toString());
                    erro += cl.validaSenha(fdSenha.getText().toString());
                    erro += cl.validaTelefone(fdTelefone.getText().toString());
                    erro += cl.validaCpf(fdCpf.getText().toString());
                    erro += cl.validaCnh(fdCnh.getText().toString());

                    motorista.setNome(fdNome.getText().toString());
                    motorista.setEmail(fdEmail.getText().toString());
                    motorista.setSenha(fdSenha.getText().toString());
                    motorista.setPais(fdPais.getText().toString());
                    motorista.setEstado(fdEstado.getText().toString());
                    motorista.setCidade(fdCidade.getText().toString());
                    motorista.setBairro(fdBairro.getText().toString());
                    motorista.setRua(fdRua.getText().toString());
                    motorista.setTelefone(fdTelefone.getText().toString());
                    motorista.setCpf(fdCpf.getText().toString());
                    motorista.setCnh(fdCnh.getText().toString());
                    motorista.setAvaliacao(10);

                    if(erro.equals("")) {
                        builder = new AlertDialog.Builder(Cadastro.this);
                        builder.setTitle("Cadastro");
                        builder.setMessage("Cadastrando...");
                        dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        cadastraMotorista(motorista.getEmail(), motorista.getSenha());
                    }
                    else{
                        Toast.makeText(Cadastro.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            qrMotorista = referenciaMotorista.child(codificador.codificar(me.getMotorista().getEmail()));
            final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
            btCadastro.setText("CONFIRMAR");
            qrMotorista.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    motorista = dataSnapshot.getValue(Motorista.class);
                    Log.i("Cliente", motorista.toString());
                    fdNome.setText(motorista.getNome());
                    fdEmail.setText(usuario.getEmail());
                    fdEmail.setEnabled(false);
                    fdSenha.setText(motorista.getSenha());
                    fdPais.setText(motorista.getPais());
                    fdEstado.setText(motorista.getEstado());
                    fdCidade.setText(motorista.getCidade());
                    fdBairro.setText(motorista.getBairro());
                    fdRua.setText(motorista.getRua());
                    fdTelefone.setText(motorista.getTelefone());
                    fdCpf.setText(motorista.getCpf());
                    fdCnh.setText(motorista.getCnh());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MotoristaLogica ml = new MotoristaLogica();
                    String erro = "";
                    erro += ml.validaNome(fdNome.getText().toString());
                    erro += ml.validaEmail(fdEmail.getText().toString());
                    erro += ml.validaSenha(fdSenha.getText().toString());
                    erro += ml.validaTelefone(fdTelefone.getText().toString());
                    erro += ml.validaCpf(fdCpf.getText().toString());
                    erro += ml.validaCnh(fdCnh.getText().toString());

                    if (erro.equals("")){
                    builder = new AlertDialog.Builder(Cadastro.this);
                    builder.setTitle("Edição");
                    builder.setMessage("Editando...");
                    dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    motorista.setNome(fdNome.getText().toString());
                    motorista.setEmail(fdEmail.getText().toString());
                    motorista.setSenha(fdSenha.getText().toString());
                    motorista.setPais(fdPais.getText().toString());
                    motorista.setEstado(fdEstado.getText().toString());
                    motorista.setCidade(fdCidade.getText().toString());
                    motorista.setBairro(fdBairro.getText().toString());
                    motorista.setRua(fdRua.getText().toString());
                    motorista.setTelefone(fdTelefone.getText().toString());
                    motorista.setCpf(fdCpf.getText().toString());
                    motorista.setCnh(fdCnh.getText().toString());
                    referenciaMotorista.child(codificador.codificar(me.getMotorista().getEmail())).setValue(motorista);

                    usuario.updatePassword(fdSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Cadastro.this, "Perfil editado com sucesso", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Cadastro.this, TelaInicial.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.cancel();
                                Toast.makeText(Cadastro.this, "Erro ao editar usuário", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                        Toast.makeText(Cadastro.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });
            Toast.makeText(Cadastro.this, "Perfil em edição", Toast.LENGTH_LONG).show();
        }



    }


    //cadastrando o usuário no firebase com email e senha
    public void cadastraMotorista(String email, String senha){
        try{
            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Cadastro.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();
                        referenciaMotorista.child(codificador.codificar(motorista.getEmail())).setValue(motorista);
                        firebaseAuth.signOut();
                        Intent intent = new Intent(Cadastro.this, TelaLogin.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        dialog.cancel();
                        Toast.makeText(Cadastro.this, "Erro ao cadastrar usuário", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }catch(Exception e){
            dialog.cancel();
            Log.i("Excecao", "Entrou no exception");
            Toast.makeText(Cadastro.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
