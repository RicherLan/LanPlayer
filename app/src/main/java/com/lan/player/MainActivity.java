package com.lan.player;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.beggar.beggarplayer.core.BeggarPlayerController;
import com.beggar.beggarplayer.core.config.BeggarPlayerConfig;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BeggarPlayerController playerController =
        new BeggarPlayerController(new BeggarPlayerConfig(null));
  }
}