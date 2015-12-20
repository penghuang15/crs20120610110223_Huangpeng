package com.iot.ecjtu.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView inputHint;
    Button btnCalc;
    ImageView imgView;
    Button btnNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.calc_input);
        inputHint = (TextView) findViewById(R.id.input_hint);
        btnCalc = (Button) findViewById(R.id.btn_calc);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStatement = input.getText().toString();
                //Todo: 这里执行计算功能
                String input = inputStatement.replaceAll(" ", "");
                char[] chs = input.toCharArray();
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

                        double result = Double.parseDouble(s);

                        //btnNull.setText("Go");

                        //结果在这里输出
                        inputHint.setText("= " + new Double(result).toString());

                    }
                } catch (Exception e) {
                    inputHint.setText("请正确输入");
                }
            }

            public List<String> math2(List<String> list) {
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

            public List<String> math(List<String> list, int x, double temp) {
                list.set(x - 1, String.valueOf(temp));
                list.remove(x);
                list.remove(x);
                return list;
            }


        });
    }
    }

