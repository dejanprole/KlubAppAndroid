package com.vozoljubitelji.klubapp;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vozoljubitelji.klubapp.HttpHandler;
import com.vozoljubitelji.klubapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinanceActivity extends AppCompatActivity {

    private static final String URL = "http://klubljubiteljazeleznice-beograd.com/android_app_klub/getallfinance.php";

    private String TAG = MainActivity.class.getSimpleName();
    private ListView listViewIncome;
    private ListView listViewOutcome;
    private ListView listViewMembers;

    private TextView balanceTextView;
    private TextView sumIncomeTextView;
    private TextView sumOutcomeTextView;

    String balance;
    String income;
    String outcome;

    ArrayList<HashMap<String, String>> incomeList;
    ArrayList<HashMap<String, String>> outcomeList;
    ArrayList<HashMap<String, String>> membersList;

    Typeface typefaceLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        typefaceLight = Typeface.createFromAsset(this.getAssets(), "fonts/Dosis-Light.ttf");


        incomeList = new ArrayList<>();
        outcomeList = new ArrayList<>();
        membersList = new ArrayList<>();

        listViewIncome = (ListView) findViewById(R.id.list_income);
        listViewOutcome = (ListView) findViewById(R.id.list_outcome);
        listViewMembers = (ListView) findViewById(R.id.list_members);

        balanceTextView = (TextView) findViewById(R.id.balance);
        sumIncomeTextView = (TextView) findViewById(R.id.sumincome);
        sumOutcomeTextView = (TextView) findViewById(R.id.sumoutcome);





        new GetResults().execute();
    }

    private class GetResults extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(FinanceActivity.this,"Podaci se učitavaju.",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(URL);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray balanceArray = jsonObj.getJSONArray("currentBalance");
                    for (int i = 0; i < balanceArray.length(); i++) {
                        JSONObject balanceArrayJSONObject = balanceArray.getJSONObject(i);
                        balance = balanceArrayJSONObject.getString("balance");
                        Log.i("BALANCE", balance.toString());
                    }

                    JSONArray incomeArray = jsonObj.getJSONArray("incomeSum");
                    for (int i = 0; i < incomeArray.length(); i++) {
                        JSONObject incomeArrayJSONObject = incomeArray.getJSONObject(i);
                        income = incomeArrayJSONObject.getString("income");
                        Log.i("INCOME", income.toString());
                    }

                    JSONArray outcomeArray = jsonObj.getJSONArray("outcomeSum");
                    for (int i = 0; i < outcomeArray.length(); i++) {
                        JSONObject outcomeArrayJSONObject = outcomeArray.getJSONObject(i);
                        outcome = outcomeArrayJSONObject.getString("outcome");
                        Log.i("OUTCOME", outcome.toString());
                    }

                    JSONArray incomePerYear = jsonObj.getJSONArray("income_per_year");
                    for (int i = 0; i < incomePerYear.length(); i++) {
                        JSONObject incomePerYearJSONObject = incomePerYear.getJSONObject(i);
                        String income = incomePerYearJSONObject.getString("income");
                        String year = incomePerYearJSONObject.getString("year");
                        HashMap<String, String> incomeHash = new HashMap<>();
                        incomeHash.put("income", income);
                        incomeHash.put("year", year);
                        incomeList.add(incomeHash);
                    }

                    JSONArray outcomePerYear = jsonObj.getJSONArray("outcome_per_year");
                    for (int i = 0; i < outcomePerYear.length(); i++) {
                        JSONObject outcomePerYearJSONObject = outcomePerYear.getJSONObject(i);
                        String outcome = outcomePerYearJSONObject.getString("outcome");
                        String year = outcomePerYearJSONObject.getString("year");
                        HashMap<String, String> outcomeHash = new HashMap<>();
                        outcomeHash.put("outcome", outcome);
                        outcomeHash.put("year", year);
                        outcomeList.add(outcomeHash);
                    }

                    JSONArray membersPerYear = jsonObj.getJSONArray("members_per_year");
                    for (int i = 0; i < outcomePerYear.length(); i++) {
                        JSONObject membersPerYearJSONObject = membersPerYear.getJSONObject(i);
                        String income = membersPerYearJSONObject.getString("income");
                        String year = membersPerYearJSONObject.getString("year");
                        HashMap<String, String> membersHash = new HashMap<>();
                        membersHash.put("income", income);
                        membersHash.put("year", year);
                        membersList.add(membersHash);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Problem sa parsiranjem Json rezultate: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Problem sa parsiranjem Json rezultate: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Problem sa konekcijom ka serveru.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            balanceTextView.setText(balance);

            sumIncomeTextView.setText(income);

            sumOutcomeTextView.setText(outcome);

            ListAdapter adapter = new SimpleAdapter(FinanceActivity.this, incomeList,
                    R.layout.list_item_finance, new String[]{ "income","year"},
                    new int[]{R.id.income, R.id.year});
            listViewIncome.setAdapter(adapter);

            ListAdapter adapterOutcome = new SimpleAdapter(FinanceActivity.this, outcomeList,
                    R.layout.list_item_finance_outcome, new String[]{ "outcome","year"},
                    new int[]{R.id.outcome, R.id.year});
            listViewOutcome.setAdapter(adapterOutcome);

            ListAdapter adapterMebers = new SimpleAdapter(FinanceActivity.this, membersList,
                    R.layout.list_item_finance, new String[]{ "income","year"},
                    new int[]{R.id.income, R.id.year});
            listViewMembers.setAdapter(adapterMebers);
        }
    }
}