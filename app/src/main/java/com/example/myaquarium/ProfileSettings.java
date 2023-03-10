package com.example.myaquarium;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myaquarium.server.Requests;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileSettings extends AppCompatActivity {
    private EditText name;
    private EditText surname;
    private EditText city;
    private EditText phone;
    private EditText login;

    private TextView password;
    private ImageView image;
    private Button save;

    private RelativeLayout root;

    private JSONObject userInfo;

    private Requests requests;
    private Bitmap bitmap;
    private String newAvatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        this.setToolbar();

        requests = new Requests();
        root = findViewById(R.id.root);

        image = this.findViewById(R.id.image);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        city = findViewById(R.id.city);
        phone = findViewById(R.id.phone);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        save = findViewById(R.id.save);

        this.getUser();

        password.setOnClickListener(view -> this.changePassword());
        save.setOnClickListener(view -> this.updateUser());

        TextView download = findViewById(R.id.download);
        download.setOnClickListener(view -> downloadImage());

        TextView calculator = findViewById(R.id.service);
        TextView profile = findViewById(R.id.profile);
        TextView forum = findViewById(R.id.forum);

        calculator.setOnClickListener(view -> this.startActivity(new Intent(this, Service.class)));
        forum.setOnClickListener(view -> this.startActivity(new Intent(this, Forum.class)));
        profile.setOnClickListener(view -> this.startActivity(new Intent(this, Profile.class)));
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = findViewById(R.id.title);
        textView.setText(getApplicationContext().getString(R.string.settings_text));

        toolbar.setNavigationOnClickListener(view -> {
            this.startActivity(new Intent(this, Profile.class));
        });
    }

    private void changePassword() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("?????????????????? ????????????");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registrationWindow = inflater.inflate(R.layout.change_password_window, null);
        dialog.setView(registrationWindow);

        MaterialEditText oldPasswordField = registrationWindow.findViewById(R.id.oldPassword);
        MaterialEditText newPasswordField = registrationWindow.findViewById(R.id.newPassword);

        dialog.setNegativeButton("????????????????", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.setPositiveButton("??????????????????", (dialogInterface, i) -> {
            String oldPassword = oldPasswordField.getText().toString();
            String newPassword = newPasswordField.getText().toString();

            if (TextUtils.isEmpty(oldPassword)) {
                Snackbar.make(root, "?????????????? ?????????????? ????????????", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(newPassword)) {
                Snackbar.make(root, "?????????????? ?????????? ????????????", Snackbar.LENGTH_SHORT).show();
                return;
            }

            List<NameValuePair> params = new ArrayList<>(List.of(
                    new BasicNameValuePair("oldPassword", oldPassword),
                    new BasicNameValuePair("newPassword", newPassword)
                )
            );

            Runnable runnable = () -> {
                try {
                    JSONArray message = requests.setRequest(requests.urlRequest + "user/password", params);
                    JSONObject object = new JSONObject(String.valueOf(message.getJSONObject(0)));
                    if (object.optString("success").equals("1")) {
                        dialogInterface.dismiss();
                        getNotice("???????????? ?????? ?????????????? ??????????????!");
                    } else {
                        dialogInterface.dismiss();
                        getNotice("?????????????? ???????????? ?????????????? ????????????. ?????????????????? ??????????????!");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();

        });
        dialog.show();
    }

    private void getNotice(String message) {
        this.runOnUiThread(() -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileSettings.this);
            dialog.setMessage(message);
            dialog.setPositiveButton("??????????????", (dialogInterface, i) -> dialogInterface.dismiss());
            dialog.show();
        });
    }

    private void getUser() {
        userInfo = requests.getUser();

        name.setText(userInfo.optString("user_name"));
        if (!userInfo.optString("surname").equals("null"))
            surname.setText(userInfo.optString("surname"));
        if (!userInfo.optString("city").equals("null"))
            city.setText(userInfo.optString("city"));
        if (!userInfo.optString("phone").equals("null"))
            phone.setText(userInfo.optString("phone"));
        login.setText(userInfo.optString("login"));

        if (!Objects.equals(userInfo.optString("avatar"), "")) {
            Picasso.get()
                    .load(requests.urlRequestImg + userInfo.optString("avatar"))
                    .into(image);
        } else {
            image.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void updateUser() {
        List<NameValuePair> params = new ArrayList<>(List.of(
                new BasicNameValuePair("avatar", newAvatar),
                new BasicNameValuePair("name", name.getText().toString()),
                new BasicNameValuePair("surname", surname.getText().toString()),
                new BasicNameValuePair("login", login.getText().toString()),
                new BasicNameValuePair("city", city.getText().toString()),
                new BasicNameValuePair("phone", phone.getText().toString())
            )
        );

        Runnable runnable = () -> {
            try {
                JSONArray message = requests.setRequest(requests.urlRequest + "user/update", params);
                JSONObject object = new JSONObject(String.valueOf(message.getJSONObject(0)));
                if (object.optString("success").equals("1")) {
                    root.post(() -> {
                        Toast.makeText(
                                this,
                                "???????????? ???????? ?????????????? ??????????????????!", Toast.LENGTH_SHORT
                        ).show();
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void downloadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    Runnable runnable = () -> {
                        try {
                            this.bitmap = Picasso.get().load(uri).resize(500, 0).get();
                            image.post(() -> {
                                this.generateImage(bitmap);
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();

                    Picasso.get().load(uri).resize(500, 0).into(image);
                }
            });

    private void generateImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            this.newAvatar = Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            this.newAvatar = "";
        }
    }

}