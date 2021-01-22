package entities;

public class Contract {
    private int durataContractului;
    private Distribuitor distribuitor;
    private Consumator consumator;
    private int valoarePlata;

    /**
     *
     * @param durataContractului
     * @param distribuitor
     * @param consumator
     * @param valoarePlata
     */
    public Contract(int durataContractului,
                    Distribuitor distribuitor,
                    Consumator consumator,
                    int valoarePlata) {
        this.durataContractului = durataContractului;
        this.distribuitor = distribuitor;
        this.consumator = consumator;
        this.valoarePlata = valoarePlata;
    }

    /**
     *
     * @return
     */
    public Distribuitor getDistribuitor() {
        return distribuitor;
    }

    /**
     *
     * @param distribuitor
     */
    public void setDistribuitor(Distribuitor distribuitor) {
        this.distribuitor = distribuitor;
    }

    /**
     *
     * @return
     */
    public Consumator getConsumator() {
        return consumator;
    }

    /**
     *
     * @param consumator
     */
    public void setConsumator(Consumator consumator) {
        this.consumator = consumator;
    }

    /**
     *
     * @return
     */
    public int getValoarePlata() {
        return valoarePlata;
    }

    /**
     *
     * @param valoarePlata
     */
    public void setValoarePlata(int valoarePlata) {
        this.valoarePlata = valoarePlata;
    }

    /**
     *
     * @return
     */
    public int getDurataContractului() {
        return durataContractului;
    }

    /**
     *
     * @param durataContractului
     */
    public void setDurataContractului(int durataContractului) {
        this.durataContractului = durataContractului;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Contract{"
                + "\n     durataContractului=" + durataContractului
                + "\n     valoarePlata=" + valoarePlata
                + "\n     C-D=" + consumator.getId()
                + "-" + distribuitor.getId()
                + "\n}";
    }
}
