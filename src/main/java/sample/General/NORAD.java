package sample.General;

import java.util.ArrayList;

public class NORAD {

    public ArrayList<String> DateTime(String tleDT) {

        ArrayList<String> datetime = new ArrayList<>();

        datetime.add(tleDT.substring(8, 10));
        datetime.add(tleDT.substring(5, 7));
        datetime.add(tleDT.substring(0, 4));

        datetime.add(tleDT.substring(11, 13));
        datetime.add(tleDT.substring(14, 16));
        datetime.add(tleDT.substring(17));

        return datetime;
    }

    public Object[] NORAD_parser(String line_1, String line_2) {

        Object[] result = new Object[11];
        Other other = new Other();
        result[0] = other.number_object(line_2.substring(1, 7));
        result[0] = line_1.substring(2, 7).replaceAll(" ","");
        result[0] = Integer.parseInt(result[0].toString());
        result[1] = 2000 + Integer.parseInt(line_1.substring(18, 20));//year
        result[2] = Integer.parseInt(line_1.substring(20, 23));//day
        result[10] = 0;
        result[3] = Double.parseDouble("0" + line_1.substring(23, 32))/**24*3600*/;//T_omega
        result[4] = Double.parseDouble(line_2.substring(7, 16));//Orbit Inclination (degrees)
        result[5] = Double.parseDouble(line_2.substring(16, 25));//Right Ascension of Ascending Node (degrees)
        result[6] = Double.parseDouble("0." + line_2.substring(26, 33));//Eccentricity (decimal point assumed)
        result[7] = Double.parseDouble(line_2.substring(33, 42));//Argument of Perigee (degrees)
        result[8] = Double.parseDouble(line_2.substring(42, 51));//Mean Anomaly (degrees)
        result[9] = Double.parseDouble(line_2.substring(51, 63));//Mean Motion (revolutions/day)

        return result;
    }


    public String[] getting_date_and_time(String str_0){
        String[] str = new String[3];
        boolean b = false;
        for (int i = 0; i < 3; i++) str[i] = "";
        int i = 15;

        while ((i != 24) & (b != true)) {
            if ((str_0.charAt(i) == ' ') & (str_0.charAt(i + 1) != ' ') & (str_0.charAt(i + 2) != ' ')) {

                for (int j = 0; j < 2; j++) str[0] = str[0] + str_0.charAt(i + j + 1);    //year
                str[0] = "20" + str[0];

                for (int j = 0; j < 3; j++) str[1] = str[1] + str_0.charAt(i + j + 1 + 2);    //day

                int j=0;
                while(str_0.charAt(i + j + 1 + 2 + str[1].length()) != ' ') {
                    str[2] = str[2] + str_0.charAt(i + j + 1 + 2 + str[1].length());    //t_omega
                    j++;
                }
                str[2] = "0" + str[2];

                b=true;

            }

            i++;

        }
        return str;
    }


    public String[] getting_elements_new(String str_0) {
        String[] str = new String[7];
        for (int i = 0; i < 7; i++) str[i] = "";

        int count=0;
        int i=0;

        while(count <= 5) {
            if ((str_0.charAt(i) == ' ') & (str_0.charAt(i + 1) != ' ')) {
                i++;
                while (str_0.charAt(i) != ' ') {
                    str[count] = str[count] + str_0.charAt(i);
                    i++;
                }
                count++;
                i--;
            }
            i++;
        }
        for (int j = i; j < (str_0.length() - 6); j++) str[count] = str[count] + str_0.charAt(j);

        return str;
    }


}
