package com.example.cafe.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NamedQuery(name = "Category.getAllCategory", query = "select c from Category c where c.id in(select p.category from Product p where p.status='true')")
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "category")
public class Category {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
