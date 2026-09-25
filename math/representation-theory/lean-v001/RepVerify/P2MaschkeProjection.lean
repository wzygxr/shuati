/-
# P2 — Maschke 平均化投影（形式化验证）

对应作业第 2 题。设 `G` 为有限群，`ρ : G → V →ₗ[ℂ] V` 为复表示，`W` 为 `G`-不变
子空间，`π : V → W` 为线性投影（像含于 `W` 且在 `W` 上为恒等），定义

  P = (1/|G|) ∑_{g ∈ G} ρ g ∘ π ∘ ρ g⁻¹

则：
(a) `P(V) = W`、`P|_W = id`、`P² = P`；
(b) `P` 与所有 `ρ h` 交换；
(c) `ker P` 是 `G`-不变的且 `V = W ⊕ ker P`（`IsCompl W (ker P)`）。

这给出 Maschke 定理的证明：`W` 有 `G`-不变补空间。
-/
import Mathlib.Data.Complex.Basic
import Mathlib.RepresentationTheory.Basic
import Mathlib.Algebra.Module.NatInt
import Mathlib.Algebra.BigOperators.Group.Finset.Basic

open Finset

namespace RepVerify

variable {G : Type*} [Group G] [Fintype G]
variable {V : Type*} [AddCommGroup V] [Module ℂ V]
variable (ρ : Representation ℂ G V) (π : V →ₗ[ℂ] V) (W : Submodule ℂ V)

/-- 线性映射的有限和逐点取值律（自证，避免依赖具体 simp 名目）。 -/
theorem linmap_sum_apply {ι : Type*} [DecidableEq ι] (s : Finset ι)
    (F : ι → (V →ₗ[ℂ] V)) (v : V) :
    (∑ i ∈ s, F i) v = ∑ i ∈ s, F i v := by
  induction s using Finset.induction with
  | empty => simp
  | insert a s ha ih =>
    rw [Finset.sum_insert ha, Finset.sum_insert ha, LinearMap.add_apply, ih]

/-- 平均化投影 `P = (1/|G|) ∑ g, ρ g ∘ₗ π ∘ₗ ρ g⁻¹`。 -/
def avgProj : V →ₗ[ℂ] V :=
  (Fintype.card G : ℂ)⁻¹ • ∑ g : G, ρ g ∘ₗ π ∘ₗ ρ g⁻¹

private theorem cardG_ne_zero : (Fintype.card G : ℂ) ≠ 0 :=
  Nat.cast_ne_zero.mpr Fintype.card_ne_zero

/-- (a-1) `P` 固定 `W` 中每个向量：`P|_W = id_W`。 -/
theorem avgProj_apply_of_mem
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w)
    {w : V} (hw : w ∈ W) :
    avgProj ρ π w = w := by
  classical
  have hstep : ∀ g : G, ρ g (π (ρ g⁻¹ w)) = w := by
    intro g
    rw [hπf _ (hW g⁻¹ w hw)]
    show (ρ g * ρ g⁻¹) w = w
    rw [← map_mul, mul_inv_cancel, map_one]
    rfl
  have hsum : (∑ g : G, ρ g ∘ₗ π ∘ₗ ρ g⁻¹) w = (Fintype.card G : ℂ) • w := by
    rw [linmap_sum_apply (V := V) Finset.univ
        (fun g => ρ g ∘ₗ π ∘ₗ ρ g⁻¹) w]
    simp only [LinearMap.comp_apply]
    rw [Finset.sum_congr rfl (fun g _ => hstep g), Finset.sum_const,
      Finset.card_univ, ← Nat.cast_smul_eq_nsmul]
  show ((Fintype.card G : ℂ)⁻¹ • _) w = w
  rw [LinearMap.smul_apply, hsum, smul_smul, inv_mul_cancel₀ (cardG_ne_zero (G := G)),
    one_smul]

/-- (a-2) `P` 的像含于 `W`。 -/
theorem avgProj_apply_mem
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπr : ∀ v : V, π v ∈ W)
    (v : V) : avgProj ρ π v ∈ W := by
  classical
  show ((Fintype.card G : ℂ)⁻¹ • _) v ∈ W
  rw [LinearMap.smul_apply]
  apply Submodule.smul_mem
  rw [linmap_sum_apply (V := V) Finset.univ
      (fun g => ρ g ∘ₗ π ∘ₗ ρ g⁻¹) v]
  apply Submodule.sum_mem
  intro g _
  show ρ g (π (ρ g⁻¹ v)) ∈ W
  exact hW g _ (hπr (ρ g⁻¹ v))

/-- (a-3) `P(V) = W`。 -/
theorem avgProj_range
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) :
    LinearMap.range (avgProj ρ π) = W := by
  apply le_antisymm
  · rintro v ⟨u, rfl⟩
    exact avgProj_apply_mem ρ π W hW hπr u
  · intro w hw
    exact ⟨w, avgProj_apply_of_mem ρ π W hW hπf hw⟩

/-- (a-4) 幂等性 `P² = P`。 -/
theorem avgProj_comp_self
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) :
    avgProj ρ π ∘ₗ avgProj ρ π = avgProj ρ π := by
  ext v
  rw [LinearMap.comp_apply,
    avgProj_apply_of_mem ρ π W hW hπf (avgProj_apply_mem ρ π W hW hπr v)]

/-- (b-1) 共轭不变性：`ρ h ∘ P ∘ ρ h⁻¹ = P`（对每个 `h ∈ G`）。 -/
theorem avgProj_conj
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) (h : G) :
    (ρ h ∘ₗ avgProj ρ π) ∘ₗ ρ h⁻¹ = avgProj ρ π := by
  ext v
  simp only [LinearMap.comp_apply, avgProj, LinearMap.smul_apply, LinearMap.coe_sum,
    map_smul, map_sum, Finset.sum_apply, LinearMap.comp_apply]
  congr 1
  have step : ∀ g : G,
      ρ h (ρ g (π (ρ g⁻¹ (ρ h⁻¹ v)))) = ρ (h * g) (π (ρ (h * g)⁻¹ v)) := by
    intro g
    have e1 : ρ h (ρ g (π (ρ g⁻¹ (ρ h⁻¹ v)))) = ρ (h * g) (π (ρ g⁻¹ (ρ h⁻¹ v))) := by
      show (ρ h * ρ g) _ = ρ (h * g) _
      rw [map_mul]
    rw [e1]
    congr 2
    have e2 : ρ g⁻¹ (ρ h⁻¹ v) = ρ (g⁻¹ * h⁻¹) v := by
      show (ρ g⁻¹ * ρ h⁻¹) v = ρ (g⁻¹ * h⁻¹) v
      rw [map_mul]
    rw [e2, ← mul_inv_rev]
  calc ∑ g : G, ρ h (ρ g (π (ρ g⁻¹ (ρ h⁻¹ v))))
      = ∑ g : G, ρ (h * g) (π (ρ (h * g)⁻¹ v)) :=
        Finset.sum_congr rfl fun g _ => step g
    _ = ∑ g : G, ρ g (π (ρ g⁻¹ v)) :=
        Equiv.sum_comp (Equiv.mulLeft h) _

/-- (b-2) `P` 与所有 `ρ h` 交换。 -/
theorem avgProj_comm
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) (h : G) :
    ρ h ∘ₗ avgProj ρ π = avgProj ρ π ∘ₗ ρ h := by
  have key := avgProj_conj ρ π W hW hπf hπr h
  have hid : ρ h⁻¹ ∘ₗ ρ h = (1 : V →ₗ[ℂ] V) := by
    ext v
    rw [LinearMap.comp_apply, LinearMap.one_apply]
    calc ρ h⁻¹ (ρ h v) = ρ (h⁻¹ * h) v := by rw [map_mul]
      _ = ρ 1 v := by rw [inv_mul_cancel]
      _ = v := by rw [map_one]; rfl
  calc ρ h ∘ₗ avgProj ρ π
      = ((ρ h ∘ₗ avgProj ρ π) ∘ₗ ρ h⁻¹) ∘ₗ ρ h := by
        rw [LinearMap.comp_assoc, hid, LinearMap.comp_one]
    _ = avgProj ρ π ∘ₗ ρ h := by rw [key]

/-- (c-1) `ker P` 是 `G`-不变的。 -/
theorem avgProj_ker_invariant
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) (h : G)
    {v : V} (hv : v ∈ LinearMap.ker (avgProj ρ π)) :
    ρ h v ∈ LinearMap.ker (avgProj ρ π) := by
  rw [LinearMap.mem_ker] at hv ⊢
  rw [← LinearMap.comp_apply, ← avgProj_comm ρ π W hW hπf hπr h, LinearMap.comp_apply, hv,
    map_zero]

/-- (c-2) `V = W ⊕ ker P`（直和互补）。 -/
theorem avgProj_isCompl
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) :
    IsCompl W (LinearMap.ker (avgProj ρ π)) := by
  constructor
  · rw [Submodule.disjoint_def]
    intro x hxW hxK
    rw [LinearMap.mem_ker] at hxK
    have hx := avgProj_apply_of_mem ρ π W hW hπf hxW
    rw [hxK] at hx
    exact hx.symm
  · rw [codisjoint_iff, eq_top_iff]
    intro v _
    have hdec : v = avgProj ρ π v + (v - avgProj ρ π v) := (add_sub_cancel _ v).symm
    have hker : (v - avgProj ρ π v) ∈ LinearMap.ker (avgProj ρ π) := by
      rw [LinearMap.mem_ker, map_sub,
        avgProj_apply_of_mem ρ π W hW hπf (avgProj_apply_mem ρ π W hW hπr v), sub_self]
    rw [hdec]
    exact Submodule.add_mem_sup (avgProj_apply_mem ρ π W hW hπr v) hker

/-- 打包陈述：第 2 题全部结论。 -/
theorem maschke_projection_package
    (hW : ∀ (g : G) (w : V), w ∈ W → ρ g w ∈ W)
    (hπf : ∀ w ∈ W, π w = w) (hπr : ∀ v : V, π v ∈ W) :
    (∀ w ∈ W, avgProj ρ π w = w)
      ∧ (∀ v : V, avgProj ρ π v ∈ W)
      ∧ LinearMap.range (avgProj ρ π) = W
      ∧ avgProj ρ π ∘ₗ avgProj ρ π = avgProj ρ π
      ∧ (∀ h : G, ρ h ∘ₗ avgProj ρ π = avgProj ρ π ∘ₗ ρ h)
      ∧ (∀ h : G, ∀ v ∈ LinearMap.ker (avgProj ρ π),
          ρ h v ∈ LinearMap.ker (avgProj ρ π))
      ∧ IsCompl W (LinearMap.ker (avgProj ρ π)) :=
  ⟨fun w hw => avgProj_apply_of_mem ρ π W hW hπf hw,
   fun v => avgProj_apply_mem ρ π W hW hπr v,
   avgProj_range ρ π W hW hπf hπr,
   avgProj_comp_self ρ π W hW hπf hπr,
   fun h => avgProj_comm ρ π W hW hπf hπr h,
   fun h v hv => avgProj_ker_invariant ρ π W hW hπf hπr h hv,
   avgProj_isCompl ρ π W hW hπf hπr⟩

end RepVerify
