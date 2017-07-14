package mivadounou.projet2.uut.ucao.mivadounou.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.models.CommandeMenu;

/**
 * Created by leinad on 7/13/17.
 */

public class CommandeDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TIME_PATTERN = "HH:mm";

    private TextView cMenuTitleTextView;
    private TextView cMenuPriceTextView;
    private TextView cMenuRestauTitleTextView;
    private TextView cMenuEndAtDateTextView;
    private TextView cMenuEndAtTimeTextView;

    private ImageView cMenuChooseDate;
    private ImageView cMenuChooseTime;
    private ImageView cMenuIconType;

    private Button cMenuSend;
    private Button cMenuCancel;

    private DatabaseReference mDatabase;

    private EditText quantityEditText;

    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private CommandeMenu commandeMenu;

    public static CommandeDialogFragment newInstance(CommandeMenu commandeMenu) {

        CommandeDialogFragment commandeDialogFragment = new CommandeDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("commandeMenu", commandeMenu);
        commandeDialogFragment.setArguments(args);
        return commandeDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commandeMenu = getArguments().getParcelable("commandeMenu");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.commande_menu_dialog, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        cMenuTitleTextView = (TextView) v.findViewById(R.id.commande_menu_title);
        cMenuPriceTextView = (TextView) v.findViewById(R.id.commande_menu_price);
        cMenuRestauTitleTextView = (TextView) v.findViewById(R.id.commande_menu_restau);
        cMenuEndAtDateTextView = (TextView) v.findViewById(R.id.commande_menu_end_at_date);
        cMenuEndAtTimeTextView = (TextView) v.findViewById(R.id.commande_menu_end_at_time);

        quantityEditText = (EditText) v.findViewById(R.id.commande_menu_qte);
        quantityEditText.setText("1");

        cMenuChooseDate = (ImageView) v.findViewById(R.id.commande_menu_choose_date);
        cMenuChooseTime = (ImageView) v.findViewById(R.id.commande_menu_choose_Time);
        cMenuIconType = (ImageView) v.findViewById(R.id.menu_icon_type);

        cMenuEndAtDateTextView.setText(dateFormat.format(calendar.getTime()));
        cMenuEndAtTimeTextView.setText(timeFormat.format(calendar.getTime()));

        switch (commandeMenu.getMenuType()) {
            case "Plat Africain":
                cMenuIconType.setImageResource(R.drawable.ic_restaurant);
                break;
            case "Pizza":
                cMenuIconType.setImageResource(R.drawable.ic_pie);
                break;
            case "Burger":
                cMenuIconType.setImageResource(R.drawable.ic_fast_food);
                break;
        }

        cMenuTitleTextView.setText(commandeMenu.getMenuTitle());
        cMenuPriceTextView.setText("Prix : " + commandeMenu.getUnitPrice() + " FCFA");
        cMenuRestauTitleTextView.setText(commandeMenu.getRestauTitle());

        cMenuChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        cMenuChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        cMenuSend = (Button) v.findViewById(R.id.commande_menu_send);
        cMenuCancel = (Button) v.findViewById(R.id.commande_menu_cancel);

        cMenuSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "From dialog Fragment", Toast.LENGTH_SHORT).show();
                if (quantityEditText.getText().toString().isEmpty()) {
                    quantityEditText.setError("Quantité invalide");
                    return;
                } else if (Integer.valueOf(quantityEditText.getText().toString()) <= 0) {
                    quantityEditText.setError("Quantité invalide");
                    return;
                }

                MainActivity.mProgressDialog.setMessage("Envoie de la commande encoure");
                MainActivity.showProgressDialog();

                sendCommande();
            }
        });

        cMenuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void sendCommande() {
        int quantity = Integer.valueOf(quantityEditText.getText().toString());
        commandeMenu.setQuantity(quantity);
        commandeMenu.setTotalPrice(quantity * commandeMenu.getUnitPrice());
        commandeMenu.setEndAtTimestamp(calendar.getTimeInMillis());
        commandeMenu.setCreateAtTimestamp(Calendar.getInstance().getTimeInMillis());
        commandeMenu.setStatus(CommandeMenu.COMMANDE_SENT);

        String newCommandeKey = mDatabase.child("user-commandes").push().getKey();

        Map<String, Object> commandeMenuValues = commandeMenu.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/commande/" + newCommandeKey, commandeMenuValues);
        childUpdates.put("/user-commandes/" + commandeMenu.getUserKey() + "/" + newCommandeKey, commandeMenuValues);
        childUpdates.put("/restau-commandes/" + commandeMenu.getRestauKey() + "/" + newCommandeKey, commandeMenuValues);

        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        MainActivity.hideProgressDialog();

                        dismiss();

                        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sweetAlertDialog
                                        .setTitleText("Commande éffectuée")
                                        .setContentText("Commande envoyée avec succès au retaurant " + commandeMenu.getRestauTitle())
                                        .setConfirmClickListener(null)
                                        .show();
                            }
                        }, 50);


                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        MainActivity.hideProgressDialog();

                        Toast.makeText(getActivity(), "Failed to update children " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showDatePickerDialog() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    public void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                true
        );
        timePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);

        updateDateTime();
    }

    @Override
    public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        updateDateTime();
    }

    public void updateDateTime() {
        cMenuEndAtDateTextView.setText(dateFormat.format(calendar.getTime()));
        cMenuEndAtTimeTextView.setText(timeFormat.format(calendar.getTime()));
    }
}
