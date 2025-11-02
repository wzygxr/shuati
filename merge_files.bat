@echo off
setlocal enabledelayedexpansion

REM 创建目标文件夹
set "target_dir=d:\src\src\merged_files"
if not exist "%target_dir%" mkdir "%target_dir%"

REM 遍历所有class文件夹
for /d %%i in ("d:\src\src\class*") do (
    echo 正在处理: %%i
    
    REM 获取文件夹名称
    for %%f in ("%%i") do set "folder_name=%%~nxf"
    
    REM 创建输出文件
    set "output_file=%target_dir%\!folder_name!.txt"
    
    REM 清空或创建输出文件
    type nul > "!output_file!"
    
    REM 添加文件夹标题
    echo =============================================== >> "!output_file!"
    echo 文件夹: !folder_name! >> "!output_file!"
    echo =============================================== >> "!output_file!"
    echo. >> "!output_file!"
    
    REM 合并所有.md文件
    echo [Markdown 文件] >> "!output_file!"
    echo =============================================== >> "!output_file!"
    for /r "%%i" %%f in (*.md) do (
        echo 文件: %%~nxf >> "!output_file!"
        echo =============================================== >> "!output_file!"
        type "%%f" >> "!output_file!"
        echo. >> "!output_file!"
        echo =============================================== >> "!output_file!"
        echo. >> "!output_file!"
    )
    
    REM 合并所有代码文件
    echo [代码文件] >> "!output_file!"
    echo =============================================== >> "!output_file!"
    for /r "%%i" %%f in (*.java,*.cpp,*.py,*.c,*.js,*.ts,*.html,*.css,*.php,*.rb,*.go,*.rs,*.swift,*.kt,*.scala,*.cs) do (
        echo 文件: %%~nxf >> "!output_file!"
        echo =============================================== >> "!output_file!"
        type "%%f" >> "!output_file!"
        echo. >> "!output_file!"
        echo =============================================== >> "!output_file!"
        echo. >> "!output_file!"
    )
    
    echo 完成: !folder_name!
)

echo.
echo ===============================================
echo 所有文件夹处理完成！
echo 合并后的文件保存在: %target_dir%
echo ===============================================

pause