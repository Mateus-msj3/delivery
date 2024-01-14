package com.io.github.msj.delivery.domain.entities;

import com.io.github.msj.delivery.domain.entities.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

}
