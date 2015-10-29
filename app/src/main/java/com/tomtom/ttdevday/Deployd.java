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

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

public interface Deployd {
    String TEST_API = "http://192.168.58.1:2403";
    String API = "http://nodejs-ttdevday.rhcloud.com/";
    @GET("/presentations")
    abstract Observable<List<Presentation>> presentations();

    @POST("/votes")
    abstract Observable<Vote> vote(@Body Vote vote);
}
