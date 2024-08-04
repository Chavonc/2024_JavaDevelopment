// Generated by view binder compiler. Do not edit!
package com.example.test.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.test.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AlertdialogSignUpBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText limitInput;

  @NonNull
  public final EditText remainInput;

  @NonNull
  public final LinearLayout signupAlertdialog;

  private AlertdialogSignUpBinding(@NonNull LinearLayout rootView, @NonNull EditText limitInput,
      @NonNull EditText remainInput, @NonNull LinearLayout signupAlertdialog) {
    this.rootView = rootView;
    this.limitInput = limitInput;
    this.remainInput = remainInput;
    this.signupAlertdialog = signupAlertdialog;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AlertdialogSignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AlertdialogSignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.alertdialog_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AlertdialogSignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.limit_input;
      EditText limitInput = ViewBindings.findChildViewById(rootView, id);
      if (limitInput == null) {
        break missingId;
      }

      id = R.id.remain_input;
      EditText remainInput = ViewBindings.findChildViewById(rootView, id);
      if (remainInput == null) {
        break missingId;
      }

      LinearLayout signupAlertdialog = (LinearLayout) rootView;

      return new AlertdialogSignUpBinding((LinearLayout) rootView, limitInput, remainInput,
          signupAlertdialog);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}