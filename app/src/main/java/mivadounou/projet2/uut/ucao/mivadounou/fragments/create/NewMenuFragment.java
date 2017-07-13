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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;
import mivadounou.projet2.uut.ucao.mivadounou.fragments.user.UserRestauFragment;
import mivadounou.projet2.uut.ucao.mivadounou.models.MenuRestau;

public class NewMenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText menuTitleText;
    private EditText menuDescText;
    private EditText menuPriceText;

    private Spinner menuTypeSpinner;

    private Button newMenu;

    private Activity mActivity;

    private DatabaseReference mDatabase;

    private FloatingActionButton floatingActionButton;

    private ImageView imageView;

    private Boolean isUpdate = false;
    private Boolean isImageUpdate = false;
    private String currentKey;
    private String currentTitle;
    private String currentDesc;
    private String currentPrice;
    private String currentType;
    private String currentMd5Hash;

    private StorageReference mStorage;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    public NewMenuFragment() {
        // Required empty public constructor
    }

    public static NewMenuFragment newInstance(String param1, String param2) {
        NewMenuFragment fragment = new NewMenuFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentKey = getArguments().getString("currentKey");
            currentTitle = getArguments().getString("currentTitle");
            currentDesc = getArguments().getString("currentDesc");
            currentPrice = getArguments().getString("currentPrice");
            currentType = getArguments().getString("currentType");
            currentMd5Hash = getArguments().getString("currentMd5Hash");
            isUpdate = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_menu, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mStorage = FirebaseStorage.getInstance().getReference();

        imageView = (ImageView) rootView.findViewById(R.id.default_img);
        menuTitleText = (EditText) rootView.findViewById(R.id.menu_title_field);
        menuDescText = (EditText) rootView.findViewById(R.id.menu_desc_field);
        menuPriceText = (EditText) rootView.findViewById(R.id.menu_price_field);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_menu_image);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseImage();
            }
        });

        avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);

        menuTypeSpinner = (Spinner) rootView.findViewById(R.id.menu_type_field);
        newMenu = (Button) rootView.findViewById(R.id.button_new_menu);
        newMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(menuTitleText.getText().toString())) {
                    menuTitleText.setError("Nom du menu invalide.");
                    return;
                }
                if (TextUtils.isEmpty(menuDescText.getText().toString())) {
                    menuDescText.setError("Prix du menu invalide.");
                    return;
                }
                if (TextUtils.isEmpty(menuPriceText.getText().toString())) {
                    menuPriceText.setError("Prix du menu invalide.");
                    return;
                }

                if (MainActivity.isNetworkAvailable(mActivity)) {

                    Long tsLong = System.currentTimeMillis() / 1000;
                    String currentTimeStamp = tsLong.toString();

                    SharedPreferences userSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);

                    MenuRestau menuRestau = new MenuRestau(
                            userSharedPreferences.getString(NewRestauFragment.RESTAU_KEY, ""),
                            userSharedPreferences.getString(NewRestauFragment.RESTAU_TITLE, ""),
                            menuTitleText.getText().toString().trim(),
                            menuDescText.getText().toString().trim(),
                            Integer.valueOf(menuPriceText.getText().toString()),
                            menuTypeSpinner.getSelectedItem().toString(),
                            currentTimeStamp);

                    createOrUpdateMenu(menuRestau);
                } else {
                    Toast.makeText(mActivity, "Verifier votre connexion internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        stopAnim();

        if (isUpdate) {

            startAnim();

            final StorageReference imageRef = mStorage.child("images").child("menu").child(currentKey);

            Glide.with(mActivity)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .crossFade()
                    .signature(new StringSignature(currentMd5Hash))
                    .listener(new RequestListener<StorageReference, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, StorageReference ref,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            stopAnim();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource,
                                                       StorageReference ref, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            stopAnim();
                            return false;
                        }
                    })
                    .into(imageView);

            menuTitleText.setText(currentTitle);
            menuPriceText.setText(currentPrice);
            menuDescText.setText(currentDesc);
            newMenu.setText("Modifier");

            switch (currentType) {
                case "Plat Africain":
                    menuTypeSpinner.setSelection(0);
                    break;
                case "Pizza":
                    menuTypeSpinner.setSelection(1);
                    break;
                case "Burger":
                    menuTypeSpinner.setSelection(2);
                    break;
            }
        }
    }

    private void startChooseImage() {
        CropImage.activity()
                .setMinCropResultSize(500, 200)
                .setMaxCropResultSize(3080, 2780)
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

                    imageView.setImageBitmap(bitmap);

                    isImageUpdate = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void startAnim() {
        avLoadingIndicatorView.smoothToShow();
        // or avi.smoothToShow();
    }

    public void stopAnim() {
        avLoadingIndicatorView.smoothToHide();
        // or avi.smoothToHide();
    }

    private void createOrUpdateMenu(final MenuRestau menuRestau) {

        final String newMenukey = isUpdate ? currentKey : mDatabase.child("restau-menus").push().getKey();

        if (MainActivity.isNetworkAvailable(mActivity)) {

            if (isImageUpdate) {

                MainActivity.mProgressDialog.setMessage("Envoie de l'image encoure ...");
                MainActivity.showProgressDialog();

                final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images").child("menu").child(newMenukey);

                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads

                        MainActivity.hideProgressDialog();

                        MainActivity.mDialog("", "Echec de l'envoie de l'image veuillez vérifier votre connexion internet !").show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        MainActivity.hideProgressDialog();

                        if (isUpdate) {
                            MainActivity.mProgressDialog.setMessage("Modification du menu encoure ...");
                        } else {
                            MainActivity.mProgressDialog.setMessage("Creation du menu encoure ...");
                        }

                        MainActivity.showProgressDialog();

                        imageRef.getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
                            @Override
                            public void onComplete(@NonNull Task<StorageMetadata> task) {

                                menuRestau.setMd5Hash(task.getResult().getMd5Hash());

                                Map<String, Object> menuValues = menuRestau.toMap();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/menu/" + newMenukey, menuValues);
                                childUpdates.put("/restau-menus/" + menuRestau.getRestauId() + "/" + newMenukey, menuValues);

                                mDatabase.updateChildren(childUpdates)
                                        .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                MainActivity.hideProgressDialog();

                                                if (!isUpdate) {
                                                    Toast.makeText(mActivity, "Nouveau menu crée !", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(mActivity, "Menu modifié !", Toast.LENGTH_LONG).show();
                                                }

                                                if (getParentFragment() instanceof UserRestauFragment) {

                                                    ((UserRestauFragment) getParentFragment()).hideAndShow(UserRestauFragment.TAG_USER_RESTAU_HOME_FRAGMENT);
                                                }

                                            }
                                        })
                                        .addOnFailureListener(mActivity, new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                MainActivity.hideProgressDialog();
                                                Toast.makeText(mActivity, "Failed to update children " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            } else {

                if (isUpdate) {
                    MainActivity.mProgressDialog.setMessage("Modification du menu encoure ...");
                    menuRestau.setMd5Hash(currentMd5Hash);
                } else {
                    MainActivity.mProgressDialog.setMessage("Creation du menu encoure ...");
                }

                MainActivity.showProgressDialog();

                Map<String, Object> menuValues = menuRestau.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/menu/" + newMenukey, menuValues);
                childUpdates.put("/restau-menus/" + menuRestau.getRestauId() + "/" + newMenukey, menuValues);

                mDatabase.updateChildren(childUpdates)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                MainActivity.hideProgressDialog();

                                if (!isUpdate) {
                                    Toast.makeText(mActivity, "Nouveau menu crée !", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mActivity, "Menu modifié !", Toast.LENGTH_LONG).show();
                                }

                                if (getParentFragment() instanceof UserRestauFragment) {

                                    ((UserRestauFragment) getParentFragment()).hideAndShow(UserRestauFragment.TAG_USER_RESTAU_HOME_FRAGMENT);
                                }

                            }
                        })
                        .addOnFailureListener(mActivity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                MainActivity.hideProgressDialog();
                                Toast.makeText(mActivity, "Failed to update children " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            Toast.makeText(mActivity, "Verifier votre connexion internet", Toast.LENGTH_SHORT).show();
        }
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
