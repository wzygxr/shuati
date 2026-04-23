# 设置输出文件路径
$outputPath = "e:\代码\class187\class186+这届的数据结构预算法.txt"

# 清空或创建输出文件
"" | Out-File -FilePath $outputPath -Encoding UTF8

# 获取目录中的所有.md和.java文件
$files = Get-ChildItem -Path "e:\代码\class187" -Include "*.md","*.java" | Where-Object { $_.Name -ne "class186+这届的数据结构预算法.txt" }

# 遍历每个文件并将其内容追加到输出文件
foreach ($file in $files) {
    # 添加文件名标识
    "# File: $($file.Name)" | Out-File -FilePath $outputPath -Append -Encoding UTF8
    "" | Out-File -FilePath $outputPath -Append -Encoding UTF8  # 空行
    
    # 添加文件内容
    Get-Content $file.FullName -Encoding UTF8 | Out-File -FilePath $outputPath -Append -Encoding UTF8
    
    # 添加分隔符
    "" | Out-File -FilePath $outputPath -Append -Encoding UTF8  # 空行
    ("=" * 80) | Out-File -FilePath $outputPath -Append -Encoding UTF8
    "" | Out-File -FilePath $outputPath -Append -Encoding UTF8  # 空行
}

Write-Host "File merging completed. Output file: $outputPath"