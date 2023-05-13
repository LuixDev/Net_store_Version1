package com.rysolf.netstore.dialogos;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.widget.AppCompatImageView;


import android.widget.EditText;
import android.widget.Toast;

import com.rysolf.netstore.R;





import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class DialogFragment extends androidx.fragment.app.DialogFragment {



    AppCompatImageView btnSalir,btnEnviar;
    Session session;

    String correo;
    String contraseña;
    EditText report;
    public DialogFragment() {
        // Required empty public constructor
    }



    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return DialogReport();
    }

    private Dialog DialogReport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog, null);
        btnSalir=v.findViewById(R.id.cancelar);
        report=v.findViewById(R.id.report);
        correo="netstore370@gmail.com";
        contraseña="admin123*";
        btnEnviar=v.findViewById(R.id.enviar);
        builder.setView(v);
        eventosBotones();

        return builder.create();
    }

    private void eventosBotones() {

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Properties properties = new Properties();
                properties.put("mail.smtp.host","smtp.googlemail.com");
                properties.put("mail.smtp.socketFactory.port","465");
                properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.port","465");
                try{
                    session=Session.getDefaultInstance(properties, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication(correo,contraseña);
                        }
                    });
                    if(session!=null){
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(correo));
                        message.setSubject("Reporte de usuario");
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("netstore113@gmail.com"));
                        message.setContent(report.getText().toString(),"text/html; charset=utf-8");
                        Transport.send(message);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }


                Toast.makeText(getActivity(), "Enviado", Toast.LENGTH_SHORT).show();
                dismiss();
                
            }

            
        });



        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }

        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }
}