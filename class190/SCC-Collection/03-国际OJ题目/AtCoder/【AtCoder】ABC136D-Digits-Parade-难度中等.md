# 【AtCoder】ABC136 D - Digits Parade

## 题目信息

- **题目链接**: https://atcoder.jp/contests/abc136/tasks/abc136_d
- **难度**: ABC D题（中等偏上）
- **算法标签**: 字符串、模拟、强连通分量（变形）

## 题目描述

给定一个由'R'和'L'组成的字符串。初始时每个位置有一个孩子。

每秒，站在'R'上的孩子会向右移动，站在'L'上的孩子会向左移动。

如果两个孩子同时到达同一个位置，他们就会停止移动。

求10^100秒后，每个位置上停留的孩子数量。

## 输入输出格式

### 输入格式
一个字符串s，由'R'和'L'组成

### 输出格式
输出n个数，表示每个位置上的孩子数量

### 数据范围
- $1 \leq |s| \leq 10^5$

## 笔试/面试考察点分析

### 核心考察点
1. **周期性观察**：$10^{100}$远大于字符串长度，系统会达到稳定状态
2. **RL交界处的聚集**：'R'和'L'交界的位置是关键
3. **奇偶性分析**：最终位置取决于距离的奇偶性

### SCC思想的应用
- 虽然这不是传统SCC问题，但可以用类似SCC的思想分析
- 'R'和'L'将字符串分成若干段，每段形成一个"连通块"
- 每个'RL'交界处最终形成一个聚集点（类似SCC）

## 解题思路

### 关键观察
1. 'R'会向右移动，直到遇到'L'或边界
2. 'L'会向左移动，直到遇到'R'或边界
3. 'RL'交界处会形成聚集
4. 每个'R...L'段独立计算

### 具体策略
- 找到所有的'RL'位置
- 对于每个'R...L'段，统计最终聚集位置
- 奇数距离的到左边，偶数距离的到右边（或相反）

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

int main() {
    string s;
    cin >> s;
    
    int n = s.size();
    vector<int> ans(n, 0);  // 每个位置的答案
    
    // 预处理：计算每个位置到左边最近的R和右边最近的L的距离
    vector<int> left_r(n, -1);   // left_r[i] = i左边最近的R的位置
    vector<int> right_l(n, -1);  // right_l[i] = i右边最近的L的位置
    
    // 从左到右找R
    int last_r = -1;
    for (int i = 0; i < n; i++) {
        if (s[i] == 'R') {
            last_r = i;
        }
        left_r[i] = last_r;
    }
    
    // 从右到左找L
    int last_l = -1;
    for (int i = n - 1; i >= 0; i--) {
        if (s[i] == 'L') {
            last_l = i;
        }
        right_l[i] = last_l;
    }
    
    // 对于每个位置，判断它最终会去到哪里
    for (int i = 0; i < n; i++) {
        if (s[i] == 'R') {
            // R会向右走到最近的L前面
            int target = right_l[i];
            if (target == -1) {
                // 没有L，会一直走到边界（但题目保证有解）
                ans[i]++;
            } else {
                int dist = target - i;  // 距离
                // 根据奇偶性判断
                if (dist % 2 == 0) {
                    ans[target - 1]++;  // 偶数距离到左边
                } else {
                    ans[target]++;      // 奇数距离到右边
                }
            }
        } else {
            // L会向左走到最近的R后面
            int target = left_r[i];
            if (target == -1) {
                ans[i]++;
            } else {
                int dist = i - target;
                if (dist % 2 == 0) {
                    ans[target + 1]++;  // 偶数距离到右边
                } else {
                    ans[target]++;      // 奇数距离到左边
                }
            }
        }
    }
    
    // 输出结果
    for (int i = 0; i < n; i++) {
        if (i > 0) cout << " ";
        cout << ans[i];
    }
    cout << endl;
    
    return 0;
}
```

## 更简洁的解法

```cpp
#include <bits/stdc++.h>
using namespace std;

int main() {
    string s;
    cin >> s;
    int n = s.size();
    
    vector<int> ans(n, 0);
    
    // 双指针遍历每个'R...L'段
    for (int i = 0; i < n; ) {
        if (s[i] == 'L') {
            i++;
            continue;
        }
        
        // 找到R段
        int r_start = i;
        while (i < n && s[i] == 'R') i++;
        int r_end = i - 1;
        
        // 找到L段
        int l_start = i;
        while (i < n && s[i] == 'L') i++;
        int l_end = i - 1;
        
        // 交界点在r_end和l_start之间
        // R的数量: r_end - r_start + 1
        // L的数量: l_end - l_start + 1
        int r_cnt = r_end - r_start + 1;
        int l_cnt = l_end - l_start + 1;
        
        // 交界点是r_end（最后一个R的位置）
        // R的孩子根据距离奇偶性分布在r_end和r_end+1
        // L的孩子根据距离奇偶性分布在r_end和r_end+1
        
        // 简化：所有R和L最终聚集在r_end和r_end+1两个位置
        ans[r_end] += (r_cnt + 1) / 2 + l_cnt / 2;
        ans[r_end + 1] += r_cnt / 2 + (l_cnt + 1) / 2;
    }
    
    for (int i = 0; i < n; i++) {
        if (i > 0) cout << " ";
        cout << ans[i];
    }
    cout << endl;
    
    return 0;
}
```

## 复杂度分析

- **时间**：$O(n)$
- **空间**：$O(n)$

## ML/DL关联

### 序列建模
- **RNN/LSTM**：类似这种序列传播问题可以用RNN建模
- **Transformer**：注意力机制可以捕捉长距离依赖

### 物理模拟
- **图神经网络**：将位置建模为节点，移动规则建模为边
- **消息传递**：模拟粒子在图中的传播过程

## 笔试面试问题

**Q: 为什么时间是10^100？**
> 这是一个足够大的数，确保系统达到稳态。实际上，系统在O(n)时间内就会稳定。

**Q: 如何理解"连通块"的概念？**
> 每个'R...L'段形成一个独立系统，段之间互不影响。这类似于SCC中的连通分量概念。
