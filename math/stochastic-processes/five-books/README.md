# 随机过程五书长期项目

本目录用于长期保存并恢复“随机过程五书”历史项目的 **canonical 证据、书目、版本审计、旧批成果索引和后续新增成果**。

## 固定项目书目（历史项目原始五书口径）

| 代号 | 教材 | 指定版本 |
|---|---|---|
| R | Sheldon M. Ross, *Introduction to Probability Models* | 12th ed., 2019, ISBN 9780128143476 |
| D | Richard Durrett, *Essentials of Stochastic Processes* | 3rd ed., Springer, 2016, ISBN 9783319456133 |
| H | 胡迪鹤《应用随机过程引论》 | 哈尔滨工业大学出版社，1984 |
| L | 林元烈《应用随机过程》 | 清华大学出版社，2002，ISBN 9787302059585 |
| Z | 张波、商豪、邓军《应用随机过程》 | 第6版，2023，ISBN 9787300321066 |

> 重要：L 与 Z 在历史“五书”项目中是 **两本不同教材**，不能合并成一个模糊的《应用随机过程》。

## 当前 79 号状态

统一总账在后续迁移时把第 79 项压缩成了：

- `《应用随机过程》（历史项目书目）`
- 状态：`BIBLIO_REOPEN`
- 动作：恢复作者/版次与旧母版

本轮恢复历史五书资料后发现：林元烈 2002 与张波/商豪/邓军 2023 原本同时存在，所以当前第 79 项存在 **书目迁移碰撞（migration collision）**。在找到更早的“编号 79 对应关系”证据之前，不把 79 强行判给其中任何一本。

详见：

- `book-079/audit-2026-09-18.md`
- `ledger/historical-five-book-roster.md`
- `source-audits/random-process-603p.md`
- `legacy-results/MANIFEST.md`

## GitHub 交付规则

从 2026-09-18 起，本项目每一轮完成时：

1. 书目/题源/排除证据写入 GitHub；
2. 新增 Markdown / TeX / Python / JSON / CSV / 日志等源文件写入 GitHub；
3. PDF / ZIP 等二进制成果在接口和 GitHub 单文件限制允许时同步；
4. 无法直接镜像的大文件必须在 manifest 中保留文件名、大小、哈希（若可得）、Library/Drive 来源和未镜像原因；
5. 任何“已上传”结论必须以 GitHub 重新读取核验为准。

本目录是此后随机过程五书线的长期接续点。
