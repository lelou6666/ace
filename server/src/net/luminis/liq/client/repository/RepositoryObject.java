package net.luminis.liq.client.repository;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

/**
 * A basic repository object, intended to be stored in a ObjectRepository of its given type.
 * A RepositoryObject is associatable.
 */
public interface RepositoryObject extends Associatable {
    /**
     * This key is used to store the RepositoryObject an event comes from, in the Event object.
     */
    public final static String EVENT_ENTITY = "entity";

    public static final String PUBLIC_TOPIC_ROOT = RepositoryObject.class.getPackage().getName().replace('.', '/') + "/public/";
    public static final String PRIVATE_TOPIC_ROOT = RepositoryObject.class.getPackage().getName().replace('.', '/') + "/private/";

    public static final String TOPIC_ADDED_SUFFIX = "ADDED";
    public static final String TOPIC_REMOVED_SUFFIX = "REMOVED";
    public static final String TOPIC_CHANGED_SUFFIX = "CHANGED";
    public static final String TOPIC_ALL_SUFFIX = "*";

    /**
     * Adds a named attribute to this object's attributes. If the name already exists,
     * it will be overwritten, and the old value is returned; will return <code>null</code>
     * when the value is new.
     */
    public String addAttribute(String key, String value);
    /**
     * Gets a named attribute. Returns <code>null<code> when the attribute named by
     * <code>key</code> does not exist.
     */
    public String getAttribute(String key);
    /**
     * Returns an enumeration of all attribute keys.
     */
    public Enumeration<String> getAttributeKeys();
    /**
     * Adds a named tag to this object's attributes. If the name already exists,
     * it will be overwritten, and the old value is returned; will return <code>null</code>
     * when the value is new.
     */
    public String addTag(String key, String value);
    /**
     * Gets a named tag. Returns <code>null<code> when the attribute named by
     * <code>key</code> does not exist.
     */
    public String getTag(String key);
    /**
     * Returns an enumeration of all tags in this object, coming from both the
     * tags and the attributes.
     */
    public Enumeration<String> getTagKeys();
    /**
     * Returns a <code>Dictionary</code> representing this object. It will contain all keys,
     * from <code>getTagKeys</code>, and all values that correspond to them. If a key is present
     * in both the attributes and the tags, the corresponding value will be an array of two
     * <code>String</code> objects; otherwise a single <code>String</code> is returned.
     */
    public Dictionary<String, Object> getDictionary();
    /**
     * Indicates that this object should no longer be used.
     */
    public boolean isDeleted();
    /**
     * Creates a filter string for use in associations, optionally with some
     * additional properties. The basic implementation will use all <code>getDefiningKeys</code>.
     * @param properties Properties indicating specifics of the filter to be created.
     * @return A string representation of a filter, for use in <code>Association</code>s.
     */
    public String getAssociationFilter(Map<String, String> properties);

    /**
     * Determines the cardinality of this endpoint of an association, given
     * the passed properties.
     * @param properties Properties indicating specifics of this endpoint.
     * @return The necessary cardinality.
     */
    public int getCardinality(Map<String, String> properties);

    /**
     * Returns a <code>Comparator</code> for this type of object. Descendent
     * classes are expected to return a comparator if they can be meaningfully compared,
     * and otherwise (if no order is natural), return <code>null</code>.
     * @return A <code>Comparator</code> for this type of object
     */
    @SuppressWarnings("unchecked")
    public Comparator getComparator();

    /**
     * Returns a string which uniquely defines this object. The content is
     * not intended to 'mean' anything.
     * @return A uniquely defining string.
     */
    public String getDefinition();

    public enum WorkingState {
        New, Changed, Unchanged, Removed;
    }
}
