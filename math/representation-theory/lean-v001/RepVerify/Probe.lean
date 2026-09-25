/-
# Probe.lean — API 探针（不是正式定理文件）

用途：在 CI(R) 上一次性确认 mathlib v4.29.1 中六个模块所需 API 的确切名称/签名/可计算性。
本文件允许编译失败；CI 日志会逐条报告哪些 #check/example 成立。
正式定理文件将引用本探针确认过的 API。
-/
import Mathlib.GroupTheory.SpecificGroups.Dihedral
import Mathlib.GroupTheory.Perm.Centralizer
import Mathlib.LinearAlgebra.Trace
import Mathlib.LinearAlgebra.Matrix.Permutation
import Mathlib.RepresentationTheory.Basic
import Mathlib.RepresentationTheory.Character
import Mathlib.RepresentationTheory.Maschke
import Mathlib.RingTheory.SimpleModule.IsAlgClosed
import Mathlib.Algebra.MonoidAlgebra.Module

open Equiv Matrix

-- ## M4: S_n 共轭类大小
#check @Equiv.Perm.card_isConj_eq
#check @Equiv.Perm.cycleType
#check @Equiv.Perm.isConj_iff_cycleType_eq
#check @ConjAct
#check @MulAction.card_orbit_mul_card_stabilizer_eq_card_group

-- S3 数值可计算性测试（三大类：1, 3, 2）
def pc3 : Equiv.Perm (Fin 3) := Equiv.ofBijective ![1, 2, 0] (by decide)

example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (1 : Equiv.Perm (Fin 3)) h} = 1 := by
  native_decide

example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj (Equiv.swap 0 1) h} = 3 := by
  native_decide

example : Nat.card {h : Equiv.Perm (Fin 3) | IsConj pc3 h} = 2 := by
  native_decide

-- 公式本身的数值校验（模板公式：m_2=1,m_1=1 → 3）
example : (3).factorial /
    ((3 - ({2} : Multiset ℕ).sum).factorial * ({2} : Multiset ℕ).prod *
      ∏ n ∈ ({2} : Multiset ℕ).toFinset, (({2} : Multiset ℕ).count n).factorial) = 3 := by
  native_decide

-- ## M5: 置换特征标 = 不动点数
#check @Equiv.Perm.permMatrixHom
#check @Matrix.trace_permutation
#check @Matrix.toLin'
#check @LinearMap.trace_prodMap
#check @LinearMap.trace_comp_comm
#check @Submodule.prodEquivOfIsCompl
#check @LinearEquiv.ofIsCompl
#check @LinearMap.restrict
#check @LinearMap.finrank_range_add_finrank_ker
#check @Pi.single
#check @LinearMap.funLeft
#check @Finsupp.basisSingleOne

-- ## M2: Maschke 平均化投影所需的和/标量 API
#check @Equiv.Perm.sum_comp
#check @Finset.sum_equiv
#check @LinearMap.comp
#check @LinearMap.comp_apply
#check @IsUnit.mk0
#check @NeZero
#check @Fintype.sum_equiv
#check @LinearMap.Finset.sum_apply
#check @Finset.sum_apply

-- ## M3: 正则表示 / 特征标 / Wedderburn–Artin
#check @Representation.leftRegular
#check @Representation.ofMulAction
#check @Representation.character
#check @Representation.char_orthonormal
#check @FDRep.character
#check @FDRep.char_orthonormal
#check @IsSemisimpleRing.exists_algEquiv_pi_matrix_of_isAlgClosed

#synth Fintype (Equiv.Perm (Fin 3))
#synth DecidableEq (Equiv.Perm (Fin 3))

-- ## M1: 二面体群
#check @DihedralGroup.card
#check @DihedralGroup.r_mul_sr

-- 共轭公式 r^m * (sr i) * r^{-m} = sr (i - 2m) 的 simp 可行性（mathlib 中 sr i = s·r^i，n=5 测试）
example : (DihedralGroup.r (1 : ZMod 5)) * (DihedralGroup.sr (3 : ZMod 5)) *
    (DihedralGroup.r (1 : ZMod 5))⁻¹ = DihedralGroup.sr (3 - 2 : ZMod 5) := by
  simp

-- 旋转自逆测试 sr * sr = 1 与 r 旋转共轭
example : (DihedralGroup.sr (3 : ZMod 5)) * (DihedralGroup.sr (3 : ZMod 5)) =
    (1 : DihedralGroup 5) := by
  simp

example : (DihedralGroup.sr (3 : ZMod 5)) * (DihedralGroup.sr (4 : ZMod 5)) =
    DihedralGroup.r (1 : ZMod 5) := by
  simp

-- ## M6: 不可约性与子空间
#check @Submodule.span
#check @LinearMap.ker
#check @Pi.single_sub
#check @Finset.sum_sub_distrib

-- 求和泛函 sumFun v = ∑ i, v i 的核维数 = n - 1（Fin 4 上 finrank 数值测试）
#check (4 : ℕ)
#check @Module.finrank_fintype_fun_eq_card

-- 健全性对照（必须全部成功）
#check Nat.card
#check Set.ncard
#check @Set.toFinset
#check @LinearMap.ker_eq_bot
