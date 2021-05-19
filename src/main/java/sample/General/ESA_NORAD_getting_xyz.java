package sample.General;

public class ESA_NORAD_getting_xyz {

    static double     ee1;
    static double     t_omg,t_obr,l_omg,arg_w,jd_omg,s,nu,E_anml,M_anml,Big_omg,e,i,a,M00,n00;
    static double     mm1,M_omg,dt,jd0,n0;
    static int        day,month,year;
    static String     ns;

    public double[] ESA_getting_xyz(Object[] result){

        KeplerDecartMethods KDM = new KeplerDecartMethods();

        ns = result[0].toString(); //+
        day = Integer.parseInt(result[9].toString()); //+
        month = Integer.parseInt(result[2].toString()); //+
        year = Integer.parseInt(result[1].toString()); //+
        t_omg = Double.parseDouble(result[3].toString()); //+
        e = Double.parseDouble(result[5].toString());   //+
        i = Double.parseDouble(result[6].toString())* KeplerDecartMethods.rad;   //+
        l_omg = Double.parseDouble(result[7].toString())* KeplerDecartMethods.rad;  //+
        arg_w = Double.parseDouble(result[8].toString()) * KeplerDecartMethods.rad;  //+
        a = Double.parseDouble(result[4].toString());

        t_obr = 2*Math.PI*Math.sqrt(Math.pow(a, 3)/KeplerDecartMethods.nm);
        jd_omg = KDM.date_jd(year,month,day) + t_omg/86400;
        s = KDM.sid2000(jd_omg);

        Big_omg = l_omg;

        nu = -arg_w;
        E_anml = 2*Math.atan2(Math.sin(nu/2),Math.cos(nu/2)*Math.sqrt((1+e)/(1-e)));
        M_anml = E_anml-e*Math.sin(E_anml);

        jd0 = jd_omg;

        M_omg = M_anml;     //+
        n0 = 2*Math.PI/t_obr;     //+
        dt = (jd0-jd_omg)*86400;
        mm1 = M_omg+n0*dt;

        ee1 = KDM.keplerMe(mm1,e,a,dt,0);
        double[] x_point = KDM.kepler_decart(ee1,e,a,i,KeplerDecartMethods.nm,Big_omg,arg_w);

        return x_point;
    }

    public double[] NORAD_getting_xyz(Object[] result){

        KeplerDecartMethods KDM = new KeplerDecartMethods();

        ns = result[0].toString(); //+
        day = Integer.parseInt(result[2].toString()); //+
        month = 0; //+
        year = Integer.parseInt(result[1].toString()); //+
        t_omg = Double.parseDouble(result[3].toString()); //+
        e = Double.parseDouble(result[6].toString());   //+
        i = Double.parseDouble(result[4].toString())* KeplerDecartMethods.rad;   //+
        l_omg = Double.parseDouble(result[5].toString())* KeplerDecartMethods.rad;  //+
        arg_w = Double.parseDouble(result[7].toString()) * KeplerDecartMethods.rad;  //+
        M00 = Double.parseDouble(result[8].toString())* KeplerDecartMethods.rad;
        n00 = Double.parseDouble(result[9].toString());

        t_obr = 2*Math.PI/n00*24*3600;

        jd_omg = KDM.date_jd(year,month,day) + t_omg/86400;
        s = KDM.sid2000(jd_omg);

        a = Math.pow((Math.pow(t_obr,2)*KeplerDecartMethods.nm)/(4*Math.PI*Math.PI),1.0/3.0);
        Big_omg = l_omg;

        nu = -arg_w;
        M_anml = M00;

        jd0 = jd_omg;

        ee1 = KDM.keplerMe(M00,e,a,/*dt*/0,0);
        double[] x_point = KDM.kepler_decart(ee1,e,a,i,KeplerDecartMethods.nm,Big_omg,arg_w);

        return x_point;
    }

}
