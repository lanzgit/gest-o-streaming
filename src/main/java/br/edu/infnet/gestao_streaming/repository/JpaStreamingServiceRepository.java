package br.edu.infnet.gestao_streaming.repository;

import br.edu.infnet.gestao_streaming.model.StreamingService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("!test")
interface JpaStreamingServiceRepository extends JpaRepository<StreamingService, Long> {}
