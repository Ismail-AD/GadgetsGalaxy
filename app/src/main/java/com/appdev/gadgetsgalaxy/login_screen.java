package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.FragmentLoginScreenBinding;
import com.appdev.gadgetsgalaxy.utils.Validation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class login_screen extends Fragment {

    private FragmentLoginScreenBinding binding;
    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    GoogleSignInClient googleSignInClient;

    private static final String PREFS_NAME = "TypePrefs";
    private static final String KEY_USER_TYPE = "userType";

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false);


        binding.newId.setOnClickListener(view -> findNavController(login_screen.this).navigate(R.id.action_login_screen_to_signup_screen));

        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.suMail.getText().toString().trim();
            String password = binding.suPass.getText().toString();

            String checkMessage = new Validation().userValidationCheck(email, password);
            boolean userValidationReceived = new Validation().userValidationCheckReturn(email, password);

            if (userValidationReceived) {
//                binding.pg.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.common), PorterDuff.Mode.SRC_IN);
                binding.pg.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        firebaseDatabase.getReference().child("userProfiles").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .get().addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful() && profileTask.getResult().exists()) {
                                        User_info userProfile = profileTask.getResult().getValue(User_info.class);
                                        if (userProfile != null) {
                                            String userType = userProfile.getUserType();
                                            if ("ADMIN".equals(userType)) {
                                                saveUserType("ADMIN");
                                                findNavController(this).navigate(R.id.action_login_screen_to_admin_page);
                                            } else if ("USER".equals(userType)) {
                                                saveUserType("USER");
                                                findNavController(this).navigate(R.id.action_login_screen_to_home_page);
                                            }
                                        }
                                    } else {
                                        binding.pg.setVisibility(View.GONE);
                                        Toast.makeText(requireContext(), "Failed to retrieve user profile", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        binding.pg.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                binding.pg.setVisibility(View.GONE);
                Toast.makeText(requireContext(), checkMessage, Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }

    private void saveUserType(String userType) {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WEB_CLIENT_ID))
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);

        resultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                if (data != null) {
                                    try {
                                        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                                                .getResult(ApiException.class);
                                        if (account != null) {
                                            firebaseAuthWithGoogle(account.getIdToken());
                                        }
                                    } catch (ApiException e) {
                                        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
        binding.loginGoogle.setOnClickListener(v -> {
            resultLauncher.launch(googleSignInClient.getSignInIntent());
        });

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userImage = firebaseUser.getPhotoUrl().toString();
                    String userName = firebaseUser.getDisplayName();
                    User_info userProfile = new User_info(0, 0, firebaseAuth.getCurrentUser().getUid(), userName, firebaseAuth.getCurrentUser().getEmail(), "", "", userImage, "USER");

                    firebaseDatabase.getReference().child("userProfiles")
                            .child(firebaseAuth.getCurrentUser().getUid())
                            .setValue(userProfile)
                            .addOnSuccessListener(aVoid -> {
                                findNavController(this).navigate(R.id.action_login_screen_to_home_page);
                            });
                }
            } else {
                Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
}