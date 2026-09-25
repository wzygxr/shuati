/-
# Probe.lean — API 探针第 2 轮

目标：确认 S3 数值计算路线、ZMod 算术收尾、若干线性映射引理的确切名称。
本文件允许编译失败；CI 日志逐条报告成败。正式定理文件只用探针确认过的 API。
-/
import Mathlib.GroupTheory.SpecificGroups.Dihedral
import Mathlib.GroupTheory.Perm.Centralizer
import Mathlib.GroupTheory.Perm.Cycle.Concrete
import Mathlib.LinearAlgebra.Trace
import Mathlib.LinearAlgebra.Matrix.Permutation
import Mathlib.RepresentationTheory.Basic
import Mathlib.RepresentationTheory.Character
import Mathlib.RepresentationTheory.Maschke
import Mathlib.RingTheory.SimpleModule.IsAlgClosed
import Mathlib.Algebra.MonoidAlgebra.Module

open Equiv Matrix BigOperators

-- ## 轮次1遗留：ZMod 算术收尾三选
example : (3 : ZMod 5) - 1 + -1 = 3 - 2 := by decide
example : (3 : ZMod 5) - 1 + -1 = 3 - 2 := by omega
example : (3 : ZMod 5) - 1 + -1 = 3 - 2 := by norm_num

-- Dihedral simp+decide 组合是否能闭合共轭公式
example : (DihedralGroup.r (1 : ZMod 5)) * (DihedralGroup.sr (3 : ZMod 5)) *
    (DihedralGroup.r (1 : ZMod 5))⁻¹ = DihedralGroup.sr (3 - 2 : ZMod 5) := by
  simp <;> decide
example : (DihedralGroup.sr (3 : ZMod 5)) * (DihedralGroup.sr (4 : ZMod 5)) =
    DihedralGroup.r (1 : ZMod 5) := by
  simp <;> decide

-- ## S3 数值路线 A：cycleType 可计算性
example : (Equiv.swap (0 : Fin 3) (1 : Fin 3)).cycleType = {2} := by native_decide
example : (Equiv.swap (0 : Fin 3) (1 : Fin 3)).cycleType = {2} := by decide
example : (c[(0 : Fin 3), 1, 2]).cycleType = {3} := by native_decide
example : Fintype.card (Equiv.Perm (Fin 3)) = 6 := by native_decide

-- ## S3 数值路线 B：Fintype.card 子类型的 decide/native_decide
example : Fintype.card {h : Equiv.Perm (Fin 3) // IsConj (Equiv.swap 0 1) h} = 3 := by
  native_decide
example : Fintype.card {h : Equiv.Perm (Fin 3) // IsConj (1 : Equiv.Perm (Fin 3)) h} = 1 := by
  native_decide
example : Fintype.card {h : Equiv.Perm (Fin 3) // IsConj (c[(0 : Fin 3), 1, 2]) h} = 2 := by
  native_decide

-- ## S3 数值路线 C：mathlib 公式 rw 后纯算术
example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (Equiv.swap 0 1) h} = 3 := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  native_decide
example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (c[(0 : Fin 3), 1, 2]) h} = 2 := by
  rw [Equiv.Perm.card_isConj_eq, Fintype.card_fin]
  native_decide

-- ## 名称确认
#check @Matrix.permMatrixHom
#check @Fin.castPred
#check @Fin.sum_univ_castSucc
#check @Equiv.Perm.isSwap_iff_cycleType
#check @Finset.mul_prod_erase
#check @Nat.cast_smul_eq_nsmul
#check @Equiv.sum_comp
#check @Equiv.mulLeft
#check @Pi.basisFun
#check @LinearMap.trace_eq_matrix_trace
#check @codisjoint_iff
#check @LinearMap.funLeft
#check @Finsupp.basisSingleOne
#check @Representation

-- LinearMap 和的取值律：候选写法逐一测试
example {G V : Type*} [Group G] [Fintype G] [AddCommGroup V] [Module ℂ V]
    (F : G → (V →ₗ[ℂ] V)) (v : V) :
    (∑ g : G, F g) v = ∑ g : G, (F g) v := by
  exact Finset.sum_apply _ _ _
example {G V : Type*} [Group G] [Fintype G] [AddCommGroup V] [Module ℂ V]
    (F : G → (V →ₗ[ℂ] V)) (v : V) :
    (∑ g : G, F g) v = ∑ g : G, (F g) v := by
  simp only [LinearMap.coeFn_sum, sum_apply]

-- Units 可判定性/有限性（IsConj 计算链条）
#synth Fintype (Equiv.perm (Fin 3)) -- 故意写错：应报 unknown
#synth Fintype (Equiv.Perm (Fin 3))
#synth DecidableEq (Equiv.Perm (Fin 3))
