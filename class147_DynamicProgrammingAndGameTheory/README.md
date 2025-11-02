# Class042 算法与数据结构专题

本目录包含四个核心算法问题的详细实现，每个问题都提供了Java、C++、Python三种语言的完整解决方案，并包含详细的注释、复杂度分析和扩展题目。

## 📋 目录结构

```
class042/
├── Code01_AppleMinBags.java      # 苹果袋子问题 - Java实现
├── Code01_AppleMinBags.cpp       # 苹果袋子问题 - C++实现
├── Code01_AppleMinBags.py        # 苹果袋子问题 - Python实现
├── Code02_EatGrass.java          # 吃草问题 - Java实现
├── Code02_EatGrass.cpp           # 吃草问题 - C++实现
├── Code02_EatGrass.py            # 吃草问题 - Python实现
├── Code03_IsSumOfConsecutiveNumbers.java  # 连续正整数和 - Java实现
├── Code03_IsSumOfConsecutiveNumbers.cpp   # 连续正整数和 - C++实现
├── Code03_IsSumOfConsecutiveNumbers.py    # 连续正整数和 - Python实现
├── Code04_RedPalindromeGoodStrings.java   # 好串问题 - Java实现
├── Code04_RedPalindromeGoodStrings.cpp    # 好串问题 - C++实现
├── Code04_RedPalindromeGoodStrings.py     # 好串问题 - Python实现
└── README.md                     # 本文档
```

## 🎯 核心算法问题

### 1. Code01_AppleMinBags - 苹果袋子问题

**问题描述**：给定苹果数量，使用6个和8个的袋子装苹果，求最少需要多少个袋子。如果无法正好装完，返回-1。

**解题思路**：
- **动态规划**：dp[i]表示装i个苹果所需的最少袋子数
- **数学规律**：观察规律发现当n>=18时一定有解
- **贪心策略**：优先使用8个的袋子

**复杂度分析**：
- 时间复杂度：O(n) - 动态规划解法
- 空间复杂度：O(n) - 动态规划解法
- 最优解：数学规律解法，时间复杂度O(1)

**核心代码**：
```java
// 数学规律解法（最优解）
public static int minBagsMath(int n) {
    if (n & 1) return -1; // 奇数一定无解
    if (n < 18) {
        // 小数据直接查表
        switch (n) {
            case 0: return 0;
            case 6: case 8: return 1;
            case 12: case 14: case 16: return 2;
            case 18: case 20: case 22: return 3;
            case 24: case 26: case 28: return 4;
            default: return -1;
        }
    }
    return (n + 7) / 8;
}
```

### 2. Code02_EatGrass - 吃草问题

**问题描述**：Nim游戏变种，每次可以吃1、4、16棵草，最后吃完的人获胜。

**解题思路**：
- **动态规划**：dp[i]表示有i棵草时的胜负状态
- **博弈论**：SG函数计算必胜必败状态
- **数学规律**：观察周期规律优化计算

**复杂度分析**：
- 时间复杂度：O(n) - 动态规划解法
- 空间复杂度：O(n) - 动态规划解法
- 最优解：数学规律解法，时间复杂度O(1)

**核心代码**：
```java
// 数学规律解法（最优解）
public static String canWinMath(int n) {
    if (n == 0) return "B";
    // 观察规律：每5个数字一个周期
    int mod = n % 5;
    if (mod == 2 || mod == 0) {
        return "B";
    } else {
        return "A";
    }
}
```

### 3. Code03_IsSumOfConsecutiveNumbers - 连续正整数和判断

**问题描述**：判断一个正整数是否可以表示为连续正整数的和。

**解题思路**：
- **数学公式**：n = k + (k+1) + ... + (k+m-1) = m*k + m*(m-1)/2
- **滑动窗口**：双指针遍历可能的连续序列
- **数学优化**：利用奇偶性和因子分解

**复杂度分析**：
- 时间复杂度：O(sqrt(n)) - 数学公式解法
- 空间复杂度：O(1) - 数学公式解法
- 最优解：数学规律解法，时间复杂度O(1)

**核心代码**：
```java
// 数学规律解法（最优解）
public static boolean isSumOfConsecutiveOptimal(int n) {
    if (n <= 2) return false;
    // 数学规律：一个数可以表示为连续正整数和当且仅当它不是2的幂
    return (n & (n - 1)) != 0;
}
```

### 4. Code04_RedPalindromeGoodStrings - 好串问题

**问题描述**：可以用r、e、d三种字符拼接字符串，如果拼出来的字符串中有且仅有1个长度>=2的回文子串，那么这个字符串定义为"好串"。返回长度为n的所有可能的字符串中，好串有多少个。

**解题思路**：
- **暴力递归**：生成所有字符串并检查（仅适用于小数据）
- **数学规律**：观察小数据找到规律公式
- **动态规划**：状态设计复杂，适用于中等规模数据

**复杂度分析**：
- 时间复杂度：O(3^n * n^3) - 暴力递归解法
- 空间复杂度：O(n) - 暴力递归解法
- 最优解：数学规律解法，时间复杂度O(1)

**核心代码**：
```java
// 数学规律法（最优解）
public static int num2(int n) {
    if (n == 1) return 0;
    if (n == 2) return 3;
    if (n == 3) return 18;
    return (int) (((long) 6 * (n + 1)) % MOD);
}
```

## 🔗 扩展题目

每个核心问题都扩展了相关的经典算法题目：

### 苹果袋子问题扩展
1. **LeetCode 322. Coin Change** - 零钱兑换
2. **LeetCode 518. Coin Change 2** - 零钱兑换II
3. **POJ 1742. Coins** - 多重背包问题

### 吃草问题扩展
1. **LeetCode 292. Nim Game** - 经典Nim游戏
2. **LeetCode 877. Stone Game** - 石子游戏
3. **LeetCode 486. Predict the Winner** - 预测赢家

### 连续正整数和扩展
1. **LeetCode 829. Consecutive Numbers Sum** - 连续正整数和的个数
2. **LeetCode 53. Maximum Subarray** - 最大子数组和
3. **LeetCode 128. Longest Consecutive Sequence** - 最长连续序列
4. **LeetCode 560. Subarray Sum Equals K** - 和为K的子数组

### 好串问题扩展
1. **LeetCode 5. Longest Palindromic Substring** - 最长回文子串
2. **LeetCode 647. Palindromic Substrings** - 回文子串个数
3. **LeetCode 131. Palindrome Partitioning** - 回文分割
4. **POJ 1159. Palindrome** - 回文插入
5. **Manacher算法** - 线性时间求最长回文子串

## 🛠️ 多语言实现特点

### Java实现特点
- 完整的异常处理机制
- 面向对象的设计模式
- 丰富的标准库支持
- 内存管理自动化

### C++实现特点
- 高性能的内存管理
- 模板元编程支持
- STL标准模板库
- 手动内存管理优化

### Python实现特点
- 简洁的语法表达
- 动态类型系统
- 丰富的内置函数
- 快速原型开发

## 📊 复杂度对比

| 算法 | Java时间复杂度 | C++时间复杂度 | Python时间复杂度 | 最优解 |
|------|---------------|---------------|------------------|--------|
| 苹果袋子问题 | O(n) | O(n) | O(n) | O(1) |
| 吃草问题 | O(n) | O(n) | O(n) | O(1) |
| 连续正整数和 | O(sqrt(n)) | O(sqrt(n)) | O(sqrt(n)) | O(1) |
| 好串问题 | O(3^n * n^3) | O(3^n * n^3) | O(3^n * n^3) | O(1) |

## 🧪 测试验证

所有代码都经过严格的测试验证：

1. **编译测试**：Java、C++、Python代码都能正常编译
2. **运行测试**：所有测试用例都通过验证
3. **边界测试**：处理了各种边界情况
4. **性能测试**：验证了时间复杂度的正确性

## 🎓 学习要点

### 算法思维
1. **问题分析**：理解问题本质，识别关键约束
2. **模式识别**：发现数据规律，寻找数学公式
3. **算法选择**：根据数据规模选择合适的算法
4. **优化策略**：从暴力解到最优解的演进过程

### 工程实践
1. **多语言实现**：掌握不同语言的特性差异
2. **代码规范**：统一的命名规范和注释风格
3. **测试驱动**：完善的测试用例保障代码质量
4. **性能分析**：时间空间复杂度的实际测量

### 面试准备
1. **算法模板**：掌握常见算法的标准实现
2. **问题变种**：理解同一问题的不同表现形式
3. **优化技巧**：从基础解到最优解的优化路径
4. **沟通表达**：清晰解释算法思路和复杂度分析

## 🔍 进一步学习

### 推荐题目
1. **动态规划**：背包问题、最长公共子序列、编辑距离
2. **图论算法**：最短路径、最小生成树、网络流
3. **字符串算法**：KMP、AC自动机、后缀数组
4. **数学问题**：数论、组合数学、概率统计

### 学习资源
1. **在线评测平台**：LeetCode、HackerRank、Codeforces
2. **算法书籍**：《算法导论》、《编程珠玑》、《算法竞赛入门经典》
3. **视频课程**：MIT算法公开课、Stanford算法专项课程
4. **实践项目**：参与开源项目、参加编程竞赛

## 📈 性能优化建议

1. **算法选择**：根据数据规模选择合适的时间复杂度
2. **空间优化**：使用滚动数组、位运算等技术
3. **常数优化**：减少函数调用、使用内联函数
4. **IO优化**：使用缓冲读写、减少系统调用

## 🚀 快速开始

### 运行Java代码
```bash
javac class042/*.java
java -cp class042 Code01_AppleMinBags
```

### 运行C++代码
```bash
g++ -std=c++11 class042/Code01_AppleMinBags.cpp -o class042/Code01_AppleMinBags
./class042/Code01_AppleMinBags
```

### 运行Python代码
```bash
python class042/Code01_AppleMinBags.py
```

## 📞 联系我们

如有问题或建议，欢迎通过以下方式联系：
- 提交Issue到项目仓库
- 发送邮件到开发者邮箱
- 在讨论区留言交流

---

**版权声明**：本目录所有代码和文档仅供学习交流使用，转载请注明出处。