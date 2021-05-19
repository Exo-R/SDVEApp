package sample.General;

import javafx.beans.property.*;

public class TableApproximation {

    private StringProperty delta_R;
    private IntegerProperty N_apprx;
    private DoubleProperty Percent;

    public TableApproximation(String delta_R, int N_apprx, double Percent) {
        this.delta_R = new SimpleStringProperty(delta_R);
        this.N_apprx = new SimpleIntegerProperty(N_apprx);
        this.Percent = new SimpleDoubleProperty(Percent);
    }

    public StringProperty get_dR_Property() {
        return delta_R;
    }

    public IntegerProperty get_N_apprx_Property() {
        return N_apprx;
    }

    public DoubleProperty get_Percent_Property() {
        return Percent;
    }


}//endclass
