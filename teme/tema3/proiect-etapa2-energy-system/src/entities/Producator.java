package entities;

import java.util.ArrayList;

public class Producator {

    private int id;
    private int numarulMaximDistribuitori;
    EnergyType tipulEnergiei;
    private double pretKW;
    private int energiePerDistribuitor;
    public ArrayList<Distribuitor> listaDistribuitoriClienti = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> istoricClienti = new ArrayList<ArrayList<Integer>>();
    private boolean aFostSchimbat = false;

    /**
     *
     */
    public void inregistreazaClientiPeOLuna() {
        ArrayList<Integer> istoricLuna = new ArrayList<>();
        for (Distribuitor distribuitor : listaDistribuitoriClienti) {
            for (ContractDP contractDP : distribuitor.getListaContractelorCuFurnizorii()) {
                if (contractDP.getProducator().getId() == this.getId()) {
                    istoricLuna.add(Integer.valueOf(distribuitor.getId()));
                }
            }
        }
        this.istoricClienti.add(istoricLuna);
    }

    /**
     *
     * @return
     */
    public ArrayList<Distribuitor> getListaDistribuitoriClienti() {
        return listaDistribuitoriClienti;
    }
    /**
     *
     * @param distribuitor
     */
    public void adaugaClienti(Distribuitor distribuitor) {
        this.listaDistribuitoriClienti.add(distribuitor);
    }

    /**
     *
     * @param toCopyProducator
     */
    public Producator(Producator toCopyProducator) {
        this.id = toCopyProducator.id;
        this.numarulMaximDistribuitori = toCopyProducator.numarulMaximDistribuitori;
        this.tipulEnergiei = toCopyProducator.tipulEnergiei;
        this.pretKW = toCopyProducator.pretKW;
        this.energiePerDistribuitor = toCopyProducator.energiePerDistribuitor;
    }

    /**
     *
     * @param id
     * @param numarulMaximDistribuitori
     * @param tipulEnergiei
     * @param pretKW
     * @param energiePerDistribuitor
     */
    public Producator(int id,
                      int numarulMaximDistribuitori,
                      EnergyType tipulEnergiei,
                      double pretKW,
                      int energiePerDistribuitor) {
        this.id = id;
        this.numarulMaximDistribuitori = numarulMaximDistribuitori;
        this.tipulEnergiei = tipulEnergiei;
        this.pretKW = pretKW;
        this.energiePerDistribuitor = energiePerDistribuitor;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param numarulMaximDistribuitori
     */
    public void setNumarulMaximDistribuitori(int numarulMaximDistribuitori) {
        this.numarulMaximDistribuitori = numarulMaximDistribuitori;
    }

    /**
     *
     * @param pretKW
     */
    public void setPretKW(double pretKW) {
        this.pretKW = pretKW;
    }

    /**
     *
     * @param energiePerDistribuitor
     */
    public void setEnergiePerDistribuitor(int energiePerDistribuitor) {
        this.energiePerDistribuitor = energiePerDistribuitor;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getNumarulMaximDistribuitori() {
        return numarulMaximDistribuitori;
    }

    /**
     *
     * @return
     */
    public double getPretKW() {
        return pretKW;
    }

    /**
     *
     * @return
     */
    public int getEnergiePerDistribuitor() {
        return energiePerDistribuitor;
    }

    /**
     *
     * @return
     */
    public EnergyType getTipulEnergiei() {
        return tipulEnergiei;
    }

    /**
     *
     * @param tipulEnergiei
     */
    public void setTipulEnergiei(EnergyType tipulEnergiei) {
        this.tipulEnergiei = tipulEnergiei;
    }

    /**
     *
     * @return
     */
    public int pret() {
        return (int) (this.pretKW * this.energiePerDistribuitor);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Producator{"

                + "\n   id=" + id
                + "\n   numarulMaximDistribuitori=" + numarulMaximDistribuitori
                + "\n   numarulDeDistribuitoriCurent=" + listaDistribuitoriClienti.size()
                + "\n   tipulEnergiei=" + tipulEnergiei
                + "\n   pretKW=" + pretKW
                + "\n   energiePerDistribuitor=" + energiePerDistribuitor
                + "\n}";
    }
}
