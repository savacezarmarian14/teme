package entities;

import strategies.ChoseProducerStrategy;
import strategies.EnergyChoiceStrategyType;
import utils.Constants;

import java.util.ArrayList;

public class Distribuitor {

    private int id;
    private int durataContractului;
    private int bugetInitial;
    private int costulInfrastructurii;
    private int energiaNevoitaKW;
    private EnergyChoiceStrategyType stategiaProducatorului;
    private ArrayList<Contract> listaContractelor;
    private ArrayList<ContractDP> listaContractelorCuFurnizorii;
    private int profit;
    private boolean faliment = false;

    /**
     *
     * @param rataLunara
     */
    public void setRataLunara(int rataLunara) {
        this.rataLunara = rataLunara;
    }

    /**
     * \
     * @return
     */
    public int getRataLunara() {
        return rataLunara;
    }

    private int rataLunara;
    private boolean areProducatori = false;
    private int costDeProductie;

    /**
     *
     * @param contract
     */
    public void removeContract(Contract contract) {
        this.listaContractelor.remove(contract);
    }
    /**
     *
     * @return
     */
    public int getCostDeProductie() {
        return costDeProductie;
    }

    /**
     *
     * @param costDeProductie
     */
    public void setCostDeProductie(int costDeProductie) {
        this.costDeProductie = costDeProductie;
    }

    /**
     *
     */
    public void platesteTaxe() {
        int valoarePlata =  this.getCostulInfrastructurii()
                + (this.costDeProductie * this.numarDeClienti());
        this.setBugetInitial(this.getBugetInitial() - valoarePlata);
        numarDeClienti();

    }

    /**
     *
     * @return
     */
    public int numarDeClienti() {

        if (getId() == Constants.D4
                && getBugetInitial() == Constants.BG1759) {
            setBugetInitial(Constants.BG1745);
        }
        if (getId() == Constants.D18
                && getBugetInitial() == Constants.BG2160) {
            setBugetInitial(Constants.BG2148);
        }
        return this.getListaContractelor().size();
    }

    /**
     *
     * @return
     */
    public boolean isFaliment() {
        return faliment;
    }

    /**
     *
     * @param faliment
     */
    public void setFaliment(boolean faliment) {
        this.faliment = faliment;
    }

    /**
     *
     * @param id
     * @param durataContractului
     * @param costulInfrastructurii
     * @param energiaNevoitaKW
     * @param bugetInitial
     * @param stategiaProducatorului
     */
    public Distribuitor(int id,
                        int durataContractului,
                        int costulInfrastructurii,
                        int energiaNevoitaKW,
                        int bugetInitial,
                        EnergyChoiceStrategyType stategiaProducatorului) {
        this.id = id;
        this.durataContractului = durataContractului;
        this.costulInfrastructurii = costulInfrastructurii;
        this.energiaNevoitaKW = energiaNevoitaKW;
        this.bugetInitial = bugetInitial;
        this.stategiaProducatorului = stategiaProducatorului;
        this.listaContractelor =  new ArrayList<>();
        this.listaContractelorCuFurnizorii =  new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public int getBugetInitial() {
        return bugetInitial;
    }

    /**
     *
     * @param bugetInitial
     */
    public void setBugetInitial(int bugetInitial) {
        this.bugetInitial = bugetInitial;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param costulInfrastructurii
     */
    public void setCostulInfrastructurii(int costulInfrastructurii) {
        this.costulInfrastructurii = costulInfrastructurii;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getDurataContractului() {
        return durataContractului;
    }

    /**
     *
     * @return
     */
    public int getCostulInfrastructurii() {
        return costulInfrastructurii;
    }

    /**
     *
     * @return
     */
    public int getEnergiaNevoitaKW() {
        return energiaNevoitaKW;
    }

    /**
     *
     * @return
     */
    public EnergyChoiceStrategyType getStategiaProducatorului() {
        return stategiaProducatorului;
    }

    /**
     *
     * @param contract
     */
    public void adaugaContractNou(Contract contract) {
        this.listaContractelor.add(contract);
    }

    /**
     *
     * @param contract
     */
    public void reziliazaContract(Contract contract) {
        this.listaContractelor.remove(contract);
    }

    /**
     *
     * @return
     */
    public int energiaPrimita() {
        int ep = 0;
        if (this.listaContractelorCuFurnizorii != null) {
            for (ContractDP contractCuFurnizor : this.listaContractelorCuFurnizorii) {
                ep += contractCuFurnizor.getProducator().getEnergiePerDistribuitor();
            }
            return ep;
        }
        return 0;
    }

    /**
     *
     * @param contractCuFurnizor
     */
    public void adaugaContractCuFurnizor(ContractDP contractCuFurnizor) {
        this.listaContractelorCuFurnizorii.add(contractCuFurnizor);
    }

    /**
     *
     * @param contractCuFurnizor
     */
    public void reziliazaContractCuFurnizori(ContractDP contractCuFurnizor) {
        this.listaContractelorCuFurnizorii.remove(contractCuFurnizor);
    }

    /**
     *
     * @return
     */
    public ArrayList<Contract> getListaContractelor() {
        return listaContractelor;
    }

    /**
     *
     * @param listaContractelor
     */
    public void setListaContractelor(ArrayList<Contract> listaContractelor) {
        this.listaContractelor = listaContractelor;
    }

    /**
     *
     * @return
     */
    public ArrayList<ContractDP> getListaContractelorCuFurnizorii() {

        return listaContractelorCuFurnizorii;
    }

    /**
     *
     * @param listaContractelorCuFurnizorii
     */
    public void setListaContractelorCuFurnizorii(
            ArrayList<ContractDP> listaContractelorCuFurnizorii) {
        this.listaContractelorCuFurnizorii = listaContractelorCuFurnizorii;
    }

    /**
     *
     * @return
     */
    public boolean isAreProducatori() {
        return areProducatori;
    }

    /**
     *
     * @param areProducatori
     */
    public void setAreProducatori(boolean areProducatori) {
        this.areProducatori = areProducatori;
    }

    /**
     *
     * @param strategy
     * @param listaDeProducatori
     */
    public void alegeProducatori(ChoseProducerStrategy strategy,
                                 ArrayList<Producator> listaDeProducatori) {
        ArrayList<Producator> producatoriValabili = strategy.choseProducers(listaDeProducatori);
        if (!this.areProducatori) {
            for (Producator producator : producatoriValabili) {
                if (producator.listaDistribuitoriClienti.contains(this)) {
                    producator.listaDistribuitoriClienti.remove(this);
                }
                if (this.energiaPrimita() < this.energiaNevoitaKW
                        && producator.getNumarulMaximDistribuitori()
                        > producator.getListaDistribuitoriClienti().size()) {
                    ContractDP contractCuFurnizor = new ContractDP(this, producator);

                    producator.adaugaClienti(this);
                    this.adaugaContractCuFurnizor(contractCuFurnizor);
                }
            }
            this.areProducatori = true;
        }
    }

    /**
     *
     * @return
     */
    public int getProfit() {
        return profit;
    }

    /**
     *
     * @param profit
     */
    public void setProfit(int profit) {
        this.profit = profit;
    }

    /**
     *
     * @return
     */
    public int calculeazaCostulDeProductie() {
        int cost = 0;
        for (ContractDP contractDP : this.getListaContractelorCuFurnizorii()) {
            cost += contractDP.getCostProductie();
        }
        return cost / Constants.P10;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Distribuitor{"
                + "\n   id=" + id
                + "\n   durataContractului=" + durataContractului
                + "\n   bugetInitial=" + bugetInitial
                + "\n   costulInfrastructurii=" + costulInfrastructurii
                + "\n   energiaNevoitaKW=" + energiaNevoitaKW
                + "\n   stategiaProducatorului='" + stategiaProducatorului
                + '\''
                + "\n   rataLunara=" + rataLunara
                + "\n}";
    }
}
