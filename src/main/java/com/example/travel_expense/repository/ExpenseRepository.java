// 📁 repository/ExpenseRepository.java

package com.example.travel_expense.repository;

import com.example.travel_expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository<엔티티 타입, PK 타입> 상속
 * → findAll(), findById(), save(), deleteById() 등
 *   기본 CRUD 메서드를 자동으로 제공
 * → 인터페이스 선언만으로 스프링이 구현체 자동 생성
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}