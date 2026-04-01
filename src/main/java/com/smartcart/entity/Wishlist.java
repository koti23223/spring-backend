//package com.smartcart.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "wishlist")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class Wishlist {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long wishlistId;
//
//	@ManyToOne
//	@JoinColumn(name = "user_id", nullable = false)
//	private User user;
//
//	@ManyToOne
//	@JoinColumn(name = "product_id", nullable = false)
//	private Product product;
//}

package com.smartcart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wishlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String email;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}