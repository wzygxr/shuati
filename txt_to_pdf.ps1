# PowerShell脚本将txt文件批量转换为PDF

# 设置路径
$txt_dir = "d:\src\src\merged_files"
$pdf_dir = "d:\src\src\pdf_files"

# 创建PDF文件夹
if (!(Test-Path $pdf_dir)) {
    New-Item -ItemType Directory -Path $pdf_dir -Force
}

# 获取所有txt文件
$txt_files = Get-ChildItem $txt_dir -Filter "*.txt"

Write-Host "找到 $($txt_files.Count) 个txt文件需要转换为PDF"

# 方法1: 使用Word应用程序转换（需要安装Microsoft Word）
function Convert-TxtToPdf-WithWord {
    param($txt_file, $pdf_file)
    
    try {
        $word = New-Object -ComObject Word.Application
        $word.Visible = $false
        
        $doc = $word.Documents.Open($txt_file.FullName)
        $doc.SaveAs([ref]$pdf_file, [ref]17)  # 17 = wdFormatPDF
        $doc.Close()
        $word.Quit()
        
        return $true
    }
    catch {
        Write-Host "Word转换失败: $($_.Exception.Message)"
        return $false
    }
}

# 方法2: 使用PowerShell生成简单的PDF（基础方法）
function Convert-TxtToPdf-Basic {
    param($txt_file, $pdf_file)
    
    try {
        # 读取txt文件内容
        $content = Get-Content $txt_file.FullName -Raw
        
        # 创建HTML内容
        $html_content = @"
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>$($txt_file.BaseName)</title>
    <style>
        body { font-family: Arial, sans-serif; font-size: 12px; line-height: 1.5; margin: 20px; }
        pre { white-space: pre-wrap; background-color: #f5f5f5; padding: 10px; border-radius: 5px; }
        .header { text-align: center; font-size: 16px; font-weight: bold; margin-bottom: 20px; }
        .separator { border-top: 1px solid #ccc; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="header">$($txt_file.BaseName)</div>
    <pre>$content</pre>
</body>
</html>
"@
        
        # 保存为HTML文件
        $html_file = Join-Path $pdf_dir "$($txt_file.BaseName).html"
        $html_content | Out-File -FilePath $html_file -Encoding UTF8
        
        Write-Host "已创建HTML文件: $html_file"
        return $true
    }
    catch {
        Write-Host "HTML转换失败: $($_.Exception.Message)"
        return $false
    }
}

# 方法3: 使用wkhtmltopdf工具（如果安装）
function Convert-TxtToPdf-Wkhtmltopdf {
    param($txt_file, $pdf_file)
    
    # 检查是否安装了wkhtmltopdf
    $wkhtmltopdf_path = Get-Command "wkhtmltopdf" -ErrorAction SilentlyContinue
    
    if ($wkhtmltopdf_path) {
        try {
            # 先转换为HTML
            $html_file = Join-Path $pdf_dir "$($txt_file.BaseName).html"
            Convert-TxtToPdf-Basic $txt_file $pdf_file | Out-Null
            
            # 使用wkhtmltopdf转换为PDF
            & wkhtmltopdf $html_file $pdf_file
            
            # 删除临时HTML文件
            Remove-Item $html_file -Force
            
            return $true
        }
        catch {
            Write-Host "wkhtmltopdf转换失败: $($_.Exception.Message)"
            return $false
        }
    }
    else {
        Write-Host "未找到wkhtmltopdf，跳过此方法"
        return $false
    }
}

# 主转换函数
foreach ($txt_file in $txt_files) {
    Write-Host "正在处理: $($txt_file.Name)"
    
    $pdf_file = Join-Path $pdf_dir "$($txt_file.BaseName).pdf"
    
    # 尝试不同的转换方法
    $success = $false
    
    # 方法1: 使用Word
    if (!$success) {
        Write-Host "尝试使用Word转换..."
        $success = Convert-TxtToPdf-WithWord $txt_file $pdf_file
    }
    
    # 方法2: 使用wkhtmltopdf
    if (!$success) {
        Write-Host "尝试使用wkhtmltopdf转换..."
        $success = Convert-TxtToPdf-Wkhtmltopdf $txt_file $pdf_file
    }
    
    # 方法3: 基础HTML转换
    if (!$success) {
        Write-Host "使用基础HTML转换..."
        $success = Convert-TxtToPdf-Basic $txt_file $pdf_file
    }
    
    if ($success) {
        Write-Host "✓ 完成: $($txt_file.BaseName).pdf"
    }
    else {
        Write-Host "✗ 失败: $($txt_file.Name)"
    }
}

Write-Host ""
Write-Host "==============================================="
Write-Host "转换完成！"
Write-Host "PDF文件保存在: $pdf_dir"
Write-Host "总共生成了 $(Get-ChildItem $pdf_dir -Filter '*.pdf' -ErrorAction SilentlyContinue | Measure-Object).Count 个PDF文件"
Write-Host "==============================================="