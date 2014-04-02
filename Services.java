package com.example.notepad;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.os.Environment;


public class Services
{
    //Purpose:Logic to save the existing file after edit.
public void save1() throws IOException
{
LaunchPage_Main.saved_state=true;
File myFile = new File(LaunchPage_Main.last_opened);
FileOutputStream fos = new FileOutputStream(myFile);
String body=LaunchPage_Main.gettext().toString();
        fos.write(body.getBytes());
        fos.close();
}

//Purpose:Logic to open an existing file.
public void open(CharSequence title)
{
try
{
File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/NP/");
String Title=path.getAbsolutePath()+"/"+title.toString();
LaunchPage_Main.last_opened=Title;
new StringBuffer();
String everything=readFile(Title);
LaunchPage_Main.opened_text = everything;
LaunchPage_Main.settext(everything);
LaunchPage_Main.text_setsel(LaunchPage_Main.gettext().length());
LaunchPage_Main.saved_state=true;
}

catch(Exception e)
{
e.getStackTrace();
}
}

//Purpose:Logic to read the contents of a file.
private String readFile(String pathname) throws IOException
{
File file = new File(pathname);
StringBuilder fileContents = new StringBuilder((int)file.length());
Scanner scanner = new Scanner(file);
String lineSeparator = System.getProperty("line.separator");

try {
while(scanner.hasNextLine())
{
String nl=scanner.nextLine();
if(!scanner.hasNextLine())
fileContents.append(nl);
else fileContents.append(nl+lineSeparator);
}
return fileContents.toString();
}
finally
{
scanner.close();
}
}
public void delete()
{
if(LaunchPage_Main.last_opened==null)
return;
File file = new File(LaunchPage_Main.last_opened);
file.delete();
LaunchPage_Main.last_opened=null;
LaunchPage_Main.settext("");
LaunchPage_Main.saved_state=true;
}
}
