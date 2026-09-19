# 拓扑八书单书化 Canonical 总账（2026-09-19）

## 选择原则

直接单书最高稳定稿 > 可审计跨版本并集 > umbrella 历史快照。正式题、reader task、补充题严格分账；不以文件名版本号或修改时间单独决定 canonical。

## 结果

|序号|书目|严格状态|页数|书签|SHA-256|
|---:|---|---|---:|---:|---|
|1|Allen Hatcher, *Algebraic Topology*|572/572 formal complete|1241|0|`b9a232bd4a3e0b75bb4aa52f5c982a6c3a6e404eae8b3e9ee11bcbe58a603b2d`|
|2|Edwin H. Spanier, *Algebraic Topology*|352/352 formal complete|879|3132|`4449cd9fd61d610db7fb34bd967c1de6c4aa47e390e3ba2b6312dd205915f9eb`|
|3|Greenberg–Harper, *Algebraic Topology: A First Course*|182/182 numbered formal complete|399|1513|`c34f74e0d81c96a12c194cca377107edea2b9508c961f85b9a8ea9d80b7933d7`|
|4|Bott–Tu, *Differential Forms in Algebraic Topology*|86/86 numbered formal complete；28 inline reader tasks 独立分账|265|1136|`4cfbe83297f2262f6a9d2c8567ae3cb1235011153f400325ed822a5d978c2f25`|
|5|Warner, *Foundations of Differentiable Manifolds and Lie Groups*, GTM 94|130/130 formal complete|234|1139|`711ca1941e18376ebf95f2c5c0b13050f973472c1a8b06c1270d64466c55b917`|
|6|William S. Massey, *A Basic Course in Algebraic Topology*, GTM 127|237/237 formal complete|631|3|`2a42800831d3b0209ba92d9a0a26b8a0682c8d858aaa5bf2df938463406bdea4`|
|7|Milnor–Stasheff, *Characteristic Classes*|81/81 formal complete|239|825|`9aed9eef085b8281b051034c37666c22d35a59a3086c1a5bab2b1bd2742d4c10`|
|8|廖山涛、刘旺金《同伦论基础》|64/64 formal complete|239|339|`1f043691edb01798ea7b061ebbe3b41fab6bd6a0caa2533188b8a560c258ebba`|

## 关键版本裁决

- **Spanier**：采用 v026；它从原书 p.283 恢复 J5/J6，因此严格分母为 352，而不是 v025 的 350。
- **Greenberg–Harper**：存在两个同号 v009。采用“第五轮正确率审计与显式练习补漏版”：399 页、1513 个书签；另一支为 386 页、1475 个书签。逐章比较显示，较长分支补回作者明确交给读者的任务，并保留或重写较瘦分支的审计内容。编号正式题仍为 182/182，不将 reader tasks 虚增为 formal。
- **Massey**：从六书合订本中页级拆分。保留原合订本物理页 3–521（Chapters I–X）与 567–677（Chapters XI–XV + Appendices audit），排除物理页 522–563 的 Milnor 内容及 umbrella 状态页；新增一页审计封面和顶层书签。
- **Bott–Tu**：86/86 只指 numbered Exercises；28 个 inline reader tasks 单列。

## PDF 验收

- 8 本合计 **4,127 页**、**8,087 个书签**、**9,769 个链接对象**。
- 全部 PDF 可打开、未加密，0 个无文本层页面。
- 每本均抽查封面、前部、正文中段和末页；Massey 另检查 Chapter X/Chapter XI 拼接边界。
- 完整交付 ZIP 已通过完整性测试。

## 存储与删除政策

- 8 本 PDF、QA、CSV、SHA-256 和 ZIP 已持久化到 ChatGPT Library。
- Google Drive 已创建目标目录 `02_拓扑_代数拓扑与特征类八书`；由于账号存储配额此前已拒绝新增二进制上传，本轮没有虚报 PDF 已上传。
- 本目录保存审计清单与哈希。旧版本暂不删除；必须等新 canonical 远端上传、重新打开、页数及 SHA-256 复核通过，并确认旧文件不是独立分支或唯一题源证据后，才按精确文件 ID 清理。
