package com.example.aluno.getre.service.usuario_service;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.aluno.getre.model.Usuario;
import com.example.aluno.getre.service.dao_json.Usuario_DaoJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;


/**
 * Created by Anderson2 on 15/11/2017.
 */

public class FindLoginSenhaThread extends AsyncTask<String, Void, Usuario> {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Usuario doInBackground(String... strings) {
        String urll = "https://service.davesmartins.com.br/api/usuarios/login";

        HttpURLConnection conn = null;
        Usuario u = null;
        try {

            URL url = new URL(urll);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String jsonStr ="{\"login\":\""+ strings[0] +"\",\"senha\":\""+ strings[1] + "\"}";

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(jsonStr.length()));
            conn.getOutputStream().write(jsonStr.getBytes("UTF-8"));

            InputStream in = new BufferedInputStream(conn.getInputStream());

            StringBuilder sb = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {

                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
                String linha = sb.toString();
                JSONObject objJson = new JSONObject(linha);
                if(!objJson.get("tipo").toString().isEmpty()){
                    u = new Usuario_DaoJson().MontaUsuario(objJson);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u;
    }
}
