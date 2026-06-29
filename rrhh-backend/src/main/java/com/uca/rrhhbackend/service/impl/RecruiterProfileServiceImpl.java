package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.RecruiterProfileRequest;
import com.uca.rrhhbackend.dto.response.RecruiterProfileResponse;
import com.uca.rrhhbackend.entity.Company;
import com.uca.rrhhbackend.entity.RecruiterProfile;
import com.uca.rrhhbackend.entity.Role;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ConflictException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.RecruiterProfileMapper;
import com.uca.rrhhbackend.repository.CompanyRepository;
import com.uca.rrhhbackend.repository.RecruiterProfileRepository;
import com.uca.rrhhbackend.repository.RoleRepository;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.service.RecruiterProfileService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruiterProfileServiceImpl
        implements RecruiterProfileService {

    private static final String RECRUITER_ROLE = "RECRUITER";

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public RecruiterProfileServiceImpl(
            RecruiterProfileRepository recruiterProfileRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RecruiterProfileResponse create(
            RecruiterProfileRequest request
    ) {
        String email = request.email()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        Company company = findActiveCompany(
                request.companyId()
        );

        Role recruiterRole = roleRepository
                .findByNameIgnoreCase(RECRUITER_ROLE)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RECRUITER_ROLE);
                    role.setActive(true);

                    return roleRepository.save(role);
                });

        if (!Boolean.TRUE.equals(recruiterRole.getActive())) {
            throw new BusinessException(
                    "El rol de reclutador no está activo"
            );
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setSurname(request.surname().trim());
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setActive(true);
        user.setBlocked(false);
        user.setRole(recruiterRole);

        User savedUser = userRepository.save(user);

        RecruiterProfile recruiterProfile =
                new RecruiterProfile();

        recruiterProfile.setUser(savedUser);
        recruiterProfile.setCompany(company);
        recruiterProfile.setPosition(
                request.position().trim()
        );

        RecruiterProfile savedProfile =
                recruiterProfileRepository.save(
                        recruiterProfile
                );

        return RecruiterProfileMapper.toResponse(
                savedProfile
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterProfileResponse> findAll() {
        return recruiterProfileRepository.findAll()
                .stream()
                .map(RecruiterProfileMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterProfileResponse findById(Long id) {
        return RecruiterProfileMapper.toResponse(
                findEntity(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterProfileResponse> findByCompany(
            Long companyId
    ) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Empresa no encontrada"
            );
        }

        return recruiterProfileRepository
                .findByCompanyId(companyId)
                .stream()
                .map(RecruiterProfileMapper::toResponse)
                .toList();
    }

    @Override
    public RecruiterProfileResponse update(
            Long id,
            RecruiterProfileRequest request
    ) {
        RecruiterProfile recruiterProfile =
                findEntity(id);

        User user = recruiterProfile.getUser();

        String email = request.email()
                .trim()
                .toLowerCase();

        boolean emailChanged =
                !user.getEmail().equalsIgnoreCase(email);

        if (emailChanged
                && userRepository.existsByEmailIgnoreCase(email)) {

            throw new ConflictException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        Company company = findActiveCompany(
                request.companyId()
        );

        user.setName(request.name().trim());
        user.setSurname(request.surname().trim());
        user.setEmail(email);

        /*
         * Si se envía una contraseña diferente,
         * se actualiza cifrada.
         */
        if (request.password() != null
                && !request.password().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.password()
                    )
            );
        }

        userRepository.save(user);

        recruiterProfile.setPosition(
                request.position().trim()
        );

        recruiterProfile.setCompany(company);

        RecruiterProfile savedProfile =
                recruiterProfileRepository.save(
                        recruiterProfile
                );

        return RecruiterProfileMapper.toResponse(
                savedProfile
        );
    }

    @Override
    public void deactivate(Long id) {
        RecruiterProfile recruiterProfile =
                findEntity(id);

        User user = recruiterProfile.getUser();

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException(
                    "El reclutador ya está inactivo"
            );
        }

        user.setActive(false);

        userRepository.save(user);
    }

    private RecruiterProfile findEntity(Long id) {
        return recruiterProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reclutador no encontrado con id "
                                        + id
                        )
                );
    }

    private Company findActiveCompany(Long companyId) {
        Company company = companyRepository
                .findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empresa no encontrada"
                        )
                );

        if (!Boolean.TRUE.equals(company.getActive())) {
            throw new BusinessException(
                    "No se puede asociar un reclutador a una empresa inactiva"
            );
        }

        return company;
    }
}