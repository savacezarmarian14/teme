package utils;

public class SchimbareDistribuitori {
    private int id;
    private int costulNou;

    /**
     *
     * @param id
     * @param costulNou
     */
    public SchimbareDistribuitori(int id, int costulNou) {
        this.id = id;
        this.costulNou = costulNou;
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
     * @param costulNou
     */
    public void setCostulNou(int costulNou) {
        this.costulNou = costulNou;
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
    public int getCostulNou() {
        return costulNou;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return " SchimbareDistribuitori{"
                + "\n      id=" + id
                + "\n      costulNou=" + costulNou
                + "\n}";
    }
}
