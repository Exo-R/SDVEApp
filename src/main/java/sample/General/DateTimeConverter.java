package sample.General;


public class DateTimeConverter {

    public double Cal2Jd(double day, double month, double year) {
        if (month < 3) { month += 12; year -= 1; }
        double Jd = (int)(year*365.25)+(int)((month+1)*30.6001)+day+1720996.5-(int)(year/100)+(int)((year/100)/4);
        return Jd;
    }


    public String NORAD_DateConverter(int days, int year){

        int[] month = new int[13];
        month[1] = 31;                  month[3] = 31;
        month[4] = 30;  month[5] = 31;  month[6] = 30;
        month[7] = 31;  month[8] = 31;  month[9] = 30;
        month[10] = 31; month[11] = 30; month[12] = 31;

        int day0 = days;
        int i = 1;

        if(day0 > month[1]) {

            if(year % 4 == 0) // checking of year
                month[2] = 29;
            else
                month[2] = 28;

            while(day0 > month[i])  {

                day0 = day0 - month[i];
                i++;

            }//while

        }//if
        else
        if (day0 > 9)
            return (year + "-01-" + day0);
        else
            return (year + "-01-0" + day0);

        if (i > 9)
            if (day0 > 9)
                return (year + "-" + i + "-" + day0); //
            else
                return (year + "-" + i + "-0" + day0); //
        else
        if (day0 > 9)
            return (year + "-0" + i + "-" + day0);
        else
            return (year + "-0" + i + "-0" + day0);

    }


}
