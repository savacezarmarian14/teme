package strategies;

import entities.Distribuitor;
import utils.SchimbariLunare;

import java.util.ArrayList;

public class StrategiaModificaDistribuitor implements StrategiaDeAplicareSchimbariLunare {
    /**
     *
     * @param list
     * @param schimbariLunare
     */
    @Override
    public void aplicaSchimbare(ArrayList<?> list, SchimbariLunare schimbariLunare) {
        if (schimbariLunare.getSchimbareDistribuitori() != null) {
            Distribuitor distribuitorDeModificat = null;
            for (Distribuitor distribuitor : ((ArrayList<Distribuitor>) list)) {
                if (distribuitor.getId() == schimbariLunare.getSchimbareDistribuitori().getId()) {
                    distribuitorDeModificat = distribuitor;
                    break;
                }
            }
            distribuitorDeModificat.setCostulInfrastructurii(
                    schimbariLunare.getSchimbareDistribuitori().getCostulNou());
        }
    }
}
