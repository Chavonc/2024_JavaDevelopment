// Generated by view binder compiler. Do not edit!
package com.example.test.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.test.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySignUpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSignup;

  @NonNull
  public final Button btnToSigninpage;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final EditText inputEmail;

  @NonNull
  public final EditText inputName;

  @NonNull
  public final EditText inputPassword;

  @NonNull
  public final EditText inputPasswordagain;

  @NonNull
  public final TextView labelEmail;

  @NonNull
  public final TextView labelName;

  @NonNull
  public final TextView labelPassword;

  @NonNull
  public final TextView labelPasswordagain;

  @NonNull
  public final TextView titleSignup;

  private ActivitySignUpBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnSignup,
      @NonNull Button btnToSigninpage, @NonNull ConstraintLayout constraintLayout,
      @NonNull EditText inputEmail, @NonNull EditText inputName, @NonNull EditText inputPassword,
      @NonNull EditText inputPasswordagain, @NonNull TextView labelEmail,
      @NonNull TextView labelName, @NonNull TextView labelPassword,
      @NonNull TextView labelPasswordagain, @NonNull TextView titleSignup) {
    this.rootView = rootView;
    this.btnSignup = btnSignup;
    this.btnToSigninpage = btnToSigninpage;
    this.constraintLayout = constraintLayout;
    this.inputEmail = inputEmail;
    this.inputName = inputName;
    this.inputPassword = inputPassword;
    this.inputPasswordagain = inputPasswordagain;
    this.labelEmail = labelEmail;
    this.labelName = labelName;
    this.labelPassword = labelPassword;
    this.labelPasswordagain = labelPasswordagain;
    this.titleSignup = titleSignup;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_signup;
      Button btnSignup = ViewBindings.findChildViewById(rootView, id);
      if (btnSignup == null) {
        break missingId;
      }

      id = R.id.btn_to_signinpage;
      Button btnToSigninpage = ViewBindings.findChildViewById(rootView, id);
      if (btnToSigninpage == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.input_email;
      EditText inputEmail = ViewBindings.findChildViewById(rootView, id);
      if (inputEmail == null) {
        break missingId;
      }

      id = R.id.input_name;
      EditText inputName = ViewBindings.findChildViewById(rootView, id);
      if (inputName == null) {
        break missingId;
      }

      id = R.id.input_password;
      EditText inputPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputPassword == null) {
        break missingId;
      }

      id = R.id.input_passwordagain;
      EditText inputPasswordagain = ViewBindings.findChildViewById(rootView, id);
      if (inputPasswordagain == null) {
        break missingId;
      }

      id = R.id.label_email;
      TextView labelEmail = ViewBindings.findChildViewById(rootView, id);
      if (labelEmail == null) {
        break missingId;
      }

      id = R.id.label_name;
      TextView labelName = ViewBindings.findChildViewById(rootView, id);
      if (labelName == null) {
        break missingId;
      }

      id = R.id.label_password;
      TextView labelPassword = ViewBindings.findChildViewById(rootView, id);
      if (labelPassword == null) {
        break missingId;
      }

      id = R.id.label_passwordagain;
      TextView labelPasswordagain = ViewBindings.findChildViewById(rootView, id);
      if (labelPasswordagain == null) {
        break missingId;
      }

      id = R.id.title_signup;
      TextView titleSignup = ViewBindings.findChildViewById(rootView, id);
      if (titleSignup == null) {
        break missingId;
      }

      return new ActivitySignUpBinding((ConstraintLayout) rootView, btnSignup, btnToSigninpage,
          constraintLayout, inputEmail, inputName, inputPassword, inputPasswordagain, labelEmail,
          labelName, labelPassword, labelPasswordagain, titleSignup);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}