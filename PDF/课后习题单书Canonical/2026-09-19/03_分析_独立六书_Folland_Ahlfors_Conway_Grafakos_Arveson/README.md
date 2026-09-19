# 分析六书：单书 Canonical（2026-09-19）

本目录记录 Folland、Ahlfors、Conway、Grafakos 两卷与 Arveson 六本教材的最高稳定母本裁决、合订本拆卷、书签重建与 PDF 验收。

## 现行六本单书

|序号|书目|严格状态|页数|SHA-256|
|---:|---|---|---:|---|
|1|Folland, *Real Analysis*, 2e|495/495 formal complete；1485 variants；1980 units|894|`d98130c2e6f2982874ef59b5a8ec601a07843f1dc0a5daab7533fc7b83520a09`|
|2|Ahlfors, *Complex Analysis*, 3e|284/284 formal complete|451|`6f2ef1013dfb6b92001d8fcc68015401f767b5413fc3216383c2d6e173ee6d7d`|
|3|Conway, *Functions of One Complex Variable I*, 2e|453/453 independent exercise blocks complete；旧 452 口径已按原书题块纠正|558|`86e1ea837c55638d51aa64ed5a18da50fcbc796896aac2e421e0cc89c912471b`|
|4|Grafakos, *Classical Fourier Analysis*, 3e|351/351 formal complete；928 variants；1279 units|927|`80b50891e7c8e47fa1417c990b7b2507af54d3df36c77d2ce39cd9018af45443`|
|5|Grafakos, *Modern Fourier Analysis*, 3e|204 formal；Ch5 30/31；Ch6 closed；Ch7 §7.1 6/9、§7.2 6/6；仍在推进|618|`e32a3bffabf2644b9c98223fa6712e17494ca0baa1f0013ba2491d3411764f86`|
|6|Arveson, *A Short Course on Spectral Theory*|169/169 formal complete；507 variants；676 units|435|`4cdce3455a55fc7ba537b5699a81f03e566d5bfc078a559887049dbb40ad2a2b`|

## 关键裁决

- Folland、Ahlfors、Conway、Arveson 采用直接单书最高稳定 PDF，不从旧 umbrella 总账回退。
- Grafakos v36 合订母本第 926 页明确结束 GTM249：351/351、剩余 0；第 927 页开始 GTM250 §1.1。因此拆为：
  - Classical：母本 p.1 + p.2–926；
  - Modern：母本 p.1 + p.927–1542。
- Grafakos 原母本没有 PDF 书签；拆卷后按可识别章节与批次重建 45 条、40 条导航。
- Arveson p.434 上半部仍保留 169/169 终局条目，下半部开始其他谱理论书；为保留证据整页留在 Arveson，p.435 排除。

## QA

- 六本合计 3,883 页，全部 A4、未加密，0 个无文本层页面。
- 8,612 条书签目的页和 178 个链接对象均有效。
- PDFium 全页解码 3,883/3,883，0 failures。
- PDFium 150 dpi 人工抽查 44 个代表页，封面、目录/起始页、中段、终局总账、Grafakos 926/927 分界及末页均未见裁切、黑块、叠页或中文缺字。
- `pdf_preflight.py` 六本均 Openable=True、Likely scanned=False、XFA=False，未出现 warning；ZIP CRC 全量通过。

## 存储与清理

- PDF、QA、CSV、机器检查、SHA-256 与 ZIP 已持久化到 ChatGPT Library：`/课后习题单书Canonical_2026-09-19/03_分析_独立六书_Folland_Ahlfors_Conway_Grafakos_Arveson/`。
- Google Drive 已创建同名目录，folder ID `11WAkJVckX-nnAZlc1YEkHS8WSz2ejxXQ`；账号存储配额仍已满，故没有虚报 PDF 上传成功。
- 本批没有删除任何旧版本。只有远端新文件可打开、页数与 SHA-256 复核一致，并确认旧文件不是唯一题源证据或独立分支后，才按精确文件 ID 清理。
