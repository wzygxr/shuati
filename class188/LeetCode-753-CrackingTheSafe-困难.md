# 【LeetCode】-753-破解保险箱-欧拉路径-困难

## 题目原始链接
https://leetcode.cn/problems/cracking-the-safe/

## 题目完整描述
有一个需要密码才能打开的保险箱。密码是 n 位数, 每一位都是 0 到 k - 1 中的一个数字。

你可以随意输入密码，如果连续 n 位正确的密码序列出现在你输入的字符串中，那么就能打开保险箱。

例如，假设密码是 "345"，你可以输入 "012345" 来打开它。

给你一个整数 n 和 k，返回长度最短的密码序列，使得你能在输入的字符串中找到所有可能的密码。

### 输入输出格式
- 输入：n = 2, k = 2
- 输出："00110"

### 数据范围
- 1 <= n <= 4
- 1 <= k <= 10
- 1 <= k^n <= 4096

### 样例输入输出
```
输入：n = 1, k = 2
输出："10"
解释：所有长度为1的密码（"0", "1"）都包含在字符串"10"中。

输入：n = 2, k = 2  
输出："00110"
解释：所有长度为2的密码（"00", "01", "11", "10"）都包含在字符串"00110"中。
```

## 笔试/面试考察点分析
- **考察点1**：德布鲁因序列（De Bruijn Sequence）的理解与构造
- **考察点2**：将问题转化为欧拉路径问题的能力
- **考察点3**：构建合适的图模型（节点为长度为n-1的前缀）
- **考察点4**：Hierholzer算法的变形应用
- **考察点5**：图论建模能力（从实际问题到图论问题的抽象）

## 解题思路
这是一个经典的德布鲁因序列问题，可以转化为欧拉路径问题：

1. 构建图：每个长度为n-1的字符串作为一个节点
2. 每条边代表一个长度为n的密码：从去掉最后一个字符的前缀指向去掉第一个字符的后缀
3. 问题转化为：找到一条经过所有边恰好一次的路径（欧拉路径）
4. 由于从节点"00...0"出发，每个节点的出度=入度=k，所以一定存在欧拉回路
5. 使用Hierholzer算法找欧拉回路，构造最短序列

## 完整代码实现

```java
class Solution {
    public String crackSafe(int n, int k) {
        // 使用StringBuilder高效构建结果字符串
        StringBuilder result = new StringBuilder();
        
        // 计算总的密码数量：k^n
        int totalCombinations = (int) Math.pow(k, n);
        
        // 使用集合记录已经访问过的密码
        Set<String> visited = new HashSet<>();
        
        // 构造初始节点（n-1个0）
        StringBuilder startNode = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            startNode.append('0');
        }
        
        // DFS遍历，构造德布鲁因序列
        dfs(startNode.toString(), n, k, visited, result);
        
        // 添加起始节点
        result.append(startNode.toString());
        return result.toString();
    }
    
    private void dfs(String node, int n, int k, Set<String> visited, StringBuilder result) {
        // 尝试在当前节点后添加每一个可能的字符
        for (int i = 0; i < k; i++) {
            // 构造新的密码（边）
            String newPassword = node + i;
            
            // 如果这个密码没有被访问过
            if (!visited.contains(newPassword)) {
                // 标记为已访问
                visited.add(newPassword);
                
                // 获取下一个节点（移除第一个字符，保留后n-1位）
                String nextNode = newPassword.substring(1);
                
                // 递归访问下一个节点
                dfs(nextNode, n, k, visited, result);
                
                // 在回溯时添加当前字符到结果中
                // 这样可以保证路径的连续性
                result.append(i);
            }
        }
    }
}
```

## 代码逐行注释
- `int totalCombinations = (int) Math.pow(k, n);` - 计算总的密码组合数，面试需说明这决定了图中边的数量
- `Set<String> visited = new HashSet<>();` - 使用哈希集合记录已访问的密码，避免重复，笔试需注意数据结构选择
- `StringBuilder result = new StringBuilder();` - 使用StringBuilder高效构建字符串，面试高频考察点（避免频繁字符串拼接）
- `String newPassword = node + i;` - 构造新密码，即图中的一条边，体现德布鲁因序列的构造思想
- `String nextNode = newPassword.substring(1);` - 获取下一节点，保留后n-1位，面试需说明这是状态转移的关键
- `dfs(nextNode, n, k, visited, result);` - 递归访问下一节点，实现Hierholzer算法的核心思想
- `result.append(i);` - 在回溯时添加字符，这是欧拉路径构造的关键步骤，面试需重点解释
- **ML/DL关联**：该算法构建了包含所有可能模式的序列，可用于序列预测模型的训练数据生成

## 时间/空间复杂度分析
- **时间复杂度**：O(k^n * k) = O(k^(n+1))，每条边最多访问一次，每条边需要尝试k种可能性
- **空间复杂度**：O(k^n)，用于存储已访问的密码组合和递归栈空间
- **面试高频提问**：为什么空间复杂度是O(k^n)而不是O(k^(n+1))？因为虽然有k^(n+1)种边的可能，但实际只有k^n个节点

## 同类题目拓展
- **类似题目1**：POJ 1780 - Phone Numbers - 类似的德布鲁因序列问题
- **类似题目2**：Codeforces 508D - Tanya and Password - 另一个德布鲁因序列应用
- **变种方向1**：如果限制某些密码不能使用，如何修改算法？
- **变种方向2**：如果要求构造字典序最小的序列，如何实现？

## ML/DL关联思考
- 该算法生成的德布鲁因序列包含所有可能的n-gram模式，在自然语言处理中有重要应用
- 在图神经网络中，这种序列生成方法可用于构建训练样本，覆盖所有可能的子图模式
- 欧拉路径的思想可用于序列推荐系统，确保覆盖所有可能的用户行为序列
- 该问题体现了图论与组合数学的结合，在机器学习的特征工程中很有价值