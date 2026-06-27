package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.domain.model.StreamingService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamingServiceRepository extends JpaRepository<StreamingService, Long> {}
