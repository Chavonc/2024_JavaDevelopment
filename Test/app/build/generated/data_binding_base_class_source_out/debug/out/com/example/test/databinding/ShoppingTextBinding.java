// Generated by view binder compiler. Do not edit!
package com.example.test.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.test.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ShoppingTextBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout addedEditTextLayout;

  @NonNull
  public final LinearLayout editTextGroupLayout;

  @NonNull
  public final ImageButton imageButtonAdd;

  @NonNull
  public final EditText inputItems;

  @NonNull
  public final EditText inputName;

  private ShoppingTextBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout addedEditTextLayout, @NonNull LinearLayout editTextGroupLayout,
      @NonNull ImageButton imageButtonAdd, @NonNull EditText inputItems,
      @NonNull EditText inputName) {
    this.rootView = rootView;
    this.addedEditTextLayout = addedEditTextLayout;
    this.editTextGroupLayout = editTextGroupLayout;
    this.imageButtonAdd = imageButtonAdd;
    this.inputItems = inputItems;
    this.inputName = inputName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ShoppingTextBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ShoppingTextBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.shopping_text, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ShoppingTextBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addedEditTextLayout;
      LinearLayout addedEditTextLayout = ViewBindings.findChildViewById(rootView, id);
      if (addedEditTextLayout == null) {
        break missingId;
      }

      LinearLayout editTextGroupLayout = (LinearLayout) rootView;

      id = R.id.imageButton_add;
      ImageButton imageButtonAdd = ViewBindings.findChildViewById(rootView, id);
      if (imageButtonAdd == null) {
        break missingId;
      }

      id = R.id.inputItems;
      EditText inputItems = ViewBindings.findChildViewById(rootView, id);
      if (inputItems == null) {
        break missingId;
      }

      id = R.id.inputName;
      EditText inputName = ViewBindings.findChildViewById(rootView, id);
      if (inputName == null) {
        break missingId;
      }

      return new ShoppingTextBinding((LinearLayout) rootView, addedEditTextLayout,
          editTextGroupLayout, imageButtonAdd, inputItems, inputName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
