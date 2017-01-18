package com.tabsappdeneme.mnuriyumusak.tabsdeneme;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nuri on 16.01.2017.
 */

public class PictureNameCreator {
    public String getPictureName()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = sdf.format(new Date());
        return "tabs" + timestamp + ".jpg";
    }
}
