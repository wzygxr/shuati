/-
# Probe.lean — API 探针第 3 轮（回归控制 + M5/M6 预查）

本文件允许编译失败；CI 日志逐条报告成败。正式定理文件只用探针确认过的 API。
-/
import Mathlib.GroupTheory.SpecificGroups.Dihedral
import Mathlib.GroupTheory.Perm.Centralizer
import Mathlib.GroupTheory.Perm.Cycle.Concrete
import Mathlib.LinearAlgebra.Trace
import Mathlib.LinearAlgebra.Matrix.Permutation
import Mathlib.Data.Complex.Basic
import Mathlib.RepresentationTheory.Basic
import Mathlib.RepresentationTheory.Character
import Mathlib.RepresentationTheory.Maschke
import Mathlib.RingTheory.SimpleModule.IsAlgClosed
import Mathlib.Algebra.MonoidAlgebra.Module

open Equiv Matrix BigOperators

-- ## 回归控制（第 2 轮已全部通过）
example : (Equiv.swap (0 : Fin 3) (1 : Fin 3)).cycleType = {2} := by native_decide
example : (c[(0 : Fin 3), 1, 2]).cycleType = {3} := by native_decide
example : Fintype.card (Equiv.Perm (Fin 3)) = 6 := by native_decide
example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (Equiv.swap 0 1) h} = 3 := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  native_decide
example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (1 : Equiv.Perm (Fin 3)) h} = 1 := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  native_decide
example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (c[(0 : Fin 3), 1, 2]) h} = 2 := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  native_decide
example : Fintype.card {h : Equiv.Perm (Fin 3) // IsConj (Equiv.swap 0 1) h} = 3 := by
  native_decide

-- 二面体共轭公式（第 2 轮通过）
example : (DihedralGroup.r (1 : ZMod 5)) * (DihedralGroup.sr (3 : ZMod 5)) *
    (DihedralGroup.r (1 : ZMod 5))⁻¹ = DihedralGroup.sr (3 - 2 : ZMod 5) := by
  simp
  decide

-- ## 新探针 1：线性映射求和取值（归纳法助手，M2 的 linmap_sum_apply 原型）
example {G V : Type*} [Group G] [Fintype G] [DecidableEq G] [AddCommGroup V] [Module ℂ V]
    (F : G → (V →ₗ[ℂ] V)) (v : V) :
    (∑ g : G, F g) v = ∑ g : G, (F g) v := by
  induction (Finset.univ : Finset G) using Finset.induction with
  | empty => simp
  | insert a s ha ih =>
    rw [Finset.sum_insert ha, Finset.sum_insert ha, LinearMap.add_apply, ih]

-- ## 新探针 2：funLeft 对 Pi.single 的作用（三种 simp 集候选）
example (σ : Equiv.Perm (Fin 3)) (a : Fin 3) (x : ℂ) :
    (LinearMap.funLeft ℂ ℂ ⇑σ⁻¹) (Pi.single a x) = Pi.single (σ a) x := by
  ext i
  simp [LinearMap.funLeft, Function.comp_apply, Pi.single, Equiv.eq_symm_apply]

example (σ : Equiv.Perm (Fin 3)) (a : Fin 3) (x : ℂ) :
    (LinearMap.funLeft ℂ ℂ ⇑σ⁻¹) (Pi.single a x) = Pi.single (σ a) x := by
  ext i
  simp [LinearMap.funLeft, Pi.single, Function.update, Equiv.eq_symm_apply]

-- ## 新探针 3：basisFun 与 toMatrix 协同（矩阵元取值）
example (σ : Equiv.Perm (Fin 3)) (i j : Fin 3) :
    (LinearMap.toMatrix (Pi.basisFun ℂ (Fin 3)) (Pi.basisFun ℂ (Fin 3))
      (LinearMap.funLeft ℂ ℂ ⇑σ⁻¹)) i j
      = if i = σ j then 1 else 0 := by
  simp [LinearMap.toMatrix_apply, Pi.basisFun, LinearMap.funLeft, Pi.single,
    Function.comp_apply, Function.update, Equiv.eq_symm_apply]

-- ## 新探针 4：杂项名称确认
#check (permMatrixHom (R := ℂ) (n := Fin 3) :
  Equiv.Perm (Fin 3) →* Matrix (Fin 3) (Fin 3) ℂ)
example : (0).factorial = 1 := rfl
#check @Nat.factorial_zero
#check @Finset.prod_eq_single_of_mem
#check @LinearMap.proj
#check @Module.finrank_fintype_fun_eq_card
#check @LinearEquiv.finrank_eq
#check @Representation.char_orthonormal
