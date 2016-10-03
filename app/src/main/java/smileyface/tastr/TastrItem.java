package smileyface.tastr;

/**
 *class: public class TastrItem
 *Created by Josh on 9/25/2016.
 *desc: class to hold data for the items that the user will see and select from
 *notes:
**/
public class TastrItem
{
  //parameters
  public static String tastrID;
  public static String name;
  public static String description;
  public static String restaurant;
  public static String yelpRestaurantID;
  public static float  rating;
  public        String imagePath;
  public static String imageID;

  //constructors
  public TastrItem()
  {
    tastrID          = "";
    name             = "";
    description      = "";
    restaurant       = "";
    yelpRestaurantID = "";
    rating           =  0;
    imagePath        = "";
    imageID          = "";
  }
  //convenience constructor
  public TastrItem(String mTastrID, String mName, String mDescription, String mRestraunt, String mYelpID, float mRating, String mImagePath, String mImageID)
  {
    tastrID          = mTastrID;
    name             = mName;
    description      = mDescription;
    restaurant       = mRestraunt;
    yelpRestaurantID = mYelpID;
    rating           = mRating;
    imagePath        = mImagePath;
    imageID          = mImageID;
  }
  //potential CSV style construtor and CSV style saving.
  /*
  public TastrItem(const String tastrItemCSV)
  {
    parse tastrItemCSV into a List of Strings
    iterate through the list and assign the values
  }
  */

  /*
  public String toCSVString()
  {
    build string out of parameters
    return string;
  }
  */
}
