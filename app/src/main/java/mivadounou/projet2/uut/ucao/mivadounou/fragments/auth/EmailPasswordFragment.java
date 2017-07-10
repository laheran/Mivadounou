package mivadounou.projet2.uut.ucao.mivadounou.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmailPasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmailPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailPasswordFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private OnFragmentInteractionListener mListener;

    private EditText mEmailField;

    private EditText mPasswordField;

    private TextView mSatusEmail;

    private Button mSignInButton;

    private Button mCreateAccountButton;

    private Button mVerifyButton;

    private FirebaseAuth mAuth;

    private ViewGroup fieldsViewGroup;

    private ViewGroup statusViewGroup;

    private ViewGroup authCreateViewGroup;

    private Activity mActivity;

    private String currentEmail;

    public EmailPasswordFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EmailPasswordFragment newInstance(String param1, String param2) {
        EmailPasswordFragment fragment = new EmailPasswordFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_email_password, container, false);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        fieldsViewGroup = (ViewGroup) rootView.findViewById(R.id.field_view_group);
        statusViewGroup = (ViewGroup) rootView.findViewById(R.id.status_view_group);
        authCreateViewGroup = (ViewGroup) rootView.findViewById(R.id.auth_create_account_view_group);

        mSatusEmail = (TextView) rootView.findViewById(R.id.email_title_status);
        mEmailField = (EditText) rootView.findViewById(R.id.email_fields);
        mPasswordField = (EditText) rootView.findViewById(R.id.field_password);

        mSignInButton = (Button) rootView.findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(this);

        mCreateAccountButton = (Button) rootView.findViewById(R.id.email_create_account_button);
        mCreateAccountButton.setOnClickListener(this);

        mVerifyButton = (Button) rootView.findViewById(R.id.verify_email_button);
        mVerifyButton.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            updateUI(user);

                            MainActivity.hideProgressDialog();

                            MainActivity.mDialog("", "Création du compte reussi.\nVeuillez confirmer le compte en cliquant sur le lien de confirmation dans l'email de vérification qui vous a été envoyé !");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication echoué.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(mActivity, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            if (!user.isEmailVerified()) {

                                MainActivity.hideProgressDialog();

                                MainActivity.mDialog("", "Veuillez confirmer le compte en cliquant sur le lien de confirmation dans l'email de vérification qui vous a été envoyé !");

                            } else {

                                if (mActivity instanceof MainActivity) {

                                    ((MainActivity) mActivity).oldTag = MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT;

                                    ((MainActivity) mActivity).findIfUserHasRestau();
                                }

                            }
                        } else {

                            MainActivity.hideProgressDialog();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(mActivity, "Authentication échoué.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(e instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(mActivity, "Aucun utilisateur ne correspond a cette adresse Email.",
                                    Toast.LENGTH_LONG).show();
                        }

                        if(e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(mActivity, "Adresse Email ou mot de pass incorecte." ,
                                    Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(mActivity, e.getMessage() + " " +e.getClass().getSimpleName(),
                                Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        mVerifyButton.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        mVerifyButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(mActivity,
                                    "Email de Vérification envoyé à " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(mActivity,
                                    "Echec de l'envoie de l'email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Email invalide.");
            valid = false;
        } else {
            currentEmail = mEmailField.getText().toString().toString();
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Password invalide.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();

        if (user != null) {
            mSatusEmail.setText(currentEmail);

            fieldsViewGroup.setVisibility(View.GONE);
            authCreateViewGroup.setVisibility(View.GONE);

            mSatusEmail.setVisibility(View.VISIBLE);
            mVerifyButton.setVisibility(View.VISIBLE);

        } else {

            fieldsViewGroup.setVisibility(View.VISIBLE);
            authCreateViewGroup.setVisibility(View.VISIBLE);

            mSatusEmail.setVisibility(View.GONE);
            mVerifyButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {

            MainActivity.mProgressDialog.setMessage("Création du compte encoure ...");
            MainActivity.showProgressDialog();

            createAccount(mEmailField.getText().toString().trim(), mPasswordField.getText().toString());

        } else if (i == R.id.email_sign_in_button) {

            MainActivity.mProgressDialog.setMessage("Authentification encoure ...");
            MainActivity.showProgressDialog();

            signIn(mEmailField.getText().toString().trim(), mPasswordField.getText().toString());

        } else if (i == R.id.verify_email_button) {

            MainActivity.mProgressDialog.setMessage("Renvoie du code de vérification encoure ...");
            MainActivity.showProgressDialog();

            sendEmailVerification();
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
