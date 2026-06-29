package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.entity.User;

public interface CurrentUserService {

    /**
     * Obtiene al usuario autenticado directamente desde el contexto
     * de seguridad generado por el token JWT.
     *
     * @return usuario autenticado
     */
    User getCurrentUser();

    /**
     * Verifica que el usuario tenga el rol CANDIDATE.
     *
     * @param user usuario autenticado
     */
    void requireCandidate(User user);

    /**
     * Verifica que el usuario tenga el rol RECRUITER.
     *
     * @param user usuario autenticado
     */
    void requireRecruiter(User user);

    /**
     * Verifica que el usuario tenga el rol ADMIN.
     *
     * @param user usuario autenticado
     */
    void requireAdmin(User user);

    /**
     * Verifica si el usuario posee alguno de los roles indicados.
     *
     * @param user         usuario autenticado
     * @param allowedRoles roles permitidos
     */
    void requireAnyRole(User user, String... allowedRoles);
}