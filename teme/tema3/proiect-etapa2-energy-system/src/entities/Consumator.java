package entities;

public class Consumator {
    private int id;
    private int bugetInitial;
    private int venitLunar;
    private Contract contract;
    private boolean faliment = false;
    private FacturaRestanta facturaRestanta = null;

    /**
     *
     * @return
     */
    public FacturaRestanta getFacturaRestanta() {
        return facturaRestanta;
    }

    /**
     *
     * @param facturaRestanta
     */
    public void setFacturaRestanta(FacturaRestanta facturaRestanta) {
        this.facturaRestanta = facturaRestanta;
    }

    /**
     *
     * @return
     */
    public boolean isFaliment() {
        return faliment;
    }

    /**
     *
     * @return
     */
    public boolean nuAreBani() {
        if (this.getFacturaRestanta() != null) {
            if (this.getFacturaRestanta().valoarePlataRestanta
                    + this.getContract().getValoarePlata() > this.bugetInitial) {
                return true;
            }
        }   else {
            if (this.getContract().getValoarePlata() > this.bugetInitial) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public void platesteDistribuitorul() {
        Distribuitor distribuitorulAles = this.getContract().getDistribuitor();
        ///DACA ARE FACTURA RESTANTA
        if (this.getFacturaRestanta() != null) {
            Distribuitor distribuitorR = this.getFacturaRestanta().distribuitor;
            /// DOAR DACA NU A DAT FALIMENT PRIMESTE CASH
            if (!distribuitorR.isFaliment()) {
                this.setBugetInitial(this.getBugetInitial()
                        - this.getFacturaRestanta().valoarePlataRestanta);
                distribuitorR.setBugetInitial(distribuitorR.getBugetInitial()
                        + this.getFacturaRestanta().valoarePlataRestanta);
            }
            this.setFacturaRestanta(null);
        }
        this.setBugetInitial(this.getBugetInitial() - this.getContract().getValoarePlata());
        distribuitorulAles.setBugetInitial(distribuitorulAles.getBugetInitial()
                + this.getContract().getValoarePlata());
    }

    /**
     *
     * @param faliment
     */
    public void setFaliment(boolean faliment) {
        this.faliment = faliment;
    }

    /**
     *
     * @param id
     * @param bugetInitial
     * @param venitLunar
     */
    public Consumator(int id,
                      int bugetInitial,
                      int venitLunar) {
        this.id = id;
        this.bugetInitial = bugetInitial;
        this.venitLunar = venitLunar;
        this.contract = null;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Consumator{"
                + "\n    id=" + id
                + "\n    bugetInitial=" + bugetInitial
                + "\n    venitLunar=" + venitLunar
                + "\n    faliment=" + faliment
                + "\n    contract=" + contract
                + "\n}";
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
     * @param bugetInitial
     */
    public void setBugetInitial(int bugetInitial) {
        this.bugetInitial = bugetInitial;
    }

    /**
     *
     * @param venitLunar
     */
    public void setVenitLunar(int venitLunar) {
        this.venitLunar = venitLunar;
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
    public int getBugetInitial() {
        return bugetInitial;
    }

    /**
     *
     * @return
     */
    public int getVenitLunar() {
        return venitLunar;
    }

    /**
     *
     * @param contract
     */
    public void setContract(Contract contract) {
        this.contract = contract;
    }

    /**
     *
     * @return
     */
    public Contract getContract() {
        return contract;
    }

    /**
     *
     */
    public void reziliazaContract() {
        this.contract = null;
    }
}
