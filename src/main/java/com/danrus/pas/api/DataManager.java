package com.danrus.pas.api;

import java.util.HashMap;

/**
 * DataManager interface for managing data sources and retrieving SkinData.
 * This interface allows adding data sources, retrieving data by player name,
 * and invalidating data when necessary.
 */

public interface DataManager {

    /**
     * Adds a source of data to the manager.
     * The source must implement DataCache interface.
     *
     * @param source the data source to add
     */
    void addSource(DataCache<?> source);

    /**
     * Retrieves data associated with the given string.
     *
     * @param string the name of the player
     * @return SkinData associated with the identifier, or null if not found
     */
    SkinData getData(String string);

    /**
     * Retrieves all data available in the manager.
     *
     * @param name the name of the player
     * @param data Object data to be stored
     */
    void store(String name, Object data);

    /**
     * Invalidates the data associated with the given name.
     * This method should be called when the data is no longer valid or needs to be refreshed.
     *
     * @param name the identifier for the data to invalidate
     */
    void invalidateData(String name);

    /**
     * Retrieves a specific data source by its key.
     *
     * @param key the key of the data source
     * @return the DataCache associated with the key, or null if not found
     */

    DataCache<?> getSource(String key);


    /**
     * Retrieves all data stored in {@link com.danrus.pas.utils.data.GameCache}
     *
     * @return a HashMap containing all game data, where the key is the player name and the value is SkinData
     */

    HashMap<String, SkinData> getGameData();

    /**
     * Finds SkinData by a given string without download.
     *
     * @param string the identifier for the skin data
     * @return SkinData associated with the identifier, or null if not found
     */

    SkinData findData(String string);


    /**
     * Deletes the data associated with the given string.
     *
     * @param string the identifier for the data to delete
     */

    void delete(String string);
}
