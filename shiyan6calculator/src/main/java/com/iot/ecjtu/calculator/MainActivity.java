package com.iot.ecjtu.calculator;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    StringBuilder mStringBuilder = new StringBuilder();
    private TextView result;
    String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView)findViewById(R.id.resultLabel);

        TextView button0 = (TextView) findViewById(R.id.button_0);
        TextView button1 = (TextView) findViewById(R.id.button_1);
        TextView button2 = (TextView) findViewById(R.id.button_2);
        TextView button3 = (TextView) findViewById(R.id.button_3);
        TextView button4 = (TextView) findViewById(R.id.button_4);
        TextView button5 = (TextView) findViewById(R.id.button_5);
        TextView button6 = (TextView) findViewById(R.id.button_6);
        TextView button7 = (TextView) findViewById(R.id.button_7);
        TextView button8 = (TextView) findViewById(R.id.button_8);
        TextView button9 = (TextView) findViewById(R.id.button_9);
        TextView buttonDot = (TextView) findViewById(R.id.button_dot);
        TextView buttonEqual = (TextView) findViewById(R.id.button_equal);
        TextView buttonDivide = (TextView) findViewById(R.id.button_divide);
        TextView buttonMultiply = (TextView) findViewById(R.id.button_multiply);
        TextView buttonMinus = (TextView) findViewById(R.id.button_minus);
        TextView buttonPlus = (TextView) findViewById(R.id.button_plus);
        TextView buttonDelete = (TextView) findViewById(R.id.button_delete);
        TextView buttonClear = (TextView) findViewById(R.id.button_clear);
        TextView buttonZkh = (TextView) findViewById(R.id.button_zkh);
        TextView buttonYkh = (TextView) findViewById(R.id.button_ykh);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonZkh.setOnClickListener(this);
        buttonYkh.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_0:
                mStringBuilder.append("0");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_1:
                mStringBuilder.append("1");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_2:
                mStringBuilder.append("2");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_3:
                mStringBuilder.append("3");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_4:
                mStringBuilder.append("4");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_5:
                mStringBuilder.append("5");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_6:
                mStringBuilder.append("6");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_7:
                mStringBuilder.append("7");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_8:
                mStringBuilder.append("8");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_9:
                mStringBuilder.append("9");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_dot:
                mStringBuilder.append(".");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_divide:
                mStringBuilder.append("/");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_multiply:
                mStringBuilder.append("*");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_plus:
                mStringBuilder.append("+");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_minus:
                mStringBuilder.append("-");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_zkh:
                mStringBuilder.append("(");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_ykh:
                mStringBuilder.append(")");
                result.setText(mStringBuilder.toString());
                break;
            case R.id.button_delete:
                if (mStringBuilder.length() > 0) {
                    mStringBuilder.deleteCharAt(mStringBuilder.length() - 1);
                    result.setText(mStringBuilder.toString());
                }
                if(mResult.length()>0){
                    mResult = mResult.substring(0,mResult.length()-1);
                    result.setText(mResult);
                }
                break;
            case R.id.button_clear:
                mStringBuilder = new StringBuilder();
                result.setText(null);
                break;
            case R.id.button_equal:
                getResult(mStringBuilder);
                mStringBuilder = new StringBuilder();
                break;

        }
    }

    private String listToString(List<String> list){
        StringBuilder sb =new StringBuilder();
        for(String s : list){
                sb.append(s);
        }
        return sb.toString();
    }

    private void getResult(StringBuilder stringBuilder){
        String inputStatement = stringBuilder.toString();
        char[] chs = inputStatement.toCharArray();
        List<String> list = new ArrayList<String>();
        String value = "";
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] >= '0' && chs[i] <= '9' || chs[i] == '.') {
                value = value + String.valueOf(chs[i]);
                if (i == chs.length - 1) {
                    list.add(value);
                }
            } else {
                if (chs[i] == '(') {
                    list.add("(");
                } else {
                    list.add(value);
                    list.add(String.valueOf(chs[i]));
                }
                value = "";
            }
        }
        while (list.indexOf("") > 0) {
            list.remove(list.indexOf(""));
        }
        if(list.contains("+") || list.contains("-") || list.contains("*") || list.contains("/")) {
            try {
                while (list.indexOf("(") >= 0) {
                    int a = list.lastIndexOf("(");
                    int b = list.indexOf(")");

                    List<String> list2 = new ArrayList<String>();
                    list2 = list.subList(a + 1, b);
                    math2(list2);

                    for (String s : list2) {

                        list.add(a, s);
                    }
                    for (int x = 0; x < 3; x++) {
                        list.remove(a + 1);
                    }
                }
                math2(list);
                for (String s : list) {
                    double resultNum = Double.parseDouble(s);
                    mResult =new Double(resultNum).toString();
                    result.setText(mResult);
                }
            } catch (Exception e) {
                result.setText("错误");
            }
        }
    }

    private List<String> math2(List<String> list) {
        while (list.indexOf("*") > 0 || list.indexOf("/") > 0) {
            int a = list.indexOf("*");
            int b = list.indexOf("/");
            if (a < 0) {
                while (list.indexOf("/") > 0) {
                    double temp = (Double.parseDouble(list.get(b - 1))) / (Double.parseDouble(list.get(b + 1)));
                    math(list, b, temp);
                }
            }
            if (b < 0) {
                while (list.indexOf("*") > 0) {
                    double temp = (Double.parseDouble(list.get(a - 1))) * (Double.parseDouble(list.get(a + 1)));
                    math(list, a, temp);
                }
            }
            if (a > b && b > 0) {
                double temp = (Double.parseDouble(list.get(b - 1))) / (Double.parseDouble(list.get(b + 1)));
                math(list, b, temp);
            }
            if (b > a && a > 0) {
                double temp = (Double.parseDouble(list.get(a - 1))) * (Double.parseDouble(list.get(a + 1)));
                math(list, a, temp);
            }

        }
        while (list.indexOf("+") > 0 || list.indexOf("-") > 0) {
            int a = list.indexOf("+");
            int b = list.indexOf("-");
            if (a < 0) {
                while (list.indexOf("-") > 0) {
                    double temp = (Double.parseDouble(list.get(b - 1))) - (Double.parseDouble(list.get(b + 1)));
                    math(list, b, temp);
                }
            }
            if (b < 0) {
                while (list.indexOf("+") > 0) {
                    double temp = (Double.parseDouble(list.get(a - 1))) + (Double.parseDouble(list.get(a + 1)));
                    math(list, a, temp);
                }
            }
            if (a > b && b > 0) {
                double temp = (Double.parseDouble(list.get(b - 1))) - (Double.parseDouble(list.get(b + 1)));
                math(list, b, temp);
            }
            if (b > a && a > 0) {
                double temp = (Double.parseDouble(list.get(a - 1))) + (Double.parseDouble(list.get(a + 1)));
                math(list, a, temp);
            }
        }
        return list;

    }

    private List<String> math(List<String> list, int x, double temp) {
        list.set(x - 1, String.valueOf(temp));
        list.remove(x);
        list.remove(x);
        return list;
    }

}
