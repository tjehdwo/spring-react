package springChap3naverAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springChap3naverAPI.model.UserSNS;

public interface UserRepository extends JpaRepository<UserSNS,Long> {

}
