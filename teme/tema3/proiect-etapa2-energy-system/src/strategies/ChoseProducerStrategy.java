package strategies;

import entities.Producator;

import java.util.ArrayList;

public interface ChoseProducerStrategy {
    /**
     *
     * @param producersList
     * @return
     */
    ArrayList<Producator> choseProducers(ArrayList<Producator> producersList);
}
