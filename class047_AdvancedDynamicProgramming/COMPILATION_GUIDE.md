# Class069 编译指南

## 一、Java 编译与运行

### 编译所有Java文件
```bash
# 进入class069目录
cd class069

# 编译所有Java文件
javac *.java

# 运行特定程序
java Code01_OnesAndZeroes
java Code02_ProfitableSchemes
java Code03_KnightProbabilityInChessboard
java Code04_PathsDivisibleByK
java Code05_ScrambleString
java Code06_Coins
java Code07_KnightDialer
java Code08_UniquePaths
java TargetSum
java LastStoneWeightII
```

### 批量编译脚本（Windows）
```batch
@echo off
echo 开始编译Java文件...
javac *.java
if %errorlevel% equ 0 (
    echo Java文件编译成功！
) else (
    echo Java文件编译失败！
    pause
)
```

### 批量编译脚本（Linux/Mac）
```bash
#!/bin/bash
echo "开始编译Java文件..."
javac *.java
if [ $? -eq 0 ]; then
    echo "Java文件编译成功！"
else
    echo "Java文件编译失败！"
    exit 1
fi
```

## 二、C++ 编译与运行

### 编译单个C++文件
```bash
# 使用g++编译（推荐）
g++ -std=c++11 Code01_OnesAndZeroes.cpp -o Code01_OnesAndZeroes
./Code01_OnesAndZeroes

# 使用clang++编译
clang++ -std=c++11 Code01_OnesAndZeroes.cpp -o Code01_OnesAndZeroes
./Code01_OnesAndZeroes

# 使用MSVC编译（Windows）
cl /EHsc Code01_OnesAndZeroes.cpp
Code01_OnesAndZeroes.exe
```

### 批量编译脚本（Windows PowerShell）
```powershell
# 批量编译C++文件
Get-ChildItem -Filter "*.cpp" | ForEach-Object {
    $outputName = $_.BaseName + ".exe"
    Write-Host "正在编译: $($_.Name)"
    g++ -std=c++11 $_.Name -o $outputName
    if ($LASTEXITCODE -eq 0) {
        Write-Host "编译成功: $outputName" -ForegroundColor Green
    } else {
        Write-Host "编译失败: $($_.Name)" -ForegroundColor Red
    }
}
```

### 批量编译脚本（Linux/Mac）
```bash
#!/bin/bash
echo "开始编译C++文件..."
for file in *.cpp; do
    if [ -f "$file" ]; then
        echo "正在编译: $file"
        g++ -std=c++11 "$file" -o "${file%.cpp}"
        if [ $? -eq 0 ]; then
            echo "编译成功: ${file%.cpp}"
        else
            echo "编译失败: $file"
        fi
    fi
done
```

## 三、Python 运行

### 运行Python文件
```bash
# 直接运行
python Code01_OnesAndZeroes.py
python Code02_ProfitableSchemes.py
python Code03_KnightProbabilityInChessboard.py
python Code04_PathsDivisibleByK.py
python Code05_ScrambleString.py
python Code06_Coins.py
python Code07_KnightDialer.py
python Code08_UniquePaths.py
python TargetSum.py
python LastStoneWeightII.py
```

### 批量运行脚本（Windows）
```batch
@echo off
echo 开始运行Python程序...
for %%f in (*.py) do (
    echo 正在运行: %%f
    python %%f
    echo.
)
echo 所有程序运行完成！
pause
```

### 批量运行脚本（Linux/Mac）
```bash
#!/bin/bash
echo "开始运行Python程序..."
for file in *.py; do
    if [ -f "$file" ]; then
        echo "正在运行: $file"
        python3 "$file"
        echo
    fi
done
echo "所有程序运行完成！"
```

## 四、环境要求

### Java 环境
- **JDK版本**: 8或以上
- **编译命令**: `javac`
- **运行命令**: `java`

### C++ 环境
- **编译器**: g++ 4.8+ 或 clang++ 3.3+ 或 MSVC 2015+
- **C++标准**: C++11或以上
- **编译选项**: `-std=c++11`

### Python 环境
- **Python版本**: 3.6或以上
- **运行命令**: `python` 或 `python3`

## 五、常见编译错误与解决方案

### Java 编译错误

#### 错误1: 找不到符号
```
错误: 找不到符号
符号:   类 ArrayList
位置: 类 Solution
```
**解决方案**: 添加必要的import语句
```java
import java.util.ArrayList;
```

#### 错误2: 不兼容的类型
```
错误: 不兼容的类型: 无法将double转换为int
```
**解决方案**: 检查类型转换，使用正确的数据类型

### C++ 编译错误

#### 错误1: 缺少头文件
```
错误: 'vector'文件未找到
```
**解决方案**: 添加必要的头文件
```cpp
#include <vector>
```

#### 错误2: 使用未声明的标识符
```
错误: 使用未声明的标识符 'cout'
```
**解决方案**: 添加命名空间或包含头文件
```cpp
#include <iostream>
using namespace std;
```

#### 错误3: 模板参数错误
```
错误: 模板参数无效
```
**解决方案**: 检查模板语法，确保参数正确

### Python 运行错误

#### 错误1: 语法错误
```
SyntaxError: invalid syntax
```
**解决方案**: 检查Python语法，特别是缩进和冒号

#### 错误2: 导入错误
```
ImportError: No module named 'numpy'
```
**解决方案**: 安装缺失的模块或使用标准库替代

#### 错误3: 类型错误
```
TypeError: unsupported operand type(s)
```
**解决方案**: 检查数据类型和操作符兼容性

## 六、性能测试与优化

### Java 性能测试
```java
// 添加性能测试代码
long startTime = System.nanoTime();
// 算法代码
long endTime = System.nanoTime();
System.out.println("执行时间: " + (endTime - startTime) + " 纳秒");
```

### C++ 性能测试
```cpp
#include <chrono>
auto start = std::chrono::high_resolution_clock::now();
// 算法代码
auto end = std::chrono::high_resolution_clock::now();
auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);
std::cout << "执行时间: " << duration.count() << " 微秒" << std::endl;
```

### Python 性能测试
```python
import time
start_time = time.time()
# 算法代码
end_time = time.time()
print(f"执行时间: {end_time - start_time} 秒")
```

## 七、内存使用分析

### Java 内存分析
```bash
# 使用jconsole监控内存使用
jconsole

# 使用jstat监控垃圾回收
jstat -gc <pid>
```

### C++ 内存分析
```bash
# 使用valgrind检测内存泄漏
valgrind --leak-check=full ./program

# 使用gprof分析性能
gprof ./program gmon.out > analysis.txt
```

### Python 内存分析
```bash
# 使用memory_profiler
pip install memory_profiler
python -m memory_profiler script.py
```

## 八、跨平台兼容性

### Windows 特定配置
- 使用PowerShell或CMD运行脚本
- 确保PATH环境变量包含编译器路径
- 使用正确的文件路径分隔符（\）

### Linux/Mac 特定配置
- 使用bash或zsh运行脚本
- 确保执行权限：`chmod +x script.sh`
- 使用正确的文件路径分隔符（/）

### 通用建议
- 使用相对路径而非绝对路径
- 避免使用平台特定的系统调用
- 测试在不同平台上的兼容性

## 九、自动化构建工具

### 使用Makefile（Linux/Mac）
```makefile
CC = g++
CFLAGS = -std=c++11 -Wall
TARGETS = $(patsubst %.cpp,%,$(wildcard *.cpp))

all: $(TARGETS)

%: %.cpp
	$(CC) $(CFLAGS) -o $@ $<

clean:
	rm -f $(TARGETS)

.PHONY: all clean
```

### 使用CMake（跨平台）
```cmake
cmake_minimum_required(VERSION 3.10)
project(Class069)

set(CMAKE_CXX_STANDARD 11)

file(GLOB SOURCES "*.cpp")
foreach(source ${SOURCES})
    get_filename_component(executable ${source} NAME_WE)
    add_executable(${executable} ${source})
endforeach()
```

通过遵循本指南，您可以顺利编译和运行所有算法实现，并进行性能测试和优化。