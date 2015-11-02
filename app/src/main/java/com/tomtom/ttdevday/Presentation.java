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

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(table = "presentations", provider = "PresentationsProvider")
public class Presentation {
    @SimpleSQLColumn("id")
    public String id;
    @SimpleSQLColumn("author")
    public String author;
    @SimpleSQLColumn("title")
    public String title;
    @SimpleSQLColumn("description")
    public String description;
    @SerializedName("noVotes")
    @SimpleSQLColumn("votes_number")
    public int number;
}
