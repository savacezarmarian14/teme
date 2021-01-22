package strategies;

import entities.ContractDP;
import entities.Distribuitor;
import entities.Producator;
import utils.SchimbareProducatori;
import utils.SchimbariLunare;

import java.util.ArrayList;

public class StrategiaModificaProducator implements StrategiaDeAplicareSchimbariLunare {
    /**
     *
     * @param list
     * @param schimbariLunare
     */
    @Override
    public void aplicaSchimbare(ArrayList<?> list,
                                SchimbariLunare schimbariLunare) {
        for (SchimbareProducatori schimbareProducatori
                : schimbariLunare.getSchimbareProducatori()) {
            Producator producatorDeModificat = null;
            for (Producator producator : (ArrayList<Producator>) list) {
                if (schimbareProducatori.getId() == producator.getId()) {
                    producatorDeModificat = producator;
                    break;
                }
            }
            producatorDeModificat.setEnergiePerDistribuitor(
                    schimbareProducatori.getEnergiePerDistribuitor());
            for (int i = 0; i < producatorDeModificat.listaDistribuitoriClienti.size(); i++) {
                Distribuitor distribuitor = producatorDeModificat.listaDistribuitoriClienti.get(i);
                distribuitor.setListaContractelorCuFurnizorii(new ArrayList<ContractDP>());
                distribuitor.setAreProducatori(false);
            }
        }
    }
}
