package io;

import entities.Consumator;
import entities.Contract;
import entities.Distribuitor;
import entities.Producator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

public class Output {
    private ArrayList<Consumator> consumatori;
    private ArrayList<Distribuitor> distribuitori;
    private ArrayList<Producator> producatori;
    private String fisierOutput;

    /**
     *
     * @param consumatori
     * @param distribuitori
     * @param producatori
     * @param fisierOutput
     */
    public Output(ArrayList<Consumator> consumatori,
                  ArrayList<Distribuitor> distribuitori,
                  ArrayList<Producator> producatori,
                  String fisierOutput) {
        this.consumatori = consumatori;
        this.distribuitori = distribuitori;
        this.producatori = producatori;
        this.fisierOutput = fisierOutput;
    }

    /**
     *
     * @throws IOException
     */
    public void writeOutput() throws IOException {
        JSONObject output = new JSONObject();
        /// Creez jsonConsumatori
        JSONArray jsonConsumatori = new JSONArray();
        for (Consumator consumator : consumatori) {
            JSONObject jsonConsumator = new JSONObject();
            jsonConsumator.put("id", consumator.getId());
            jsonConsumator.put("isBankrupt", consumator.isFaliment());
            jsonConsumator.put("budget", consumator.getBugetInitial());
            jsonConsumatori.add(jsonConsumator);
        }
        output.put("consumers", jsonConsumatori);

        /// Creez jsonDistribuitori
        JSONArray jsonDistribuitori = new JSONArray();
        for (Distribuitor distribuitor : distribuitori) {
            JSONObject jsonDistribuitor = new JSONObject();
            jsonDistribuitor.put("id", distribuitor.getId());
            jsonDistribuitor.put("energyNeededKW", distribuitor.getEnergiaNevoitaKW());
            jsonDistribuitor.put("contractCost", distribuitor.getRataLunara());
            jsonDistribuitor.put("budget", distribuitor.getBugetInitial());
            jsonDistribuitor.put("producerStrategy",
                    distribuitor.getStategiaProducatorului().label);
            jsonDistribuitor.put("isBankrupt", distribuitor.isFaliment());
            /// Creez jsonContracte
            JSONArray jsonContracte = new JSONArray();
            for (Contract contract : distribuitor.getListaContractelor()) {
                JSONObject jsonContract = new JSONObject();
                jsonContract.put("consumerId", contract.getConsumator().getId());
                jsonContract.put("price", contract.getValoarePlata());
                jsonContract.put("remainedContractMonths", contract.getDurataContractului());
                jsonContracte.add(jsonContract);
            }
            jsonDistribuitor.put("contracts", jsonContracte);
            jsonDistribuitori.add(jsonDistribuitor);
        }
        output.put("distributors", jsonDistribuitori);

        /// Creez jsonProducatori
        JSONArray jsonProducatori = new JSONArray();
        for (Producator producator : producatori) {
            JSONObject jsonProducator = new JSONObject();
            jsonProducator.put("id", producator.getId());
            jsonProducator.put("maxDistributors", producator.getNumarulMaximDistribuitori());
            jsonProducator.put("priceKW", producator.getPretKW());
            jsonProducator.put("energyType", producator.getTipulEnergiei().getLabel());
            jsonProducator.put("energyPerDistributor", producator.getEnergiePerDistribuitor());
            /// creez jsonMonthlyStats
            JSONArray jsonMonthlyStats = new JSONArray();
            int month = 1;
            for (ArrayList<Integer> integerArrayList : producator.istoricClienti) {
                JSONObject jsonMonthlyStat = new JSONObject();
                Collections.sort(integerArrayList);
                jsonMonthlyStat.put("month", month++);
                jsonMonthlyStat.put("distributorsIds", integerArrayList);
                jsonMonthlyStats.add(jsonMonthlyStat);
            }
            jsonProducator.put("monthlyStats", jsonMonthlyStats);
            jsonProducatori.add(jsonProducator);
        }
        output.put("energyProducers", jsonProducatori);

        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(this.fisierOutput));
            writer.write(output.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) {

                }
            }
        }
    }
}
