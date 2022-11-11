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


public class AuthSignupFragment extends Fragment {
    View authSignupFragment;
    private FirebaseAuth firebaseAuth;
    Context context;

    public AuthSignupFragment() {
    }

    public void createUser(String firstName, String lastName, String email, String password, String birthday, String gender) {
        try {
            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(getContext(), "Email password cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).addOnSuccessListener(authResult -> {
                    Toast.makeText(context, "User created.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) requireActivity()).renderUI();
                });
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachToXML() {
        try {
            TextInputLayout firstNameInput = (TextInputLayout) authSignupFragment.findViewById(R.id.first_name);
            TextInputLayout lastNameInput = (TextInputLayout) authSignupFragment.findViewById(R.id.last_name);
            MaterialButton loginButton = (MaterialButton) authSignupFragment.findViewById(R.id.button_signup);
            TextInputLayout emailInput = (TextInputLayout) authSignupFragment.findViewById(R.id.email);
            TextInputLayout passwordInput = (TextInputLayout) authSignupFragment.findViewById(R.id.password);
            TextInputLayout birthdayInput = (TextInputLayout) authSignupFragment.findViewById(R.id.birthday);
            TextInputLayout genderInput = (TextInputLayout) authSignupFragment.findViewById(R.id.gender);
            firebaseAuth = FirebaseAuth.getInstance();

            loginButton.setOnClickListener(v -> {
                String firstName = String.valueOf(Objects.requireNonNull(firstNameInput.getEditText()).getText());
                String lastName = String.valueOf(Objects.requireNonNull(lastNameInput.getEditText()).getText());
                String email = String.valueOf(Objects.requireNonNull(emailInput.getEditText()).getText());
                String password = String.valueOf(Objects.requireNonNull(passwordInput.getEditText()).getText());
                String birthday = String.valueOf(Objects.requireNonNull(birthdayInput.getEditText()).getText());
                String gender = String.valueOf(Objects.requireNonNull(genderInput.getEditText()).getText());
                createUser(firstName, lastName, email, password, birthday, gender);
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        authSignupFragment =  inflater.inflate(R.layout.fragment_auth_signup, container, false);
        attachToXML();
        return authSignupFragment;
    }
}