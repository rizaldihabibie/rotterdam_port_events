package com.example.demo.entity;

import com.example.demo.model.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name="port_event", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false, nullable = false)
    private Long id;

    @NotNull
    private String portName;

    private String shipName;

    @NotNull
    private String shipId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull
    private Long updatedAt;


}
