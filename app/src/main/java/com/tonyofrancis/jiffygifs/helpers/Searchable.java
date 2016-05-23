package com.tonyofrancis.jiffygifs.helpers;

/**
 * Created by tonyofrancis on 5/22/16.
 * Interface that can be implemented by
 * any object that cab perform a search
 */

public interface Searchable {

    /**The performSearch method is used to pass a search query
     * to the object that will handle the search.
     * @param query - Query String
     * @return - Returns a boolean value of the query success
     * */
    boolean performSearch(String query);
}
