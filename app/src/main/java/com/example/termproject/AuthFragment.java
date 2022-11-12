package com.example.termproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public AuthFragment() {
    }

    private void continueWithGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account =accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            Toast.makeText(context, "User authenticated.", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).renderUI();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        MaterialButton googleButton = (MaterialButton) view.findViewById(R.id.button_continue_google);
        googleButton.setOnClickListener(v -> continueWithGoogle());
        MaterialButton loginButton = (MaterialButton) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> ((MainActivity) requireActivity()).navigateToFragment(new AuthLoginFragment(), R.id.nav_events, true));
        TextView emailButton = (TextView) view.findViewById(R.id.signup_button);
        emailButton.setOnClickListener(v -> ((MainActivity) requireActivity()).navigateToFragment(new AuthSignupFragment(), R.id.nav_events, true));
        return view;
    }
}