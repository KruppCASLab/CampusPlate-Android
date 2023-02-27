package com.example.campusplate_android.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.campusplate_android.R;
import com.example.campusplate_android.ui.addlisting.AddListingFragment;

import java.util.ArrayList;
import java.util.List;

public class Select {
    private String[] listItems;
    private boolean isMultiSelector;
    private boolean[] checkedItems;
    private String title;
    private ArrayList<Integer> selectedItems = new ArrayList<>();


    private SelectComplete delegate;

    public interface SelectComplete{
       void didSelectItems(List<Integer> selectedItems);
    }

    public Select(String title, String[] listItems, boolean isMultiSelector, SelectComplete delegate) {
        this.listItems = listItems;
        this.isMultiSelector = isMultiSelector;
        this.title = title;
        this.delegate = delegate;
    }

    public void show(Context context) {
        final Select select = this;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(title);
        if(isMultiSelector) {
            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked) {
                        if (!selectedItems.contains(which)) {
                            selectedItems.add(which);
                        } else {
                            selectedItems.remove(which);
                        }
                    }

                }

            });
        }else{
          mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  selectedItems.clear();
                  selectedItems.add(which);
                  delegate.didSelectItems(selectedItems);
              }
          });
        }
        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}

