@echo off
cd /d "e:\代码\class187"
echo. > all_content.txt
for %%f in (*.md *.java) do (
  if /i not "%%f"=="all_content.txt" (
    if /i not "%%f"=="merged_file.txt" (
      if /i not "%%f"=="merged_all.txt" (
        if /i not "%%f"=="class186+这届的数据结构预算法.txt" (
          if /i not "%%f"=="MergeFiles.java" (
            if /i not "%%f"=="MergeFiles.ps1" (
              if /i not "%%f"=="CombineFiles.java" (
                echo # File: %%f >> all_content.txt
                echo. >> all_content.txt
                type "%%f" >> all_content.txt
                echo. >> all_content.txt
                echo ================================================================================ >> all_content.txt
                echo. >> all_content.txt
              )
            )
          )
        )
      )
    )
  )
)
echo Files merged successfully
