package com.example.gagnej3.scientificcalculator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Solver {

    static String number = "-?\\d+\\.?\\d*";
    private static Pattern trig = Pattern.compile("(sin|tan|cos|abs|log|ln)("+number+")");
    private static Pattern parentheses = Pattern.compile("\\(([^\\(]*?)\\)");
    private static Pattern exponent = Pattern.compile("("+number+")(\\^)("+number+")");
    private static Pattern times = Pattern.compile("("+number+")([\\*/])("+number+")");
    private static Pattern add = Pattern.compile("("+number+")([\\+-])("+number+")");
    private static String ans = "0";

    //Need to add this because the scope of when solve is called is local
    public void setAns(String answer){
        ans = answer;
    }

    public static String solve(String x){
        x = x.replaceAll("e", String.valueOf(Math.E));
        x = x.replaceAll("ans", ans);
        x = x.replaceAll("pi", String.valueOf(Math.PI));

        Matcher p = parentheses.matcher(x);
        Matcher tr = trig.matcher(x);
        Matcher e = exponent.matcher(x);
        Matcher t = times.matcher(x);
        Matcher a = add.matcher(x);

        while(p.find()){
            x = p.replaceFirst(solve(p.group(1)));
            p = parentheses.matcher(x);
            tr = trig.matcher(x);
            e = exponent.matcher(x);
            t = times.matcher(x);
            a = add.matcher(x);
        }
        while(tr.find()){
            if (tr.group(1).equals("sin")){
                double y = Math.sin(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }else if (tr.group(1).equals("cos")){
                double y = Math.cos(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }else if (tr.group(1).equals("tan")){
                double y = Math.tan(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }else if (tr.group(1).equals("log")){
                double y = Math.log10(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }else if (tr.group(1).equals("ln")){
                double y = Math.log(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }else if (tr.group(1).equals("abs")){
                double y = Math.abs(Double.parseDouble(tr.group(2)));
                x = tr.replaceFirst(String.valueOf(y));
            }
            tr = trig.matcher(x);
            e = exponent.matcher(x);
            t = times.matcher(x);
            a = add.matcher(x);
        }
        while(e.find()){
            double y = Math.pow(Double.parseDouble(e.group(1)), Double.parseDouble(e.group(3)));
            x = e.replaceFirst(String.valueOf(y));
            e = exponent.matcher(x);
            t = times.matcher(x);
            a = add.matcher(x);
        }
        while(t.find()){
            if (t.group(2).equals("*")){
                double y = (Double.parseDouble(t.group(1)) * Double.parseDouble(t.group(3)));
                x = t.replaceFirst(String.valueOf(y));
            }else{
                double y = (Double.parseDouble(t.group(1)) / Double.parseDouble(t.group(3)));
                x = t.replaceFirst(String.valueOf(y));
            }
            t = times.matcher(x);
            a = add.matcher(x);
        }
        while(a.find()){
            if (a.group(2).equals("+")){
                double y = (Double.parseDouble(a.group(1)) + Double.parseDouble(a.group(3)));
                x = a.replaceFirst(String.valueOf(y));
            }else{
                double y = (Double.parseDouble(a.group(1)) - Double.parseDouble(a.group(3)));
                x = a.replaceFirst(String.valueOf(y));
            }
            a = add.matcher(x);
        }

        ans = x;
        return x;
    }
}
