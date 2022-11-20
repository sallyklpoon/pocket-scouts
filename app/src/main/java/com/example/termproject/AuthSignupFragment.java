package com.example.termproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AuthSignupFragment extends Fragment {
    View authSignupFragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    Context context;

    public AuthSignupFragment() {
    }

    public void createUser(String firstName, String lastName, String email, String password, String birthday, String gender) {
        try {
            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(getContext(), "Email password cannot be blank", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be greater than 6 chars.", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("first_name", firstName);
                    userData.put("last_name", lastName);
                    userData.put("gender", gender);
                    userData.put("birthday", birthday);
                    userData.put("verified", false);

                    // add user information to a database document, where the document id is the user's unique id
                    assert user != null;
                    db.collection("user").document(user.getUid()).set(userData)
                            .addOnSuccessListener(success -> Toast.makeText(context, "User created.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(failure -> Toast.makeText(context, "Failed to create user.", Toast.LENGTH_SHORT).show());
                    ((MainActivity) requireActivity()).renderUI();
                }).addOnFailureListener(authResult -> Toast.makeText(context, "Error in creating user. Please try again later.", Toast.LENGTH_SHORT).show());
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
            TextInputEditText birthday_value = (TextInputEditText) authSignupFragment.findViewById(R.id.birthday_value);
            birthday_value.setInputType(InputType.TYPE_NULL);
            birthday_value.setKeyListener(null);
            MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
            final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
            materialDateBuilder.setTitleText("Select Birthday!");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                String dateString = DateFormat.format("dd/MM/yyyy", new Date(selection)).toString();
                birthday_value.setText(dateString);
            });
            birthday_value.setOnClickListener(view -> materialDatePicker.show(getParentFragmentManager(), "Material"));
            birthday_value.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    birthday_value.callOnClick();
                }
            });
            String[] genders = getResources().getStringArray(R.array.gender_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, genders);
            AutoCompleteTextView textView = (AutoCompleteTextView) authSignupFragment.findViewById(R.id.gender_dropdown);
            textView.setAdapter(adapter);
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
        db = FirebaseFirestore.getInstance();
        authSignupFragment =  inflater.inflate(R.layout.fragment_auth_signup, container, false);
        attachToXML();
        return authSignupFragment;
    }
}