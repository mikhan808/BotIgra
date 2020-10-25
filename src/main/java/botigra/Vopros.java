package botigra;

public class Vopros {
    private String vopros;
    private String otvet;

    public Vopros(String vopros, String otvet) {
        this.vopros = vopros;
        this.otvet = otvet;
    }

    public Vopros(String voprosOtvet, String delimiter, Object nullParam) {
        this(voprosOtvet.split(delimiter)[0], voprosOtvet.split(delimiter)[1]);
    }

    public String getVopros() {
        return vopros;
    }

    public void setVopros(String vopros) {
        this.vopros = vopros;
    }

    public String getOtvet() {
        return otvet;
    }

    public void setOtvet(String otvet) {
        this.otvet = otvet;
    }
}
