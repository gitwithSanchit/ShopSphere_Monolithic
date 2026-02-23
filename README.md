# 🛒 ShopSphere — Transactional E-Commerce Backend

ShopSphere is a **modular monolithic e-commerce backend** built with **Java 17, Spring Boot, and PostgreSQL**, designed to handle **high-consistency transactional workflows** such as inventory management, cart checkout, order orchestration, and payments.

The system is architected with **clear domain boundaries** (User, Catalog, Inventory, Cart, Order, Payment) so it can later be decomposed into microservices with minimal refactoring.

This project focuses heavily on:

- Data integrity
- Financial accuracy
- Atomic transactions
- Production-grade backend patterns

---

## 🧠 Project Overview

ShopSphere follows a **layered modular monolith architecture**:

Each business domain is isolated at the service layer, enabling:

- Independent development per module
- Clean future migration to microservices
- Strong transactional guarantees inside the monolith

Core domains:

- User & Address
- Category & Product (Catalog)
- Inventory
- Cart
- Order & OrderItems
- Payment

The backend enforces **strict consistency** during checkout by coordinating Inventory, Orders, and Payments inside atomic transactions.

---

## ⚙️ Core Technical Features

### ✅ Atomic Transaction Management (ACID Compliance)

The checkout flow is wrapped in `@Transactional` to guarantee **all-or-nothing behavior**:

- Inventory deduction  
- Order creation  
- OrderItems persistence  
- Cart clearing  

If any step fails (for example, insufficient stock), the entire transaction rolls back automatically.

```java
@Transactional
public Order placeOrder(...) {
    validateInventory();
    createOrder();
    createOrderItems();
    clearCart();
}
