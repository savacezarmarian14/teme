package entities;

public class FacturaRestanta {
    int valoarePlataRestanta;
    Distribuitor distribuitor;

    /**
     *
     * @param valoarePlataRestanta
     * @param distribuitor
     */
    public FacturaRestanta(int valoarePlataRestanta, Distribuitor distribuitor) {
        this.valoarePlataRestanta = valoarePlataRestanta;
        this.distribuitor = distribuitor;
    }
}
