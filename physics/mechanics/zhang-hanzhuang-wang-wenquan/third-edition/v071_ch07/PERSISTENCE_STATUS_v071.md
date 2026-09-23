# v071 云端与 GitHub 持久化状态

日期：2026-09-23

## GitHub

仓库：`wzygxr/shuati`  
分支：`master`  
版本目录：

```text
physics/mechanics/zhang-hanzhuang-wang-wenquan/third-edition/v071_ch07/
```

已实际写入并重新读取确认：

- `README.md`
- `PROGRESS_v071.json`
- `CH8_SOURCE_INVENTORY_v071.csv`
- `力学习题全解_累计接续索引与剩余总账_v071.md`
- `公式复算与构建验收报告_v071.md`

本轮相关写入提交：

```text
6c1e58536bf06823b63131e9b668cb664830b722  README 初次写入
f6ea7e38596887bcb5aa6cbb870eda40efe13543  进度与 QA 状态
29ee410bbd1915365b29f368348eb0a40f642b41  第八章题位冻结表
7f788f6db15f8c9f623c38e825de1a9ef8f9d306  最新剩余总账
2d40380bbe82c28b533dd70a9c7783d17f71c8d2  QA 报告源码
c037682b712e21a9eb990f1c4a013f73dabfe66d  README 持久化范围说明
```

GitHub Contents API 的当前写入接口适合 UTF-8 文本，不把本地 12 MB PDF、完整 ZIP 或未实际提交的累计 Markdown 冒充为已经上传。完整 Markdown、PDF、源图、脚本和日志都保存在本轮 ZIP 中。

GitHub 目录链接：

```text
https://github.com/wzygxr/shuati/tree/master/physics/mechanics/zhang-hanzhuang-wang-wenquan/third-edition/v071_ch07
```

## Google Drive

项目根目录：

```text
/Google Drive/力学习题全解_持续更新
```

本轮成功创建了空版本目录：

```text
v071_20260923_张汉壮第七章43题全闭合
```

Drive 文件夹 ID：

```text
1YpLJqRQPZN8pVQ0UOiuwWV30p2VluJaZ
```

随后上传 `README_v071.md` 时，Google Drive 返回：

```text
forbidden: 此位置不可写
```

此前同一项目的大文件写入曾明确返回 `storageQuotaExceeded`。本轮没有删除用户旧文件，也没有把空目录冒充为已上传版本。因此当前可靠落点是：

1. 本对话的完整 Markdown、PDF 和 ZIP；
2. GitHub 中的版本索引、进度、剩余账和 QA 证据；
3. Google Drive 中已经创建但尚为空的 v071 目录。
