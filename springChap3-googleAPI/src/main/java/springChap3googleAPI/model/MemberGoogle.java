package springChap3googleAPI.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class MemberGoogle {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="member_seq")
	@SequenceGenerator(name="member_seq",sequenceName="member_seq",allocationSize=1)
	private Long id;
	private String email;
	private String username;
	
	public MemberGoogle() {
		
	}
	
	public MemberGoogle(String email,String username) {
		this.email = email;
		this.username = username;
	}

}
