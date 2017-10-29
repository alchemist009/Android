/**
 *
 * Helper class to perform all the required operations like modify, delete and save for contacts.
 *
 * @authors: Gunjan Tomer, Koulick Sankar Paul
 *
 * NetIDs: gxt160930, ksp160330
 *
 * version 1.3: 10/28/2017
 */
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


public class FileHandler {

    private static final String FILENAME = "mycontacts.txt";


    /**
     * @author: Gunjan Tomer
     *
     * Method to read contacts from text file and store in a list.
     *
     * @param context
     * @return List of contacts
     * @throws IOException
     */

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

    /**
     * @author: Koulick Sankar Paul
     *
     * Method to add a new contact to the text file using an object
     *
     * @param context
     * @param contact
     * @return boolean
     * @throws IOException
     */

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

    /**
     * @author: Gunjan Tomer
     *
     * Method to modify an existing contact in the text file when selected from ListView
     *
     * @param context
     * @param contact
     * @return boolean
     * @throws IOException
     */
    public static boolean modifyContact(Context context, Contact contact) throws IOException {
        List<Contact> contacts = readContacts(context);
        int index = 0;
        /**
         * Iterate through each contact in the list and find the index of the
         * one that matches theo one being currently modified
         */
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

    /**
     * @author: Koulick Sankar Paul
     *
     * Method to delete a contact from the text file
     *
     * @param context
     * @param contact
     * @return boolean
     * @throws IOException
     */
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

    /**
     * @author: Gunjan Tomer
     *
     * Method to create a new text file to store contacts
     *
     * @param context
     * @return boolean
     * @throws IOException
     */
    public static boolean createFileIfNotPresent(Context context) throws IOException {
        File file = new File(String.valueOf(context.getFilesDir()));
        File f = new File(file, FILENAME);
        if(!f.exists()) {
            f.createNewFile();
        }
        return true;
    }
}