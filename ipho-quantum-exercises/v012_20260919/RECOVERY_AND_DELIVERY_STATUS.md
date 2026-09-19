# v011 恢复交付与持久化状态

## 恢复交付

上一轮完成的程稼夫、崔宏滨第12—14章正式排版页已经恢复：

- 67/67 张第011批排版页；
- 第011批独立 PDF：67 页；
- 累计 PDF：776 页；
- 累计范围：489 个书内题位 + 1467 道变式 = 1956 个训练单元。

恢复页来自已经完成的正式页面渲染，不重新 OCR 改写公式。代价是本批恢复页在 PDF 中是页面图像，文本检索能力弱于原生文本型 PDF。恢复 Markdown 以相对路径嵌入全部页面资产，没有把图像恢复误称为纯文本原稿。

完整包中的两份 Markdown 已改用 `../页面资产/` 相对路径，解压后可直接解析页面图片。

## 完整交付包

```text
9822e9b580cb41bc5e78cdf98c6a136268864e5d047d0a9299650b89e7beb626
IPhO_原子近代量子物理_全习题详解_v011恢复与v012初审_完整包_20260919.zip
```

ZIP 已执行完整性测试，结果：`No errors detected in compressed data`。

## Google Drive

- 已创建目录：`v011_习题详解_20260919`
- folder id：`1KrApDp26SBZup0mSmENkhNug-oJhVVbr`
- 文件上传：失败
- 原因：`403 storageQuotaExceeded`

因此没有声称 Drive 文件已经写入。

## GitHub

- 仓库：`wzygxr/shuati`
- v011 分支：`ipho-quantum-v011-20260919`
- v012 审计分支：`ipho-quantum-v012-audit-20260919`

GitHub 当前保存检查点、进度账本、恢复状态和 strict-union 初审；大体积 PDF/ZIP 仍通过 ChatGPT 文件交付。
