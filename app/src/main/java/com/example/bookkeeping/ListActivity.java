package com.example.bookkeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.litepal.tablemanager.Connector;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private ListFragment listFragment;
    private int type;
    private String typeDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        type = getIntent().getIntExtra("type", -1); // 接收intent数据
        typeDesc = getIntent().getStringExtra("typeDesc");
        createDatabase();
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.frag_list);
        ImageView addButton = (ImageView) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddDialogActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("typeDesc", typeDesc);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "ListActivity --> onActivityResult: ");
                    // 通信fragment调用fragment方法
                    listFragment.refreshAdapter(ListActivity.this);
                }
                break;
            default:
        }
    }

    public void createDatabase() {
        Connector.getDatabase();
    }
}
