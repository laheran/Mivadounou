package mivadounou.projet2.uut.ucao.mivadounou.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;

/**
 * Created by leinad on 7/13/17.
 */

public class UserRestauCommandeViewHolder extends RecyclerView.ViewHolder {

    private static final String TIME_PATTERN = "HH:mm";

    private TextView cRestauAndMenuTitle;
    private TextView cQuantityAndPrice;
    private TextView cTotalPrice;
    private TextView cDateAndEndTime;
    private TextView cPastTime;
    private TextView cEndTime;
    private TextView cUsername;
    private TextView cStatus;

    private ImageView menuIconTypeImageView;

    private Button rejectCommande;
    private Button acceptCommande;
    private Button doneCommande;

    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

    public UserRestauCommandeViewHolder(View itemView) {

        super(itemView);

        cRestauAndMenuTitle = (TextView) itemView.findViewById(R.id.commande_menu_and_restau_name);
        cQuantityAndPrice = (TextView) itemView.findViewById(R.id.commande_quantity_and_unit_price);
        cTotalPrice = (TextView) itemView.findViewById(R.id.commande_total_price);
        cDateAndEndTime = (TextView) itemView.findViewById(R.id.commande_end_date_and_time);
        cPastTime = (TextView) itemView.findViewById(R.id.commande_past_time);
        cEndTime = (TextView) itemView.findViewById(R.id.commande_end_time);
        cUsername = (TextView) itemView.findViewById(R.id.commande_username);
        cStatus = (TextView) itemView.findViewById(R.id.commande_status);

        menuIconTypeImageView = (ImageView) itemView.findViewById(R.id.menu_icon_type);

        rejectCommande = (Button) itemView.findViewById(R.id.commande_reject);
        acceptCommande = (Button) itemView.findViewById(R.id.commande_accept);
        doneCommande = (Button) itemView.findViewById(R.id.commande_done);
    }

    public void bindToPost(CommandeMenu commandeMenu, View.OnClickListener rejectOnClickListener,
                           View.OnClickListener accepteOnClickListener, View.OnClickListener doneOnClickListener) {

        switch (commandeMenu.getStatus()) {
            case CommandeMenu.COMMANDE_SENT:
                cStatus.setText("Status : Reçu");
                break;

            case CommandeMenu.COMMANDE_REJECT:
                cStatus.setText("Status : Rejetter");

                acceptCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);

                rejectCommande.setEnabled(false);
                rejectCommande.setVisibility(View.GONE);

                doneCommande.setEnabled(false);
                doneCommande.setVisibility(View.GONE);
                break;

            case CommandeMenu.COMMANDE_CANCELED:
                cStatus.setText("Status : Annuler");

                acceptCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);

                rejectCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);

                doneCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);
                break;

            case CommandeMenu.COMMANDE_DONE:
                cStatus.setText("Status : Effectuer");

                acceptCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);

                rejectCommande.setEnabled(false);
                rejectCommande.setVisibility(View.GONE);

                doneCommande.setEnabled(false);
                doneCommande.setVisibility(View.GONE);
                break;

            case CommandeMenu.COMMANDE_ACCEPT:
                cStatus.setText("Status : Acceptée");

                acceptCommande.setEnabled(false);
                acceptCommande.setVisibility(View.GONE);

                rejectCommande.setEnabled(false);
                rejectCommande.setVisibility(View.GONE);

                doneCommande.setEnabled(true);
                break;
        }

        switch (commandeMenu.getMenuType()) {
            case "Plat Africain":
                menuIconTypeImageView.setImageResource(R.drawable.ic_restaurant);
                break;
            case "Pizza":
                menuIconTypeImageView.setImageResource(R.drawable.ic_pie);
                break;
            case "Burger":
                menuIconTypeImageView.setImageResource(R.drawable.ic_fast_food);
                break;
        }

        cRestauAndMenuTitle.setText(commandeMenu.getMenuTitle());
        cQuantityAndPrice.setText("Quantité : " + commandeMenu.getQuantity() + ", Prix unitaire : " + commandeMenu.getUnitPrice() + " FCFA");
        cTotalPrice.setText("Prix totale : " + commandeMenu.getTotalPrice() + " FCFA");
        cUsername.setText(commandeMenu.getUserName() + "   |   ");

        Calendar endAtCalendar = Calendar.getInstance();
        endAtCalendar.setTimeInMillis(commandeMenu.getEndAtTimestamp());

        cDateAndEndTime.setText("Pour le " + dateFormat.format(endAtCalendar.getTime()) + " à " + timeFormat.format(endAtCalendar.getTime()));

        Calendar createAtCalendar = Calendar.getInstance();
        createAtCalendar.setTimeInMillis(commandeMenu.getCreateAtTimestamp());

        PrettyTime prettyTime = new PrettyTime(new Locale("fr"));

        cPastTime.setText("Reçu " + prettyTime.format(createAtCalendar.getTime()));

        cEndTime.setText(prettyTime.format(endAtCalendar.getTime()));

        rejectCommande.setOnClickListener(rejectOnClickListener);
        acceptCommande.setOnClickListener(accepteOnClickListener);
        doneCommande.setOnClickListener(doneOnClickListener);
    }
}
