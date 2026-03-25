package com.Nexora.NexoraFinance.role.services;

import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.role.entity.Role;
import com.Nexora.NexoraFinance.role.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepo roleRepo;

    @Override
    public Response<Role> createRole(Role role) {
        if(roleRepo.findByName(role.getName()).isPresent()){
            throw new RuntimeException("Role already exists");
        }
        Role savedRole = roleRepo.save(role);

        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role saved successfully")
                .data(savedRole).build();
    }

    @Override
    public Response<Role> updateRole(Role role) {
        Role savedRole = roleRepo.findById(role.getId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        savedRole.setName(role.getName());

        Role updatedRole = roleRepo.save(savedRole);

        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(updatedRole).build();
    }

    @Override
    public Response<List<Role>> getAllRoles(Role role) {
        return null;
    }

    @Override
    public Response<?> deleteRole(Long id) {
        return null;
    }
}
