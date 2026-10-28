package com.example.case01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentModel> studentList;
    private Context context;
    private DBHelper dbHelper;

    public StudentAdapter(List<StudentModel> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final StudentModel student = studentList.get(position);

        holder.textViewID.setText("ID: " + student.getId());
        holder.itemEditName.setText(student.getName());
        holder.itemEditStudentID.setText(student.getStudentId());

        String[] majors = {"Informatics", "Information Systems"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, majors);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.itemSpinnerMajor.setAdapter(spinnerAdapter);

        if (student.getMajor() != null && student.getMajor().equals("Information Systems")) {
            holder.itemSpinnerMajor.setSelection(1);
        } else {
            holder.itemSpinnerMajor.setSelection(0);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = holder.itemEditName.getText().toString().trim();
                String updatedStudentID = holder.itemEditStudentID.getText().toString().trim();
                String updatedMajor = holder.itemSpinnerMajor.getSelectedItem().toString();

                if (updatedName.isEmpty() || updatedStudentID.isEmpty()) {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.updateStudent(new StudentModel(student.getId(), updatedName, updatedStudentID, updatedMajor));
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();

                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });

        // Bagian yang diperbarui dengan pengecekan list kosong
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteStudent(student.getId());
                studentList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

                if (studentList.isEmpty()) {
                    Toast.makeText(context, "No students left", Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewID;
        EditText itemEditName, itemEditStudentID;
        Spinner itemSpinnerMajor;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textViewID);
            itemEditName = itemView.findViewById(R.id.itemEditName);
            itemEditStudentID = itemView.findViewById(R.id.itemEditStudentID);
            itemSpinnerMajor = itemView.findViewById(R.id.itemSpinnerMajor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}