package com.example.lbw;

import android.os.Bundle;

import com.example.lbw.MyLibraries.CommonRoutines;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }


    private void openFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.flFrame, fragment);

        //add to back stack
        ft.addToBackStack(null);

        //commit
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {

            case R.id.openTakeVideoFragment:

                //check user have camera
                if (CommonRoutines.cameraHardwareExists(this))
                {
                    openFragment(new TakeVideoFragment());
                }
                else
                {
                    CommonRoutines.displayMessage(this, fab, "Error, this phone does not have a camera", Snackbar.LENGTH_LONG, Toast.LENGTH_LONG);
                }
                break;

            case R.id.openWatchVideoFragment:
                openFragment(new ViewVideoFragment());
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fab)
        {
            CommonRoutines.displayMessage(null, fab, "Testing purpose", Snackbar.LENGTH_SHORT, 0);
        }

    }
}
