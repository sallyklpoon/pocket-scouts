package com.example.termproject;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class AuthLoginFragment extends Fragment {
    View authLoginFragment;
    private FirebaseAuth firebaseAuth;
    Context context;

    public AuthLoginFragment() {
    }

    private void singInUser(String email, String password) {
        try {
            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(getContext(), "Email password cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(email.trim(), password.trim()).addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "User authenticated.", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).renderUI();
                    } else {
                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachToXML() {
        try {

            MaterialButton loginButton = (MaterialButton) authLoginFragment.findViewById(R.id.button_login);
            TextInputLayout emailInput = (TextInputLayout) authLoginFragment.findViewById(R.id.email);
            TextInputLayout passwordInput = (TextInputLayout) authLoginFragment.findViewById(R.id.password);
            firebaseAuth = FirebaseAuth.getInstance();
            loginButton.setOnClickListener(v -> {
                String email = String.valueOf(Objects.requireNonNull(emailInput.getEditText()).getText());
                String password = String.valueOf(Objects.requireNonNull(passwordInput.getEditText()).getText());
                singInUser(email, password);
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        authLoginFragment = inflater.inflate(R.layout.fragment_auth_login, container, false);
        attachToXML();
        return authLoginFragment;
    }
}