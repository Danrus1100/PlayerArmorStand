package com.danrus.pas.api;

import com.danrus.pas.impl.data.GameData;

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
    void addSource(DataHolder<?> source);

    /**
     * Add a source of data to the manager with priority.
     *
     * @param source the data source to add
     * @param priority the priority of the source, higher values indicate higher priority
     */
    void addSource(DataHolder<?> source, int priority);

    /**
     * Retrieves data associated with the given string.
     *
     * @param info the NameInfo of the player
     * @return SkinData associated with the identifier, or null if not found
     */
    SkinData getData(NameInfo info);

    /**
     * Retrieves all data available in the manager.
     *
     * @param info the NameInfo of the player
     * @param data Object data to be stored
     */
    void store(NameInfo info, Object data);

    /**
     * Invalidates the data associated with the given name.
     * This method should be called when the data is no longer valid or needs to be refreshed.
     *
     * @param info the identifier for the data to invalidate
     */
    void invalidateData(NameInfo info);

    /**
     * Retrieves a specific data source by its key.
     *
     * @param key the key of the data source
     * @return the DataCache associated with the key, or null if not found
     */

    DataHolder<?> getSource(String key);


    /**
     * Retrieves all data stored in {@link GameData}
     *
     * @return a HashMap containing all game data, where the key is the player name and the value is SkinData
     */

    HashMap<String, SkinData> getGameData();

    /**
     * Finds SkinData by a given string without download.
     *
     * @param info the NameInfo for the skin data
     * @return SkinData associated with the identifier, or null if not found
     */

    SkinData findData(NameInfo info);


    /**
     * Deletes the data associated with the given string.
     *
     * @param info the NameInfo for the data to delete
     */

    void delete(NameInfo info);
}
