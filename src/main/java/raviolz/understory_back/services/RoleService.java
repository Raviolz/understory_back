package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Role;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.RoleDTO;
import raviolz.understory_back.payloads.UpdateRoleDTO;
import raviolz.understory_back.repositories.RoleRepository;

import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(RoleDTO body) {
        String normalizedCode = body.code().trim().toUpperCase();
        String normalizedLabel = body.label().trim();

        if (roleRepository.existsByCode(normalizedCode)) {
            throw new ValidationException("Role code " + body.code() + " already exists");
        }

        if (roleRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Role label " + body.label() + " already exists");
        }

        Role role = new Role(
                body.code(),
                body.label(),
                body.description()
        );

        return roleRepository.save(role);
    }

    public Page<Role> findAll(int page, int size, String sortBy) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return roleRepository.findAll(pageable);
    }

    public Role findById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }

    public Role findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Role code is required");
        }

        String normalizedCode = code.trim().toUpperCase();

        return roleRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new NotFoundException("Role with code " + code + " not found"));
    }

    public Role update(UUID id, UpdateRoleDTO body) {
        Role found = findById(id);

        String normalizedLabel = body.label().trim();

        if (!found.getLabel().equals(normalizedLabel) && roleRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Role label " + body.label() + " already exists");
        }

        found.setLabel(body.label());
        found.setDescription(body.description());

        return roleRepository.save(found);
    }
}