package com.dbms.dbms_lnf;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LostActivity extends Activity implements OnClickListener
{
    EditText editRollno,editName,editType, editContact, editPlace, editDesc;
    Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        editRollno=(EditText)findViewById(R.id.editRollno);
        editName=(EditText)findViewById(R.id.editName);
        editType=(EditText)findViewById(R.id.editType);
        editContact=(EditText)findViewById(R.id.editContact);
        editPlace=(EditText)findViewById(R.id.editPlace);
        editDesc=(EditText)findViewById(R.id.editDesc);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnModify=(Button)findViewById(R.id.btnModify);
        btnView=(Button)findViewById(R.id.btnView);
        btnViewAll=(Button)findViewById(R.id.btnViewAll);
        btnShowInfo=(Button)findViewById(R.id.btnShowInfo);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        db=openOrCreateDatabase("ItemDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Lost(rollno VARCHAR,name VARCHAR,type VARCHAR, contact VARCHAR, place VARCHAR, desc VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view==btnAdd)
        {
            if(editRollno.getText().toString().trim().length()==0||
                    editName.getText().toString().trim().length()==0||
                    editType.getText().toString().trim().length()==0 || editContact.getText().toString().trim().length()==0 ||
                    editPlace.getText().toString().trim().length() == 0 || editDesc.getText().toString().trim().length() == 0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO Lost VALUES('"+editRollno.getText()+"','"+editName.getText()+
                    "','"+editType.getText()+"','"+editContact.getText()+"','"+editPlace.getText()+"','"+editDesc.getText()+"');");
            showMessage("Success", "Record added");
            clearText();
        }
        if(view==btnDelete)
        {
            if(editRollno.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM Lost WHERE rollno='"+editRollno.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM Lost WHERE rollno='"+editRollno.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        if(view==btnModify)
        {
            if(editRollno.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM Lost WHERE rollno='"+editRollno.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE Lost SET name='"+editName.getText()+"',type='"+editType.getText()+
                        "', contact = '"+editContact.getText()+"', place = '"+editPlace.getText()+"',desc = '"+editDesc.getText()+"' WHERE rollno='"+editRollno.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        if(view==btnView)
        {
            if(editRollno.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM Lost WHERE rollno='"+editRollno.getText()+"'", null);
            if(c.moveToFirst())
            {
                editName.setText(c.getString(1));
                editType.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
                clearText();
            }
        }
        if(view==btnViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM Lost", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Rollno: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Type: "+c.getString(2)+"\n\n");
                buffer.append("Contact: "+c.getString(3)+"\n");
                buffer.append("Place: "+c.getString(4)+"\n");
                buffer.append("Description :"+c.getString(5)+"\n");
            }
            showMessage("Lost items", buffer.toString());
        }
        if(view==btnShowInfo)
        {
            showMessage("Message","Lost and found app for SNU");
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        editRollno.setText("");
        editName.setText("");
        editType.setText("");
        editContact.setText("");
        editDesc.setText("");
        editPlace.setText("");
        editRollno.requestFocus();
    }
}
