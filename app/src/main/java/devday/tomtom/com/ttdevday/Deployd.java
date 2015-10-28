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

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface Deployd {
    String TEST_API = "http://192.168.58.1:2403";
    String API = "http://nodejs-ttdevday.rhcloud.com/";
    @GET("/presentations")
    abstract Observable<List<Presentation>> presentations();
}
