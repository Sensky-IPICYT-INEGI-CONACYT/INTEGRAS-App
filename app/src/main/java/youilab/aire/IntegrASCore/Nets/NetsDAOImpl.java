package youilab.aire.IntegrASCore.Nets;

import android.content.Context;
import android.location.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.IntegrASCore.Stations.StationsDAOImpl;
import youilab.aire.processingdata.parserJSONAirAnalysisInformation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetsDAOImpl implements NetsDAO{

    //lista de redes de monitoreo
    List<Net> NetsList;

    private StationsDAOImpl stationsDAO;




    //
    public NetsDAOImpl(){
        this.NetsList = new ArrayList<>();
        this.stationsDAO = new StationsDAOImpl();
    }
    @Override
    public List<Net> getAllNets() {

        //List<Net> listNetStations = new parserJSONAirAnalysisInformation().getAirInformationStations();
        JSONArray listNetStationsJSONArray = new parserJSONAirAnalysisInformation().getJSONNets();//se obtiene el array de redes de monitoreo

        List<Net> listNetStations = CreateNetsListObjects(listNetStationsJSONArray);//se crean las redes de monitoreo y cada una con sus estaciones de monitoreo

        return listNetStations;
    }

    @Override
    public Net getNetByCode(String netId) {
        JSONArray listNetStationsJSONArray = new parserJSONAirAnalysisInformation().getJSONNets();//se obtiene el array de redes de monitoreo

        List<Net> listNetStations = CreateNetsListObjects(listNetStationsJSONArray);//se crean las redes de monitoreo y cada una con sus estaciones de monitoreo

        return searchNet(netId,listNetStations);
    }

    @Override
    public Station getStationByCode(Net net, String stationId) {
        return searchStation(stationId,net);
    }

    @Override
    public Net getNetByLocation(Location myLocation) {

        Net myNetStation = null;
        Double distance=null;


        JSONArray listNetStationsJSONArray = new parserJSONAirAnalysisInformation().getJSONNets();//se obtiene el array de redes de monitoreo

        List<Net> listNetStations = CreateNetsListObjects(listNetStationsJSONArray);//se crean las redes de monitoreo y cada una con sus estaciones de monitoreo

        if(listNetStations!=null) {//si se tienen redes de estaciones de monitoreo
            for (Net net : listNetStations//un ciclo para todas las redes de monitoreo
            ) {
                for (Station station: net.getStationList()//para cada estacion de cada red de monitoreo
                ) {
                    Double tmpDistance = getDistance(station.getStationLocation(), myLocation);

                    if (distance == null) {//si no hay alguna asignada se toma la primera por defecto
                        distance = tmpDistance;
                        myNetStation = net;

                    } else if (tmpDistance < distance) {//si hay otra estación de monitorio mas cerca ahora esa red se escoge
                        distance = tmpDistance;
                        myNetStation = net;

                    }
                }
            }
        }

        if(myNetStation!= null){
            calculateStatusNet(true,myNetStation);//se asigna el nivel de riesgo de la red que se encontro mas cerca
        }

        return myNetStation;
    }



    private List<Net> CreateNetsListObjects(JSONArray jsonArrayNets){
        NetsList = new ArrayList<>();

        boolean bndSameCodeFounded=false;
        Iterator<JSONObject> iterator = jsonArrayNets.iterator();

        while (iterator.hasNext()) {
            JSONObject objt = iterator.next();
            if(NetsList.size()>0){
                bndSameCodeFounded=false;
                //Busca si la estación pertenece a alguna de las redes ya existentes
                for (int netNum =0;netNum<NetsList.size()&& !bndSameCodeFounded;netNum++) {
                    if(objt.get("CODE").toString().compareTo(NetsList.get(netNum).getCodeNet())==0)
                    {
                        //se agrega a la red si el codigo es el mismo
                        NetsList.get(netNum).addStation(this.stationsDAO.JSONToStation(objt));
                        bndSameCodeFounded=true;
                        break;
                    }

                }

                //si no se encontro su red, se crea una nueva con su primer observatorio
                if(bndSameCodeFounded==false)
                {
                    NetsList.add(CreateNetObject(objt));

                }
            }
            else
                NetsList.add(CreateNetObject(objt));


            // lbl.append(objt.get("STATE").toString());
            //lbl.append("\n");
        }

        return  NetsList;
    }

    private Net CreateNetObject(JSONObject objt){
        Net newNet = new Net();
        newNet.setName(objt.get("NAME").toString());
        newNet.setCodeNet(objt.get("CODE").toString());
        //Agregar la primera estación de monitoreo a la primera red
        newNet.addStation(this.stationsDAO.JSONToStation(objt));
        //Agregar la primera red de monitoreo a la lista de redes


        return newNet;
    }



    private Double getDistance(Location stationLocation, Location myLocation)
    {

        Double lon1 = Math.toRadians(stationLocation.getLongitude());
        Double lon2 = Math.toRadians(myLocation.getLongitude());
        Double lat1 = Math.toRadians(stationLocation.getLatitude());
        Double lat2 = Math.toRadians(myLocation.getLatitude());

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956 for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    private void calculateStatusNet(boolean bndNotAccess, Net myNetStation){
        int statusAir=1;
        String date="";


        for (Station station : myNetStation.getStationList()
        ) {

            if(station.getCalidadDelAire()!=6) {//si la estación esta funcionando y dando información
                if (statusAir < station.getCalidadDelAire()) {//Se toma la informacion con la peor calidad del aire y se le asigna a la red entera
                    statusAir = station.getCalidadDelAire();
                    date = station.getFecha();
                    setInfoStatus(statusAir, date,myNetStation);
                }
                bndNotAccess=false;
            }else
            {
                date=station.getFecha();
            }
        }


        if(bndNotAccess) {
            statusAir = 6;
            setInfoStatus(statusAir, date, myNetStation);
        }

    }


    private void setInfoStatus(int statusAir, String date, Net myNetStation)
    {
        if(statusAir==1)
            myNetStation.setStatusNameNet("Buena");
        else if(statusAir==2)
            myNetStation.setStatusNameNet("Aceptable");
        else if(statusAir==3)
            myNetStation.setStatusNameNet("Mala");
        else if(statusAir==4)
            myNetStation.setStatusNameNet("Muy Mala");
        else if(statusAir==5)
            myNetStation.setStatusNameNet("Extremadamente Mala");
        else if(statusAir==6)
            myNetStation.setStatusNameNet("No Disponible");

        myNetStation.setFecha(date);
        myNetStation.setStatusNet(statusAir);
    }


    private Net searchNet(String codeNet,List<Net> listNetStations)
    {
        Net myNet = new Net();
        for (Net net: listNetStations
        ) {
            if(net.getCodeNet().compareTo(codeNet)==0)
                myNet= net;
        }
        return myNet;
    }

    private Station searchStation(String idStation, Net myNet)
    {
        Station myStation = new Station();
        for (Station station: myNet.getStationList()
        ) {
            if(station.getIdStation()==Integer.valueOf(idStation))
                myStation=station;
        }
        return  myStation;
    }
}
