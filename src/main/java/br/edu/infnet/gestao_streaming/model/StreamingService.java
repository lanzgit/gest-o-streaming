package br.edu.infnet.gestao_streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "streaming_services")
public class StreamingService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(nullable = false, length = 80)
	private String category;

	protected StreamingService() {
	}

	public StreamingService(Long id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String category() {
		return category;
	}
}
