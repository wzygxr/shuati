#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <cstring>
#include <bitset>
#include <iostream>
#include <utility>  // for pair

using namespace std;

// 前缀树节点类
struct TrieNode {
    TrieNode* children[2];
    TrieNode() {
        children[0] = nullptr;
        children[1] = nullptr;
    }
};

class Code11_XorExtendedProblems {
public:
    /**
     * 题目3: The XOR-longest Path (POJ 3764)
     * 
     * 题目来源: POJ 3764
     * 链接: http://poj.org/problem?id=3764
     * 
     * 题目描述:
     * 给定一棵带权树，每条边有一个权值。找到树中最长的一条路径，使得路径上的边权异或值最大。
     * 
     * 解题思路:
     * 1. 计算每个节点到根节点的路径异或值xorPath[u]
     * 2. 任意两点u和v之间的路径异或值 = xorPath[u] ^ xorPath[v]
     * 3. 问题转化为在xorPath数组中找出两个数，使得它们的异或值最大
     * 4. 使用前缀树(Trie)解决最大异或对问题
     * 
     * 时间复杂度: O(n * 32)
     * 空间复杂度: O(n * 32)
     * 
     * 工程化考量:
     * - 使用邻接表存储树结构
     * - 深度优先搜索计算路径异或值
     * - 前缀树优化最大异或查询
     * 
     * @param n 节点数量
     * @param edges 边列表，每个边为[u, v, weight]
     * @return 最长异或路径的值
     */
    static int xorLongestPath(int n, vector<vector<int>>& edges) {
        // 构建邻接表
        vector<vector<pair<int, int>>> graph(n);
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph[u].push_back({v, w});
            graph[v].push_back({u, w});
        }
        
        // 计算每个节点到根节点(0)的路径异或值
        vector<int> xorPath(n, 0);
        vector<bool> visited(n, false);
        dfs(0, -1, 0, graph, xorPath, visited);
        
        // 使用前缀树找到最大异或对
        TrieNode* root = new TrieNode();
        int maxXOR = 0;
        
        for (int i = 0; i < n; i++) {
            // 将当前路径异或值插入前缀树
            insertToTrie(root, xorPath[i]);
            // 查询最大异或值
            maxXOR = max(maxXOR, queryMaxXOR(root, xorPath[i]));
        }
        
        // 释放内存
        deleteTrie(root);
        
        return maxXOR;
    }
    
private:
    // 深度优先搜索计算路径异或值
    static void dfs(int node, int parent, int currentXOR, 
                   vector<vector<pair<int, int>>>& graph, 
                   vector<int>& xorPath, vector<bool>& visited) {
        visited[node] = true;
        xorPath[node] = currentXOR;
        
        for (auto& neighbor : graph[node]) {
            int nextNode = neighbor.first;
            int weight = neighbor.second;
            if (nextNode != parent && !visited[nextNode]) {
                dfs(nextNode, node, currentXOR ^ weight, graph, xorPath, visited);
            }
        }
    }
    
    // 插入数字到前缀树
    static void insertToTrie(TrieNode* root, int num) {
        TrieNode* node = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node->children[bit] == nullptr) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
        }
    }
    
    // 查询与num异或的最大值
    static int queryMaxXOR(TrieNode* root, int num) {
        TrieNode* node = root;
        int maxXOR = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit;
            if (node->children[desiredBit] != nullptr) {
                maxXOR |= (1 << i);
                node = node->children[desiredBit];
            } else {
                node = node->children[bit];
            }
        }
        return maxXOR;
    }
    
    // 释放前缀树内存
    static void deleteTrie(TrieNode* root) {
        if (root == nullptr) return;
        deleteTrie(root->children[0]);
        deleteTrie(root->children[1]);
        delete root;
    }

public:
    /**
     * 题目4: Sum vs XOR (HackerRank)
     * 
     * 题目来源: HackerRank - Sum vs XOR
     * 链接: https://www.hackerrank.com/challenges/sum-vs-xor/problem
     * 
     * 题目描述:
     * 给定一个整数n，找出非负整数x的个数，使得x + n == x ^ n。
     * 
     * 解题思路:
     * 数学分析：x + n = x ^ n 当且仅当 x & n = 0
     * 即x和n在二进制表示中没有重叠的1位。
     * 1. 计算n的二进制表示中0的个数count
     * 2. 答案就是2^count
     * 
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 工程化考量:
     * - 处理n=0的特殊情况
     * - 使用long long类型处理大数
     * - 位运算优化
     * 
     * @param n 输入整数
     * @return 满足条件的x的个数
     */
    static long long sumVsXor(long long n) {
        if (n == 0) {
            return 1; // 任何x都满足
        }
        
        // 计算n的二进制表示中0的个数
        int countZeros = 0;
        long long temp = n;
        while (temp > 0) {
            if ((temp & 1) == 0) {
                countZeros++;
            }
            temp >>= 1;
        }
        
        return 1LL << countZeros;
    }

    // 继续实现其他题目...
};