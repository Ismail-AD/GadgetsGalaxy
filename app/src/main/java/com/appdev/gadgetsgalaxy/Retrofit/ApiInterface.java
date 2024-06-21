package com.appdev.gadgetsgalaxy.Retrofit;

import static com.appdev.gadgetsgalaxy.utils.Utility.SECRET_KEY;

import com.appdev.gadgetsgalaxy.data.CustomerPay_info;
import com.appdev.gadgetsgalaxy.data.Epihemeral_model;
import com.appdev.gadgetsgalaxy.data.PaymentIntent;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers("Authorization: Bearer " + SECRET_KEY)
    @POST("v1/customers")
    Call<CustomerPay_info> createCustomer();

    @Headers({"Authorization: Bearer " + SECRET_KEY, "Stripe-Version: 2024-04-10"})
    @POST("v1/ephemeral_keys")
    Call<Epihemeral_model> getEphemeralKey(@Query("customer") String userid);

    @Headers("Authorization: Bearer " + SECRET_KEY)
    @POST("v1/payment_intents")
    Call<PaymentIntent> getPayment_intents(@Query("customer") String userid, @Query("amount") int amount, @Query("currency") String curr);


}
