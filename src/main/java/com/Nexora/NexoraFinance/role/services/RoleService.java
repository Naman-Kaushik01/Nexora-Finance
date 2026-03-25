package com.Nexora.NexoraFinance.role.services;

import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.role.entity.Role;

import java.util.List;

public interface RoleService {
    Response<Role> createRole(Role role);

    Response<Role> updateRole(Role role);

    Response<List<Role>> getAllRoles();

    Response<?> deleteRole(Long id);

}
