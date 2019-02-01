package com.rayworks.quickreturnsample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rayworks.quickreturnsample.toolbox.QuickReturnAdapter;
import com.rayworks.quickreturnsample.toolbox.ViewHolder;

import java.util.Arrays;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button continueBtn = findViewById(R.id.dialogue_continue_button);
        continueBtn.setText(R.string.next);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(this,
                Arrays.asList(getString(R.string.large_text)));

        adapter.setNavigationListener(new QuickReturnAdapter.NavigationListener() {
            @Override
            public void onNext() {
                next();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void next() {
        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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

    class SimpleAdapter extends QuickReturnAdapter<String> {
        public SimpleAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        protected void onBindItemView(ViewHolder holder, int position) {
            TextView textView = holder.itemView.findViewById(R.id.text);
            if (textView != null) {
                textView.setText(getItem(position));
            }
        }

        @Override
        public int getItemLayout() {
            return R.layout.layout_view_item;
        }
    }
}
