// 📁 controller/ExpenseController.java

package com.example.travel_expense.controller;

import com.example.travel_expense.dto.ExpenseForm;
import com.example.travel_expense.entity.Expense;
import com.example.travel_expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Controller : MVC의 Controller 역할 선언
 *              → 웹 요청을 받아 처리하고 뷰 이름을 반환
 */
@Controller
public class ExpenseController {

    /**
     * @Autowired : ExpenseRepository 구현체를 스프링이 자동 주입
     *             → new 없이 의존성 주입(DI) 완성
     */
    @Autowired
    private ExpenseRepository expenseRepository;


    // ──────────────────────────────────────────────────────
    //  CREATE ① - 등록 폼 (GET /expenses/new)
    // ──────────────────────────────────────────────────────
    @GetMapping("/expenses/new")
    public String newForm(Model model) {
        model.addAttribute("categories",
                new String[]{"교통", "숙박", "식비", "관광", "기타"});
        return "expenses/new";
    }

    // ──────────────────────────────────────────────────────
    //  CREATE ② - DB 저장 (POST /expenses)
    // ──────────────────────────────────────────────────────
    @PostMapping("/expenses")
    public String create(ExpenseForm form) {
        System.out.println("지출 등록: " + form);

        Expense saved = expenseRepository.save(form.toEntity());

        return "redirect:/expenses/" + saved.getId();
    }



    // ──────────────────────────────────────────────────────
    //  READ ① - 목록 조회 (GET /expenses)
    // ──────────────────────────────────────────────────────
    @GetMapping("/expenses")
    public String index(Model model) {
        List<Expense> expenseList = expenseRepository.findAll();
        model.addAttribute("expenseList", expenseList);

        // 카테고리별 통계 계산
        double total = expenseList.stream()
                .mapToDouble(Expense::getAmountInKrw).sum();

        Map<String, Double> sumByCategory = expenseList.stream()
                .collect(Collectors.groupingBy(
                    Expense::getCategory,
                    Collectors.summingDouble(Expense::getAmountInKrw)
                ));
                

        List<Map<String, Object>> categoryStats = new ArrayList<>();
        sumByCategory.forEach((cat, sum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("category", cat);
            row.put("totalKrw", Math.round(sum));
            row.put("ratio", total > 0 ? Math.round(sum * 100.0 / total) : 0);
            categoryStats.add(row);
        });

        model.addAttribute("categoryStats", categoryStats);
        return "expenses/index";
    }

    // ──────────────────────────────────────────────────────
    //  READ ② - 상세 조회 (GET /expenses/{id})
    // ──────────────────────────────────────────────────────
    @GetMapping("/expenses/{id}")
    public String show(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElse(null);

        long krwRounded = Math.round(expense.getAmountInKrw());

        model.addAttribute("expense", expense);
        model.addAttribute("krwRounded", krwRounded);
        return "expenses/show";
    }


    // ──────────────────────────────────────────────────────
    //  UPDATE ① - 수정 폼 (GET /expenses/{id}/edit)
    // ──────────────────────────────────────────────────────
    @GetMapping("/expenses/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        model.addAttribute("expense", expense);
        model.addAttribute("categories",
                new String[]{"교통", "숙박", "식비", "관광", "기타"});
        return "expenses/edit";
    }

    // ──────────────────────────────────────────────────────
    //  UPDATE ② - DB 갱신 (POST /expenses/{id}/update)
    // ──────────────────────────────────────────────────────
    @PostMapping("/expenses/{id}/update")
    public String update(@PathVariable Long id, ExpenseForm form) {
        Expense formEntity = form.toEntity();
        Optional<Expense> target = expenseRepository.findById(id);

        if (target.isPresent()) {
            Expense existing = target.get();
            existing.patch(formEntity);
            expenseRepository.save(existing);
        }

        return "redirect:/expenses/" + id;
    }


    // ──────────────────────────────────────────────────────
    //  DELETE - 삭제 + 리다이렉트 (GET /expenses/{id}/delete)
    // ──────────────────────────────────────────────────────
    @GetMapping("/expenses/{id}/delete")
    public String delete(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return "redirect:/expenses";
    }

    // 환율 조회 엔드포인트
    @GetMapping("/api/exchange-rate")
    @ResponseBody
    public double getExchangeRate(@RequestParam String currency) {
        if (currency.equals("KRW")) return 1.0;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.frankfurter.app/latest?from="
                    + currency + "&to=KRW";
            Map response = restTemplate.getForObject(url, Map.class);
            Map rates = (Map) response.get("rates");
            return ((Number) rates.get("KRW")).doubleValue();
        } catch (Exception e) {
            // 실패 시 기본값
            return switch (currency) {
                case "USD" -> 1508.0;
                case "JPY" -> 9.4;
                case "EUR" -> 1680.0;
                default -> 1.0;
            };
        }
    }
}