package akiniyalocts.imgur;

/**
 * Created by AKiniyalocts on 2/23/15.
 */
public class Constants {
  /*
    Logging flag
   */
  public static final boolean LOGGING = true;

  /*
    Your imgur client id. You need this to upload to imgur.

    More here: https://api.imgur.com/
   */
  public static final String MY_IMGUR_CLIENT_ID = "820525b63a9a829";
  public static final String MY_IMGUR_CLIENT_SECRET = "23d4779d8b16ccee3ebb4eea337e7068659d0dbb";

  /*
    Redirect URL for android.
   */
  public static final String MY_IMGUR_REDIRECT_URL = "http://wallapet.com";

  /*
    Client Auth
   */
  public static String getClientAuth(){
    return "Client-ID " + MY_IMGUR_CLIENT_ID;
  }

}
