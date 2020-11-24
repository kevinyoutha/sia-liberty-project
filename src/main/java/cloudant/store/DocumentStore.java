package cloudant.store;

import java.util.Collection;

import cloudant.Document;

import javax.enterprise.context.RequestScoped;

/**
 * Defines the API for a Document store.
 *
 */
public interface DocumentStore {

    /**
     * Get the target db object.
     * 
     * @return Database.
     * @throws Exception 
     */
    Object getDB();

  
    /**
     * Gets all Visitors from the store.
     * 
     * @return All Visitors.
     * @throws Exception 
     */
    Collection<Document> getAll();

    /**
     * Gets an individual ToDo from the store.
     * @param id The ID of the ToDo to get.
     * @return The ToDo.
     */
    Document get(String id);

    /**
     * Persists a Visitor to the store.
     * @param dc The document to persist.
     * @return The persisted document.  The document will not have a unique ID..
     */
    Document persist(Document dc);

}
