package com.example.tjun.reviewpoj.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tjun.reviewpoj.R;

/**
 * Created by ZY on 2017/2/23.
 */

public class ViewDialogFragment extends DialogFragment {

    public interface Callback {
        void onViewDialogFragmentPositive(String fileName);

        void onViewDialogFragmentCancle();
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(view).setCancelable(false).setTitle(getString(R.string.name_and_save));


        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });


        EditText et_fileName = (EditText) view.findViewById(R.id.et_file_name);
        TextView btn_positive = view.findViewById(R.id.btn_positive);
        TextView btn_delete = view.findViewById(R.id.btn_cancle);
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onViewDialogFragmentPositive(et_fileName.getText().toString());
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onViewDialogFragmentCancle();
            }
        });




        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}