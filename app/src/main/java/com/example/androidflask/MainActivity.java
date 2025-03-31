package com.example.androidflask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectServer(View v) {
        EditText ipv4AddressView = findViewById(R.id.IPAddress);
        String ipv4Address = ipv4AddressView.getText().toString();
        EditText portNumberView = findViewById(R.id.portNumber);
        String portNumber = portNumberView.getText().toString();

        // Use 10.0.2.2 for Android Emulator
        if (ipv4Address.isEmpty()) {
            ipv4Address = "10.0.2.2"; // Default for emulator
        }

        String postUrl = "http://" + ipv4Address + ":" + portNumber + "/";
        String postBodyText = "Hello";
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        RequestBody postBody = RequestBody.create(mediaType, postBodyText);


        postRequest(postUrl, postBody);
    }

    private void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // Log the error in Logcat for debugging
                String errorMessage = e.getMessage(); // Get error details

                runOnUiThread(() -> {
                    TextView responseText = findViewById(R.id.responseText);
                    responseText.setText("Connection failed: " + errorMessage);
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    TextView responseText = findViewById(R.id.responseText);
                    try {
                        responseText.setText(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}