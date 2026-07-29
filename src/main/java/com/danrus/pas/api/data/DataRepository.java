package com.danrus.pas.api.data;

import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.info.NameInfoLike;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * DataManager interface for managing data sources and retrieving SkinData.
 * This interface allows adding data sources, retrieving data by player name,
 * and invalidating data when necessary.
 */

public interface DataRepository<T extends DataHolder> {

    /**
     * Adds a source of data to the manager.
     * The source must implement DataCache interface.
     *
     * @param source the data source to add
     */
    void addSource(DataProvider<T> source);

    /**
     * Add a source of data to the manager with priority.
     *
     * @param source the data source to add
     * @param priority the priority of the source, higher values indicate higher priority
     */
    void addSource(DataProvider<T> source, int priority);

    /**
     * Retrieves data associated with the given string.
     *
     * @param info the NameInfo of the player
     * @return SkinData associated with the identifier, or null if not found
     */
    Optional<T> getData(NameInfo info);

    /**
     * Stores data associated with the given NameInfo.
     *
     * @param info the NameInfo of the player
     * @param data Object data to be stored
     */
    void store(NameInfo info, T data);

    /**
     * Invalidates the data associated with the given name.
     * This method should be called when the data is no longer valid or needs to be refreshed.
     *
     * @param info the identifier for the data to invalidate
     */
    void invalidateData(NameInfoLike info);

    /**
     * Retrieves a specific data source by its key.
     *
     * @param key the key of the data source
     * @return the DataCache associated with the key, or null if not found
     */
    DataProvider<T> getSource(String key);

    /**
     * Retrieves all data sources managed by this DataManager.
     * @return a HashMap containing all data sources, where the key is the source key and the value is the DataCache
     */
    Map<String, DataProvider<T>> getSources();

    /**
     * Finds first DataHolder by a given string without download.
     *
     * @param infoLike the NameInfoLike for the skin data
     * @return DataHolder associated with the identifier
     */
    Optional<T> findFirst(NameInfoLike infoLike);

    /**
     * Finds all DataHolder by a given string without download.
     *
     * @param infoLike the NameInfoLike for the skin data
     * @return DataHolder associated with the identifier
     */

    Map<NameInfo, T> findAll(NameInfoLike infoLike);

    /**
     * Checks for value in cache, without any state change
     * @param info target
     * @return first founded value
     */

    Optional<T> peek(NameInfo info);

    /**
     * checks is downloading of this name info needed in reload
     * @param info NameInfo
     * @return boolean
     */
    boolean cancelRedownload(NameInfo info);

    /**
     * Deletes the data associated with the given string.
     *
     * @param info the NameInfo for the data to delete
     */
    void deleteAllOf(NameInfoLike info);

    Set<NameInfo> allNames();
}
