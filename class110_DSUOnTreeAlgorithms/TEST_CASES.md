# DSU on Tree 算法测试用例

## 测试用例设计原则

1. **边界情况测试**：空树、单节点树、链状树、星状树
2. **一般情况测试**：随机生成的树结构
3. **性能测试**：大规模数据测试
4. **正确性验证**：暴力算法对比验证

## 测试用例列表

### 1. 基础测试用例

#### 测试用例1: 单节点树
```
输入:
n = 1
colors = [1]
edges = []
queries = [(1, 1)]

期望输出:
1
```

#### 测试用例2: 链状树
```
输入:
n = 4
colors = [1, 2, 1, 3]
edges = [(1,2), (2,3), (3,4)]
queries = [(1, 1), (2, 1), (3, 1), (4, 1)]

期望输出:
3
2
2
1
```

#### 测试用例3: 星状树
```
输入:
n = 5
colors = [1, 2, 3, 2, 1]
edges = [(1,2), (1,3), (1,4), (1,5)]
queries = [(1, 1), (2, 1), (3, 1), (4, 1), (5, 1)]

期望输出:
3
1
1
1
1
```

### 2. 进阶测试用例

#### 测试用例4: 重复颜色测试
```
输入:
n = 6
colors = [1, 1, 1, 2, 2, 3]
edges = [(1,2), (1,3), (2,4), (2,5), (3,6)]
queries = [(1, 2), (2, 1), (3, 1)]

期望输出:
1
2
1
```

#### 测试用例5: 深度相关测试
```
输入:
n = 7
colors = [1, 2, 3, 1, 2, 3, 1]
edges = [(1,2), (1,3), (2,4), (2,5), (3,6), (3,7)]
queries = [(1, 1), (1, 2), (1, 3)]

期望输出:
3
3
1
```

### 3. 性能测试用例

#### 测试用例6: 大规模随机树
```
输入:
n = 100000
colors = [随机生成100000个颜色值]
edges = [随机生成99999条边]
queries = [随机生成10000个查询]

验证方法:
与暴力算法结果对比，确保一致性
```

#### 测试用例7: 极端情况测试
```
输入:
n = 100000
colors = [所有节点颜色相同]
edges = [链状结构]
queries = [所有查询都询问根节点]

期望输出:
1 (所有查询结果都是1)
```

## 测试执行方法

### Java版本测试
```bash
# 编译Java文件
javac Code09_TreeAndQueries1.java
# 运行测试
java Code09_TreeAndQueries1 < input.txt > output.txt
```

### C++版本测试
```bash
# 编译C++文件
g++ Code09_TreeAndQueries2.cpp -o Code09_TreeAndQueries2
# 运行测试
./Code09_TreeAndQueries2 < input.txt > output.txt
```

### Python版本测试
```bash
# 运行Python文件
python Code09_TreeAndQueries1.py < input.txt > output.txt
```

## 自动化测试脚本

### 测试脚本 (test_dsu_on_tree.sh)
```bash
#!/bin/bash

echo "DSU on Tree 算法测试开始"

# 测试Java版本
echo "测试Java版本..."
javac Code09_TreeAndQueries1.java
java Code09_TreeAndQueries1 < test_input.txt > java_output.txt

# 测试C++版本
echo "测试C++版本..."
g++ Code09_TreeAndQueries2.cpp -o Code09_TreeAndQueries2
./Code09_TreeAndQueries2 < test_input.txt > cpp_output.txt

# 测试Python版本
echo "测试Python版本..."
python Code09_TreeAndQueries1.py < test_input.txt > python_output.txt

# 比较结果
echo "比较测试结果..."
if diff java_output.txt cpp_output.txt > /dev/null; then
    echo "Java和C++版本结果一致"
else
    echo "Java和C++版本结果不一致"
fi

if diff java_output.txt python_output.txt > /dev/null; then
    echo "Java和Python版本结果一致"
else
    echo "Java和Python版本结果不一致"
fi

echo "测试完成"
```

## 性能基准测试

### 测试指标
1. **时间复杂度验证**：验证O(n log n)时间复杂度
2. **空间复杂度验证**：验证O(n)空间复杂度
3. **常数因子优化**：比较不同实现的运行时间

### 基准测试脚本
```python
import time
import random

def generate_test_case(n):
    # 生成随机树测试用例
    colors = [random.randint(1, n//100) for _ in range(n)]
    edges = []
    for i in range(2, n+1):
        parent = random.randint(1, i-1)
        edges.append((parent, i))
    return colors, edges

def benchmark():
    sizes = [1000, 5000, 10000, 50000, 100000]
    for n in sizes:
        colors, edges = generate_test_case(n)
        # 执行测试并记录时间
        start_time = time.time()
        # 执行DSU on Tree算法
        end_time = time.time()
        print(f"n={n}, time={end_time-start_time:.4f}s")

if __name__ == "__main__":
    benchmark()
```

## 调试技巧

### 1. 日志输出
在关键位置添加日志输出，帮助定位问题：
```java
System.err.println("Processing node " + u + ", keep=" + keep);
System.err.println("Color count: " + Arrays.toString(colorCount));
```

### 2. 断言检查
添加断言检查，确保算法正确性：
```java
assert size[u] >= 1 : "Subtree size should be at least 1";
assert colorCount[color[u]] >= 0 : "Color count should be non-negative";
```

### 3. 内存使用监控
监控内存使用情况，避免内存泄漏：
```java
Runtime runtime = Runtime.getRuntime();
long usedMemory = runtime.totalMemory() - runtime.freeMemory();
System.err.println("Used memory: " + usedMemory / 1024 / 1024 + " MB");
```

## 常见错误及解决方案

### 1. 数组越界错误
**错误信息**：ArrayIndexOutOfBoundsException
**解决方案**：
- 检查数组大小是否足够
- 确保数组访问在有效范围内
- 添加边界检查

### 2. 栈溢出错误
**错误信息**：StackOverflowError
**解决方案**：
- 增加JVM栈大小：`java -Xss64m`
- 使用迭代替代递归
- 优化递归深度

### 3. 内存超限错误
**错误信息**：OutOfMemoryError
**解决方案**：
- 优化内存使用
- 使用更高效的数据结构
- 及时释放不需要的内存

### 4. 时间超限错误
**错误信息**：Time Limit Exceeded
**解决方案**：
- 优化算法复杂度
- 减少常数因子
- 使用更高效的操作

## 测试结果验证

### 1. 暴力算法对比
实现一个O(n²)的暴力算法，用于验证结果正确性：
```java
// 暴力算法实现
public static int bruteForce(int root, int k) {
    // 对每个节点，遍历其子树统计颜色
    // 时间复杂度O(n²)
}
```

### 2. 随机数据生成
生成随机测试数据，进行大规模测试：
```python
def generate_random_tree(n):
    """生成随机树"""
    edges = []
    for i in range(2, n+1):
        parent = random.randint(1, i-1)
        edges.append((parent, i))
    return edges
```

### 3. 结果一致性检查
确保三种语言实现的结果一致：
```bash
# 比较三个版本的输出结果
diff java_output.txt cpp_output.txt python_output.txt
```