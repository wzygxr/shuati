# 已恢复旧成果 MANIFEST

日期：2026-09-18

本清单用于把旧会话 / Library 中已经找到的随机过程五书成果重新登记到 GitHub。文件“被登记”不等于二进制字节已全部镜像；实际 GitHub 状态另列。

## 已定位成果

| 文件 | 类型 | 已知大小 | GitHub镜像状态 |
|---|---|---:|---|
| Durrett第三版_第1章全习题详解_77题与231道变式_合订本.pdf | PDF | 2,353,588 B | 待二进制迁移；内容/书目已登记 |
| 随机过程五书_第002批_Durrett_1.8-1.25_详细解答与54道变式.pdf | PDF | 585,974 B | 待二进制迁移；五书书目已抽取进 ledger |
| 随机过程五书_第003批_Durrett_1.26-1.57_详细解答与96道变式.md | Markdown | 97,880 B（Library） | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第003批_Durrett_1.26-1.57_详细解答与96道变式.tex | TeX | 122,370 B | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第004批_Durrett_1.56-1.77_详解与87道变式.pdf | PDF | 634,995 B | 待二进制迁移 |
| 随机过程五书_第004批_Durrett_1.56-1.77_详解与87道变式.tex | TeX | 114,177 B | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第005批_Durrett_第2章_加速推进_阶段稿.md | Markdown | 2,794 B | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第005批_Durrett_第2章_2.1-2.16_详细解答与48道变式.md | Markdown | 7,875 B | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第005批_Durrett_2.13-2.61_复核扩写与147道变式.pdf | PDF | 742,149 B | 待二进制迁移 |
| 随机过程五书_第005批_Durrett第2章_逐题详解与183道变式_版本核对稿.md | Markdown | 109,495 B | **已镜像** → `legacy-results/durrett/` |
| 随机过程五书_第005批_Durrett第2章_逐题详解与183道变式_版本核对稿.tex | TeX | 131,986 B | **已镜像** → `legacy-results/durrett/`；补迁 commit `037a5ef14b8b8e9b6436b3b3ae3ca92a930b978a` |
| 随机过程五书_第006批_Durrett第3章_24道原题与72道变式.md | Markdown | 77,513 B（Library） | **已镜像** → `legacy-results/durrett/`；补迁 commit `459c3d09f60357e5474ac7f142e2c6b6a3478838` |
| 随机过程五书_第006批_Durrett第3章_24道原题与72道变式.pdf | PDF | 608,020 B | 待二进制迁移 |

另发现一个同标题/近同标题的第006批 PDF（608,653 B），需哈希比较后决定是否为重复构建，不直接覆盖。

## 本轮已建立的审计文件

- `../README.md`
- `../ledger/historical-five-book-roster.md`
- `../book-079/audit-2026-09-18.md`
- `../source-audits/random-process-603p.md`

这些文件已经把“历史五书清单”“79号书目迁移碰撞”“603页源包排除/候选证据”固定到 GitHub。

## 源包

`随机过程习题集-c47c528555ab.pdf`

- 603页
- 177,500,710 B
- 属于综合网页拼接源包
- GitHub 普通单文件上限不适合直接把该 177.5 MB 文件作为普通 blob 放入仓库；
- 本项目在 GitHub 中保存它的审计、分段信息、哈希/大小、来源定位和必要派生结果。

## 迁移优先级

1. TeX / Markdown / Python / JSON / 日志；
2. 可通过当前 GitHub 写入通道可靠传输的小型二进制；
3. ZIP / 大 PDF 按接口能力迁移；
4. 重复构建先做 SHA256 去重；
5. 任何旧成果迁移后，在本表更新真实状态，不写“假上传”。

## 后续新成果

从本清单建立之后：

- 新的 Markdown / TeX / Python / JSON / CSV / log 等文本成果，完成批次前直接写入本 GitHub 项目目录；
- 新的审计结论同时写进对应 `book-xxx/` 或 `source-audits/`；
- PDF / ZIP 若当前通道无法可靠传输，则先登记哈希与持久化来源，并明确标记“未镜像”，不把登记冒充上传。
