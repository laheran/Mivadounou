package mivadounou.projet2.uut.ucao.mivadounou.fragments.auth;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mivadounou.projet2.uut.ucao.mivadounou.R;
import mivadounou.projet2.uut.ucao.mivadounou.activities.MainActivity;

public class ChooseAuthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final Fragment[] FRAGMENTS = new Fragment[]{
            new GoogleSignInFragment(),
            new EmailPasswordFragment(),
            new PhoneAuthFragment(),
    };

    private static final String[] titles = new String[]{
            "Authentification par Google",
            "Authentification par Email et password",
            "Authentification par Numéro de Téléphone"
    };

    private static final String[] DESCRIPTIONS = new String[]{
            "Vouz pouvez Utiliser votre compte Google pour vous authentifier à Mivadounou.",
            "Vouz pouvez Utiliser un email valide et um mot de pass pour vous authentifier à Mivadounou.",
            "Vouz pouvez Utiliser votre numero de téléphone pour vous authentifier à Mivadounou."
    };

    private static final String[] TAGS = new String[]{
            MainActivity.TAG_GOOGLE_SIGNIN_FRAGMENT,
            MainActivity.TAG_EMAIL_PASSWORD_FRAGMENT,
            MainActivity.TAG_PHONE_AUTH_FRAGMENT
    };

    private static final int[] IMAGE_RESOURCES = new int[]{
            R.drawable.ic_google,
            R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_call_black_24dp
    };

    private ListView listView;

    private Activity mActivity;

    private OnFragmentInteractionListener mListener;

    public ChooseAuthFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChooseAuthFragment newInstance(String param1, String param2) {
        ChooseAuthFragment fragment = new ChooseAuthFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_choose_auth, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        MyArrayAdapter adapter = new MyArrayAdapter(mActivity, R.layout.item_choose_list_view,
                FRAGMENTS, DESCRIPTIONS, IMAGE_RESOURCES);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) mActivity).oldTag = TAGS[position];

                ((MainActivity) mActivity).hideAndShow(TAGS[position], FRAGMENTS[position]);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class MyArrayAdapter extends ArrayAdapter<Fragment> {

        private Context mContext;
        private String[] mDescription;
        private int[] mImageResources;

        public MyArrayAdapter(Context context, int resource,
                              Fragment[] fragments, String[] descriptions,
                              int[] mImageResources) {
            super(context, resource, fragments);

            mContext = context;
            this.mDescription = descriptions;
            this.mImageResources = mImageResources;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(R.layout.item_choose_list_view, null);
            }

            ((ImageView) view.findViewById(R.id.image_auth)).setImageResource(mImageResources[position]);
            ((TextView) view.findViewById(R.id.title_auth)).setText(titles[position]);
            ((TextView) view.findViewById(R.id.description_auth)).setText(mDescription[position]);

            return view;
        }
    }
}
