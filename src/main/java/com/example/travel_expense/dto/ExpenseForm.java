// 📁 dto/ExpenseForm.java

package com.example.travel_expense.dto;

import com.example.travel_expense.entity.Expense;

/**
 * HTML 폼에서 넘어오는 데이터를 받는 DTO 클래스
 * Entity와 분리해서 입력/변환을 안전하게 처리
 */
public class ExpenseForm {

    private Long   id;
    private String title;
    private int    amount;
    private String currency;
    private double exchangeRate;
    private int amountInKrw;
    private String category;
    private String country;
    private String expenseDate;
    private String memo;

    // DTO → Entity 변환
    // ✅ 수정 — toEntity() 에 환율 관련 필드 추가
    //          amountInKrw는 amount * exchangeRate 로 자동 계산
    public Expense toEntity() {
        // KRW면 환율 1.0으로 그대로, 외화면 환율 적용해서 원화 계산
        int krw = currency.equals("KRW")
                ? amount
                : (int)(amount * exchangeRate);

        return new Expense(id, title, amount, currency,
                exchangeRate, krw, category,
                country, expenseDate, memo);
    }
    // Getter / Setter (폼 바인딩 필수)
    public Long   getId()          { return id; }
    public String getTitle()       { return title; }
    public int    getAmount()      { return amount; }
    public String getCurrency()      { return currency; }
    public double getExchangeRate()  { return exchangeRate; }
    public int    getAmountInKrw()   { return amountInKrw; }
    public String getCategory()    { return category; }
    public String getCountry()     { return country; }
    public String getExpenseDate() { return expenseDate; }
    public String getMemo()        { return memo; }

    public void setId(Long id)                   { this.id = id; }
    public void setTitle(String title)           { this.title = title; }
    public void setAmount(int amount)            { this.amount = amount; }
    public void setCurrency(String currency)       { this.currency = currency; }
    public void setExchangeRate(double exchangeRate){ this.exchangeRate = exchangeRate; }
    public void setAmountInKrw(int amountInKrw)    { this.amountInKrw = amountInKrw; }
    public void setCategory(String category)     { this.category = category; }
    public void setCountry(String country)       { this.country = country; }
    public void setExpenseDate(String expenseDate){ this.expenseDate = expenseDate; }
    public void setMemo(String memo)             { this.memo = memo; }

    @Override
    public String toString() {
        return "ExpenseForm{title=" + title + ", amount=" + amount +
                ", currency=" + currency + ", amountInKrw=" + amountInKrw + "}";
    }
}