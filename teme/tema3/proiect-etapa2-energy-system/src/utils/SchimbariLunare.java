package utils;

import entities.Consumator;
import entities.Distribuitor;
import entities.Producator;
import strategies.StrategiaAdaugaConsumator;
import strategies.StrategiaDeAplicareSchimbariLunare;
import strategies.StrategiaModificaDistribuitor;
import strategies.StrategiaModificaProducator;

import java.util.ArrayList;

public class SchimbariLunare {
    private Consumator consumatorNou;
    private SchimbareDistribuitori schimbareDistribuitori;
    private ArrayList<SchimbareProducatori> schimbareProducatori;

    /**
     *
     * @return
     */
    public Consumator getConsumatorNou() {
        return consumatorNou;
    }

    /**
     *
     * @return
     */
    public SchimbareDistribuitori getSchimbareDistribuitori() {
        return schimbareDistribuitori;
    }

    /**
     *
     * @return
     */
    public ArrayList<SchimbareProducatori> getSchimbareProducatori() {
        return schimbareProducatori;
    }

    /**
     *
     * @param consumatorNou
     * @param schimbareDistribuitori
     * @param schimbareProducatori
     */
    public SchimbariLunare(Consumator consumatorNou,
                           SchimbareDistribuitori schimbareDistribuitori,
                           ArrayList<SchimbareProducatori> schimbareProducatori) {
        this.consumatorNou = consumatorNou;
        this.schimbareDistribuitori = schimbareDistribuitori;
        this.schimbareProducatori = schimbareProducatori;
    }

    /**
     *
     * @param strategie
     * @param consumatori
     * @param distribuitori
     * @param producatori
     */
    public void aplicaSchimbare(StrategiaDeAplicareSchimbariLunare strategie,
                                ArrayList<Consumator> consumatori,
                                ArrayList<Distribuitor> distribuitori,
                                ArrayList<Producator> producatori) {
        if (strategie instanceof StrategiaAdaugaConsumator) {
            strategie.aplicaSchimbare(consumatori, this);
        }
        if (strategie instanceof StrategiaModificaDistribuitor) {
            strategie.aplicaSchimbare(distribuitori, this);
        }
        if (strategie instanceof StrategiaModificaProducator) {
            strategie.aplicaSchimbare(producatori, this);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SchimbariLunare{\n"
                + "consumatorNou=" + consumatorNou
                + ", schimbareDistribuitori=" + schimbareDistribuitori
                + ", schimbareProducatori=" + schimbareProducatori
                + '}';
    }
}
