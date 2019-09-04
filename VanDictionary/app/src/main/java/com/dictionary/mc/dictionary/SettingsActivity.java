package com.dictionary.mc.dictionary;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        TextView clearHistory=(TextView) findViewById(R.id.clear_history);

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                myDbHelper = new DatabaseHelper(SettingsActivity.this);
                try {
                    myDbHelper.openDataBase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                showAlertDialog();
                //showAbout();
            }
        });

        TextView about =(TextView) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                myDbHelper = new DatabaseHelper(SettingsActivity.this);
                try {
                    myDbHelper.openDataBase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                showAbout();
            }
        });
    }


    private void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Are you sure?");
        builder.setMessage("All the history will be deleted");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDbHelper.deleteHistory();
                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
    private void showAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Developed and managed by Chin Community of USA");
        builder.setMessage("© Lailun Foundation - Mirang-Lai\n" +
                "For more information please visit Chin Community of USA\n" +
                "www.chincommunityusa.org");

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
