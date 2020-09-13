package com.opteamizer.android.skinlesionclassification;

import android.app.Activity;
import android.os.Bundle;

/** Main {@code Activity} class for the Camera app. */
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      getFragmentManager()
            .beginTransaction()
            .replace(R.id.container, HomeFragment.newInstance())
            .commit();
  }
  }
}
