package sample.General;

import java.util.ArrayList;

public class ESA {

    public ArrayList<String> DateTime(String Date, String Time) {
        //2017-12-31
        //17:17:39.844
        ArrayList<String> datetime = new ArrayList<>();

        datetime.add(Date.substring(8));
        datetime.add(Date.substring(5, 7));
        datetime.add(Date.substring(0, 4));

        datetime.add(Time.substring(0, 2));
        datetime.add(Time.substring(3, 5));
        datetime.add(Time.substring(6));

        return datetime;
    }

    public String[] getting_date_and_time(String str_0) {

        String[] str = new String[4];//year,month,day,T_omega
        for (int i = 0; i < 4; i++) str[i] = "";
        int i = 0;
        boolean b = false;
        double hour = 0;
        double minute = 0;
        double sec = 0;
        double day = 0;

        while ((i != 24) & (b != true)) {
            if ((str_0.charAt(i) == ' ') & (str_0.charAt(i + 5) == '-') & (str_0.charAt(i + 14) == ':')) {

                for (int j = 0; j < 4; j++) str[0] = str[0] + str_0.charAt(i + j + 1);    //year
                for (int j = 0; j < 2; j++) str[1] = str[1] + str_0.charAt(i + j + 2 + 4);    //month
                for (int j = 0; j < 2; j++) str[2] = str[2] + str_0.charAt(i + j + 9);    //day

                for (int j = 0; j < 2; j++) {
                    str[3] = str[3] + str_0.charAt(i + j + 12);    //T_omega
                }
                hour = Double.parseDouble(str[3]);
                str[3] = "";

                for (int j = 0; j < 2; j++) {
                    str[3] = str[3] + str_0.charAt(i + j + 15);    //T_omega
                }
                minute = Double.parseDouble(str[3]);
                str[3] = "";

                for (int j = 0; j < 6; j++) {
                    str[3] = str[3] + str_0.charAt(i + j + 18);    //T_omega
                }
                sec = Double.parseDouble(str[3]);
                str[3] = "";

                str[3] = String.valueOf(/*day * 24 * 3600 +*/ hour * 3600 + minute * 60 + sec);

                b = true;
            }

            i++;
        }

        return str;
    }

    public String[] getting_6_elements(String str_0) {
        String[] str = new String[6];// a, e, i, Omega, w, ly
        for (int i = 0; i < 6; i++) str[i] = "";
        int count = 0;
        int j = 0;
        int i = 0;

        str_0 = str_0 + " ";

        while (count != 2) {
            if ((str_0.charAt(i) == ' ') & (str_0.charAt(i + 1) != ' ')) {
                count++;
                j = i + 1;
            }
            i++;
        }
        int k = 0;
        while (str_0.charAt(k + i) != ' ') {
            str[0] = str[0] + str_0.charAt(k + i);
            k++;
        }
        k = 0;
        while (str_0.charAt(k + i + str[0].length() + 1) != ' ') {
            str[1] = str[1] + str_0.charAt(k + i + str[0].length() + 1);
            k++;
        }
        k = 0;
        while (str_0.charAt(k + i + str[0].length() + str[1].length() + 2) != ' ') {
            str[2] = str[2] + str_0.charAt(k + i + str[0].length() + str[1].length() + 2);
            k++;
        }
        k = 0;
        while (str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + 3) != ' ') {
            str[3] = str[3] + str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + 3);
            k++;
        }
        k = 0;
        while (str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + str[3].length() + 4) != ' ') {
            str[4] = str[4] + str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + str[3].length() + 4);
            k++;
        }
        k = 0;
        while (str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + str[3].length() + str[4].length() + 5) != ' ') {
            str[5] = str[5] + str_0.charAt(k + i + str[0].length() + str[1].length() + str[2].length() + str[3].length() + str[4].length() + 5);
            k++;
        }

        return str;
    }

    public Object[] ESA_parser(String sstr0, String sstr, String sstr_next) throws Exception{

        Object[] result = new Object[12];

        result[0] = sstr_next.substring(0, sstr_next.indexOf(' '));

        String[] str_data_time = getting_date_and_time(sstr);
        result[1] = str_data_time[0];//year
        result[2] = str_data_time[1];//month
        result[9] = str_data_time[2];//day
        result[3] = str_data_time[3];//day + T_omega
        String[] six_elements = getting_6_elements(sstr_next);
        result[4] = six_elements[0];// a
        result[5] = six_elements[1];//  e,
        result[6] = six_elements[2];//  i,
        result[7] = six_elements[3];// Omega,
        result[8] = six_elements[4];// w,
        result[10] = "";
        result[11] = six_elements[5];// ly | Mean Anomaly

        return result;
    }



}
