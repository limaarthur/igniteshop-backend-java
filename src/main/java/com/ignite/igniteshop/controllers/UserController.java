package com.ignite.igniteshop.controllers;

import com.ignite.igniteshop.dtos.UserDTO;
import com.ignite.igniteshop.dtos.UserInsertDTO;
import com.ignite.igniteshop.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        Page<UserDTO> userDTO = userService.findAllPaged(pageable);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO userInsertDTO) {
        UserDTO newUserDTO = userService.insert(userInsertDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(newUserDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO = userService.update(id, userDTO);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
