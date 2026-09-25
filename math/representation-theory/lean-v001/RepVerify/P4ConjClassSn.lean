/-
# P4 — `Sₙ` 中共轭类的大小（循环型公式）

对应作业第 4 题。设 `σ ∈ Sₙ` 的循环型为 `(m₁, …, mₙ)`（`mⱼ` 为 `j`-轮换个数，
**包括**不动点，即 `Σ j·mⱼ = n`），则

  |Cl(σ)| = n! / ∏_{j=1}^{n} j^{mⱼ} · (mⱼ)!

mathlib 已收 `Equiv.Perm.card_isConj_eq`（以 `cycleType` 表述，`cycleType` 只记录
长度 ≥ 2 的轮换），本文件把我们的 `mⱼ` 版本作为它的推论证出，两条路线（轨道–
稳定子与模板填数）已由 `card_isConj_eq` 在 mathlib 中覆盖。

另外给出 S₃ 的数值验证（由探针确认计算路径后再启用）。
-/
import Mathlib.GroupTheory.Perm.Centralizer
import Mathlib.Order.Interval.Finset.Basic
import Mathlib.Algebra.BigOperators.Group.Finset.Basic

open Finset BigOperators

namespace RepVerify

/-- `mⱼ`：`j`-轮换的个数（`j ≥ 2` 即 `cycleType` 中的重数；`j = 1` 为不动点个数
`n - cycleType.sum`）。 -/
def cycleCount {n : ℕ} (σ : Equiv.Perm (Fin n)) (j : ℕ) : ℕ :=
  if j = 1 then n - σ.cycleType.sum else σ.cycleType.count j

/-- 辅助引理：多重集的乘积写成 `toFinset` 上的"底数^重数"积。
（mathlib 未收录此精确形式，自行归纳证明。） -/
theorem multiset_prod_eq_toFinset_pow (m : Multiset ℕ) :
    m.prod = ∏ n ∈ m.toFinset, n ^ m.count n := by
  induction m using Multiset.induction with
  | empty => simp
  | cons a m ih =>
    rw [Multiset.prod_cons, ih]
    by_cases ha : a ∈ m
    · -- `a` 已出现：`toFinset` 不变，`a` 的重数 +1
      rw [Multiset.toFinset_cons,
        Finset.insert_eq_of_mem (Multiset.mem_toFinset.mpr ha)]
      have hc_a : (a ::ₘ m).count a = m.count a + 1 := Multiset.count_cons_self a m
      have step : ∀ n ∈ m.toFinset,
          n ^ (a ::ₘ m).count n = n ^ m.count n * (if n = a then n else 1) := by
        intro n hn
        by_cases hna : n = a
        · subst hna
          rw [hc_a, if_pos rfl, pow_succ]
        · rw [Multiset.count_cons_of_ne hna m, if_neg hna, mul_one]
      rw [Finset.prod_congr rfl step, Finset.prod_mul_distrib,
        Finset.prod_eq_single_of_mem a (Multiset.mem_toFinset.mpr ha)
          (fun n _ hn => if_neg hn),
        if_pos rfl, mul_comm]
    · -- `a` 未出现：`toFinset` 增加 `a`，其重数为 1
      rw [Multiset.toFinset_cons,
        Finset.prod_insert (by simpa using ha)]
      have hc_a : (a ::ₘ m).count a = 1 := by
        rw [Multiset.count_cons_self, Multiset.count_eq_zero.mpr ha]
      rw [hc_a, pow_one]
      apply congrArg (a * ·)
      apply Finset.prod_congr rfl
      intro n hn
      have hna : n ≠ a := by
        intro h
        subst h
        exact ha (Multiset.mem_toFinset.mp hn)
      rw [Multiset.count_cons_of_ne hna m]

/-- `j ≤ cycleType.sum`（`j` 出现在循环型中时）。 -/
private theorem le_cycleType_sum {n : ℕ} {σ : Equiv.Perm (Fin n)} {j : ℕ}
    (hj : j ∈ σ.cycleType) : j ≤ σ.cycleType.sum := by
  rw [← Multiset.cons_erase hj, Multiset.sum_cons]
  exact Nat.le_add_right j _

/-- **第 4 题**：`Sₙ` 共轭类大小的循环型公式（`n ≥ 1`）。 -/
theorem conj_class_size_formula {n : ℕ} (hn : 1 ≤ n) (σ : Equiv.Perm (Fin n)) :
    Nat.card {τ : Equiv.Perm (Fin n) | IsConj σ τ} =
      n.factorial / ∏ j ∈ Finset.Icc 1 n, j ^ cycleCount σ j * (cycleCount σ j).factorial := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  -- 辅助量：分母的三因子形式与 `Icc` 单因子形式的换算
  have hcyc : ∀ j ∈ Finset.Ioc 1 n, cycleCount σ j = σ.cycleType.count j := by
    intro j hj
    have h1 : 1 < j := (Finset.mem_Ioc.mp hj).1
    have hj1 : j ≠ 1 := by omega
    simp only [cycleCount, hj1, if_false]
  have hsub : σ.cycleType.toFinset ⊆ Finset.Ioc 1 n := by
    intro j hj
    rw [Finset.mem_Ioc]
    refine ⟨Equiv.Perm.two_le_of_mem_cycleType (Multiset.mem_toFinset.mp hj), ?_⟩
    exact le_trans (le_cycleType_sum (Multiset.mem_toFinset.mp hj))
      (by simpa using Equiv.Perm.sum_cycleType_le σ)
  have hvan : ∀ j ∈ Finset.Ioc 1 n, j ∉ σ.cycleType.toFinset →
      σ.cycleType.count j = 0 := by
    intro j _ hj
    exact Multiset.count_eq_zero.mpr (by simpa using hj)
  have t1 : ∏ j ∈ Finset.Ioc 1 n, j ^ σ.cycleType.count j = σ.cycleType.prod := by
    rw [multiset_prod_eq_toFinset_pow]
    apply Eq.symm
    apply Finset.prod_subset hsub
    intro j hj hnin
    rw [hvan j hj hnin, pow_zero]
  have t2 : ∏ j ∈ Finset.Ioc 1 n, (σ.cycleType.count j).factorial
      = ∏ j ∈ σ.cycleType.toFinset, (σ.cycleType.count j).factorial := by
    apply Eq.symm
    apply Finset.prod_subset hsub
    intro j hj hnin
    rw [hvan j hj hnin]
    rfl
  have hden : ∏ j ∈ Finset.Icc 1 n, j ^ cycleCount σ j * (cycleCount σ j).factorial
      = (n - σ.cycleType.sum).factorial * σ.cycleType.prod *
        ∏ j ∈ σ.cycleType.toFinset, (σ.cycleType.count j).factorial := by
    have h1in : (1 : ℕ) ∈ Finset.Icc 1 n := by simp [hn]
    rw [← Finset.mul_prod_erase _ _ h1in, Finset.Icc_erase_left]
    have hcyc1 : cycleCount σ 1 = n - σ.cycleType.sum := by simp [cycleCount]
    rw [hcyc1, one_pow, one_mul]
    have hcycProd : ∏ j ∈ Finset.Ioc 1 n, j ^ cycleCount σ j * (cycleCount σ j).factorial
        = ∏ j ∈ Finset.Ioc 1 n,
            j ^ σ.cycleType.count j * (σ.cycleType.count j).factorial :=
      Finset.prod_congr rfl fun j hj => by rw [hcyc j hj]
    rw [hcycProd, Finset.prod_mul_distrib, t1, t2, mul_assoc]
  rw [hden]

end RepVerify
