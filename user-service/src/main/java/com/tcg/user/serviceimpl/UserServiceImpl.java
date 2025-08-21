package com.tcg.user.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcg.user.dto.UserDto;
import com.tcg.user.entity.User;
import com.tcg.user.repository.UserRepository;
import com.tcg.user.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
	UserRepository userrepo;
    
    @Autowired
    ModelMapper modelmapper;
	
	@Override
	public UserDto saveUser(UserDto userdto) {
		
		User user = modelmapper.map(userdto, User.class);
				
				User saveduser = userrepo.save(user);
				UserDto responsedto = modelmapper.map(saveduser, UserDto.class);
		return responsedto;
		
		
	}
	
	@Override
	@CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
	@Retry(name = "userService")
	public UserDto getUserById(Integer id) {
	    Optional<User> findUser = userrepo.findById(id);

	    if (findUser.isEmpty()) {
	        throw new RuntimeException("User not found with id " + id);
	    }

	    return modelmapper.map(findUser.get(), UserDto.class);
	}


    /**
     * Fallback method जब DB down या लगातार failure हो
     */
    public UserDto getUserFallback(Integer id, Throwable ex) {
        System.out.println("Fallback executed due to: " + ex.getMessage());

        UserDto fallbackUser = new UserDto();
        fallbackUser.setUserId(id);
        fallbackUser.setName("Default User");
        fallbackUser.setAddress("Default address ");
    
        return fallbackUser;
    }
	

}
