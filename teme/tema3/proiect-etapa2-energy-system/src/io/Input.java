package io;

import entities.Distribuitor;
import entities.EnergyType;
import entities.Producator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import entities.Consumator;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import strategies.EnergyChoiceStrategyType;
import utils.SchimbareDistribuitori;
import utils.SchimbareProducatori;
import utils.SchimbariLunare;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Input {
    private String numeleFisieruluiJSON;

    /**
     *
     * @param numeleFisieruluiJSON
     */
    public Input(String numeleFisieruluiJSON) {
        this.numeleFisieruluiJSON = numeleFisieruluiJSON;
    }

    /**
     *
     * @param listaConsumatori
     * @param listaDistribuitori
     * @param listaProducatori
     * @param listaSchimbariLunare
     */
    public void setInput(ArrayList<Consumator> listaConsumatori,
                          ArrayList<Distribuitor> listaDistribuitori,
                          ArrayList<Producator> listaProducatori,
                          ArrayList<SchimbariLunare> listaSchimbariLunare) {

        JSONParser parser = new JSONParser();
        try {
            Object fisierJSON = parser.parse(new FileReader(this.numeleFisieruluiJSON));
            JSONObject jsonObject = (JSONObject)  fisierJSON;
            JSONObject initialData = (JSONObject) jsonObject.get("initialData");
            JSONArray consumatori = (JSONArray) initialData.get("consumers");
            JSONArray distribuitori = (JSONArray) initialData.get("distributors");
            JSONArray producatori = (JSONArray) initialData.get("producers");
            JSONArray schimbariLunare = (JSONArray) jsonObject.get("monthlyUpdates");

            /// TODO aici adaug consumatorii in lista de consumatori
            for (int i = 0; i < consumatori.size(); ++i) {
                JSONObject jsonConsumator = (JSONObject) consumatori.get(i);
                String id = String.valueOf(jsonConsumator.get("id"));
                String monthlyIncome = String.valueOf(jsonConsumator.get("monthlyIncome"));
                String initialBudget = String.valueOf(jsonConsumator.get("initialBudget"));
                listaConsumatori.add(new Consumator(Integer.parseInt(id),
                        Integer.parseInt(initialBudget),
                        Integer.parseInt(monthlyIncome)));
            }
            /// TODO aici adaug distribuitorii in lista de distribuitori
            for (int i = 0; i < distribuitori.size(); i++) {
                JSONObject jsonDistribuitor = (JSONObject) distribuitori.get(i);
                String id = String.valueOf(jsonDistribuitor.get("id"));
                String contractLength = String.valueOf(jsonDistribuitor.get("contractLength"));
                String initialBudget = String.valueOf(jsonDistribuitor.get("initialBudget"));
                String infrastrucutureCost = String.valueOf(
                        jsonDistribuitor.get("initialInfrastructureCost"));
                String energyNeeded = String.valueOf(jsonDistribuitor.get("energyNeededKW"));
                String producerStrategy = String.valueOf(jsonDistribuitor.get("producerStrategy"));
                EnergyChoiceStrategyType strategyType = null;
                switch (producerStrategy) {
                    case "GREEN" :
                        strategyType = EnergyChoiceStrategyType.GREEN;
                        break;
                    case "PRICE" :
                        strategyType = EnergyChoiceStrategyType.PRICE;
                        break;
                    case "QUANTITY" :
                        strategyType = EnergyChoiceStrategyType.QUANTITY;
                        break;
                    default:
                        break;
                }
                listaDistribuitori.add(new Distribuitor(Integer.parseInt(id),
                        Integer.parseInt(contractLength),
                        Integer.parseInt(infrastrucutureCost),
                        Integer.parseInt(energyNeeded),
                        Integer.parseInt(initialBudget),
                        strategyType));
            }
            ///TODO aici adaug producatorii in lista de producatori
            for (int i = 0; i < producatori.size(); i++) {
                JSONObject jsonProducator = (JSONObject) producatori.get(i);
                String id = String.valueOf(jsonProducator.get("id"));
                String energyType = String.valueOf(jsonProducator.get("energyType"));
                String maxDistribuitors = String.valueOf(jsonProducator.get("maxDistributors"));
                String priceKW = String.valueOf(jsonProducator.get("priceKW"));
                String energyPerDistribuitor = String.valueOf(
                        jsonProducator.get("energyPerDistributor"));
                EnergyType energyType1 = EnergyType.SOLAR;
                switch (energyType) {
                    case "COAL" :
                        energyType1 = EnergyType.COAL;
                        break;
                    case "WIND" :
                        energyType1 = EnergyType.WIND;
                        break;
                    case "HYDRO" :
                        energyType1 = EnergyType.HYDRO;
                        break;
                    case "NUCLEAR" :
                        energyType1 = EnergyType.NUCLEAR;
                        break;
                    case "SOLAR" :
                        energyType1 = EnergyType.SOLAR;
                        break;
                    default:
                        break;
                }
                listaProducatori.add(new Producator(Integer.parseInt(id),
                        Integer.parseInt(maxDistribuitors),
                        energyType1,
                        Double.parseDouble(priceKW),
                        Integer.parseInt(energyPerDistribuitor)));
            }
            /// TODO adaug schimbari lunare in lista de schimbari lunare
            for (int i = 0; i < schimbariLunare.size(); i++) {
                JSONObject jsonSchimbareLunara = (JSONObject) schimbariLunare.get(i);
                JSONArray consumatorNouArray = (JSONArray) jsonSchimbareLunara.get("newConsumers");
                JSONObject jsonConsumatorNou = null;
                /// TODO NOUL CONSUMATOR
                if (consumatorNouArray.size() != 0) {
                    jsonConsumatorNou = (JSONObject) consumatorNouArray.get(0);
                }
                Consumator consumatorNou = null;
                if (jsonConsumatorNou != null) {
                    int id = Integer.parseInt(String.valueOf(jsonConsumatorNou.get("id")));
                    int initialBudget = Integer.parseInt(String.valueOf(
                            jsonConsumatorNou.get("initialBudget")));
                    int monthlyIncome = Integer.parseInt(String.valueOf(
                            jsonConsumatorNou.get("monthlyIncome")));
                    consumatorNou = new Consumator(id,
                            initialBudget,
                            monthlyIncome);
                }
                JSONArray schimbareDistribuitorArray = (JSONArray)
                        jsonSchimbareLunara.get("distributorChanges");
                JSONObject jsonSchimbareDistribuitor = null;
                ///TODO SCHIMBARE DISTRIBUITORI
                if (schimbareDistribuitorArray.size() != 0) {
                    jsonSchimbareDistribuitor = (JSONObject) schimbareDistribuitorArray.get(0);
                }
                SchimbareDistribuitori schimbareDistribuitori = null;
                if (jsonSchimbareDistribuitor != null) {
                    int id = Integer.parseInt(String.valueOf(jsonSchimbareDistribuitor.get("id")));
                    int infrastructureCost = Integer.parseInt(String.valueOf(
                            jsonSchimbareDistribuitor.get("infrastructureCost")));
                    schimbareDistribuitori = new SchimbareDistribuitori(id,
                            infrastructureCost);
                }
                JSONArray schimbareProducatoriArray = (JSONArray)
                        jsonSchimbareLunara.get("producerChanges");
                JSONObject jsonSchimbareProducatori = null;
                ArrayList<SchimbareProducatori> schimbareProducatoriArrayList = new ArrayList<>();
                for (int j = 0; j < schimbareProducatoriArray.size(); j++) {
                    if (schimbareProducatoriArray.size() != 0) {
                        jsonSchimbareProducatori = (JSONObject) schimbareProducatoriArray.get(j);
                    }
                    SchimbareProducatori schimbareProducatori = null;
                    if (jsonSchimbareProducatori != null) {
                        int id = Integer.parseInt(
                                String.valueOf(jsonSchimbareProducatori.get("id")));
                        int energyPerDistribuitor = Integer.parseInt(String.valueOf(
                                jsonSchimbareProducatori.get("energyPerDistributor")));
                        schimbareProducatori = new SchimbareProducatori(id,
                                energyPerDistribuitor);
                        schimbareProducatoriArrayList.add(schimbareProducatori);
                    }
                }
                listaSchimbariLunare.add(new SchimbariLunare(consumatorNou,
                        schimbareDistribuitori,
                        schimbareProducatoriArrayList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject fisierulJSON = new JSONObject();
    }
}
