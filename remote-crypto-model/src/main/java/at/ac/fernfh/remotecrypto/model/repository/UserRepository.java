package at.ac.fernfh.remotecrypto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import at.ac.fernfh.remotecrypto.model.RegisteredUser;

@Repository
public interface UserRepository extends JpaRepository<RegisteredUser, Long>{

	public List<RegisteredUser> findByPulbicKeyHash(String pulbicKeyHash);
	
	public List<RegisteredUser> findBySubject(String subject);
}
