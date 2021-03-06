package com.example.aluno.getre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.getre.model.Administrador;
import com.example.aluno.getre.model.Cliente;
import com.example.aluno.getre.model.Motorista;
import com.example.aluno.getre.model.Usuario;
import com.example.aluno.getre.model.enums.ECrud;
import com.example.aluno.getre.model.enums.ETipoUsuario;
import com.example.aluno.getre.service.usuario_service.AllUsuariosThread;
import com.example.aluno.getre.service.usuario_service.FindLoginSenhaThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    Button btnLogar, btnCadastrar;
    TextView edtLogin, edtSenha;
    final int CAD_VIEW_USUARIO = 1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            Toast.makeText(getApplicationContext(), "Usuario cadastrado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BindFields();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login, senha;
                login = edtLogin.getText().toString();
                senha = edtSenha.getText().toString();
                Usuario u = null;


                try {
                    u = new FindLoginSenhaThread().execute(login, senha).get();
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), "Falha ao Logar \r\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    Toast.makeText(getApplicationContext(), "Falha ao Logar \r\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

//                try {
//                    List<Usuario> lstUsuarios = new AllUsuariosThread().execute().get();
//
//                    for(Usuario user : lstUsuarios){
//                        if(user.getLogin().equals(login)
//                                && user.getSenha().equals(senha)){
//                            u = user;
//                        }
//                    }
//
//                } catch (InterruptedException e) {
//                    Toast.makeText(getApplicationContext(), "Falha ao Logar \r\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                } catch (ExecutionException e) {
//                    Toast.makeText(getApplicationContext(), "Falha ao Logar \r\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }

                if(u == null){
                    edtSenha.setText("");
                    Toast.makeText(getApplicationContext(), "Login ou Senha Incorreta!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent itn = new Intent();
                    itn.putExtra("usuario", u);
                    if(u instanceof Administrador){
                        setResult(4, itn);
                    }else if(u instanceof Motorista){
                        setResult(3, itn);
                    }else{
                        setResult(2, itn);
                    }
                    finish();
                }
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itn = new Intent(getApplicationContext(), CadViewUsuarioActivity.class);
                itn.putExtra("tipoUsuario", ETipoUsuario.Cliente);
                itn.putExtra("op", ECrud.create);
                startActivityForResult(itn, CAD_VIEW_USUARIO);
            }
        });




    }

    private void BindFields() {
        btnLogar = (Button) findViewById(R.id.frmLogin_btnLogar);
        btnCadastrar = (Button) findViewById(R.id.frmLogin_btnCadastra);

        edtLogin = (TextView) findViewById(R.id.frmLogin_edtLogin);
        edtSenha = (TextView) findViewById(R.id.frmLogin_edtSenha);
    }
}
