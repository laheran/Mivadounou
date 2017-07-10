package mivadounou.projet2.uut.ucao.mivadounou.fragments.create;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.user.UserRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.models.Restau;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRestauFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRestauFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRestauFragment extends Fragment {

    public final static String RESTAU_CREATED = "RESTAU_CREATED";
    public final static String RESTAU_TITLE = "RESTAU_TITLE";
    public final static String RESTAU_KEY = "RESTAU_KEY";
    public final static String FIRST_TIME_USER_RESTAU = "FIRST_TIME_USER_RESTAU";

    private OnFragmentInteractionListener mListener;

    private EditText mRestauNameField;
    private EditText mRetauDescField;
    private EditText mRetauLocationField;

    private Button mNewRestau;

    private Activity mActivity;

    private FragmentTransaction fragmentTransaction;

    private ActionBar actionBar;

    private DatabaseReference mDatabase;

    private ImageView newRestauImageView;

    private FloatingActionButton chooseImageFloatingActionButton;

    private SharedPreferences userSharedPreferences;

    public NewRestauFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewRestauFragment newInstance() {
        NewRestauFragment fragment = new NewRestauFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_restau, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mNewRestau = (Button) rootView.findViewById(R.id.button_new_restau);
        mNewRestau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mRestauNameField.getText().toString())) {
                    mRestauNameField.setError("Nom du restaurant invalide.");
                    return;
                }
                if (TextUtils.isEmpty(mRetauDescField.getText().toString())) {
                    mRetauDescField.setError("Description du restaurant invalide.");
                    return;
                }
                if (TextUtils.isEmpty(mRetauLocationField.getText().toString())) {
                    mRetauLocationField.setError("Addresse du restaurant invalide.");
                    return;
                }

                MainActivity.mProgressDialog.setMessage("Creation du restaurant encoure...");
                MainActivity.showProgressDialog();
                submitNewRestau();
            }
        });

        newRestauImageView = (ImageView) rootView.findViewById(R.id.new_restau_image);
        chooseImageFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_menu_image);
        chooseImageFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseImage();
            }
        });

        mRestauNameField = (EditText) rootView.findViewById(R.id.restau_name_field);
        mRetauDescField = (EditText) rootView.findViewById(R.id.restau_descp_field);
        mRetauLocationField = (EditText) rootView.findViewById(R.id.restau_location_field);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        userSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
    }

    private void startChooseImage() {
        CropImage.activity()
                .setMinCropResultSize(500, 200)
                .setMaxCropResultSize(2080, 1780)
                .setAspectRatio(10, 7)
                .start(mActivity, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newRestauImageView.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void submitNewRestau() {

        FirebaseUser firebaseUser = getCurrentUser();

        Long tsLong = System.currentTimeMillis() / 1000;
        String currentTimeStamp = tsLong.toString();

        Restau restau = new Restau(firebaseUser.getUid(),
                mRestauNameField.getText().toString().trim(),
                mRetauDescField.getText().toString().trim(),
                mRetauLocationField.getText().toString().trim(),
                currentTimeStamp);

        createNewRestau(restau);
    }

    private void createNewRestau(final Restau restau) {

        final String newRestaukey = mDatabase.child("user-restaus").push().getKey();

        Map<String, Object> restauValues = restau.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/restau/" + newRestaukey, restauValues);
        childUpdates.put("/user-restaus/" + restau.getUid() + "/" + newRestaukey, restauValues);

        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userSharedPreferences.edit().putBoolean(NewRestauFragment.RESTAU_CREATED, true).apply();
                        userSharedPreferences.edit().putString(NewRestauFragment.RESTAU_TITLE, mRestauNameField.getText().toString()).apply();
                        userSharedPreferences.edit().putString(NewRestauFragment.RESTAU_KEY, newRestaukey).apply();
                        userSharedPreferences.edit().putBoolean(NewRestauFragment.FIRST_TIME_USER_RESTAU, false).apply();

                        updateToolbarText(mRestauNameField.getText().toString());

                        final StorageReference imageRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("images")
                                .child("restau")
                                .child(newRestaukey);

                        newRestauImageView.setDrawingCacheEnabled(true);
                        newRestauImageView.buildDrawingCache();
                        Bitmap bitmap = newRestauImageView.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        MainActivity.hideProgressDialog();
                        MainActivity.mProgressDialog.setMessage("Envoie de l'image encoure ...");
                        MainActivity.showProgressDialog();

                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads

                                MainActivity.hideProgressDialog();

                                ((MainActivity) mActivity).oldTag = MainActivity.TAG_NEW_RESTAU_FRAGMENT;

                                ((MainActivity) mActivity).hideAndShow(MainActivity.TAG_USER_RESTAU_FRAGMENT, new UserRestauFragment());

                                MainActivity.mDialog("", "Le Restaurant a bien été crée mais l'image associer n'a pas pu être envoiyer").show();


                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                MainActivity.hideProgressDialog();

                                ((MainActivity) mActivity).oldTag = MainActivity.TAG_NEW_RESTAU_FRAGMENT;

                                ((MainActivity) mActivity).hideAndShow(MainActivity.TAG_USER_RESTAU_FRAGMENT, new UserRestauFragment());

                                Toast.makeText(mActivity, "Nouveau restaurant crée !", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(mActivity, "Failed to update children " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateToolbarText(CharSequence text) {
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
