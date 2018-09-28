package com.example.administrator.loancalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;

public class CalculatorActivity extends AppCompatActivity {
    public SharedPreferences sp;
    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // When initialise activity then initialise all this variable
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        etLoanAmount=(EditText) findViewById(R.id.loan_amount);
        etDownPayment=(EditText) findViewById(R.id.down_payment);
        etTerm=(EditText) findViewById(R.id.term);
        etAnnualInterestRate=(EditText) findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment=(TextView) findViewById(R.id.monthly_repayment);
        tvTotalRepayment=(TextView) findViewById(R.id.total_repayment);
        tvTotalInterest=(TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest=(TextView) findViewById(R.id.average_monthly_interest);

        sp = getSharedPreferences("loanhistory", Context.MODE_PRIVATE);
    }

    public void onClick(View v){ // To control what is the button is used for.
        switch (v.getId()){
            case R.id.button_calculate:
                calculate();
                Log.d("Check","Button Clicked!");
                break;

            case R.id.button_reset:
                reset();
                Log.d("Check","Button Clicked!");
                break;
        }
    }

    private void calculate (){ // Calculation algorithm for Calculate button
        String amount=etLoanAmount.getText().toString();
        String downPayment=etDownPayment.getText().toString();
        String interestRate=etAnnualInterestRate.getText().toString();
        String term=etTerm.getText().toString();

        double loanAmount=Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest=Double.parseDouble(interestRate)/12/100;
        double noOfMonth=(Integer.parseInt(term)*12);

        if (noOfMonth > 0){
            double monthlyRepayment = loanAmount * (interest+(interest/(java.lang.Math.pow((1+interest),noOfMonth)-1)));
            double totalRepayment=monthlyRepayment*noOfMonth;
            double totalInterest=totalRepayment-loanAmount;
            double monthlyInterest=totalInterest/noOfMonth;

            tvMonthlyPayment.setText(String.valueOf(monthlyRepayment));
            tvTotalRepayment.setText(String.valueOf(totalRepayment));
            tvTotalInterest.setText(String.valueOf(totalInterest));
            tvAverageMonthlyInterest.setText(String.valueOf(monthlyInterest));

            sp.edit() // Save data to storage as file.
                    .putString("Loan Amount", amount)
                    .putString("Down Payment", downPayment)
                    .putString("Interest Rate", interestRate)
                    .putString("Term", term)
                    .putString("Monthly Payment", String.valueOf(monthlyRepayment))
                    .putString("Total Repayment", String.valueOf(totalRepayment))
                    .putString("Total Interest", String.valueOf(totalInterest))
                    .putString("Average Monthly Interest", String.valueOf(monthlyInterest))
                    .commit();
        }
    }

    private void reset(){ // Reset to default value for reset button
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }

    @Override
    protected void onResume() { // This onResume is using parent onResume.
        super.onResume();

        if (sp != null){
            etLoanAmount.setText(sp.getString("Loan Amount",""));
            etDownPayment.setText(sp.getString("Down Payment", ""));
            etAnnualInterestRate.setText(sp.getString("Interest Rate", ""));
            etTerm.setText(sp.getString("Term", ""));

            tvMonthlyPayment.setText(sp.getString("Monthly Payment", "0.00"));
            tvTotalRepayment.setText(sp.getString("Total Repayment", "0.00"));
            tvTotalInterest.setText(sp.getString("Total Interest", "0.00"));
            tvAverageMonthlyInterest.setText(sp.getString("Average Monthly Interest", "0.00"));
        }
    }
}

