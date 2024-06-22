package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;
import static com.appdev.gadgetsgalaxy.utils.Utility.PUBLISHABLE;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appdev.gadgetsgalaxy.Retrofit.ApiInterface;
import com.appdev.gadgetsgalaxy.Retrofit.RetrofitClient;
import com.appdev.gadgetsgalaxy.data.CustomerPay_info;
import com.appdev.gadgetsgalaxy.data.Epihemeral_model;
import com.appdev.gadgetsgalaxy.data.Order_info;
import com.appdev.gadgetsgalaxy.data.PaymentIntent;
import com.appdev.gadgetsgalaxy.data.Product_info;
import com.appdev.gadgetsgalaxy.data.User_info;
import com.appdev.gadgetsgalaxy.databinding.AddressDetailsBinding;
import com.appdev.gadgetsgalaxy.databinding.FragmentCheckoutFragmentBinding;
import com.appdev.gadgetsgalaxy.utils.FirebaseUtil;
import com.appdev.gadgetsgalaxy.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class checkout_fragment extends Fragment {

    FragmentCheckoutFragmentBinding binding;
    BottomSheetDialog bottomSheetDialog;

    AddressDetailsBinding addressDetailsBinding;
    Dialog progressDialog;
    DatabaseReference dbref;
    DatabaseReference cartRef;
    ValueEventListener eventListener;
    ValueEventListener eventListenerForCart;
    List<Product_info> productInfoList = new ArrayList<>();
    List<Order_info> orderInfoList = new ArrayList<>();
    int totalPayment = 0;
    String name, email, address, customer_id, ephemeral_key, client_secret;
    PaymentSheet paymentSheet;
    ApiInterface apiInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckoutFragmentBinding.inflate(inflater, container, false);

        progressDialog = new Dialog(requireContext());

        dbref = FirebaseUtil.getFirebaseDatabase().getReference().child("userProfiles")
                .child(Objects.requireNonNull(FirebaseUtil.getFirebaseAuth().getUid()));
        cartRef = FirebaseUtil.getFirebaseDatabase()
                .getReference()
                .child("Cart")
                .child(FirebaseUtil.getFirebaseAuth().getUid());
        binding.backBtn.setOnClickListener(v->{
            findNavController(this).popBackStack();
        });


        // STRIPE SETUP
        PaymentConfiguration.init(requireContext(), PUBLISHABLE);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        binding.pay.setOnClickListener(view10 -> {
            showProgressDialog();
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dismissProgressDialog();
                    if (dataSnapshot.exists()) {
                        User_info userProfile = dataSnapshot.getValue(User_info.class);
                        if (userProfile != null) {
                            String address = userProfile.getAddress();
                            if (address != null && !address.isEmpty()) {
                                dismissProgressDialog();
                                initiatePayment();
                            } else {
                                dismissProgressDialog();
                                Toast.makeText(requireContext(), "Add valid shipping address !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dismissProgressDialog();
                }
            });
        });


        binding.plus.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(
                    requireContext());
            addressDetailsBinding = AddressDetailsBinding.inflate(getLayoutInflater());
            addressDetailsBinding.closeBottomSheet.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

            addressDetailsBinding.savecatbtn.setOnClickListener(v1 -> {
                addressDetailsBinding.pg.setVisibility(View.VISIBLE);
                addressDetailsBinding.savecatbtn.setVisibility(View.INVISIBLE);
                String message = validateFields();
                if (validateFields().isEmpty()) {
                    String street = addressDetailsBinding.titlestreet.getText().toString();
                    String city = addressDetailsBinding.titleCity.getText().toString();
                    String combinedAddress = city + ", " + street; // Combine city and street

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("address", combinedAddress);
                    updateMap.put("contact", addressDetailsBinding.titlecontact.getText().toString());
                    updateMap.put("postalCode", addressDetailsBinding.titlepostal.getText().toString());

                    dbref.updateChildren(updateMap)
                            .addOnSuccessListener(aVoid -> {
                                addressDetailsBinding.pg.setVisibility(View.INVISIBLE);
                                addressDetailsBinding.savecatbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(requireContext(), "Details submitted Successfully", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }).addOnFailureListener(e -> {
                                addressDetailsBinding.pg.setVisibility(View.INVISIBLE);
                                addressDetailsBinding.savecatbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });

                } else {
                    addressDetailsBinding.pg.setVisibility(View.INVISIBLE);
                    addressDetailsBinding.savecatbtn.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });


            bottomSheetDialog.setContentView(addressDetailsBinding.getRoot());
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        });
        return binding.getRoot();
    }

    private void initiatePayment() {
        paymentSheet.presentWithPaymentIntent(client_secret,
                new PaymentSheet.Configuration("Galaxy Gadgets",
                        new PaymentSheet.CustomerConfiguration(customer_id, ephemeral_key)));
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(requireContext(), "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            String message = ((PaymentSheetResult.Failed) paymentSheetResult).getError().getLocalizedMessage();
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            remainingDatabaseOperations();
        }
    }

    private void remainingDatabaseOperations() {
        showProgressDialog();
        DatabaseReference databaseReferenceForAdmin = FirebaseUtil.getFirebaseDatabase().getReference("pendingOrders").child(Objects.requireNonNull(FirebaseUtil.getFirebaseAuth().getUid()));
        DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference("orders").child(FirebaseUtil.getFirebaseAuth().getUid());
        long value = System.currentTimeMillis();
        String orderId = String.valueOf(value);
        for (Product_info productInfo : productInfoList) {
            Order_info orderInfo = new Order_info(productInfo.getItem_name(), productInfo.getImageUrl(), productInfo.getCategory(), productInfo.getItem_price(), productInfo.getItem_discounted_price(), productInfo.getModel(), productInfo.getSelectedQuantity(), productInfo.getItem_id(), productInfo.getQuantity_available(), productInfo.getItem_rating(), productInfo.getDesc(), new Utility().formatDate(value), FirebaseUtil.getFirebaseAuth().getUid(), orderId, "PENDING", 0, name, email, address);
            orderInfoList.add(orderInfo);
        }
        databaseReference.child(orderId).setValue(orderInfoList)
                .addOnSuccessListener(aVoid -> {
                    databaseReferenceForAdmin.child(orderId).setValue(orderInfoList).addOnCompleteListener(task -> {
                        dismissProgressDialog();
                        Toast.makeText(requireContext(), "Your Order has been placed", Toast.LENGTH_SHORT).show();
                        cartRef.removeValue().addOnCompleteListener(taskad -> {
                            if (taskad.isSuccessful()) {
                                  findNavController(this).popBackStack();
                            }
                        });
                    });
                })
                .addOnFailureListener(e -> {
                    dismissProgressDialog();
                    Toast.makeText(requireContext(), "Failed to place order.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgressDialog();
        getCustomerID();

        eventListenerForCart = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product_info productInfo = dataSnapshot.getValue(Product_info.class);
                        if (productInfo != null) {
                            productInfoList.add(productInfo);
                            int priceToUse = productInfo.getItem_discounted_price() > 0 ? productInfo.getItem_discounted_price() : productInfo.getItem_price();
                            totalPayment += priceToUse * productInfo.getSelectedQuantity();
                        }
                    }
                    if (totalPayment > 0) {
                        binding.completePayment.setVisibility(View.VISIBLE);
                        binding.amount.setText("Rs: " + totalPayment);
                        getPaymentIntent();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        cartRef.addValueEventListener(eventListenerForCart);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User_info userProfile = snapshot.getValue(User_info.class);
                    if (userProfile != null) {
                        String useraddress = userProfile.getAddress();
                        if (useraddress != null && !useraddress.isEmpty()) {
                            binding.username.setText(userProfile.getName());
                            if (userProfile.getAddress().length() > 25) {
                                binding.address.setText(userProfile.getAddress().substring(0, 24) + "...");
                            } else {
                                binding.address.setText(userProfile.getAddress());
                            }
                            binding.userContact.setText(userProfile.getContact());
                            showDetails();
                            name = userProfile.getName();
                            address = useraddress;
                            email = userProfile.getEmail();
                        } else {
                            hideDetails();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbref.addValueEventListener(eventListener);
    }

    private void getCustomerID() {
        Call<CustomerPay_info> customer = apiInterface.createCustomer();
        customer.enqueue(new Callback<CustomerPay_info>() {
            @Override
            public void onResponse(Call<CustomerPay_info> call, Response<CustomerPay_info> response) {
                if (response.isSuccessful() && response.body() != null) {
                    customer_id = response.body().id;
                    getEphemeral(customer_id);
                }
            }

            @Override
            public void onFailure(Call<CustomerPay_info> call, Throwable t) {

            }
        });
    }

    private void getEphemeral(String id) {
        Call<Epihemeral_model> ephemeralKey = apiInterface.getEphemeralKey(id);
        ephemeralKey.enqueue(new Callback<Epihemeral_model>() {
            @Override
            public void onResponse(Call<Epihemeral_model> call, Response<Epihemeral_model> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ephemeral_key = response.body().secret;
                    if (client_secret != null && !client_secret.isEmpty()) {
                        dismissProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<Epihemeral_model> call, Throwable t) {

            }
        });
    }

    private void getPaymentIntent() {

        Call<PaymentIntent> paymentIntent = apiInterface.getPayment_intents(customer_id, totalPayment * 100, "pkr");
        paymentIntent.enqueue(new Callback<PaymentIntent>() {
            @Override
            public void onResponse(Call<PaymentIntent> call, Response<PaymentIntent> response) {
                if (response.body() != null && response.isSuccessful()) {
                    client_secret = response.body().client_secret;
                    if (ephemeral_key != null && !ephemeral_key.isEmpty()) {
                        dismissProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentIntent> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDetails() {
        binding.userDetails.setVisibility(View.VISIBLE);
        binding.addDetails.setVisibility(View.GONE);
        binding.pgAddress.setVisibility(View.GONE);
    }

    private void hideDetails() {
        binding.userDetails.setVisibility(View.GONE);
        binding.addDetails.setVisibility(View.VISIBLE);
        binding.pgAddress.setVisibility(View.GONE);
    }

    public void showProgressDialog() {
        progressDialog.setContentView(R.layout.progress_bar);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private String validateFields() {
        String street = addressDetailsBinding.titlestreet.getText().toString().trim();
        String city = addressDetailsBinding.titleCity.getText().toString().trim();
        String postalCode = addressDetailsBinding.titlepostal.getText().toString().trim();
        String houseNumber = addressDetailsBinding.titleHouseno.getText().toString().trim();
        String phone = addressDetailsBinding.titlecontact.getText().toString().trim();

        if (TextUtils.isEmpty(street)) {
            return "Street is required";
        }

        if (TextUtils.isEmpty(city)) {
            return "City is required";
        }

        if (TextUtils.isEmpty(postalCode)) {
            return "Postal code is required";
        }

        if (!Pattern.matches("^[0-9]{5}$", postalCode)) {
            return "Invalid postal code";
        }

        if (TextUtils.isEmpty(houseNumber)) {
            return "House number is required";
        }

        if (TextUtils.isEmpty(phone)) {
            return "Phone number is required";
        }

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            return "Invalid phone number";
        }

        return "";
    }


}