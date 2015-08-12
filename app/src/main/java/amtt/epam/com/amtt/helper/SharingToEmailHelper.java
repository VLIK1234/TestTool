package amtt.epam.com.amtt.helper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.util.Constants;

/**
 * @author IvanBakach
 * @version on 07.08.2015
 */
public class SharingToEmailHelper {

    public static void senAttachmentImage(Activity activity, String message, ArrayList<String> listUri){
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("*/*");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getCurrentUsersEmail(activity)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Attachment from amtt");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(Intent.EXTRA_STREAM, convertListFileToListUri(listUri));
        activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private static String getCurrentUsersEmail(Context context){
        String possibleEmail = Constants.Symbols.EMPTY;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        return possibleEmail;
    }

    private static ArrayList<Uri> convertListFileToListUri(ArrayList<String> listFilePaths){
        ArrayList<Uri> listUri = new ArrayList<>();
        for (String filePath:listFilePaths) {
            listUri.add(Uri.fromFile(new File(filePath)));
        }
        return listUri;
    }
}
