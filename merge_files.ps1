# PowerShell脚本批量合并文件
$target_dir = "d:\src\src\merged_files"

# 创建目标文件夹
if (!(Test-Path $target_dir)) {
    New-Item -ItemType Directory -Path $target_dir -Force
}

# 获取所有class文件夹
$class_folders = Get-ChildItem "d:\src\src" -Directory | Where-Object {$_.Name -like "class*"}

Write-Host "找到 $($class_folders.Count) 个class文件夹需要处理"

foreach ($folder in $class_folders) {
    Write-Host "正在处理: $($folder.Name)"
    
    $output_file = Join-Path $target_dir "$($folder.Name).txt"
    
    # 创建输出文件并添加标题
    "===============================================" | Out-File -FilePath $output_file -Encoding UTF8
    "文件夹: $($folder.Name)" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    "" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    
    # 合并所有.md文件
    "[Markdown 文件]" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    
    $md_files = Get-ChildItem $folder.FullName -Recurse -File | Where-Object {$_.Extension -eq ".md"}
    foreach ($file in $md_files) {
        "文件: $($file.Name)" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        Get-Content $file.FullName -Encoding UTF8 | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    }
    
    # 合并所有代码文件
    "[代码文件]" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    
    $code_extensions = @(".java", ".cpp", ".py", ".c", ".js", ".ts", ".html", ".css", ".php", ".rb", ".go", ".rs", ".swift", ".kt", ".scala", ".cs")
    $code_files = Get-ChildItem $folder.FullName -Recurse -File | Where-Object {$code_extensions -contains $_.Extension}
    
    foreach ($file in $code_files) {
        "文件: $($file.Name)" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        Get-Content $file.FullName -Encoding UTF8 | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "===============================================" | Out-File -FilePath $output_file -Encoding UTF8 -Append
        "" | Out-File -FilePath $output_file -Encoding UTF8 -Append
    }
    
    Write-Host "完成: $($folder.Name)"
}

Write-Host ""
Write-Host "==============================================="
Write-Host "所有文件夹处理完成！"
Write-Host "合并后的文件保存在: $target_dir"
Write-Host "总共生成了 $(Get-ChildItem $target_dir -File).Count 个文件"
Write-Host "==============================================="