// 📁 entity/Expense.java

package com.example.travel_expense.entity;

import jakarta.persistence.*;

/**
 * @Entity : 이 클래스를 JPA 엔티티로 선언
 *           → 스프링이 'expense' 테이블을 자동 생성
 * @Table  : 매핑할 DB 테이블 이름 지정
 */
@Entity
@Table(name = "expense")
public class Expense {

    /**
     * @Id             : PK(기본키) 필드 선언
     * @GeneratedValue : AUTO_INCREMENT — DB가 id 자동 부여
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column : 컬럼 속성 지정
     *   nullable = false → NOT NULL 제약조건
     *   length = 100     → VARCHAR(100)
     */
    @Column(nullable = false, length = 100)
    private String title;       // 지출 항목명

    @Column(nullable = false)
    private double amount;         // 금액 (원)

    @Column(length = 10)
    private String currency;    // 통화 종류 (KRW/USD/JPY/EUR)

    @Column
    private double exchangeRate;    // 환율 (예: 1USD = 1350.0원)

    @Column
    private double amountInKrw;    // 원화 환산 금액 (amount * exchangeRate 자동 계산)

    @Column(length = 20)
    private String category;    // 교통/숙박/식비/관광/기타

    @Column(length = 100)
    private String country;     // 여행 국가

    @Column(length = 20)
    private String expenseDate; // 지출 날짜

    @Column(columnDefinition = "TEXT")
    private String memo;        // 메모

    // JPA 필수 기본 생성자
    protected Expense() {}

    // 폼 데이터 → 엔티티 변환용 생성자
    public Expense(Long id, String title, double amount, String currency,
                   double exchangeRate, double amountInKrw, String category,
                   String country, String expenseDate, String memo) {
        this.id          = id;
        this.title       = title;
        this.amount      = amount;
        this.currency      = currency;
        this.exchangeRate  = exchangeRate;
        this.amountInKrw   = amountInKrw;
        this.category    = category;
        this.country     = country;
        this.expenseDate = expenseDate;
        this.memo        = memo;
    }

    // Getter
    public Long   getId()          { return id; }
    public String getTitle()       { return title; }
    public double getAmount()      { return amount; }
    public String getCurrency()      { return currency; }
    public double getExchangeRate()  { return exchangeRate; }
    public double getAmountInKrw()   { return amountInKrw; }
    public String getCategory()    { return category; }
    public String getCountry()     { return country; }
    public String getExpenseDate() { return expenseDate; }
    public String getMemo()        { return memo; }

    // 화면 표시용: 원화 환산 금액을 반올림한 정수(long)로 반환
    // double을 그대로 출력하면 큰 숫자에서 1.5E8 같은 지수 표기가 나오기 때문에 사용
    public long getKrwRounded() {
        return Math.round(amountInKrw);
    }

    // Update 시 변경된 필드만 덮어쓰는 메서드
    public void patch(Expense target) {
        if (target.title       != null) this.title       = target.title;
        if (target.amount      != 0)    this.amount      = target.amount;
        if (target.currency     != null) this.currency     = target.currency;
        if (target.exchangeRate != 0)    this.exchangeRate = target.exchangeRate;
        if (target.amountInKrw  != 0)    this.amountInKrw  = target.amountInKrw;
        if (target.category    != null) this.category    = target.category;
        if (target.country     != null) this.country     = target.country;
        if (target.expenseDate != null) this.expenseDate = target.expenseDate;
        if (target.memo        != null) this.memo        = target.memo;
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", title=" + title +
                ", amount=" + amount +",currency=" + currency +
        ", amountInKrw=" + amountInKrw + "}";
    }
}