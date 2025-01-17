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

public final class ExpenseTextBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText inputDate;

  @NonNull
  public final EditText inputMoney;

  @NonNull
  public final EditText inputName;

  @NonNull
  public final EditText inputTime;

  private ExpenseTextBinding(@NonNull LinearLayout rootView, @NonNull EditText inputDate,
      @NonNull EditText inputMoney, @NonNull EditText inputName, @NonNull EditText inputTime) {
    this.rootView = rootView;
    this.inputDate = inputDate;
    this.inputMoney = inputMoney;
    this.inputName = inputName;
    this.inputTime = inputTime;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ExpenseTextBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ExpenseTextBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.expense_text, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ExpenseTextBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.inputDate;
      EditText inputDate = ViewBindings.findChildViewById(rootView, id);
      if (inputDate == null) {
        break missingId;
      }

      id = R.id.inputMoney;
      EditText inputMoney = ViewBindings.findChildViewById(rootView, id);
      if (inputMoney == null) {
        break missingId;
      }

      id = R.id.inputName;
      EditText inputName = ViewBindings.findChildViewById(rootView, id);
      if (inputName == null) {
        break missingId;
      }

      id = R.id.inputTime;
      EditText inputTime = ViewBindings.findChildViewById(rootView, id);
      if (inputTime == null) {
        break missingId;
      }

      return new ExpenseTextBinding((LinearLayout) rootView, inputDate, inputMoney, inputName,
          inputTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
