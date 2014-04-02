package com.example.notepad;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
// Logic to implement Dialog box for Saving file. File name to be entered by the user. XML file is pop.xml
public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener
{

  public Activity c;
  public Dialog d;
  public Button save, cancel;
  public String body;
  private boolean flag;
  public CustomDialogClass(Activity a, String text)
  {
    super(a);
    this.c = a;
    body = text;
  }

  //Purpose:It contains logic for what has to be done when the application is created initially.
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.pop);
    save = (Button) findViewById(R.id.Save_Button);
    save.setText("Save");
    cancel = (Button) findViewById(R.id.Cancel_Button);
    save.setOnClickListener(this);
    cancel.setOnClickListener(this);

  }

  //Purpose:Mouse click listener logic
  @Override
  public void onClick(View v)
  {
    switch (v.getId()) {
    case R.id.Save_Button:
     TextView filename=(TextView) findViewById(R.id.FileName);
     String text=filename.getText().toString();
        try
        {
         writeToFile(text, body);
         LaunchPage_Main.saved_state=true;
        }
        catch (Exception e)
        {
        }
      break;
    case R.id.Cancel_Button:
     this.cancel();
     LaunchPage_Main.saved_state=false;
     Button b=(Button) findViewById(R.id.Save_Button);
     b.setText("Save");
     flag=false;
     dismiss();
      break;
    default:
      break;
    }
  }
  
  //Purpose:Saving the contents in the notepad file.
  public void writeToFile(String fileName, String body)
  {
      FileOutputStream fos = null;

      try
      {
          final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NP/" );

          if (!dir.exists())
          {
              dir.mkdirs();
          }

          final File myFile = new File(dir, fileName + ".txt");

          if ( !myFile.exists())
          {
              myFile.createNewFile();
          }
          else if( flag== false && myFile.exists())
          {
         duplicate();
         return;
          }
          
          fos = new FileOutputStream(myFile);

          fos.write(body.getBytes());
          fos.close();
          dismiss();
          
      }
      catch (IOException e)
      {
     e.printStackTrace();
      }
  }

  //Purpose:Check if the file already exists.
  private void duplicate()
  {
TextView t=(TextView) findViewById(R.id.label);
t.setVisibility(0);
Button b=(Button) findViewById(R.id.Save_Button);
b.setText("Replace");
flag=true;	
  }
}

    Status
    API
    Training
    Shop
    Blog
    About

    © 2014 GitHub, Inc.
    Terms
    Privacy
    Security
    Contact

