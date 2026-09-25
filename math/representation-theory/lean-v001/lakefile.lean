import Lake
open Lake DSL

package repverify where
  -- nothing special

require mathlib from git
  "https://github.com/leanprover-community/mathlib4" @ "v4.29.1"

@[default_target]
lean_lib RepVerify where
  -- library of formally verified homework results
