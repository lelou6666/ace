package net.luminis.liq.client.repositoryuseradmin;

import java.io.IOException;
import java.net.URL;

import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * RepositoryUserAdmin is used for managing a User Admin repository
 * that is present on a server. It uses the UserAdmin interface to
 * allow alterations. Any non-supported functions from UserAdmin 
 * (or its related classes) will result in a {@link UnsupportedOperationException}.<br>
 * <br>
 * This service uses the same checkout/commit/revert scheme that
 * RepositoryAdmin does; when making changes, they will always be stored locally,
 * but they will only be updated on the server once commit is called.<br>
 * <br>
 * Note that this implementation will <b>not</b> send any events.
 */
public interface RepositoryUserAdmin extends UserAdmin {

    /**
     * Logs in to a specific repository location.
     * @param user A user object to use in the connection
     * @param repositoryLocation A URL representing the base URL of the repository service
     * @param repositoryCustomer The 'customer' for which the repository is registered
     * @param repositoryName The 'name' for which the repository is registered
     * @param writeAccess <code>true</code> if write-access is required, <code>false</code> otherwise.
     * @throws IOException Thrown when there is a problem handling the backup files.
     */
    public void login(User user, URL repositoryLocation, String repositoryCustomer, String repositoryName) throws IOException;
    
    /**
     * Logs out the user.
     * @param force Even when something goes wrong, force a logout.
     * @throws IOException When there is a problem writing the current status to local storage.
     */
    public void logout(boolean force) throws IOException;
    
    /**
     * Checks out the latest version from the server. If any changes exist, they will
     * be reflected in this service's user admin.
     * @throws IOException If there is a problem communicating with the server.
     */
    public void checkout() throws IOException;

    /**
     * Writes all changes made to this service's user admin to the server.
     * @throws IOException If there is a problem communicating with the server.
     */
    public void commit() throws IOException;

    /**
     * Undoes all changes to this service's user admin, and restores the previously
     * checked out or committed version.
     * @throws IOException If there is a problem retrieving the data from the
     * local backup.
     */
    public void revert() throws IOException;
}
