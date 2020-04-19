package at.ac.fernfh.remotecrypto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.fernfh.remotecrypto.model.RegisteredUser;

/**
 * Repository definition for all registered users.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<RegisteredUser, Long>{

	/**
	 * Find all users with the defined public key. 
	 * 
	 * @param pulbicKeyHash hash over the public key.
	 * 
	 * @return list of all users found.
	 */
	public List<RegisteredUser> findByPulbicKeyHash(String pulbicKeyHash);
	
	/**
	 * Find the registered users with the defined subject.
	 * 
	 * @param subject the subject of the user.
	 * 
	 * @return list of all users found.
	 */
	public List<RegisteredUser> findBySubject(String subject);
}
