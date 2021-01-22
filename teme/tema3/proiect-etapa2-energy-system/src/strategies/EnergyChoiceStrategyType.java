package strategies;

/**
 * Strategy types for distributors to choose their producers
 */
public enum EnergyChoiceStrategyType {
    GREEN("GREEN"),
    PRICE("PRICE"),
    QUANTITY("QUANTITY");
    public final String label;

    /**
     *
     * @param label
     */
    EnergyChoiceStrategyType(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "EnergyChoiceStrategyType{"
                + "label='"
                + label
                + '\''
                + '}';
    }
}
