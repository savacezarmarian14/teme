package strategies;

import entities.Producator;

import java.util.ArrayList;

public class GreenStrategy implements ChoseProducerStrategy {
    /**
     *
     * @param producersList
     * @return
     */
    @Override
    public ArrayList<Producator> choseProducers(ArrayList<Producator> producersList) {
        ArrayList<Producator> producatoriAlesi = new ArrayList<>();
        for (Producator producator : producersList) {
            if  (producator.getTipulEnergiei().isRenewable()) {
                producatoriAlesi.add(producator);
            }
        }
        for (Producator producator : producersList) {
            if (!producator.getTipulEnergiei().isRenewable()) {
                producatoriAlesi.add(producator);
            }
        }
        this.sortByPrice(producatoriAlesi);
        this.sortByQuantity(producatoriAlesi);
        return producatoriAlesi;
    }

    /**
     *
     * @param listaProducatori
     */
    public void sortByPrice(ArrayList<Producator> listaProducatori) {
        for (int i = 0; i < listaProducatori.size() - 1; i++) {
            for (int j = i + 1; j < listaProducatori.size(); j++) {
                if (listaProducatori.get(i).getTipulEnergiei().isRenewable()
                        == listaProducatori.get(j).getTipulEnergiei().isRenewable()) {
                    if (listaProducatori.get(i).getPretKW()
                            > listaProducatori.get(j).getPretKW()) {
                        Producator auxiliar = listaProducatori.get(i);
                        listaProducatori.set(i, listaProducatori.get(j));
                        listaProducatori.set(j, auxiliar);
                    }
                }
            }
        }
    }

    /**
     *
     * @param listaProducatori
     */
    public void sortByQuantity(ArrayList<Producator> listaProducatori) { //PRICE IS EQUAL
        for (int i = 0; i < listaProducatori.size() - 1; i++) {
            for (int j = i + 1; j < listaProducatori.size(); j++) {
                if (listaProducatori.get(i).getPretKW() == listaProducatori.get(j).getPretKW()
                        && listaProducatori.get(i).getTipulEnergiei().isRenewable()
                        == listaProducatori.get(j).getTipulEnergiei().isRenewable()) {
                    if (listaProducatori.get(i).getEnergiePerDistribuitor()
                            < listaProducatori.get(j).getEnergiePerDistribuitor()) {
                        Producator auxiliar = listaProducatori.get(i);
                        listaProducatori.set(i, listaProducatori.get(j));
                        listaProducatori.set(j, auxiliar);
                    }
                }
            }
        }
    }
}
