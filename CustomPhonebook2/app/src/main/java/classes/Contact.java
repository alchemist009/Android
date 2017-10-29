/**
 * Helper class to create objects for contacts and implement custom method like toString(),
 * compareTo(), and equals() for them.
 *
 * @authors: Gunjan Tomer, Koulick Sankar Paul
 *
 * NetIDs: gxt160930, ksp160330
 *
 * version 1.3: 10/28/2017
 */
package classes;

import android.support.annotation.NonNull;
import java.io.Serializable;

/**
 * @authors: Koulick Sankar Paul, Gunjan Tomer
 *
 * Class to store contact details as objects
 */

public class Contact implements Comparable, Serializable {
    private String firstName, lastName;
    private String phoneNumber, emailID;

    private static final String S_FIRST_NAME = "FN:";
    private static final String S_LAST_NAME = "LN:";
    private static final String S_PHONE_NUMBER = "PN:";
    private static final String S_EMAIL_ID = "EI:";

    private Contact(){}

    public Contact(String firstName, String lastName, String phoneNumber, String emailID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailID = emailID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailID() {
        return emailID;
    }


    /**
     *
     * @authors: Gunjan Tomer
     *
     * Method to override the default equals and use it to compare contact objects instead.
     *
     * A true value is returned when either the corresponding contact details match or one
     * of the fields is left empty after modification and then compared to an existing object in
     * the contacts object list.
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Contact))
            return false;
        Contact c = (Contact) obj;
        return (firstName.equals(c.firstName) || firstName.equals(""))
                && (lastName.equals(c.lastName) || lastName.equals(""))
                && (phoneNumber.equals(c.phoneNumber) || phoneNumber.equals(""))
                && (emailID.equals(c.emailID) || emailID.equals(""));
    }

    /**
     * @authors: Koulick Sankar Paul
     *
     * Method to override the default toString operation and use a StringBuilder to create a string
     * with all the contact details appended separated by whitespace.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (lastName != null) {
            sb.append(" ").append(lastName);
        }
        if (phoneNumber != null) {
            sb.append(" (").append(phoneNumber).append(")");
        }
        return sb.toString();
    }

    /**
     *
     * @author: Koulick Sankar Paul
     *
     * Method to override the default compareTo operation to perform comparisons based on FirstName
     *
     * @param o
     * @return int
     */
    @Override
    public int compareTo(@NonNull Object o) {
        return this.firstName.compareTo(((Contact) o).firstName);
    }

    /**
     * @author: Koulick Sankar Paul
     *
     * Method to read a string separated by tabs from the text file and store each field in the
     * corresponding field in the Contact object.
     *
     * @param line
     * @return Contact object
     */
    public static Contact readFromString(String line) {
        String[] split = line.split("\\t");
        Contact c = new Contact();

        if (!(split[0].substring(3)).isEmpty() && !split[0].substring(3).equals("null")) {
            c.firstName = (split[0].substring(3)).trim();
        }
        if (!(split[1].substring(3)).isEmpty() && !split[1].substring(3).equals("null")) {
            c.lastName = (split[1].substring(3)).trim();
        }
        if (!(split[2].substring(3)).isEmpty() && !split[2].substring(3).equals("null")) {
            c.phoneNumber = (split[2].substring(3)).trim();
        }
        if (!(split[3].substring(3)).isEmpty() && !split[3].substring(3).equals("null")) {
            c.emailID = (split[3].substring(3)).trim();
        }

        return c;
    }

    /**
     * @author: Gunjan Tomer
     *
     * Method to get a string separated by tabs ready to be written to the text file.
     *
     * @return String
     */
    public String getWritableString() {
        return S_FIRST_NAME + firstName + "\t"
                + S_LAST_NAME + lastName + "\t"
                + S_PHONE_NUMBER + phoneNumber + "\t"
                + S_EMAIL_ID + emailID;
    }
}