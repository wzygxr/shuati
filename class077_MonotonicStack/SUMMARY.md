# 单调栈算法题目实现汇总

## 已实现的题目列表

### 1. Daily Temperatures (每日温度)
- **题目描述**: 给定每天温度，计算每一天到下一个更高温度需要等待的天数
- **解题思路**: 使用单调递减栈存储索引
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [DailyTemperatures.java](DailyTemperatures.java)
  - C++: [DailyTemperatures.cpp](DailyTemperatures.cpp)
  - Python: [DailyTemperatures.py](DailyTemperatures.py)

### 2. Largest Rectangle in Histogram (柱状图中最大矩形)
- **题目描述**: 给定柱状图高度，计算能勾勒出的最大矩形面积
- **解题思路**: 使用单调递增栈
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [LargestRectangleInHistogram.java](LargestRectangleInHistogram.java)
  - C++: [LargestRectangleInHistogram.cpp](LargestRectangleInHistogram.cpp)
  - Python: [LargestRectangleInHistogram.py](LargestRectangleInHistogram.py)

### 3. Trapping Rain Water (接雨水)
- **题目描述**: 给定柱状图高度，计算能接住多少雨水
- **解题思路**: 使用单调递减栈计算凹槽面积
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [TrappingRainWater.java](TrappingRainWater.java)
  - C++: [TrappingRainWater.cpp](TrappingRainWater.cpp)
  - Python: [TrappingRainWater.py](TrappingRainWater.py)

### 4. Next Greater Element I (下一个更大元素 I)
- **题目描述**: 在nums2中找到nums1每个元素的下一个更大元素
- **解题思路**: 使用单调递减栈预处理，哈希表查询
- **时间复杂度**: O(nums1.length + nums2.length)
- **空间复杂度**: O(nums2.length)
- **实现文件**:
  - Java: [NextGreaterElementI.java](NextGreaterElementI.java)
  - C++: [NextGreaterElementI.cpp](NextGreaterElementI.cpp)
  - Python: [NextGreaterElementI.py](NextGreaterElementI.py)

### 5. 456. 132 模式
- **题目描述**: 判断数组中是否存在132模式的子序列
- **解题思路**: 使用单调栈维护可能的最大中间值，从右往左遍历
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [Pattern132.java](Pattern132.java)
  - C++: [Pattern132.cpp](Pattern132.cpp)
  - Python: [Pattern132.py](Pattern132.py)

### 6. 907. Sum of Subarray Minimums (子数组的最小值之和)
- **题目描述**: 找到所有连续子数组的最小值之和
- **解题思路**: 使用单调栈找到每个元素作为最小值能覆盖的区间范围
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [SumOfSubarrayMinimums.java](SumOfSubarrayMinimums.java)
  - C++: [SumOfSubarrayMinimums.cpp](SumOfSubarrayMinimums.cpp)
  - Python: [SumOfSubarrayMinimums.py](SumOfSubarrayMinimums.py)

### 7. 503. Next Greater Element II (下一个更大元素 II)
- **题目描述**: 循环数组中的下一个更大元素
- **解题思路**: 遍历数组两次模拟循环效果
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [NextGreaterElementII.java](NextGreaterElementII.java)
  - C++: [NextGreaterElementII.cpp](NextGreaterElementII.cpp)
  - Python: [NextGreaterElementII.py](NextGreaterElementII.py)

### 8. 901. Online Stock Span (在线股票跨度)
- **题目描述**: 计算股票价格跨度
- **解题思路**: 使用单调递减栈存储价格和跨度
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [OnlineStockSpan.java](OnlineStockSpan.java)
  - C++: [OnlineStockSpan.cpp](OnlineStockSpan.cpp)
  - Python: [OnlineStockSpan.py](OnlineStockSpan.py)

### 9. 85. Maximal Rectangle (最大矩形)
- **题目描述**: 二维矩阵中最大矩形面积
- **解题思路**: 逐行构建高度数组，应用柱状图最大矩形解法
- **时间复杂度**: O(rows * cols)
- **空间复杂度**: O(cols)
- **实现文件**:
  - Java: [MaximalRectangle.java](MaximalRectangle.java)
  - C++: [MaximalRectangle.cpp](MaximalRectangle.cpp)
  - Python: [MaximalRectangle.py](MaximalRectangle.py)

### 10. 2281. 巫师的总力量和
- **题目描述**: 计算所有连续巫师组的总力量之和
- **解题思路**: 结合单调栈和前缀和技术
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [TotalStrengthOfWizards.java](TotalStrengthOfWizards.java)
  - C++: [TotalStrengthOfWizards.cpp](TotalStrengthOfWizards.cpp)
  - Python: [TotalStrengthOfWizards.py](TotalStrengthOfWizards.py)

### 11. 洛谷P5788 【模板】单调栈
- **题目描述**: 单调栈模板题，打印每个位置的右侧大于该位置数字的最近位置
- **解题思路**: 使用单调递减栈存储索引
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [LuoguP5788.java](LuoguP5788.java)
  - C++: [LuoguP5788.cpp](LuoguP5788.cpp)
  - Python: [LuoguP5788.py](LuoguP5788.py)

### 12. 洛谷P1901 发射站
- **题目描述**: 通信站信号强度计算，高的通信站可以向低的通信站发送信号
- **解题思路**: 使用单调栈分别计算每个通信站向左和向右能发送到的最近更高通信站
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [LuoguP1901.java](LuoguP1901.java)
  - C++: [LuoguP1901.cpp](LuoguP1901.cpp)
  - Python: [LuoguP1901.py](LuoguP1901.py)

### 13. 洛谷P2866 [USACO06NOV] Bad Hair Day S
- **题目描述**: 坏头发天数计算
- **解题思路**: 使用单调栈计算每个牛能看到右边多少头牛
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现文件**:
  - Java: [LuoguP2866.java](LuoguP2866.java)
  - C++: [LuoguP2866.cpp](LuoguP2866.cpp)
  - Python: [LuoguP2866.py](LuoguP2866.py)

## 测试结果验证

所有实现都已通过测试用例验证，输出结果正确：

1. **Daily Temperatures**: 输入: [73, 74, 75, 71, 69, 72, 76, 73]，输出: [1, 1, 4, 2, 1, 1, 0, 0]
2. **Largest Rectangle in Histogram**: 输入: [2, 1, 5, 6, 2, 3]，输出: 10
3. **Trapping Rain Water**: 输入: [0,1,0,2,1,0,1,3,2,1,2,1]，输出: 6
4. **Next Greater Element I**: nums1: [4, 1, 2], nums2: [1, 3, 4, 2]，输出: [-1, 3, -1]
5. **456. 132 模式**: 输入: [3, 1, 4, 2]，输出: true
6. **907. Sum of Subarray Minimums**: 输入: [3,1,2,4]，输出: 17
7. **503. Next Greater Element II**: 输入: [1,2,1]，输出: [2,-1,2]
8. **901. Online Stock Span**: 输入: [100, 80, 60, 70, 60, 75, 85]，输出: [1, 1, 1, 2, 1, 4, 6]
9. **85. Maximal Rectangle**: 输入: 4x5矩阵，输出: 6
10. **2281. 巫师的总力量和**: 输入: [1,3,1,2]，输出: 44

## 学习资源

- [详细算法解析](README.md)
- [LeetCode题目链接](https://leetcode.cn/)
- [洛谷题目链接](https://www.luogu.com.cn/)
- [USACO题目链接](https://www.usaco.org/)

## 使用说明

### 编译和运行Java代码
```bash
javac FileName.java
java FileName
```

### 编译和运行C++代码
```bash
g++ -std=c++11 FileName.cpp -o FileName
./FileName.exe
```

### 运行Python代码
```bash
python FileName.py
```

## 代码测试

所有代码都包含详细的测试用例和性能测试，确保算法的正确性和效率。每个文件都包含：
- 边界测试用例
- 功能测试用例  
- 性能测试用例
- 异常处理测试

## 工程化建议

1. **代码规范**: 遵循各语言的编码规范，使用有意义的变量名
2. **异常处理**: 添加适当的异常处理机制
3. **性能优化**: 使用合适的数据结构和算法优化
4. **测试覆盖**: 确保测试用例覆盖各种边界情况
5. **文档完善**: 提供详细的注释和使用说明

## 测试结果验证

所有实现都已通过测试用例验证，输出结果正确：

1. **Daily Temperatures**:
   - 输入: [73, 74, 75, 71, 69, 72, 76, 73]
   - 输出: [1, 1, 4, 2, 1, 1, 0, 0]

2. **Largest Rectangle in Histogram**:
   - 输入: [2, 1, 5, 6, 2, 3]
   - 输出: 10

3. **Trapping Rain Water**:
   - 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
   - 输出: 6

4. **Next Greater Element I**:
   - nums1: [4, 1, 2], nums2: [1, 3, 4, 2]
   - 输出: [-1, 3, -1]

## 学习资源

- [详细算法解析](README.md)
- [LeetCode题目链接](https://leetcode.cn/)
- [洛谷题目链接](https://www.luogu.com.cn/)

## 使用说明

### 编译和运行Java代码
```bash
javac FileName.java
java FileName
```

### 编译和运行C++代码
```bash
g++ -std=c++11 FileName.cpp -o FileName
./FileName.exe
```

### 运行Python代码
```bash
python FileName.py
```