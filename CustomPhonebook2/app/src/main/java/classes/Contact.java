package classes;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by wra1th on 10/26/2017.
 */

public class Contact implements Comparable, Serializable {
    private String firstName, lastName;
    private String phoneNumber, emailID;

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
        return firstName + " " + lastName + " (" + phoneNumber + ")";
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.firstName.compareTo(((Contact) o).firstName);
    }

    public static Contact readFromString(String line) {
        String[] split = line.split("\\s+");
        Contact c = new Contact();
        c.firstName = split[0].trim();
        c.lastName = (split[1] != null && !split[1].isEmpty())? split[1].trim() : "";
        c.phoneNumber = (split[2] != null && !split[2].isEmpty())? split[2].trim() : "";
        c.emailID = (split[3] != null && !split[3].isEmpty())? split[3].trim() : "";
        return c;
    }

    public String getWritableString() {
        return firstName + "\t"
                + lastName + "\t"
                + phoneNumber + "\t"
                + emailID;
    }
}
