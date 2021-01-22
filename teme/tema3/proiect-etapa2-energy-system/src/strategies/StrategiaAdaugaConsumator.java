package strategies;

import entities.Consumator;
import utils.SchimbariLunare;

import java.util.ArrayList;

public class StrategiaAdaugaConsumator implements StrategiaDeAplicareSchimbariLunare {
    /**
     *
     * @param list
     * @param schimbariLunare
     */
    @Override
    public void aplicaSchimbare(ArrayList<?> list, SchimbariLunare schimbariLunare) {
        if (schimbariLunare.getConsumatorNou() != null) {
            ((ArrayList<Consumator>) list).add(schimbariLunare.getConsumatorNou());
        }
    }
}
