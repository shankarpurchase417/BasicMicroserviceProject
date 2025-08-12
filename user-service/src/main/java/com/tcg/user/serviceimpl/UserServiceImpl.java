package com.tcg.user.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcg.user.dto.UserDto;
import com.tcg.user.entity.User;
import com.tcg.user.repository.UserRepository;
import com.tcg.user.service.UserService;

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
	public UserDto getUserById(Integer id) {
        Optional<User> findUser = userrepo.findById(id);
        
       UserDto userdto = modelmapper.map(findUser, UserDto.class);
       return userdto;
    }
	

}
