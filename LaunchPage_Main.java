package com.example.notepad;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Menu;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Scroller;

// Main Activity of the application. Xml file is activity_launch_page_main.xml
public class LaunchPage_Main extends Activity
{
Activity a = null;
static boolean saved_state;
static EditText text = null;
static String STATE_TEXT = null;
public static String last_opened = null;
public static String opened_text = "";
public View view;
Services serv = new Services();

  //Purpose: Sets the last opened and save state at the launch of the application.
@Override
protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
last_opened=null;
setContentView(R.layout.activity_launch_page__main);
saved_state=true;
setscreeen();
a = this;
createFolder();
}

//Purpose:Logic to create a folder for saving the text files in memory.
private void createFolder()
{
File mydir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/NP");
if(!mydir.exists())
{
mydir.mkdirs();
}
}

private void setscreeen()
{
text=(EditText)findViewById(R.id.Text_ID);
setscroll();
setOnClick();
}

//Purpose:Logic to set onscreen cursor for the text.
private void setOnClick()
{
final InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);	
view = findViewById(R.id.View_ID);
view.setOnClickListener(new OnClickListener()
{
@Override
public void onClick(View arg0)
{
text.setFocusableInTouchMode(true);
imm.showSoftInput(text, 0);
return;
}
});

text.setOnTouchListener(new OnTouchListener()
{
@Override
public boolean onTouch(View arg0, MotionEvent arg1)
{
switch(arg1.getAction())
{
case MotionEvent.ACTION_DOWN:
Layout layout = ((EditText)arg0).getLayout();
float x = arg1.getX()+text.getScrollX();
float y = arg1.getY()+text.getScrollY();
int line = layout.getLineForVertical((int)y);
int offset = layout.getOffsetForHorizontal(line, x);
if(offset>0)
if(x>layout.getLineMax(0))
text.setSelection(offset);
else
text.setSelection(offset-1);
break;
}
return true;
}
});
text.setOnClickListener(new OnClickListener()
{
@Override
public void onClick(View arg0)
{
text.setFocusableInTouchMode(true);
imm.showSoftInput(text, 0);
return;
}

});	
text.addTextChangedListener(new TextWatcher()
{

@Override
public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
{}

@Override
public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
{}

@Override
public void afterTextChanged(Editable arg0)
{
saved_state=false;
}
}) ;
}

//Purpose:Logic to add the recently saved documents to the menu.
@SuppressWarnings({ "unchecked", "rawtypes" })
private void addRecent(SubMenu menu) throws IOException
{
File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/NP/");
String s = "";

File f = new File(path.getAbsolutePath());

File [] files = f.listFiles();
MenuItem norec=menu.findItem(R.id.norecent);
if(files.length>0) norec.setVisible(false);
else norec.setVisible(true);
Arrays.sort( files, new Comparator()
{
public int compare(Object o1, Object o2)
{

if (((File)o1).lastModified() > ((File)o2).lastModified())
{
return -1;
} else if (((File)o1).lastModified() < ((File)o2).lastModified())
{
return +1;
} else
{
return 0;
}
}
});
MenuItem rec1=menu.findItem(R.id.recent1);
MenuItem rec2=menu.findItem(R.id.recent2);
MenuItem rec3=menu.findItem(R.id.recent3);
MenuItem rec4=menu.findItem(R.id.recent4);
MenuItem rec5=menu.findItem(R.id.recent5);
for(int i=0;i<files.length;i++)
if(s=="") s=files[i].getName().toString();
else s=s+","+files[i].getName().toString();
String[] ss=s.split(",");
int length ;
if(ss.length>5)
length = 5;
else length = ss.length;
for(int i=0;i<length;i++)
{
int d=i+1;
switch(d)
{
case 1: rec1.setTitle(ss[0]);
rec1.setVisible(true);
break;
case 2: rec2.setTitle(ss[1]);
rec2.setVisible(true);
break;
case 3: rec3.setTitle(ss[2]);
rec3.setVisible(true);
break;
case 4: rec4.setTitle(ss[3]);
rec4.setVisible(true);
break;
case 5: rec5.setTitle(ss[4]);
rec5.setVisible(true);
break;
default : break;
}
}

}

    //Purpose:Logic to set the scroll bar for the page
private void setscroll()
{
Scroller s=new Scroller(getApplicationContext());
text.setScroller(s);
text.setVerticalScrollBarEnabled(true);
text.setMovementMethod(new ScrollingMovementMethod());
}

@Override
public boolean onCreateOptionsMenu(Menu menu)
{
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.mainmenu,menu);
return true;
}

//Purpose:Logic to switch between the selected items..
@Override
public boolean onOptionsItemSelected(MenuItem item)
{
switch (item.getItemId())
{
case R.id.Compose:
last_opened=null;

if(saved_state==false)
confirmNew();
else text.setText("");
saved_state=true;
break;

case R.id.saveas:
callDialog();
break;

case R.id.save1:

try
{
if(last_opened==null)
callDialog();
else
serv.save1();
}
catch (IOException e1)
{
e1.printStackTrace();
}
break;

case R.id.Recent:
try
{
addRecent(item.getSubMenu());
}
catch (IOException e)
{
e.printStackTrace();
}
break;

case R.id.recent1:
if(saved_state==false)
confirmOpen(item.getTitle());
else
serv.open(item.getTitle());
break;

case R.id.recent2:
if(saved_state==false)
confirmOpen(item.getTitle());
else
serv.open(item.getTitle());
break;

case R.id.recent3:
if(saved_state==false)
confirmOpen(item.getTitle());
else
serv.open(item.getTitle());
break;

case R.id.recent4:
if(saved_state==false)
confirmOpen(item.getTitle());
else
serv.open(item.getTitle());
break;

case R.id.recent5:
if(saved_state==false)
confirmOpen(item.getTitle());
else
serv.open(item.getTitle());
break;

case R.id.Exit:
if(saved_state==true) finish();
else confirmExit();
break;

case R.id.Delete:
confirmDelete();
invalidateOptionsMenu();
break;

case R.id.Settings:
SubMenu sm=item.getSubMenu();
MenuItem dl = sm.findItem(R.id.Delete);
if(last_opened==null)
dl.setEnabled(false);
else dl.setEnabled(true);
break;

case R.id.Theme:
Custom_Theme ct = new Custom_Theme(this);
ct.show();
break;

default:

}

return true;
}


  //Purpose:Logic to get confirmation for the delete operation.
private void confirmDelete()
{
AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
alt_bld.setMessage("Are you sure you want to delete?");
alt_bld.setCancelable(false);
alt_bld.setPositiveButton("Yes", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
serv.delete();
}
});
alt_bld.setNegativeButton("No", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
}
});
AlertDialog alert = alt_bld.create();
// Title for AlertDialog
alert.setTitle("Confirm Delete");
alert.show();

}

//Purpose:Logic to obtain user confirmation on exit
private void confirmExit()
{
AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
alt_bld.setMessage("Do you want to save this before closing?");
alt_bld.setCancelable(false);
alt_bld.setPositiveButton("Yes", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
if(last_opened==null)
callDialog();
else
try
{
serv.save1();
}
catch (IOException e)
{
e.printStackTrace();
}
finish();
}
});
alt_bld.setNegativeButton("No", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
finish();
}
});
alt_bld.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
}
});
AlertDialog alert = alt_bld.create();
// Title for AlertDialog
alert.setTitle("Confirm Exit");
alert.show();

}

//Purpose:Logic to open an existing file without saving the current file.
private void confirmOpen(final CharSequence title)
{
AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
alt_bld.setMessage("Do you want to save this before closing?");
alt_bld.setCancelable(false);
alt_bld.setPositiveButton("Yes", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
try
{
if(last_opened==null)
callDialog();
else serv.save1();
}
catch (IOException e)
{
e.printStackTrace();
}
serv.open(title);
}
});
alt_bld.setNegativeButton("No", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
serv.open(title);
}
});
alt_bld.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
}
});
AlertDialog alert = alt_bld.create();
// Title for AlertDialog
alert.setTitle("Confirm Open");
alert.show();
}

//Purpose:Logic to open a new file without saving the contents of the current file.
private void confirmNew()
{
AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
alt_bld.setMessage("Do you want to save this before closing?");
alt_bld.setCancelable(false);
alt_bld.setPositiveButton("Yes", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
if(last_opened==null)
callDialog();
else
try
{
serv.save1();
}
catch (IOException e)
{
e.printStackTrace();
}
text.setText("");
}
});
alt_bld.setNegativeButton("No", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
text.setText("");
}
});
alt_bld.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
{
public void onClick(DialogInterface dialog, int id)
{
dialog.cancel();
}
});
AlertDialog alert = alt_bld.create();
// Title for AlertDialog
alert.setTitle("Save Before Closing");
alert.show();
}

@Override
public void onSaveInstanceState(Bundle savedInstanceState)
{
// Save the user's current game state
savedInstanceState.putString(STATE_TEXT, text.getText().toString());
// Always call the superclass so it can save the view hierarchy state
super.onSaveInstanceState(savedInstanceState);
}
public void onRestoreInstanceState(Bundle savedInstanceState)
{
// Always call the superclass so it can restore the view hierarchy
super.onRestoreInstanceState(savedInstanceState);
// Restore state members from saved instance
text.setText(savedInstanceState.getString(STATE_TEXT));
}

public static void settext(String s)
{
text.setText(s);
}
public static void text_setsel(int l)
{
text.setSelection(l);
}
public static Editable gettext()
{
return text.getText();
}
private void callDialog()
{
CustomDialogClass cdd=new CustomDialogClass(this, text.getText().toString());
cdd.show();
}
// Dialog box for changing the theme.
public class Custom_Theme extends Dialog implements	android.view.View.OnClickListener
{
Button apply;
Button cancel;
RadioButton light;
RadioButton dark;
public Custom_Theme(Context context)
{
super(context);
}

@Override
public void onClick(View v)
{
switch(v.getId())
{
case R.id.apply_theme:
if(light.isChecked())
{	
text.setBackgroundColor(Color.WHITE);
view.setBackgroundColor(Color.WHITE);
text.setTextColor(Color.BLACK);
}
else if(dark.isChecked())
{
text.setBackgroundColor(Color.BLACK);
view.setBackgroundColor(Color.BLACK);
text.setTextColor(Color.WHITE);
}
dismiss();
break;

case R.id.cancel_theme:
dismiss();
break;

default:

}

}

protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.theme);
apply = (Button) findViewById(R.id.apply_theme);
cancel = (Button) findViewById(R.id.cancel_theme);
light = (RadioButton) findViewById(R.id.Light);
dark = (RadioButton) findViewById(R.id.Dark);
apply.setOnClickListener(this);
cancel.setOnClickListener(this);

}
}
}
