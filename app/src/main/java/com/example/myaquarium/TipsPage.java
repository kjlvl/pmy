package com.example.myaquarium;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myaquarium.service.Navigation;
import com.example.myaquarium.service.Requests;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TipsPage extends AppCompatActivity {
    private Requests requests;
    private List<JSONObject> tipsList;
    private LinearLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_page);
        Navigation.setToolbar(
                this,
                getApplicationContext().getString(R.string.tips_text),
                Tips.class
        );
        Navigation.setMenuNavigation(this);


        requests = new Requests();

        layout = findViewById(R.id.layout);
        Bundle arguments = getIntent().getExtras();

        this.getTips(arguments.getString("id"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getTips(String id) {
        tipsList = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>(List.of(
                new BasicNameValuePair("id", id)
        ));
        Runnable runnable = () -> {
            try {
                JSONArray list = requests.setRequest(requests.urlRequest + "tips/view", params);
                JSONObject object = new JSONObject(String.valueOf(list.getJSONObject(0)));
                tipsList.add(object);
                this.runOnUiThread(() -> this.generateTips(id));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateTips(String id) {
        List<String> titles = List.of(tipsList.get(0).optString("titles").split(";"));
        List<String> content = List.of(tipsList.get(0).optString("content").split(";"));
        List<String> images = List.of(tipsList.get(0).optString("images").split(";"));

        int countTitle = 0;
        int countImage = 0;
        for (String item: content) {
            switch (item) {
                case "title": {
                    TextView title = new TextView(this);
                    title.setTextSize(18);
                    countTitle = this.updateTitle(title, countTitle, titles);
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    layout.addView(title);
                    break;
                }
                case "image":
                    ImageView image = new ImageView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            400
                    );
                    image.setLayoutParams(params);
                    String uri = requests.urlRequestImg + images.get(countImage);
                    Picasso.get().load(uri).into(image);
                    image.setOnClickListener(view -> {
                        Intent intent = new Intent(this, ImageViewer.class);
                        intent.putExtra("image",  uri);
                        intent.putExtra("class", "TipsPage");
                        intent.putExtra("id", id);
                        startActivity(intent);
                    });

                    countImage++;
                    layout.addView(image);
                    break;
                case "title1": {
                    TextView title = new TextView(this);
                    title.setTextSize(17);
                    countTitle = this.updateTitle(title, countTitle, titles);
                    layout.addView(title);
                    break;
                }
                case "title2": {
                    TextView title = new TextView(this);
                    title.setTextSize(15);
                    countTitle = this.updateTitle(title, countTitle, titles);
                    layout.addView(title);
                    break;
                }
                default:
                    item = item.replace("n", "\n\n");
                    item = item.replace("**", "\n● ");
                    TextView contentText = new TextView(this, null);
                    contentText.setTextSize(15);
                    contentText.setText(item);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentText.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                    }
                    contentText.setTextColor(getResources().getColor(R.color.black));
                    layout.addView(contentText);
                    break;
            }
        }
    }

    private int updateTitle(TextView title, int countTitle, List<String> titles) {
        title.setText(titles.get(countTitle));
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,10,0,10);
        title.setLayoutParams(params);
        countTitle++;

        return countTitle;
    }
}




