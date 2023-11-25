package com.proyectouts.adminong.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyectouts.adminong.config.SedeDTOConverter;
import com.proyectouts.adminong.dto.SedeDTO;
import com.proyectouts.adminong.repositories.OrganizacionRepository;
import com.proyectouts.adminong.repositories.SedeRepository;
import com.proyectouts.adminong.repositories.VoluntarioRepository;
import com.proyectouts.adminong.repositories.entities.OrganizacionEntity;
import com.proyectouts.adminong.repositories.entities.SedeEntity;
import com.proyectouts.adminong.repositories.entities.VoluntarioEntity;
import com.proyectouts.adminong.services.SedeService;
import jakarta.transaction.Transactional;


@Service
public class SedeServiceImpl implements SedeService{

    @Autowired
    private SedeRepository sedeRepository;

     @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Autowired
    private SedeDTOConverter sedeDTOConverter;

    @Override
    @Transactional
    public void save(SedeDTO sedeDTO) {
        SedeEntity sedeEntity = sedeDTOConverter.convertToEntity(sedeDTO);

        VoluntarioEntity voluntarioJefe = voluntarioRepository.findById(sedeDTO.getVoluntarioJefeId()).orElse(null);
        OrganizacionEntity organizacion = organizacionRepository.findById(sedeDTO.getOrganizacionId()).orElse(null);

        if (voluntarioJefe == null || organizacion == null) {
            return;
        }

        sedeEntity.setVoluntarioJefe(voluntarioJefe);
        sedeEntity.setOrganizacion(organizacion);

        sedeRepository.save(sedeEntity);
    }

    @Override
    public List<SedeDTO> findAll() {
        List<SedeEntity> sedeEntities = (List<SedeEntity>) sedeRepository.findAll();
        return sedeEntities.stream().map(sede->sedeDTOConverter.convertToDTO(sede)).toList();
    }

    public SedeEntity findById(Long id) {
        return sedeRepository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public void deleteById(Long idSede) {
        SedeEntity sede = sedeRepository.findById(idSede).orElseThrow();
        sede.getEnvios().removeAll(sede.getEnvios());
        sedeRepository.delete(sede);
    }

}