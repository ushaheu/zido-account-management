package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "expenses")
public class Expenses {

    @Id
    private String expenseType;
    private String expenseDescription;
}
