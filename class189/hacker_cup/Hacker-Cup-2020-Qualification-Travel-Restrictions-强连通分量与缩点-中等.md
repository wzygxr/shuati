# Facebook Hacker Cup 2020 Qualification - Travel Restrictions

## 题目信息
- **平台**: Facebook Hacker Cup
- **年份**: 2020
- **阶段**: Qualification Round（资格赛）
- **题目**: Travel Restrictions（旅行限制）
- **难度**: 中等
- **类型**: 图论/强连通分量

## 题目链接
- https://www.facebook.com/codingcompetitions/hacker-cup

## 题目描述
有N个国家，编号1到N。初始时，任意两个国家之间都有双向航班。

但由于疫情，每个国家设置了入境限制：
- 国家i的入境限制用一个字符表示：'Y'表示接受入境，'N'表示不接受入境

对于任意两个国家i和j：
- 从i到j可以直飞当且仅当：国家i允许出境（总允许）且国家j允许入境（限制[j]=='Y'）

给定每个国家的入境限制，判断对于每对国家(i,j)，是否存在从i到j的路径。

## 输入格式
第一行包含一个整数T，表示测试用例数量。

每个测试用例的第一行包含一个整数N，表示国家数量。

第二行包含一个长度为N的字符串，第i个字符表示国家i的入境限制（'Y'或'N'）。

## 输出格式
对于每个测试用例，输出N行，每行N个字符。

第i行第j个字符表示从国家i到国家j是否存在路径：
- 'Y'表示存在路径
- 'N'表示不存在路径

## 样例输入
```
2
3
YNY
3
YYY
```

## 样例输出
```
Case #1:
YYN
NNY
YNN
Case #2:
YYY
YYY
YYY
```

## 笔试/面试考察点分析

### 核心考察点
1. **图建模**：将问题转化为有向图可达性问题
2. **强连通分量理解**：本题涉及有向图的连通性分析
3. **可达性矩阵**：计算所有点对之间的可达性
4. **时间复杂度优化**：避免O(N³)的Floyd算法

### 面试高频提问
1. **本题与强连通分量的关系**？
   - 如果所有国家都允许入境（全'Y'），图完全连通，只有一个SCC
   - 如果有'N'限制，需要分析哪些节点形成SCC
   - SCC内的节点互相可达

2. **如何高效计算所有点对可达性**？
   - 朴素Floyd：O(N³)
   - 对每个节点BFS：O(N × (N+E))
   - 利用题目特性优化（见题解）

3. **如果N=10000，如何优化**？
   - 使用强连通分量缩点
   - 缩点后得到DAG，在DAG上计算可达性
   - 复杂度降为O(N+M)

## 解题思路

### 核心观察
1. **边的存在条件**：从i到j有直飞边 ⟺ limit[j]=='Y'
2. **特殊情况**：
   - 如果limit[i]=='N'，没有航班能飞入i，i是一个"汇点"
   - 如果所有limit都是'Y'，完全连通
3. **可达性分析**：
   - 从i出发，只能到达limit='Y'的国家
   - 如果i本身limit='N'，无法到达任何国家（除自己）

### 优化解法
利用题目特殊性质：
- 从i出发，所有可达节点limit都是'Y'
- 只需判断目标节点是否允许入境
- 特殊情况处理：连续'N'的影响

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <queue>
using namespace std;

/**
 * 解决单个测试用例
 * @param n 国家数量
 * @param limits 入境限制字符串
 * @return 可达性矩阵
 * Hacker Cup要点：图论建模是关键
 * ML关联：可达性矩阵类似于图的邻接矩阵幂运算
 */
vector<string> solve(int n, const string& limits) {
    // 初始化可达性矩阵，全部为'N'
    vector<string> reachable(n, string(n, 'N'));
    
    // 对每个国家作为起点，计算可达性
    for (int i = 0; i < n; i++) {
        reachable[i][i] = 'Y'; // 自己到自己的路径总是存在
        
        // BFS/DFS计算从i出发的可达性
        // 由于题目特殊性质，可以简化计算
        
        // 向右扩展：从i向右走，只要limit[j]=='Y'就能到达
        for (int j = i + 1; j < n; j++) {
            // 从j-1到j需要：limit[j]=='Y'（j允许入境）
            if (limits[j] == 'Y' && reachable[i][j-1] == 'Y') {
                reachable[i][j] = 'Y'; // i可以到达j
            }
        }
        
        // 向左扩展：从i向左走，只要limit[j]=='Y'就能到达
        for (int j = i - 1; j >= 0; j--) {
            // 从j+1到j需要：limit[j]=='Y'（j允许入境）
            if (limits[j] == 'Y' && reachable[i][j+1] == 'Y') {
                reachable[i][j] = 'Y'; // i可以到达j
            }
        }
    }
    
    return reachable; // 返回可达性矩阵
}

int main() {
    ios::sync_with_stdio(false); // IO优化
    cin.tie(nullptr);
    
    int T; // 测试用例数量
    cin >> T; // 读取测试用例数
    
    // 遍历每个测试用例
    for (int caseNum = 1; caseNum <= T; caseNum++) {
        int n; // 国家数量
        cin >> n; // 读取n
        
        string limits; // 入境限制字符串
        cin >> limits; // 读取限制字符串
        
        // 解决问题
        vector<string> ans = solve(n, limits);
        
        // 按照Hacker Cup格式输出
        cout << "Case #" << caseNum << ":" << endl;
        for (const string& row : ans) {
            cout << row << endl; // 每行输出可达性
        }
    }
    
    return 0;
}
```

## Python实现

```python
def solve(n, limits):
    """
    解决Travel Restrictions问题
    计算可达性矩阵
    """
    # 初始化可达性矩阵
    reachable = [['N'] * n for _ in range(n)]
    
    for i in range(n):
        reachable[i][i] = 'Y'  # 自己到自己可达
        
        # 向右扩展
        for j in range(i + 1, n):
            if limits[j] == 'Y' and reachable[i][j-1] == 'Y':
                reachable[i][j] = 'Y'
        
        # 向左扩展
        for j in range(i - 1, -1, -1):
            if limits[j] == 'Y' and reachable[i][j+1] == 'Y':
                reachable[i][j] = 'Y'
    
    # 转换为字符串列表
    return [''.join(row) for row in reachable]

def main():
    T = int(input())  # 测试用例数
    
    for case_num in range(1, T + 1):
        n = int(input())  # 国家数量
        limits = input().strip()  # 入境限制
        
        ans = solve(n, limits)
        
        print(f"Case #{case_num}:")
        for row in ans:
            print(row)

if __name__ == "__main__":
    main()
```

## 进阶：强连通分量解法

对于更一般的图，可以使用SCC缩点：

```cpp
// SCC缩点后计算可达性的扩展代码
#include <vector>
#include <stack>
using namespace std;

const int MAXN = 10005;

vector<int> adj[MAXN]; // 原图邻接表
vector<int> adj_dag[MAXN]; // 缩点后DAG邻接表
int dfn[MAXN], low[MAXN], timestamp = 0;
bool in_stack[MAXN];
stack<int> st;
int scc_id[MAXN], scc_cnt = 0;

// Tarjan算法
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        scc_cnt++;
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
        } while (v != u);
    }
}

// 在DAG上计算可达性
void calcReachabilityInDAG() {
    // 对每个SCC节点，在DAG上进行BFS/DFS
    // 计算SCC之间的可达性
}
```

## 时间/空间复杂度分析

### 时间复杂度
- **解法一（利用题目性质）**：O(N²)
  - 对每个起点，向左右扩展
  - 总复杂度O(N²)
  
- **解法二（通用SCC缩点）**：O(N+M)
  - Tarjan求SCC：O(N+M)
  - DAG上计算可达性：O(SCC_cnt × E_dag)

### 空间复杂度
- **邻接表**：O(N+M)
- **可达性矩阵**：O(N²)
- **总空间复杂度**：O(N²)（需要输出矩阵）

## Hacker Cup参赛指南

### 赛制特点
- **多轮竞赛**：资格赛→第一轮→第二轮→决赛
- **时间窗口**：每轮持续数天
- **提交方式**：提交输出文件而非代码
- **罚分机制**：错误提交有罚分

### 准备建议
1. **熟悉赛制**：与Kick Start不同，需要提交输出文件
2. **生成器技巧**：写程序生成所有测试用例的输出
3. **验证脚本**：写脚本验证输出格式正确性
4. **图论基础**：Hacker Cup图论题目较多

### 进阶题目
- **Hacker Cup 2020 Round 1 - Perimetric Chapter 1**：几何
- **Hacker Cup 2020 Round 2 - Timber**: 区间DP
- **Hacker Cup 2020 Final - Logan's Treasure**: 复杂图论

## ML/DL关联思考

### 1. 可达性矩阵与图嵌入
```python
# 可达性矩阵可以转换为图的相似度矩阵
import numpy as np

# 可达性矩阵作为特征
reachability_matrix = np.array([...])

# 用于图神经网络的消息传递
# 可达性矩阵定义了信息传播的通道
```

### 2. 图的连通性与ML
- **图分类**：连通性是重要的图级特征
- **异常检测**：不连通的子图可能是异常
- **推荐系统**：用户-物品图的连通性影响推荐效果

### 3. 社交网络应用
- 本题模型类似于社交网络的传播模型
- 'Y'/'N'类似于用户是否接受信息
- 强连通分量对应紧密的社交圈子
