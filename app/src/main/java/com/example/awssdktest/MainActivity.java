package com.example.awssdktest;

import android.os.Bundle;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private dynamoDBTest testTable;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Amazon Cognito 認証情報プロバイダーを初期化します
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:fad1ebbd-cafa-4ecc-91a0-07df8f5d6567", // ID プールの ID
                Regions.AP_NORTHEAST_1 // リージョン
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        Region apNortheast1 = Region.getRegion(Regions.AP_NORTHEAST_1);
        ddbClient.setRegion(apNortheast1);

        final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);



        /*
        runnable = new Runnable() {
            public void run() {
                dynamoDBTest testTable = new dynamoDBTest();
                testTable.setID(3);
                testTable.setPrice(20000);
                System.out.println(testTable);
                mapper.save(testTable);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        */

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                dynamoDBTest testTable = new dynamoDBTest();
                testTable.setID(3);
                testTable.setPrice(20000);
                System.out.println(testTable);
                mapper.save(testTable);
                */
                // 全レコード取得方法
                List<dynamoDBTest> newsItem = mapper.scan(dynamoDBTest.class, new DynamoDBScanExpression());

                // Item read
                Log.d("News Item:", newsItem.toString());
                for (dynamoDBTest item : newsItem) {
                    System.out.format("ID=%s, Price=%s %n",
                            item.getID(), item.getPrice());
                }
                dynamoDBTest item = mapper.load(dynamoDBTest.class, 11);
                System.out.println("---------");
                System.out.println(item);
                //item.setPrice(1413240);
                //mapper.save(item);

                // Priceを指定してscanする方法
                Map<String, Condition> conditions = new HashMap<String, Condition>();
                Condition priceCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ.toString())
                        .withAttributeValueList(new AttributeValue().withN(Integer.toString(100)));
                conditions.put("Price", priceCondition);
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.setScanFilter(conditions);
                List<dynamoDBTest> newItems = mapper.scan(dynamoDBTest.class, scanExpression);
                Log.d("News Item:", newsItem.toString());
                for (dynamoDBTest item2 : newItems) {
                    System.out.format("ID=%s, Price=%s %n",
                            item2.getID(), item2.getPrice());
                }
            }
        }).start();
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
