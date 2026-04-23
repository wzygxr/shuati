# Google Kick Start 2020 Round A - Allocation

## 题目信息
- **平台**: Google Kick Start
- **年份**: 2020
- **轮次**: Round A
- **题目**: Allocation (分配)
- **难度**: 入门
- **类型**: 贪心算法/排序

## 题目链接
- https://codingcompetitions.withgoogle.com/kickstart/round/000000000019ffc7

## 题目描述
有一个公司有N栋房子要出售，每栋房子有一个价格。公司希望尽可能多地出售房子，但总价格不能超过预算B。

请问：公司最多能买多少栋房子？

## 输入格式
第一行包含一个整数T，表示测试用例数量。

每个测试用例的第一行包含两个整数N和B，分别表示房子数量和预算。

第二行包含N个整数，表示每栋房子的价格。

## 输出格式
对于每个测试用例，输出一行，格式为"Case #x: y"，其中x是测试用例编号（从1开始），y是最多能买的房子数量。

## 样例输入
```
3
4 100
20 90 40 90
4 50
30 30 10 10
3 300
999 999 999
```

## 样例输出
```
Case #1: 2
Case #2: 3
Case #3: 0
```

## 笔试/面试考察点分析

### 核心考察点
1. **贪心算法**：选择最便宜的房子
2. **排序算法**：对价格排序后从小到大选择
3. **基础编程能力**：输入输出格式处理
4. **边界条件**：预算不足时的处理

### 为什么放在强连通分量专题？
本题是Kick Start的入门题目，与强连通分量无直接关系。但作为**国际竞赛平台**的代表，展示了：
- 国际竞赛的输入输出格式
- 多测试用例处理模式
- 基础算法思维

Kick Start中的图论题目通常出现在Round B/C/D，难度更高。

### Kick Start题型分布
- **Round A**：基础题，测试编程能力
- **Round B/C/D**：中等难度，涉及图论、DP等
- **Round E/F/G**：难题，涉及复杂图论、高级数据结构

## 解题思路

### 贪心策略
1. 将所有房子按价格从小到大排序
2. 从便宜的房子开始购买，直到预算用完
3. 统计能购买的房子数量

### 为什么贪心正确？
要最大化房子数量，必须优先选择价格低的房子。这是一个经典的贪心问题。

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 解决单个测试用例
 * @param n 房子数量
 * @param b 预算
 * @param prices 价格数组
 * @return 最多能买的房子数量
 * Kick Start要点：注意输出格式"Case #x: y"
 * ML关联：贪心思想类似于某些启发式优化算法
 */
int solve(int n, int b, vector<int>& prices) {
    // 步骤1：对价格进行升序排序，贪心选择便宜的房子
    // sort默认升序，确保从小到大购买
    sort(prices.begin(), prices.end());
    
    int count = 0; // 能购买的房子数量计数器
    int remaining = b; // 剩余预算，初始为总预算b
    
    // 步骤2：遍历排序后的价格，从便宜的开始购买
    for (int price : prices) {
        // 检查剩余预算是否足够购买当前房子
        if (remaining >= price) {
            // 预算足够，购买该房子
            remaining -= price; // 扣除房价，更新剩余预算
            count++; // 购买数量加1
        } else {
            // 预算不足，无法购买当前及后续房子（已排序）
            break; // 跳出循环，结束购买
        }
    }
    
    return count; // 返回最多能购买的房子数量
}

int main() {
    ios::sync_with_stdio(false); // 关闭iostream同步，加速IO
    cin.tie(nullptr); // 解除cin和cout绑定
    
    int T; // 测试用例数量
    cin >> T; // 读取测试用例数
    
    // 遍历每个测试用例
    for (int caseNum = 1; caseNum <= T; caseNum++) {
        int n, b; // n:房子数量 b:预算
        cin >> n >> b; // 读取n和b
        
        vector<int> prices(n); // 存储n个房子的价格
        // 读取n个房子的价格
        for (int i = 0; i < n; i++) {
            cin >> prices[i]; // 读取第i个房子的价格
        }
        
        // 解决问题并获取答案
        int ans = solve(n, b, prices);
        
        // 按照Kick Start格式输出结果
        // 注意：Case后有空格，#后无空格
        cout << "Case #" << caseNum << ": " << ans << endl;
    }
    
    return 0; // 程序正常结束
}
```

## Python实现

```python
def solve():
    """
    解决Allocation问题的核心函数
    采用贪心策略：优先选择价格低的房子
    """
    # 读取房子数量n和预算b
    n, b = map(int, input().split())
    
    # 读取n个房子的价格
    prices = list(map(int, input().split()))
    
    # 对价格进行升序排序，贪心选择
    prices.sort()
    
    count = 0  # 购买房子数量
    remaining = b  # 剩余预算
    
    # 遍历排序后的价格
    for price in prices:
        if remaining >= price:  # 预算足够
            remaining -= price  # 扣除房价
            count += 1  # 数量加1
        else:  # 预算不足
            break  # 停止购买
    
    return count

def main():
    """
    主函数，处理多测试用例
    """
    T = int(input())  # 读取测试用例数量
    
    # 遍历每个测试用例
    for case_num in range(1, T + 1):
        ans = solve()  # 解决当前测试用例
        # 按照Kick Start格式输出
        print(f"Case #{case_num}: {ans}")

if __name__ == "__main__":
    main()
```

## 时间/空间复杂度分析

### 时间复杂度
- **排序操作**：O(N log N)
- **遍历选择**：O(N)
- **总时间复杂度**：O(N log N)

### 空间复杂度
- **价格数组**：O(N)
- **总空间复杂度**：O(N)

## Kick Start参赛指南

### 赛制特点
- **在线竞赛**：每年多轮，每轮持续数小时
- **多测试用例**：每题包含多个测试用例
- **即时反馈**：提交后立即知道结果
- **全球排名**：根据解题数量和速度排名

### 准备建议
1. **熟悉Google编码环境**：使用Google指定的编译器和语言
2. **练习输入输出**：多测试用例处理是重点
3. **掌握基础算法**：排序、搜索、基础图论
4. **时间管理**：合理分配时间给不同难度的题目

### 图论题目进阶
- **2020 Round B - Robot Programming Strategy**：博弈论
- **2020 Round C - Countdown**：数学
- **2020 Round D - Record Breaking Days**：简单遍历

## 同类题目拓展

### Kick Start平台
- **Kick Start 2019 Round A - Training**：排序+前缀和
- **Kick Start 2020 Round B - Bike Tour**：数组遍历
- **Kick Start 2020 Round C - Perfect Subarray**：前缀和

### 其他平台同类题
- **Codeforces 1392A - Omkar and Password**：简单贪心
- **AtCoder ABC 081 B - Shift only**：基础模拟
- **LeetCode 455 - Assign Cookies**：经典贪心

## ML/DL关联思考

### 1. 贪心算法与ML
- **贪心策略**：某些在线学习算法采用贪心更新
- **特征选择**：贪心选择最相关的特征
- **决策树**：贪心选择最优划分特征

### 2. 排序在ML中的应用
```python
# 排序后选择阈值（如ROC曲线分析）
sorted_scores = sorted(predictions)
threshold = sorted_scores[k]  # 选择第k个作为阈值
```

### 3. 国际竞赛与ML职业
- Kick Start表现优秀者可获得Google面试机会
- 算法能力是ML Engineer的核心技能
- 国际竞赛经历在简历中是亮点
