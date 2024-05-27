package com.appdev.gadgetsgalaxy.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;

public class Validation {

    public String userValidationCheck(String email, String password) {
        if (TextUtils.isEmpty(email))
            return "Please enter the email to proceed !";
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Please enter a valid Email !";
        else if (TextUtils.isEmpty(password))
            return "Please enter the password to proceed !";
        else if (password.length() <= 6)
            return "Password must contain 7 characters !";
        else
            return "";
    }

    public String newUserValidationCheck(String email, String password, String cpassword) {
        if (TextUtils.isEmpty(email))
            return "Please enter the email to proceed !";
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Please enter a valid Email !";
        else if (TextUtils.isEmpty(password))
            return "Please enter the password to proceed !";
        else if (TextUtils.isEmpty(cpassword))
            return "Please enter the password to proceed !";
        else if (password.length() <= 6)
            return "Password must contain 7 characters !";
        else if (!password.equals(cpassword))
            return "Passwords didn't matched !";
        else
            return "";
    }

    public boolean userValidationCheckReturn(String email, String password) {
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(password) &&
                password.length() > 6;
    }

    public boolean newUserConfirmValidationCheck(String email, String password, String cpassword) {
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(cpassword) &&
                password.equals(cpassword) &&
                password.length() > 6;
    }

    public boolean resetValidationCheck(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String resetValidationCheckMessage(String email) {
        if (TextUtils.isEmpty(email))
            return "Please enter the email to proceed !";
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Please enter a valid Email !";
        else
            return "Check your Email Primary Messages or Spam Messages";
    }

    public String validateInputs(String title, String price, String category, String model, String quantity, String desc, Uri imageUri) {
        int priceFormat;
        if (price.isEmpty()) {
            priceFormat = 0;
        } else {
            priceFormat = Integer.parseInt(price);
        }
        int quantityFormat;
        if (quantity.isEmpty()) {
            quantityFormat = 0;
        } else {
            quantityFormat = Integer.parseInt(quantity);
        }

        if (imageUri == null)
            return "No Image Selected";
        else if (TextUtils.isEmpty(title))
            return "Product title is required.";
        else if (quantityFormat <= 0)
            return "Please enter a valid quantity.";
        else if (priceFormat <= 0)
            return "Please enter a valid price.";
        else if (TextUtils.isEmpty(category))
            return "Category is required.";
        else if (TextUtils.isEmpty(model))
            return "Model is required.";
        else if (TextUtils.isEmpty(quantity))
            return "Product quantity is required.";
        else if (TextUtils.isEmpty(desc))
            return "Product description is required.";
        else
            return "";

    }

}
