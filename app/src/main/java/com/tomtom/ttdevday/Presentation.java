/*
 *
 *  Copyright (C) TomTom International B.V., 2015.
 *  All rights reserved.
 * /
 */

package com.tomtom.ttdevday;/*
* Copyright (C) TomTom International B.V., 2015
* All rights reserved.
*/

import com.google.gson.annotations.SerializedName;

public class Presentation {
    public String id;
    public String author;
    public String title;
    public String description;
    @SerializedName("noVotes")
    public int number;
}
