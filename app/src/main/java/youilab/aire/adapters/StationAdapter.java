package youilab.aire.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import youilab.aire.R;
import youilab.aire.StationInformation;
import youilab.aire.IntegrASCore.Stations.Station;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

    private List<Station> mDataSetStations;
    private Context myContext;
    private String codeNet;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station, parent, false);

        StationAdapter.MyViewHolder vh = new StationAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
// set the data in items
        holder.textName.setText(mDataSetStations.get(position).getName());

        setColorStation(mDataSetStations.get(position).getCalidadDelAire(),holder.cardView,holder.iconStation);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(myContext, StationInformation.class);
                intent.putExtra("idStation", String.valueOf(mDataSetStations.get(position).getIdStation())); // put station id in Intent
                intent.putExtra("codeNet",codeNet); //put net code in Intent
                myContext.startActivity(intent); // start Intent
            }
        });
    }

    private void setColorStation(int statusAirValue, CardView card, ImageView iconStation) {

        if (statusAirValue == 1) {
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.bien));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_verde));
        } else if (statusAirValue == 2) {
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.aceptable));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_amarillo));
        } else if (statusAirValue == 3) {
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.mala));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_naranja));
        } else if (statusAirValue == 4) {
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.muy_mala));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_rojo));
        } else if (statusAirValue == 5) {
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.extremadamente_mala));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_morado));
        } else if (statusAirValue == 6){
            card.setCardBackgroundColor(myContext.getResources().getColor(R.color.grey));
            iconStation.setImageDrawable(myContext.getResources().getDrawable(R.drawable.marcador_blanco));
        }
    }
    @Override
    public int getItemCount() {
        return mDataSetStations.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textName;
        public ImageView iconStation;

        public MyViewHolder(CardView v) {
            super(v);
            cardView = v;
            textName = v.findViewById(R.id.nameStation);
            iconStation = v.findViewById(R.id.imgIconStation);

        }
    }

    public StationAdapter(List<Station> myDataset, Context context,String codeNet) {
        mDataSetStations = myDataset;
        this.myContext= context;
        this.codeNet= codeNet;
    }
}
