package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "trip_expense_details")
public class TripExpenseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "trip_id")
    private TripDetail tripDetail;
    @OneToOne
    @JoinColumn(name = "expense_id")
    private Expenses expense;
    private BigDecimal expenseAmount;
}
