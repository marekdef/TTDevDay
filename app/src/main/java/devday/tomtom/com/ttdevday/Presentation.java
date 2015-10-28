/*
 *
 *  Copyright (C) TomTom International B.V., 2015.
 *  All rights reserved.
 * /
 */

package devday.tomtom.com.ttdevday;/*
* Copyright (C) TomTom International B.V., 2015
* All rights reserved.
*/

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Presentation {
    String id;
    String author;
    String title;
    String description;
    @SerializedName("noVotes")
    int number;
}
