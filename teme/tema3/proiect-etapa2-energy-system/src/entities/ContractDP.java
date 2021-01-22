package entities;

public class ContractDP {
    private Distribuitor distribuitor;

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
    public Producator getProducator() {
        return producator;
    }

    /**
     *
     * @param producator
     */
    public void setProducator(Producator producator) {
        this.producator = producator;
    }

    /**
     *
     * @return
     */
    public int getCostProductie() {
        return costProductie;
    }

    /**
     *
     * @param costProductie
     */
    public void setCostProductie(int costProductie) {
        this.costProductie = costProductie;
    }

    private Producator producator;
    private int costProductie;

    /**
     *
     * @param distribuitor
     * @param producator
     */
    public ContractDP(Distribuitor distribuitor, Producator producator) {
        this.distribuitor = distribuitor;
        this.producator = producator;
        costProductie = (int) (producator.getEnergiePerDistribuitor() * producator.getPretKW());
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "--ContractDP--"
                + "\nCostProductie = " + costProductie
                + "\n" + distribuitor
                + "\n" + producator;
    }
}
