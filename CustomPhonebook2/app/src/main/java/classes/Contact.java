package classes;

import android.support.annotation.NonNull;
import java.io.Serializable;

/**
 * Created by wra1th on 10/26/2017.
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Contact))
            return false;
        Contact c = (Contact) obj;
        return firstName.equals(c.firstName)
                && lastName.equals(c.lastName)
                && phoneNumber.equals(c.phoneNumber)
                && emailID.equals(c.emailID);
    }

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

    @Override
    public int compareTo(@NonNull Object o) {
        return this.firstName.compareTo(((Contact) o).firstName);
    }

    public static Contact readFromString(String line) {
        String[] split = line.split("\\t");
        Contact c = new Contact();

        if (!(split[0].substring(3)).isEmpty()) {
            c.firstName = (split[0].substring(3)).trim();
        }
        if (!(split[1].substring(3)).isEmpty()) {
            c.lastName = (split[1].substring(3)).trim();
        }
        if (!(split[2].substring(3)).isEmpty()) {
            c.phoneNumber = (split[2].substring(3)).trim();
        }
        if (!(split[3].substring(3)).isEmpty()) {
            c.emailID = (split[3].substring(3)).trim();
        }

        return c;
    }

    public String getWritableString() {
        return S_FIRST_NAME + firstName + "\t"
                + S_LAST_NAME + lastName + "\t"
                + S_PHONE_NUMBER + phoneNumber + "\t"
                + S_EMAIL_ID + emailID;
    }
}