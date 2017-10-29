package classes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wra1th on 10/26/2017.
 */

public class FileHandler {

    private static final String FILENAME = "mycontacts.txt";

    public static List<Contact> readContacts(Context context) throws IOException {
        List<Contact> list = new ArrayList<>();
        FileInputStream stream = context.openFileInput(FILENAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        while((line = br.readLine()) != null) {
            list.add(Contact.readFromString(line));
        }
        br.close();
        stream.close();
        Collections.sort(list);
        return list;
    }

    public static boolean addContact(Context context, Contact contact) throws IOException {
        List<Contact> contacts = readContacts(context);
        contacts.add(contact);
        Collections.sort(contacts);
        FileOutputStream stream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
        for (Contact c : contacts) {
            bw.write(c.getWritableString());
            bw.newLine();
        }
        bw.close();
        stream.close();
        return true;
    }

    public static boolean modifyContact(Context context, Contact contact) throws IOException {
        List<Contact> contacts = readContacts(context);
        int index = 0;
        for (Contact c : contacts) {
            index += 1;
            if(contact.getFirstName().equals(c.getFirstName()) ||
                    contact.getLastName().equals(c.getLastName()) ||
                    contact.getEmailID().equals(c.getEmailID()) ||
                    contact.getPhoneNumber().equals(c.getPhoneNumber())) {
                break;
            }
        }
        contacts.remove(index-1);
        contacts.add(contact);
        Collections.sort(contacts);
        FileOutputStream stream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
        for (Contact c : contacts) {
            bw.write(c.getWritableString());
            bw.newLine();
        }
        bw.close();
        stream.close();
        return true;
    }

    public static boolean deleteContact(Context context, Contact contact) throws IOException {
        List<Contact> contacts = readContacts(context);
        contacts.remove(contact);
        Collections.sort(contacts);
        FileOutputStream stream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
        for (Contact c : contacts) {
            bw.write(c.getWritableString());
            bw.newLine();
        }
        bw.close();
        stream.close();
        return true;
    }

    public static boolean createFileIfNotPresent(Context context) throws IOException {
        File file = new File(String.valueOf(context.getFilesDir()));
        File f = new File(file, FILENAME);
        if(!f.exists()) {
            f.createNewFile();
        }
        return true;
    }
}