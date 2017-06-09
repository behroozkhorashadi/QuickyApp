package com.khorashadi.quickapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private TextView textView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.msg);
    }

    @Override
    public void onStart() {
        super.onStart();
        compositeDisposable.add(RxView.clicks(button1).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Snackbar.make(findViewById(android.R.id.content), "Have button 1 do something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }));
        compositeDisposable.add(RxView.clicks(button2).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Snackbar.make(findViewById(android.R.id.content), "Have button 2 do something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
