package mivadounou.projet2.uut.ucao.mivadounou.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mivadounou.projet2.uut.ucao.mivadounou.models.Person;
import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Akif;
import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Bpc;
import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Noces;
import mivadounou.projet2.uut.ucao.mivadounou.fragments_resto.Yogi;

/**
 * Created by LAHERAN on 05/04/2017.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private OnItemClickListener listener;
    List<Person> persons;

    public RVAdapter(List<Person> persons){

        this.persons = persons;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        private OnItemClickListener listener;

        ViewHolder(View itemView ) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (position) {
                case 0:
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new Noces();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, myFragment).addToBackStack(null).commit();
                    break;

                case 1:
                    AppCompatActivity aloc = (AppCompatActivity) v.getContext();
                    Fragment loc = new Yogi();
                    aloc.getSupportFragmentManager().beginTransaction().replace(R.id.content, loc).addToBackStack(null).commit();
                    break;

                case 2:
                    AppCompatActivity acm = (AppCompatActivity) v.getContext();
                    Fragment cm = new Akif();
                    acm.getSupportFragmentManager().beginTransaction().replace(R.id.content, cm).addToBackStack(null).commit();
                    break;
                default:
                    AppCompatActivity activityd = (AppCompatActivity) v.getContext();
                    Fragment rech = new Bpc();
                    activityd.getSupportFragmentManager().beginTransaction().replace(R.id.content, rech).addToBackStack(null).commit();
                    break;
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


    }


    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_main, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.personName.setText(persons.get(i).getName());
        viewHolder.personPhoto.setImageResource(persons.get(i).getPhotoId());
        viewHolder.personAge.setText(persons.get(i).getAge());
        viewHolder.setOnItemClickListener(listener);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }


    public interface OnItemClickListener{
        public void onItemClick(String textName);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
