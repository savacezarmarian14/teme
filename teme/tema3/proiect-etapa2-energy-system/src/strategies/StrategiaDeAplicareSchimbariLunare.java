package strategies;

import utils.SchimbariLunare;

import java.util.ArrayList;

public interface StrategiaDeAplicareSchimbariLunare {
    /**
     *
     * @param list
     * @param schimbariLunare
     */
    void aplicaSchimbare(ArrayList<?> list,
                         SchimbariLunare schimbariLunare);
}
