package youilab.aire.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import youilab.aire.R;
import youilab.aire.IntegrASCore.Stations.Station;
import youilab.aire.IntegrASCore.Nets.Net;
import youilab.aire.StationInformation;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;
    private List<Net> mDataSetStations;
private Context myContext;
private Activity myActivity;



// Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textView;
        public RelativeLayout bodyStatus;
        public ImageView imgStatus;

    public TextView textViewDate;
    public TextView textViewState;
    public TextView textViewLabel;
        public MyViewHolder(CardView v) {
            super(v);
            cardView = v;
            textView = v.findViewById(R.id.nombreRed);
            textViewLabel = v.findViewById(R.id.lblCalidad);
            textViewDate = v.findViewById(R.id.fecha);
            bodyStatus = v.findViewById(R.id.bodyStationInfo);
            imgStatus = v.findViewById(R.id.icon_verde);
            textViewState = v.findViewById(R.id.estado);
        }
    }

    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    public MyAdapter(List<Net> myDataset, Context context,Activity activity) {
        mDataSetStations = myDataset;
        this.myContext= context;
        this.myActivity = activity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_net, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
// - replace the contents of the view with that element
        Net myStationNet = mDataSetStations.get(position);
        holder.textView.setText(myStationNet.getName());
        int statusAir=1;
        String estado="";
        String fecha="";
        boolean bndNotAccess=true;

        for (Station station : myStationNet.getStationList()
        ) {
            if(station.getCalidadDelAire()!=6) {
                if (statusAir <= station.getCalidadDelAire()) {
                    statusAir = station.getCalidadDelAire();
                    estado = station.getState();
                    fecha = station.getFecha();
                }
                bndNotAccess=false;

            }else{
                estado = station.getState();
                fecha = station.getFecha();
            }
        }

            if(bndNotAccess)
                statusAir=6;

         changeColors(statusAir,holder);

        String[] datosFecha = fecha.split("T");
        if(myStationNet.getStationList().size()>1)
        holder.textViewDate.setText(myStationNet.getStationList().size()+" estaciones");
        else
            holder.textViewDate.setText(myStationNet.getStationList().size()+" estación");

        holder.textViewState.setText(estado);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// se inicia el cuadro de dialogo
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setTitle("Selecciona una estación");

                // A la lista de titilos de encuesta se le agregan todas las disponibles
                ArrayList<String> stringSurveyList = new ArrayList<String>();
                for (Station station: myStationNet.getStationList()) {
                    stringSurveyList.add(station.getName());
                }

                //creamos un arreglo adaptable a la cantidad de encuesta en la lista
                String[] arr = new String[stringSurveyList.size()];
                arr = stringSurveyList.toArray(arr);

                //creamos al conjunto de datos en el cuadro de dialogo y les ponemos un envento de click a cada uno
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(myActivity, StationInformation.class);
                        intent.putExtra("idStation", String.valueOf(myStationNet.getStationList().get(which).getIdStation())); // put station id in Intent
                        intent.putExtra("codeNet",myStationNet.getCodeNet()); //put net code in Intent
                        myActivity.startActivity(intent); // start Intent
                        //se abre la actividad para la captura de la encuesta selecciond
                            //se envia a la actividad la infoamcrion de latitud, altitud, longitud y el id de la encuesta seleccinada y que se va a aplicar




                    }
                });

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void changeColors(int statusAir, MyViewHolder holder)
    {
        if(statusAir==2){
            holder.cardView.setCardBackgroundColor(myContext.getResources().getColor(R.color.aceptable));

            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_amarillo));
            /*
            holder.textViewState.setTextColor(myContext.getColor(R.color.grey));
            holder.textViewDate.setTextColor(myContext.getColor(R.color.grey));
            holder.textView.setTextColor(myContext.getColor(R.color.grey));
            holder.textViewLabel.setTextColor(myContext.getColor(R.color.grey));
            */
        }else  if(statusAir==3){
            holder.cardView.setCardBackgroundColor(myContext.getResources().getColor(R.color.mala));
            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_naranja));
        }else
        if(statusAir==4){
            holder.cardView.setCardBackgroundColor(myContext.getResources().getColor(R.color.muy_mala));
            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_rojo));
        }else  if(statusAir==5){
            holder.cardView.setCardBackgroundColor(myContext.getResources().getColor(R.color.extremadamente_mala));
            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_morado));
        }else  if(statusAir==6){
            holder.cardView.setCardBackgroundColor(myContext.getResources().getColor(R.color.greylight));
            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_blanco));
        }else  if(statusAir==1){
            holder.cardView.setCardBackgroundColor(myContext.getColor(R.color.bien));
            holder.imgStatus.setImageDrawable(myContext.getDrawable(R.drawable.marcador_verde));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSetStations.size();
    }
}
