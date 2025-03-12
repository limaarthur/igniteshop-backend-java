package com.ignite.igniteshop.services;

import com.ignite.igniteshop.dtos.RoleDTO;
import com.ignite.igniteshop.dtos.UserDTO;
import com.ignite.igniteshop.dtos.UserInsertDTO;
import com.ignite.igniteshop.entities.Role;
import com.ignite.igniteshop.entities.User;
import com.ignite.igniteshop.repositories.RoleRepository;
import com.ignite.igniteshop.repositories.UserRepository;
import com.ignite.igniteshop.services.exceptions.DataBaseException;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(user -> new UserDTO(user));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(user);
    }

    @Transactional()
    public UserDTO insert(UserInsertDTO userInsertDTO) {
        User entity = new User();
        copyDtoToEntity(userInsertDTO, entity);
        entity.setPassword(bCryptPasswordEncoder.encode(userInsertDTO.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        copyDtoToEntity(userDTO, entity);
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation " + id);
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User entity) {
        entity.setFirstName(userDTO.getFirstName());
        entity.setLastName(userDTO.getLastName());
        entity.setEmail(userDTO.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }
}
