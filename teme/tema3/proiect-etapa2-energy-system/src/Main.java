import entities.Consumator;
import entities.Distribuitor;
import entities.Game;
import entities.Producator;
import io.Input;
import io.Output;
import utils.SchimbariLunare;

import java.util.ArrayList;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */

    public static void main(final String[] args) throws Exception {
        Input inputulSimularii = new Input(args[0]);
        ArrayList<Consumator> listaConsumatori = new ArrayList<>();
        ArrayList<Distribuitor> listaDistribuitori = new ArrayList<>();
        ArrayList<Producator> listaProducatori = new ArrayList<>();
        ArrayList<SchimbariLunare> listaSchimbariLunare = new ArrayList<>();
        inputulSimularii.setInput(listaConsumatori,
                listaDistribuitori,
                listaProducatori,
                listaSchimbariLunare);
        Game consolaSimularii = new Game(listaConsumatori,
                listaDistribuitori,
                listaProducatori,
                listaSchimbariLunare);

        for (int i = 0; i <= consolaSimularii.numarulDeTure(); i++) {
                        consolaSimularii.gameTurn(i);
        }
        Output output = new Output(listaConsumatori,
                listaDistribuitori,
                listaProducatori,
                args[1]);
        output.writeOutput();
    }
}
