package sample.General;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;

import java.util.ArrayList;

public class KeplerDecartMethods {

    //constants
    static public double nm = 398600.4418;
    static public double eps = 10e-12;
    static public double rad = Math.PI/180;


    public double keplerMe(double m0, double e, double a, double t, double t0){
        double m, n, ee0, ee1;
        double diff = 1.0;

        n = Math.sqrt(nm/(a*a*a));
        m = n*(t - t0) + m0;
        ee0 = m;
        while (diff > eps){
            ee1 = m + e*Math.sin(ee0);
            diff = Math.abs(ee0 - ee1);
            ee0 = ee1;
        }
        return ee0;
    }


    public double arctg(double x, double y){
        double a;
        if (Math.abs(y) < 1e-18) return a = Math.signum(x)*0.5*Math.PI;
        else {
            a = Math.atan(x/y);
            if (y < 0) a = a + Math.PI;
            if (a < 0) a = a + 2*Math.PI;
            return a;
        }
    }


    public double[] kepler_decart(double ext,double e,double a,double i,double mu,double sigb,double sigm){
        double p,r;
        double xyz_point[] = new double[6];
        double e_big = ext ;
        double omg_big = sigb;
        double omg = sigm;
        double v,u,v_r,v_n;

        v=Math.atan2((Math.sqrt(1.0-Math.pow(e,2))*Math.sin(e_big)),(Math.cos(e_big)-e));
        r=a*(1-e*Math.cos(e_big));
        u=v+omg;

        xyz_point[0]=r*(Math.cos(u)*Math.cos(omg_big)-Math.sin(u)*Math.sin(omg_big)*Math.cos(i));
        xyz_point[1]=r*(Math.cos(u)*Math.sin(omg_big)+Math.sin(u)*Math.cos(omg_big)*Math.cos(i));
        xyz_point[2]=r*Math.sin(u)*Math.sin(i);

        p=a*(1-Math.pow(e,2));
        v_r=Math.sqrt(mu/p)*e*Math.sin(v);
        v_n=Math.sqrt(mu/p)*(1+e*Math.cos(v));

        xyz_point[3]=xyz_point[0]/r*v_r+(-Math.sin(u)*Math.cos(omg_big)-Math.cos(u)*Math.sin(omg_big)*Math.cos(i))*v_n;
        xyz_point[4]=xyz_point[1]/r*v_r+(-Math.sin(u)*Math.sin(omg_big)+Math.cos(u)*Math.cos(omg_big)*Math.cos(i))*v_n;
        xyz_point[5]=xyz_point[2]/r*v_r+Math.cos(u)*Math.sin(i)*v_n;

        return xyz_point;
    }


    public double reduce(double a){
        double pi2 = 2*Math.PI;
        double b = a - (int)(a/pi2) * pi2;
        if (a<0) b = b + pi2;
        return b;
    }


    public double  date_jd(int year,int month,double day){
        int han=100;
        double m1,d,date,jd;
        int i,me,ja,jb;

        date=year+(double)(month)/100+day/1e4;
        i=(int)(date);
        m1=(date-(double)(i))*(double)(han);
        me=(int)(m1);
        d=(m1-me)*han;

        if (me<=2) {
            i=i-1;
            me=me+12;
        }
        jd=(int)(365.25*i)+(int)(30.6001*(me+1))+d+1720994.5;
        if (date < 1582.1015){
            return jd;
        }
        ja=(int)(i/100);
        jb=2-ja+(int)(ja/4);
        jd=jd+jb;
        return jd;
    }


    public double sid2000(double jd){
        double m,mm,d,t,s,sr;
        int jd2000=2451545;
        int jdyear=36525;

        m=jd-((int)jd)-0.5;
        d=jd-m-jd2000;
        t=(d+m)/jdyear;
        mm=m*86400;
        s=(24110.54841+mm+236.555367908*(d+m)+(0.093104*t+6.21E-6*Math.pow(t,2))*t)/86400*2*Math.PI;
        sr = reduce(s);
        return sr;
    }


    public double SemimajorAxis(double[] XYZ_Vxyz){
        final double mj = 398600.4418;
        double R,V2,H;

        R = Math.sqrt(Math.pow(XYZ_Vxyz[0],2) + Math.pow(XYZ_Vxyz[1],2) + Math.pow(XYZ_Vxyz[2],2));
        V2 = Math.pow(XYZ_Vxyz[3],2) + Math.pow(XYZ_Vxyz[4],2) + Math.pow(XYZ_Vxyz[5],2);
        H = V2/2.0 - mj/R;

        return -mj/2.0/H;
    }


    public ArrayList<Double> CoorToKepler(ArrayList<Double> x){

        final double mj = 398600.4418;
        double R,V2,c1,c2,c3,C,f1,f2,f3,F,H;
        double A,E,I;
        ArrayList<Double> a_e_i = new ArrayList<>();

        if(x.get(3) != null && x.get(4) != null && x.get(5) != null) {

            R = Math.sqrt(Math.pow(x.get(0), 2) + Math.pow(x.get(1), 2) + Math.pow(x.get(2), 2));
            V2 = Math.pow(x.get(3), 2) + Math.pow(x.get(4), 2) + Math.pow(x.get(5), 2);

            c1 = x.get(1) * x.get(5) - x.get(2) * x.get(4);
            c2 = x.get(2) * x.get(3) - x.get(0) * x.get(5);
            c3 = x.get(0) * x.get(4) - x.get(1) * x.get(3);

            C = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2) + Math.pow(c3, 2));

            f1 = -mj * x.get(0) / R + c3 * x.get(4) - c2 * x.get(5);
            f2 = -mj * x.get(1) / R + c1 * x.get(5) - c3 * x.get(3);
            f3 = -mj * x.get(2) / R + c2 * x.get(3) - c1 * x.get(4);

            F = Math.sqrt(Math.pow(f1, 2) + Math.pow(f2, 2) + Math.pow(f3, 2));

            H = V2 / 2.0 - mj / R;

            A = -mj / 2 / H;
            E = F / mj;
            I = Math.acos(c3 / C);

            a_e_i.add(A);
            a_e_i.add(E);
            a_e_i.add(I / rad);
        }

        return a_e_i;
    }


    public ArrayList<Double> Coor2Elem(ArrayList<Double> CoordVel,
            int year, int month, int day, int hour, int min, double sec){

        PVCoordinates pvCoordinates = new PVCoordinates(
                new Vector3D(
                        CoordVel.get(0) * 1000,
                        CoordVel.get(1) * 1000,
                        CoordVel.get(2) * 1000),
                new Vector3D(
                        CoordVel.get(3) * 1000,
                        CoordVel.get(4) * 1000,
                        CoordVel.get(5) * 1000));

        AbsoluteDate absoluteDate = new AbsoluteDate(year, month, day, hour, min, sec,
                TimeScalesFactory.getUTC());

        KeplerianOrbit keplerianOrbit = new KeplerianOrbit(
                pvCoordinates, FramesFactory.getICRF(), absoluteDate, Constants.EGM96_EARTH_MU/*KeplerDecartMethods.nm*/);

        ArrayList<Double> elements = new ArrayList<>();

        elements.add(keplerianOrbit.getA() / 1000);
        elements.add(keplerianOrbit.getE());
        elements.add(keplerianOrbit.getI() * 180.0/Math.PI);
        elements.add(keplerianOrbit.getRightAscensionOfAscendingNode() * 180.0/Math.PI);
        if (keplerianOrbit.getPerigeeArgument() < 0)
            elements.add((2 * Math.PI + keplerianOrbit.getPerigeeArgument()) * 180.0/Math.PI);
        else
            elements.add(keplerianOrbit.getPerigeeArgument() * 180.0/Math.PI);
        elements.add(keplerianOrbit.getMeanAnomaly() * 180.0/Math.PI);

        return elements;
    }

    public ArrayList<Double> Coor2Elem(ArrayList<Double> CoordVel){

        double c1,c2,c3,c,l1,l2,l3,l;
        double cos_i, sin_i, cos_omega_big, sin_omega_big, cos_omega, sin_omega;
        double sin_e_big, cos_e_big;
        double r, V2, h, e_big_pr;
        double mu = 398600.4418;

        ArrayList<Double> elements = new ArrayList<>();

        r = Math.sqrt(Math.pow(CoordVel.get(0), 2) + Math.pow(CoordVel.get(1), 2) + Math.pow(CoordVel.get(2), 2));
        V2 = Math.pow(CoordVel.get(3), 2) + Math.pow(CoordVel.get(4), 2) + Math.pow(CoordVel.get(5), 2);

        h = V2 / 2.0 - mu / r;

        c1 = CoordVel.get(1) * CoordVel.get(5) - CoordVel.get(2) * CoordVel.get(4);
        c2 = CoordVel.get(2) * CoordVel.get(3) - CoordVel.get(0) * CoordVel.get(5);
        c3 = CoordVel.get(0) * CoordVel.get(4) - CoordVel.get(1) * CoordVel.get(3);

        l1 = - mu * CoordVel.get(0) / r + CoordVel.get(4) * c3 - CoordVel.get(5) * c2;
        l2 = - mu * CoordVel.get(1) / r + CoordVel.get(5) * c1 - CoordVel.get(3) * c3;
        l3 = - mu * CoordVel.get(2) / r + CoordVel.get(3) * c2 - CoordVel.get(4) * c1;

        c = Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2) + Math.pow(c3, 2));
        l = Math.sqrt(Math.pow(l1, 2) + Math.pow(l2, 2) + Math.pow(l3, 2));

        elements.add(- mu / 2.0 / h); //a
        elements.add(l / mu); //e

        //for i
        cos_i = c3 / c;
        sin_i = Math.sqrt(1 - Math.pow(cos_i, 2));

        //for omega_big
        sin_omega_big = c1 / (c * sin_i);
        cos_omega_big = - c2 / (c * sin_i);

        //for omega
        sin_omega = l3 / (l * sin_i);
        cos_omega = l1 / l * cos_omega_big + l2 / l * sin_omega_big;

        elements.add(Math.atan2(sin_i, cos_i) * 180.0/Math.PI); //i

        elements.add(Math.atan2(sin_omega_big, cos_omega_big) * 180.0/Math.PI); //omega_big_pr
        elements.add(Math.atan2(sin_omega, cos_omega) * 180.0/Math.PI); //omega_pr

        sin_e_big = (
                        CoordVel.get(0) * CoordVel.get(3) +
                        CoordVel.get(1) * CoordVel.get(4) +
                        CoordVel.get(2) * CoordVel.get(5)) /
                        (elements.get(1) * Math.sqrt(mu * elements.get(0)));
        cos_e_big = (1 - r / elements.get(0)) / elements.get(1);

        e_big_pr = Math.atan2(sin_e_big, cos_e_big);

        elements.add(e_big_pr - elements.get(0) * sin_e_big); //m_big0_pr

        return elements;
    }

}
