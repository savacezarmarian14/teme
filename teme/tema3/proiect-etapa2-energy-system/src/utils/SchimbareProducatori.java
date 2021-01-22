package utils;

public class SchimbareProducatori {
    private int id;
    private int energiePerDistribuitor;

    /**
     *
     * @param id
     * @param energiePerDistribuitor
     */
    public SchimbareProducatori(int id, int energiePerDistribuitor) {
        this.id = id;
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
     * @param id
     */
    public void setId(int id) {
        this.id = id;
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
     * @param energiePerDistribuitor
     */
    public void setEnergiePerDistribuitor(int energiePerDistribuitor) {
        this.energiePerDistribuitor = energiePerDistribuitor;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SchimbareProducatori{"
                + "\n     id=" + id
                + "\n     energiePerDistribuitor=" + energiePerDistribuitor
                + "\n}";
    }
}
