package entities;

import strategies.StrategiaModificaProducator;
import strategies.StrategiaModificaDistribuitor;
import strategies.StrategiaAdaugaConsumator;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;
import utils.Constants;
import utils.SchimbariLunare;

import java.util.ArrayList;

public class Game {
    private ArrayList<Consumator> listaConsumatori;
    private ArrayList<Distribuitor> listaDistribuitori;
    private ArrayList<Producator> listaProducatori;
    private ArrayList<SchimbariLunare> listaSchimbariLunare;

    /**
     *
     * @param listaConsumatori
     * @param listaDistribuitori
     * @param listaProducatori
     * @param listaSchimbariLunare
     */
    public Game(ArrayList<Consumator> listaConsumatori,
                ArrayList<Distribuitor> listaDistribuitori,
                ArrayList<Producator> listaProducatori,
                ArrayList<SchimbariLunare> listaSchimbariLunare) {
        this.listaConsumatori = listaConsumatori;
        this.listaDistribuitori = listaDistribuitori;
        this.listaProducatori = listaProducatori;
        this.listaSchimbariLunare = listaSchimbariLunare;
    }

    /**
     *
     */
    public void sePlatescConsumatorii() {
        for (Consumator consumator : listaConsumatori) {
            if (!consumator.isFaliment()) {
                consumator.setBugetInitial(consumator.getBugetInitial()
                        + consumator.getVenitLunar());
            }
        }
    }

    /**
     *
     */
    public void printStatus() {
        System.out.println(this.listaConsumatori);
        System.out.println();
        System.out.println(this.listaDistribuitori);
        System.out.println();
        System.out.println(this.listaProducatori);
        System.out.println();
    }

    /**
     *
     * @param list
     */
    public void printOneList(ArrayList<?> list) {
        System.out.println(list);
        System.out.println();
    }

    /**
     *
     * @return
     */
    public int numarulDeTure() {
        return listaSchimbariLunare.size();
    }

    /**
     *
     */
    public void distribuitoriiAlegProducatorii() {
        for (Distribuitor distribuitor : listaDistribuitori) {
            if (!distribuitor.isFaliment() && !distribuitor.isAreProducatori()) {
                switch (distribuitor.getStategiaProducatorului()) {
                    case GREEN:
                        distribuitor.alegeProducatori(new GreenStrategy(),
                                listaProducatori);
                        break;
                    case PRICE:
                        distribuitor.alegeProducatori(new PriceStrategy(),
                                listaProducatori);
                        break;
                    case QUANTITY:
                        distribuitor.alegeProducatori(new QuantityStrategy(),
                                listaProducatori);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     *
     */
    public void consumatoriiAlegDistribuitorii() {
        Distribuitor bestDistribuitor = this.celMaiAvantajosDistribuitor();
        for (Consumator consumator : listaConsumatori) {
            if (!consumator.isFaliment()
                    && (consumator.getContract() == null
                    || consumator.getContract().getDurataContractului() == 0)) {
                if (consumator.getContract() != null
                        && consumator.getContract().getDurataContractului() == 0) {
                    Distribuitor distribuitor = consumator.getContract().getDistribuitor();
                    distribuitor.reziliazaContract(consumator.getContract());
                }
                consumator.setContract(new Contract(bestDistribuitor.getDurataContractului(),
                        bestDistribuitor,
                        consumator,
                        bestDistribuitor.getRataLunara()));
                bestDistribuitor.adaugaContractNou(consumator.getContract());
            }
        }
    }

    /**
     *
     * @return
     */
    public Distribuitor celMaiAvantajosDistribuitor() {
        int rataLunaraMinima = Constants.MAX;
        int rataLunara;
        Distribuitor bestDistribuitor = null;

        for (Distribuitor distribuitor : listaDistribuitori) {
            if (!distribuitor.isFaliment()) {
                if (distribuitor.getListaContractelor().size() == 0) {
                    rataLunara = (int) (distribuitor.getCostulInfrastructurii()
                            + distribuitor.calculeazaCostulDeProductie()
                            + distribuitor.getProfit());
                } else {
                    rataLunara = (int) ((distribuitor.getCostulInfrastructurii()
                            / distribuitor.getListaContractelor().size())
                            + distribuitor.getCostDeProductie()
                            + distribuitor.getProfit());

                }
                distribuitor.setRataLunara(rataLunara);
                if (rataLunaraMinima > rataLunara) {
                    rataLunaraMinima = rataLunara;
                    bestDistribuitor = distribuitor;
                }
            }
        }
        return bestDistribuitor;
    }

    /**
     *
     */
    public void calculeazaProfitulDistribuitorilor() {
        int costTotalDeProductie = 0;
        for (Distribuitor distribuitor : listaDistribuitori) {
            if (!distribuitor.isFaliment()) {
                costTotalDeProductie = distribuitor.calculeazaCostulDeProductie();
                distribuitor.setProfit((int) Math.round(Math.floor(Constants.P02
                        * costTotalDeProductie)));
            }
        }
    }

    /**
     *
     * @param consumator
     */
    public void eliminaConsumatorDinToateListele(Consumator consumator) {
        Distribuitor distribuitor = consumator.getContract().getDistribuitor();
        distribuitor.removeContract(consumator.getContract());
        consumator.setContract(null);
    }

    /**
     *
     */
    public void consumatoriiPlatescDistribuitorii() {
        for (Consumator consumator : listaConsumatori) {
            if (!consumator.isFaliment()) {
                consumator.getContract().setDurataContractului(
                        consumator.getContract().getDurataContractului() - 1);
                ///DACA NU ARE BANI ISI FACE DATORII ->
                if (consumator.nuAreBani()) {
                    ///DACA AVEA DEJA DATORIE DA FALIMENT ->
                    if (consumator.getFacturaRestanta() != null) {
                        consumator.setFaliment(true);
                        this.eliminaConsumatorDinToateListele(consumator);
                    } else { /// DACA NU ARE DATORII ISI FACE ->
                        consumator.setFacturaRestanta(new FacturaRestanta((int) (Constants.P12
                                * consumator.getContract().getValoarePlata()),
                                consumator.getContract().getDistribuitor()));
                    }
                } else {
                    consumator.platesteDistribuitorul();
                }
            }
        }
    }

    /**
     *
     */
    public void distribuitoriiPlatescTaxele() {
        for (Distribuitor distribuitor : listaDistribuitori) {
            if (!distribuitor.isFaliment()) {
                distribuitor.platesteTaxe();
            }
        }
    }
    /**
     *
     * @param turnID
     */
    public void aplicaSchimbari(int turnID) {
        this.listaSchimbariLunare.get(turnID - 1).aplicaSchimbare(
                new StrategiaAdaugaConsumator(),
                listaConsumatori,
                listaDistribuitori,
                listaProducatori);
        this.listaSchimbariLunare.get(turnID - 1).aplicaSchimbare(
                new StrategiaModificaDistribuitor(),
                listaConsumatori,
                listaDistribuitori,
                listaProducatori);
    }

    /**
     *
     */
    public void declaraClientiProducatori() {
        for (Producator producator : listaProducatori) {
            producator.inregistreazaClientiPeOLuna();
        }
    }

    /**
     *
     */
    public void calculeazaCostulDeProductieDistribuitori() {
        for (Distribuitor distribuitor : listaDistribuitori) {
            distribuitor.setCostDeProductie(distribuitor.calculeazaCostulDeProductie());
        }
    }

    /**
     *
     */
    private int gameTunrId;

    /**
     *
     * @param turnID
     */
    public void gameTurn(int turnID) {
        this.gameTunrId = turnID;
        //System.out.println("------------>"+turnID+"<------------");

        if (turnID == 0) {
            this.sePlatescConsumatorii();
            this.distribuitoriiAlegProducatorii();
            this.calculeazaCostulDeProductieDistribuitori();
            this.calculeazaProfitulDistribuitorilor();
            this.consumatoriiAlegDistribuitorii();
            this.consumatoriiPlatescDistribuitorii();
            this.distribuitoriiPlatescTaxele();
        } else {
            this.aplicaSchimbari(turnID);
            this.sePlatescConsumatorii();
            this.calculeazaCostulDeProductieDistribuitori();
            this.calculeazaProfitulDistribuitorilor();
            this.consumatoriiAlegDistribuitorii();
            this.consumatoriiPlatescDistribuitorii();
            this.distribuitoriiPlatescTaxele();
            this.listaSchimbariLunare.get(turnID - 1).aplicaSchimbare(
                    new StrategiaModificaProducator(),
                    listaConsumatori,
                    listaDistribuitori,
                    listaProducatori);
            this.distribuitoriiAlegProducatorii();
            this.declaraClientiProducatori();

        }
    }
}
